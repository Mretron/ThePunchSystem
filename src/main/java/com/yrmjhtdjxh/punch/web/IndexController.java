package com.yrmjhtdjxh.punch.web;

import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.mapper.StudentRoleMapper;
import com.yrmjhtdjxh.punch.service.PunchRecordService;
import com.yrmjhtdjxh.punch.service.StudentAndPunchRecordService;
import com.yrmjhtdjxh.punch.service.StudentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author etron
 *
 */
@ResponseBody
@Controller
@Api(tags = "主页")
public class IndexController {

   /* @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRoleMapper studentRoleMapper;
*/

    private StudentAndPunchRecordService studentAndPunchRecordService;

    private PunchRecordService punchRecordService;

    @Autowired
    public IndexController(StudentAndPunchRecordService studentAndPunchRecordService, PunchRecordService punchRecordService) {
        this.studentAndPunchRecordService = studentAndPunchRecordService;
        this.punchRecordService = punchRecordService;
    }

    /**
     * 提供首页展示当前学生和目前的打卡信息（所有人的打卡排名情况）
     * @return
     */
    @GetMapping("/getStudentAndPunchInfo")
    public Map<String, Object> getStudentAndPunchInfo(HttpSession session) {
        return studentAndPunchRecordService.getStudentAndPunchInfo(session);
    }

    /**
     * 开始打卡的接口
     * @param startPunchMap
     * @param session
     * @return
     */
    @PostMapping("/startPunch")
    public Map<String ,Object> startPunch(@RequestBody Map<String, String> startPunchMap,
                                          HttpSession session,
                                          HttpServletRequest request) {
        return punchRecordService.startPunch(startPunchMap,session,request);
    }

    /**
     * 结束打卡的接口
     * @param startPunchMap,
     * @param session
     * @return
     */
    @PostMapping("/endPunch")
    public Map<String, Object> endPunch(@RequestBody Map<String, String> startPunchMap,
                                        HttpSession session,
                                        HttpServletRequest request){
        return punchRecordService.endPunch(startPunchMap,session,request);
    }


    /**
     * 只是为了更新数据库
     * @return
     */
/*    @GetMapping("/updateDatabase")
    public Map<String, String> updateDatabase() {

        List<String> studentsID = studentService.getAllStudentID();

        for(String temp : studentsID) {
            studentRoleMapper.insert(Long.parseLong(temp), 2);
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "yes");
        return map;
    }*/

}
