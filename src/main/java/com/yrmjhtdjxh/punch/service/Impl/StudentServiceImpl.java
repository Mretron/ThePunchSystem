package com.yrmjhtdjxh.punch.service.Impl;

import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.mapper.StudentMapper;
import com.yrmjhtdjxh.punch.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void changePunch(Long studentID, int punchStatus) {
        studentMapper.updatePunchByStudentID(studentID, punchStatus);
    }

    @Override
    public void updateStudentInfo(Student student) {
        studentMapper.updateStudentInfo(student);
    }
}
