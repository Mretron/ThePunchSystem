package com.yrmjhtdjxh.punch.web;

import com.yrmjhtdjxh.punch.VO.Result;
import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.domain.StudentRole;
import com.yrmjhtdjxh.punch.form.StudentRoleForm;
import com.yrmjhtdjxh.punch.service.StudentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "用户模块")
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
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid Student student) {
        return studentService.register(student);
    }

    /**
     * 删除用户及相关信息
     * @param userId
     * @param session
     * @return
     */
    @GetMapping("/deleteUser")
    public Result deleteUser(Long userId,HttpSession session){
        return studentService.deleteUser(userId,session);
    }

    /**
     * 登录的接口
     * @return
     */
    @PostMapping("/login")
    public Map<String, String> getOne(@RequestBody Map<String, String> loginMap, HttpSession session){
        return studentService.login(loginMap,session);
    }


    /**
     * 修改个人信息的接口
     */
    @PostMapping("updateStudentInfo")
    public Map<String, Object> update(@RequestBody @Valid Student student,
                                      HttpSession session) {
        return studentService.updateStudentInfo(student,session);
    }

    @PostMapping("/updateUserRole")
    public Result updateUserRole(@RequestBody @Valid StudentRoleForm form,HttpSession httpSession){
        return studentService.updateUserRole(form,httpSession);
    }

    /**
     *  获取注册用户列表
     */
    @GetMapping("/getRegisterUserList")
    public Result getRegisterUserList(HttpSession httpSession) {
        return studentService.getRegisterUserList(httpSession);
    }

}

