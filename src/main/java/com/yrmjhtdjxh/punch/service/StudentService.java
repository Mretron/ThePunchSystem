package com.yrmjhtdjxh.punch.service;

import com.yrmjhtdjxh.punch.domain.Student;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface StudentService {
    void insert(Student student);
    Student getOne(Long studentID);
    Student checkStudent(Long studentID, String password);
    void changePunch(Long studentID, int punchStatus);
    Map<String, String> register(Student student);
    Map<String, Object> updateStudentInfo(Student student, HttpSession session);
    Map<String, String> login(Map<String, String> loginMap, HttpSession session);
}
