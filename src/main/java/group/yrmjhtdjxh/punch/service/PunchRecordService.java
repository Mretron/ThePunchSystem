package group.yrmjhtdjxh.punch.service;


import group.yrmjhtdjxh.punch.domain.PunchRecord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public interface PunchRecordService {

    void insert(PunchRecord punchRecord);

    PunchRecord getUnfinishPunchByStudnetID(Long studentID);

    void endRecordTime(PunchRecord punchRecord);

    void deleteShortPunchTime(Long studentID);

    Map<String, Object> startPunch(Map<String, String> startPunchMap, HttpSession session, HttpServletRequest request);

    Map<String, Object> endPunch(Map<String, String> startPunchMap, HttpSession session, HttpServletRequest request);
}
