package com.yrmjhtdjxh.punch.service;


import com.yrmjhtdjxh.punch.domain.PunchRecord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public interface PunchRecordService {


    Map<String, Object> startPunch(Map<String, String> startPunchMap, HttpSession session, HttpServletRequest request);

    Map<String, Object> endPunch(Map<String, String> startPunchMap, HttpSession session, HttpServletRequest request);
}
