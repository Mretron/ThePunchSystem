package com.yrmjhtdjxh.punch.service.Impl;

import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.mapper.StudentMapper;
import com.yrmjhtdjxh.punch.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void insert(Student student) {
        studentMapper.insert(student);
    }

    @Override
    public Student getOne(Long studentID) {
        return studentMapper.getOne(studentID);
    }

    @Override
    public Student checkStudent(Long studentID, String password) {
        return studentMapper.findStudentByStudentIDAndPassword(studentID, password);
    }

    @Override
    public Map<String, String> register(Student student) {
        Map<String, String> map = new HashMap<>();

        // 如果已经存在该学号
        if(getOne(student.getStudentID()) != null ){
            map.put("status", "fail");
            map.put("error", "该学号已经存在");
            return map;
        }

        student.setId(0);
        student.setGrade(getGrade(student.getStudentID()));
        student.setCreateTime(new Date());

        // 设置默认头像
        if(student.getSex() == 1) {
            student.setAvatar("http://47.102.114.0:8080/images/boyAvatar.jpg");
        } else {
            student.setAvatar("http://47.102.114.0:8080/images/girlAvatar.png");
        }
        // 匹配图片的地址，python已经保存，传送过来url

        // 刚注册默认未开始打卡
        student.setPunch(false);

        try{
            insert(student);
            map.put("status", "success");
        }catch (Exception e) {
            map.put("status", "fail");
        }
        return map;
    }

    @Override
    public Map<String, Object> updateStudentInfo(Student student, HttpSession session) {
        Map<String , Object> map = new HashMap<>();
        Student student1  = (Student) session.getAttribute("student");
        // 没有登录
        if(student1 == null) {
            map.put("status", "fail");
            return map;
        }
        studentMapper.updateStudentInfo(student);
        session.setAttribute("student", student);
        map.put("status", "success");
        return map;
    }


    @Override
    public Map<String, String> login(Map<String, String> loginMap, HttpSession session) {
        Long studentID = Long.parseLong(loginMap.get("studentID"));
        String password = loginMap.get("password");
        Map<String, String> map = new HashMap<>();
        // 后面来看是否需要MD5加密
        Student student = checkStudent(studentID, password);
        if(student == null) {
            map.put("status","fail");
        } else {
            map.put("status","success");
            // 登录成功，放入session
            student.setPassword(null);
            session.setAttribute("student",student);
        }
        return map;
    }

    /**
     * 根据学号提取出年级
     * @param studentID
     * @return
     */
    public int getGrade(Long studentID) {
        return Integer.parseInt(studentID.toString().substring(0,4));
    }

    @Override
    public void changePunch(Long studentID, int punchStatus) {
        studentMapper.updatePunchByStudentID(studentID, punchStatus);
    }
}
