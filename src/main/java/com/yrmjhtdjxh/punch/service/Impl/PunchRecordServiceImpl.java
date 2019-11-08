package com.yrmjhtdjxh.punch.service.Impl;

import com.yrmjhtdjxh.punch.VO.StudentVO;
import com.yrmjhtdjxh.punch.domain.PunchRecord;
import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.mapper.PunchRecordMapper;
import com.yrmjhtdjxh.punch.mapper.StudentMapper;
import com.yrmjhtdjxh.punch.service.PunchRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PunchRecordServiceImpl implements PunchRecordService {

    /**
     * 打卡IP地址
     */
    private static String IP = "125.70.254.67";


    /**
     * 少于0.5小时，打卡失败
     */
    private static double MIN_RECORD_TIME = 0.5;

    /**
     * 每次打卡最多5小时
     */
    private static double MAX_RECORD_TIME = 5.0;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PunchRecordMapper punchRecordMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void insert(PunchRecord punchRecord) {
        punchRecordMapper.insert(punchRecord);
    }

    @Override
    public PunchRecord getUnfinishPunchByStudnetID(Long studentID) {
        return punchRecordMapper.getUnfinishPunchByStudnetID(studentID);
    }

    @Override
    public void endRecordTime(PunchRecord punchRecord) {
        punchRecordMapper.endPunchRecord(punchRecord);
    }

    @Override
    public void deleteShortPunchTime(Long studentID) {
        punchRecordMapper.deleteByStudentID(studentID);
    }

    @Override
    public Map<String, Object> startPunch(Map<String, String> startPunchMap, HttpSession session, HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();

        // 首先判断有没有登录
        StudentVO student = (StudentVO) session.getAttribute("student");

        // 如果该学生已经打卡，或者没有登录
        if(student == null || student.isPunch()) {
            map.put("status", "fail");
            map.put("msg", "已经在打卡或者没有登录。");
            return map;
        }
/*
        String  local = GetIPAddressUtils.getIpAddress(request);
        logger.info("当前的IP地址为："+ local +"是否等于125.70.254.67");

        // 如果IP地址不为LC2路由器
        if(!local.equals(IP)){
            map.put("status", "fail");
            map.put("msg", "请尝试连接LC2，并且拔出网线重试打卡");
            return map;
        }*/

        // 否则识别为正常的打卡状态
        student.setPunch(true);
        session.setAttribute("student", student);


        Long studentID = Long.parseLong(startPunchMap.get("studentID"));

        // 将student的打卡状态改变
        studentMapper.updatePunchByStudentID(studentID, 1);

        // 插入一条打卡信息的数据
        PunchRecord punchRecord = new PunchRecord(0, studentID, new Date(), new Date(), new Double(0), new Date());
        insert(punchRecord);

        map.put("status", "success");
        // 返回即可
        return map;
    }

    @Override
    public Map<String, Object> endPunch(Map<String, String> startPunchMap, HttpSession session, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        Long studentID = Long.parseLong(startPunchMap.get("studentID"));

        // 首先判断是否登录
        StudentVO student = (StudentVO) session.getAttribute("student");
        // 首先将得到数据库中这条打卡的数据
        PunchRecord punchRecord = getUnfinishPunchByStudnetID(studentID);

        // 如果该学生没有登录或者没有开始打卡
        if(student == null|| punchRecord == null ) {
            map.put("status", "fail");
            map.put("msg","没有登录或者没有开始打卡。");
            return map;
        }

/*
        // 判断IP是否正确
        String  local = GetIPAddressUtils.getIpAddress(request);
        logger.info("当前的IP地址为："+ local +"是否等于125.70.254.67");

        // 如果IP地址不为LC2路由器
        if(!local.equals(IP)){
            map.put("status", "fail");
            map.put("msg", "请尝试连接LC2，并且拔出网线重试打卡");
            return map;
        }*/


        // 将本次时间计算出来
        punchRecord.setEndPunchTime(new Date());
        Double d = calculateRecordTime(punchRecord.getBeginPunchTime(), punchRecord.getEndPunchTime());

        // 更新session中student的打卡状态
        student.setPunch(false);
        session.setAttribute("student", student);


        if(d < MIN_RECORD_TIME || !isTheSameDay(punchRecord.getBeginPunchTime(), punchRecord.getEndPunchTime())) {
            // 将student的状态改变
            studentMapper.updatePunchByStudentID(studentID, 0);
            deleteShortPunchTime(studentID);
            map.put("status", "fail");
            map.put("msg", "当前打卡时间过短小于半小时或者识别为隔天打卡记录，请稍后重新打卡。");
            return map;
        }

        // 如果超过了5个小时，只记录为5个小时
        if(d > MAX_RECORD_TIME) {
            d = MAX_RECORD_TIME;
        }

        // 否则说明打卡成功，存入数据库
        punchRecord.setRecordTime(d);
        endRecordTime(punchRecord);

        // 将student的状态改变
        studentMapper.updatePunchByStudentID(studentID, 0);

        map.put("status", "success");

        // 返回即可
        return map;
    }

    /**
     * 计算这次打卡时间
     * 并且保留两位小数
     * @param startTime
     * @param endTime
     * @return
     */
    public Double calculateRecordTime(Date startTime, Date endTime) {
        Double recordTime =(endTime.getTime()-startTime.getTime())/(60*60*1000.0);
        BigDecimal bigDecimal = new BigDecimal(recordTime);
        recordTime = bigDecimal.setScale(2, BigDecimal.ROUND_UP).doubleValue();
        return recordTime;
    }

    /**
     * 是否是隔天数据
     * @return
     */
    public boolean isTheSameDay(Date startTime, Date endTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = simpleDateFormat.format(startTime);
        String end = simpleDateFormat.format(endTime);
        return start.equals(end);
    }
}
