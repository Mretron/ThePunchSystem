package com.yrmjhtdjxh.punch.web;

import com.yrmjhtdjxh.punch.domain.IndexStudent;
import com.yrmjhtdjxh.punch.domain.PunchRecord;
import com.yrmjhtdjxh.punch.domain.Student;
import com.yrmjhtdjxh.punch.service.PunchRecordService;
import com.yrmjhtdjxh.punch.service.StudentAndPunchRecordService;
import com.yrmjhtdjxh.punch.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author etron
 *
 */
@ResponseBody
@Controller
public class IndexController {


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
    @RequestMapping("/getStudentAndPunchInfo")
    public Map<String, Object> getStudentAndPunchInfo(HttpSession session) {
        return studentAndPunchRecordService.getStudentAndPunchInfo(session);
    }

    /**
     * 开始打卡的接口
     * @param startPunchMap
     * @param session
     * @return
     */
    @RequestMapping("/startPunch")
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
    @RequestMapping("/endPunch")
    public Map<String, Object> endPunch(@RequestBody Map<String, String> startPunchMap,
                                        HttpSession session,
                                        HttpServletRequest request){
        return punchRecordService.endPunch(startPunchMap,session,request);
    }

}
