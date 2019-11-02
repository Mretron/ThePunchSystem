package com.yrmjhtdjxh.punch.service;


import com.yrmjhtdjxh.punch.domain.PunchRecord;

public interface PunchRecordService {

    void insert(PunchRecord punchRecord);

    PunchRecord getUnfinishPunchByStudnetID(Long studentID);

    void endRecordTime(PunchRecord punchRecord);

    void deleteShortPunchTime(Long studentID);

}
