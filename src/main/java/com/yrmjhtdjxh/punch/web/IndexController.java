package com.yrmjhtdjxh.punch.web;

import com.yrmjhtdjxh.punch.domain.IndexStudent;
import com.yrmjhtdjxh.punch.domain.PunchRecord;
import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.service.PunchRecordService;
import com.yrmjhtdjxh.punch.service.StudentAndPunchRecordService;
import com.yrmjhtdjxh.punch.service.StudentService;
import com.yrmjhtdjxh.punch.util.GetIPAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author etron
 *
 */
@ResponseBody
@Controller
public class IndexController {

    /**
     * 少于0.5小时，打卡失败
     */
    private static double MIN_RECORD_TIME = 0.5;

    /**
     * 每次打卡最多5小时
     */
    private static double MAX_RECORD_TIME = 5.0;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 打卡IP地址
     */
    private static String IP = "125.70.254.67";


    @Autowired
    private StudentAndPunchRecordService studentAndPunchRecordService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PunchRecordService punchRecordService;


    /**
     * 提供首页展示当前学生和目前的打卡信息（所有人的打卡排名情况）
     * @return
     */
    @RequestMapping("/getStudentAndPunchInfo")
    public Map<String, Object> getStudentAndPunchInfo(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        Student student = (Student) session.getAttribute("student");
        if(student != null) {
            // 取出当前学生的信息
            map.put("status", "success");
            map.put("student", studentService.getOne(student.getStudentID()));
            // 如果有未完成的打卡记录放入
            PunchRecord punchRecord = punchRecordService.getUnfinishPunchByStudnetID(student.getStudentID());
            if(punchRecord == null) {
                map.put("unfinishTime", 0);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
                map.put("unfinishTime", simpleDateFormat.format(punchRecord.getBeginPunchTime()));
            }
        }else {
            map.put("status", "fail");
            return map;
        }
        // 取出首页包装好的时间排名类
        List<IndexStudent> indexStudents = studentAndPunchRecordService.getSort();
        map.put("indexStudents", indexStudents);
        return map;
    }

    /**
     * 开始打卡的接口
     * @param startPunchMap
     * @param session
     * @return
     */
    @RequestMapping("/startPunch")
    public Map<String ,Object> startPunch(@RequestBody Map<String, String> startPunchMap,
                                          HttpSession session,
                                          HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();

        // 首先判断有没有登录
        Student student = (Student) session.getAttribute("student");

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
        studentService.changePunch(studentID, 1);

        // 插入一条打卡信息的数据
        PunchRecord punchRecord = new PunchRecord(0, studentID, new Date(), new Date(), new Double(0), new Date());
        punchRecordService.insert(punchRecord);

        map.put("status", "success");
        // 返回即可
        return map;
    }

    /**
     * 结束打卡的接口
     * @param startPunchMap,
     * @param session
     * @return
     */
    @RequestMapping("/endPunch")
    public Map<String, Object> endPunch(@RequestBody Map<String, String> startPunchMap,
                                        HttpSession session,
                                        HttpServletRequest request){

        Map<String, Object> map = new HashMap<>();

        Long studentID = Long.parseLong(startPunchMap.get("studentID"));

        // 首先判断是否登录
        Student student = (Student) session.getAttribute("student");
        // 首先将得到数据库中这条打卡的数据
        PunchRecord punchRecord = punchRecordService.getUnfinishPunchByStudnetID(studentID);

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
            studentService.changePunch(studentID, 0);
            punchRecordService.deleteShortPunchTime(studentID);
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
        punchRecordService.endRecordTime(punchRecord);

        // 将student的状态改变
        studentService.changePunch(studentID, 0);

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
