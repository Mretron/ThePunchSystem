package group.yrmjhtdjxh.punch.mapper;

import group.yrmjhtdjxh.punch.domain.PunchRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PunchRecordMapperTest {

    @Autowired
    private PunchRecordMapper punchRecordMapper;

    @Test
    public void insert() {
        List<PunchRecord> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            PunchRecord punchRecord = new PunchRecord();
            punchRecord.setStudentID(201731062632L);
            punchRecord.setDate(new Date(new Date().getTime()+i*86400000));
            punchRecord.setRecordTime(3.5+i*0.11);
            list.add(punchRecord);
        }
        for (PunchRecord p:list
             ) {
            punchRecordMapper.insert(p);
        }
    }

}