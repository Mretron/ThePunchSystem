package com.yrmjhtdjxh.punch.service.Impl;

import com.yrmjhtdjxh.punch.VO.StudentVO;
import com.yrmjhtdjxh.punch.domain.IndexStudent;
import com.yrmjhtdjxh.punch.domain.PunchRecord;
import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.mapper.PunchRecordMapper;
import com.yrmjhtdjxh.punch.mapper.StudentMapper;
import com.yrmjhtdjxh.punch.service.PunchRecordService;
import com.yrmjhtdjxh.punch.service.StudentAndPunchRecordService;
import com.yrmjhtdjxh.punch.util.GetWeekUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudentAndPunchRecordServiceImpl implements StudentAndPunchRecordService {


    private StudentMapper studentMapper;
    private PunchRecordMapper punchRecordMapper;
    private PunchRecordService punchRecordService;

    @Autowired
    public StudentAndPunchRecordServiceImpl(StudentMapper studentMapper, PunchRecordMapper punchRecordMapper,
                                            PunchRecordService punchRecordService) {
        this.studentMapper = studentMapper;
        this.punchRecordMapper = punchRecordMapper;
        this.punchRecordService = punchRecordService;
    }

    @Override
    public Map<String, Object> getStudentAndPunchInfo(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        StudentVO student = (StudentVO) session.getAttribute("student");
        if(student != null) {
            // 取出当前学生的信息
            map.put("status", "success");
            map.put("student", studentMapper.getOne(student.getStudentID()));
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
        List<IndexStudent> indexStudents = getSort();
        map.put("indexStudents", indexStudents);
        return map;
    }

    /**
     * 将学生List全部取出来
     * 然后根据学生学号将个人的时间信息取出来
     * 然后包装成IndexStudent的类，按照每周打卡信息排名
     * 进行排好序
     * 返回即可
     * @return
     */
    public List<IndexStudent> getSort() {

        List<IndexStudent> list = new ArrayList<>();

        // 1.先将所有的student信息查出来放到一个List中
        List<StudentVO> students = studentMapper.getAllByRole(null);

        // 2.循环这个List去根据IndexStudent的结构计算 每个student的punchRecordMapper信息
        for(StudentVO student : students) {
            IndexStudent indexStudent = new IndexStudent();

            indexStudent.setName(student.getName());
            indexStudent.setGrade(student.getGrade());
            indexStudent.setAvatar(student.getAvatar());
            indexStudent.setPunch(student.isPunch());

            // 设置今天打卡时间
            indexStudent.setTodayTime(to2Double(punchRecordMapper.getTodayByStudentID(student.getStudentID())));

            // 设置本周打卡时间
            indexStudent.setWeekTime(to2Double(punchRecordMapper.getWeekByStudentID(GetWeekUtil.getThisMonday(), GetWeekUtil.getNextMonday(), student.getStudentID())));

            // 设置本周剩余时间 用28个小时，减去已经打卡的时间
            Double weekLeftTime = (28.0 - indexStudent.getWeekTime() <= 0) ? 0 : to2Double(28.0 - indexStudent.getWeekTime());
            indexStudent.setWeekLeftTime(weekLeftTime);
            list.add(indexStudent);

        }
        // 然后将这个List根据本周打卡时间排序
        Collections.sort(list, new SumWeekComparator());
        return list;
    }

    public static void main(String[] args) throws Exception{

        SimpleDateFormat myFormatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm");
        java.util.Date date= myFormatter.parse( "2003-06-01 16:45");
        java.util.Date mydate= myFormatter.parse( "2003-06-01 10:30");
        Double day=(date.getTime()-mydate.getTime())/(60*60*1000.0);
        System.out.println( "相差的日期: " + day);

    }

    /**
     * 使得List 集合根据周打卡时间排序
     */
    private class SumWeekComparator implements Comparator<IndexStudent> {
        @Override
        public int compare(IndexStudent o1, IndexStudent o2) {
            Double result = o1.getWeekTime() - o2.getWeekTime();
            return result >= 0 ? -1 : 1 ;
        }
    }

    /**
     * 保留两位小数
     * @param aDouble
     * @return
     */
    private Double to2Double(Double aDouble) {
        if(aDouble == null) {
            return new Double(0);
        }
        BigDecimal bigDecimal = new BigDecimal(aDouble);
        aDouble = bigDecimal.setScale(2, BigDecimal.ROUND_UP).doubleValue();
        return aDouble;
    }

}
