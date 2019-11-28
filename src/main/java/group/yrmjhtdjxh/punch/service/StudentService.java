package group.yrmjhtdjxh.punch.service;

import group.yrmjhtdjxh.punch.VO.Result;
import group.yrmjhtdjxh.punch.VO.StudentVO;
import group.yrmjhtdjxh.punch.domain.Student;
import group.yrmjhtdjxh.punch.form.StudentForm;
import group.yrmjhtdjxh.punch.form.StudentRoleForm;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface StudentService {
    void insert(Student student);
    StudentVO getOne(Long studentID);
    StudentVO checkStudent(Long studentID, String password);
    void changePunch(Long studentID, int punchStatus);
    Map<String, String> register(StudentForm student);
    Map<String, Object> updateStudentInfo(Student student, HttpSession session);
    Map<String, String> login(Map<String, String> loginMap, HttpSession session);

    Result getRegisterUserList(HttpSession httpSession);

    Result updateUserRole(StudentRoleForm form, HttpSession httpSession);

    Result deleteUser(Long userId, HttpSession session);

    Result getPunchChart(HttpSession httpSession);
}
