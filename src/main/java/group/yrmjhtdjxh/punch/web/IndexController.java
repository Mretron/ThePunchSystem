package group.yrmjhtdjxh.punch.web;

import group.yrmjhtdjxh.punch.service.PunchRecordService;
import group.yrmjhtdjxh.punch.service.StudentAndPunchRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author etron
 *
 */
@ResponseBody
@Controller
@Api(tags = "主页")
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


}
