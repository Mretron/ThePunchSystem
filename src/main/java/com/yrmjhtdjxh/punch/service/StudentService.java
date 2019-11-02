package com.yrmjhtdjxh.punch.service;

import com.yrmjhtdjxh.punch.domain.Student;

public interface StudentService {
    void insert(Student student);
    Student getOne(Long studentID);
    Student checkStudent(Long studentID, String password);
    void changePunch(Long studentID, int punchStatus);
    void updateStudentInfo(Student student);

}
