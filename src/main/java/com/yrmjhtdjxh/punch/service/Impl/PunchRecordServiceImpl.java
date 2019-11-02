package com.yrmjhtdjxh.punch.service.Impl;

import com.yrmjhtdjxh.punch.domain.PunchRecord;
import com.yrmjhtdjxh.punch.mapper.PunchRecordMapper;
import com.yrmjhtdjxh.punch.service.PunchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PunchRecordServiceImpl implements PunchRecordService {

    @Autowired
    private PunchRecordMapper punchRecordMapper;

    @Override
    public void insert(PunchRecord punchRecord) {
        punchRecordMapper.insert(punchRecord);
    }

    @Override
    public PunchRecord getUnfinishPunchByStudnetID(Long studentID) {
        return punchRecordMapper.getUnfinishPunchByStudnetID(studentID);
    }

    @Override
    public void endRecordTime(PunchRecord punchRecord) {
        punchRecordMapper.endPunchRecord(punchRecord);
    }

    @Override
    public void deleteShortPunchTime(Long studentID) {
        punchRecordMapper.deleteByStudentID(studentID);
    }
}
