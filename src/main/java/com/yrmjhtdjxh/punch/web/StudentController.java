package com.yrmjhtdjxh.punch.web;

import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author etron
 * 主要是用于student相关的控制器
 * 登录，注册，信息修改
 */
@ResponseBody
@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;


    /**
     * 注册的接口
     * @param student
     * @return
     */
    @RequestMapping("/register")
    public Map<String, String> register(@RequestBody Student student) {

        Map<String, String> map = new HashMap<>();

        // 如果已经存在该学号
        if(studentService.getOne(student.getStudentID()) != null ){
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
            studentService.insert(student);
            map.put("status", "success");
        }catch (Exception e) {
            map.put("status", "fail");
        }
        return map;
    }

    /**
     * 登录的接口
     * @return
     */
    @RequestMapping("/login")
    public Map<String, String> getOne(@RequestBody Map<String, String> loginMap,

                          HttpSession session){
        Long studentID = Long.parseLong(loginMap.get("studentID"));
        String password = loginMap.get("password");
        Map<String, String> map = new HashMap<>();
        // 后面来看是否需要MD5加密
        Student student = studentService.checkStudent(studentID, password);
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
     * 修改个人信息的接口
     */
    @RequestMapping("updateStudentInfo")
    public Map<String, Object> update(@RequestBody Student student,
                                      HttpSession session) {
        Map<String , Object> map = new HashMap<>();
        Student student1  = (Student) session.getAttribute("student");
        // 没有登录
        if(student1 == null) {
            map.put("status", "fail");
            return map;
        }
        studentService.updateStudentInfo(student);
        session.setAttribute("student", student);
        map.put("status", "success");
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



}

