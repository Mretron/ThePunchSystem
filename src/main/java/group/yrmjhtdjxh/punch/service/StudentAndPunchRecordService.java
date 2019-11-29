package group.yrmjhtdjxh.punch.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface StudentAndPunchRecordService {

    Map<String, Object> getStudentAndPunchInfo(HttpSession session);
}
