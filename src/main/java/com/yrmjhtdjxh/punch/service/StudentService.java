package com.yrmjhtdjxh.punch.service;

import com.yrmjhtdjxh.punch.VO.Result;
import com.yrmjhtdjxh.punch.VO.StudentVO;
import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.form.StudentRoleForm;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface StudentService {
    void insert(Student student);
    Student getOne(Long studentID);
    StudentVO checkStudent(Long studentID, String password);
    void changePunch(Long studentID, int punchStatus);
    Map<String, String> register(Student student);
    Map<String, Object> updateStudentInfo(Student student, HttpSession session);
    Map<String, String> login(Map<String, String> loginMap, HttpSession session);

    Result getRegisterUserList(HttpSession httpSession);

    Result updateUserRole(StudentRoleForm form, HttpSession httpSession);

    Result deleteUser(Long userId, HttpSession session);

    Result getPunchChart(HttpSession httpSession);
}
