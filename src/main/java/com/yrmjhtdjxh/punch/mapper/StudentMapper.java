package com.yrmjhtdjxh.punch.mapper;

import com.yrmjhtdjxh.punch.domain.Student;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author etron
 */

public interface StudentMapper {

    void insert(Student student);

    Student getOne(Long studentID);

    Student findStudentByStudentIDAndPassword(Long studentID, String password);

    List<Student> getAll();

    void updatePunchByStudentID(Long studentID, int punchStatus);

    void updateStudentInfo(Student student);
}
