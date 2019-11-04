package com.yrmjhtdjxh.punch.web;

import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author etron
 * 主要是用于student相关的控制器
 * 登录，注册，信息修改
 */
@ResponseBody
@Controller
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 注册的接口
     * @param student
     * @return
     */
    @RequestMapping("/register")
    public Map<String, String> register(@RequestBody @Valid Student student) {
        return studentService.register(student);
    }

    /**
     * 登录的接口
     * @return
     */
    @RequestMapping("/login")
    public Map<String, String> getOne(@RequestBody Map<String, String> loginMap, HttpSession session){
        return studentService.login(loginMap,session);
    }


    /**
     * 修改个人信息的接口
     */
    @RequestMapping("updateStudentInfo")
    public Map<String, Object> update(@RequestBody @Valid Student student,
                                      HttpSession session) {
        return studentService.updateStudentInfo(student,session);
    }

}

