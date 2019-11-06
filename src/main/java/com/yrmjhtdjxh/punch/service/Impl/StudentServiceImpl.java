package com.yrmjhtdjxh.punch.service.Impl;

import com.yrmjhtdjxh.punch.VO.Result;
import com.yrmjhtdjxh.punch.VO.StudentVO;
import com.yrmjhtdjxh.punch.VO.TimeRecordVO;
import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.enums.UserRole;
import com.yrmjhtdjxh.punch.form.StudentRoleForm;
import com.yrmjhtdjxh.punch.mapper.PunchRecordMapper;
import com.yrmjhtdjxh.punch.mapper.StudentMapper;
import com.yrmjhtdjxh.punch.mapper.StudentRoleMapper;
import com.yrmjhtdjxh.punch.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentRoleMapper studentRoleMapper;

    @Autowired
    private PunchRecordMapper punchRecordMapper;

    @Override
    public void insert(Student student) {
        studentMapper.insert(student);
    }

    @Override
    public StudentVO getOne(Long studentID) {
        return studentMapper.getOne(studentID);
    }

    @Override
    public StudentVO checkStudent(Long studentID, String password) {
        return studentMapper.findStudentByStudentIDAndPassword(studentID, password);
    }

    @Override
    public Result getRegisterUserList(HttpSession httpSession) {
        if (!userAuthentication(httpSession,UserRole.ADMINISTRATOR)){
            return Result.error(403,"权限不足");
        }
        return Result.success(studentMapper.getAllByRole(null));
    }

    @Override
    public Result deleteUser(Long userId, HttpSession session) {
        if (!userAuthentication(session,UserRole.ADMINISTRATOR)){
            return Result.error(403,"权限不足");
        }
        if (userId == null){
            return Result.error(500,"请求参数不为空");
        }
        StudentVO studentVO = (StudentVO) session.getAttribute("student");
        if (userId == studentVO.getId()) {
            return Result.error(500,"删除自己？");
        }
        //删除角色信息
        studentRoleMapper.deleteByUserId(userId);
        //删除用户信息
        studentMapper.deleteStudentByUserId(userId);
        //删除打卡信息
        punchRecordMapper.deleteByStudentID(userId);
        return Result.success();
    }

    @Override
    public Result updateUserRole(StudentRoleForm form, HttpSession httpSession) {
        if (!userAuthentication(httpSession,UserRole.AVERAGE_USER)){
            return Result.error(403,"权限不足");
        }
        if (studentMapper.getOne(form.getUserId()) == null){
            return Result.error(500,"用户不存在");
        }
        studentRoleMapper.updateUserRole(form.getUserId(),form.getUserRole());
        return Result.success();
    }

    @Override
    public Map<String, String> register(Student student) {
        Map<String, String> map = new HashMap<>();

        // 如果已经存在该学号
        if(getOne(student.getStudentID()) != null ){
            map.put("status", "fail");
            map.put("error", "该学号已经存在");
            return map;
        }

        student.setId(0);
        student.setGrade(getGrade(student.getStudentID()));
        student.setCreateTime(new Date());
        student.setPassword(student.getPassword());

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
            insert(student);
            studentRoleMapper.insert(student.getStudentID(),UserRole.REGISTER_USER.getValue());
            map.put("status", "success");
        }catch (Exception e) {
            map.put("status", "fail");
        }
        return map;
    }

    @Override
    public Map<String, Object> updateStudentInfo(Student student, HttpSession session) {
        Map<String , Object> map = new HashMap<>();
        Student student1  = (Student) session.getAttribute("student");
        // 没有登录
        if(student1 == null) {
            map.put("status", "fail");
            return map;
        }
        studentMapper.updateStudentInfo(student);
        session.setAttribute("student", student);
        map.put("status", "success");
        return map;
    }


    @Override
    public Map<String, String> login(Map<String, String> loginMap, HttpSession session) {
        Long studentID = Long.parseLong(loginMap.get("studentID"));
        String password = loginMap.get("password");
        Map<String, String> map = new HashMap<>();
        // 后面来看是否需要MD5加密
        StudentVO student = checkStudent(studentID, password);
        if(student == null || student.getUserRole().equals(UserRole.REGISTER_USER.getValue())) {
            map.put("status","fail");
        } else {
            map.put("status","success");
            map.put("role", String.valueOf(student.getUserRole()));
            // 登录成功，放入session
            session.setAttribute("student",student);

        }
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

    @Override
    public void changePunch(Long studentID, int punchStatus) {
        studentMapper.updatePunchByStudentID(studentID, punchStatus);
    }

    @Override
    public Result getPunchChart(HttpSession httpSession) {
        if (!userAuthentication(httpSession,UserRole.AVERAGE_USER)){
            return Result.error(403,"权限不足");
        }
        StudentVO studentVO = (StudentVO) httpSession.getAttribute("student");
        List<TimeRecordVO> list = punchRecordMapper.getChartByTimeAndUser(studentVO.getStudentID(),new Date(System.currentTimeMillis()-30*86400000L),new Date());
        return Result.success(list);
    }

    private boolean userAuthentication(HttpSession httpSession, UserRole userRole) {
        StudentVO studentVO = (StudentVO) httpSession.getAttribute("student");
        if (studentVO == null){
            return false;
        }else {
            return userRole == null || userRole.getValue() >= (studentVO.getUserRole());
        }
    }
}
