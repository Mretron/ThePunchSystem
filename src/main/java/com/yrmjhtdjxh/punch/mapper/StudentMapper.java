package com.yrmjhtdjxh.punch.mapper;

import com.yrmjhtdjxh.punch.VO.StudentVO;
import com.yrmjhtdjxh.punch.domain.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author etron
 */
@Repository
public interface StudentMapper {

    void insert(Student student);

    Student getOne(Long studentID);

    StudentVO findStudentByStudentIDAndPassword(Long studentID, String password);

    List<StudentVO> getAllByRole(@Param("userRole")Integer userRole);

    void updatePunchByStudentID(Long studentID, int punchStatus);

    int deleteStudentByUserId(@Param("userId")Long userId);

    void updateStudentInfo(Student student);
}
