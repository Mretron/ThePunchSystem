package group.yrmjhtdjxh.punch.mapper;

import group.yrmjhtdjxh.punch.VO.TimeRecordVO;
import group.yrmjhtdjxh.punch.domain.PunchRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PunchRecordMapper {

    List<PunchRecord> findByStudentID(Long studentID);

    /**
     * 根据学号，直接mysql计算出当前学生的所有已经生效的打卡时间
     * @param studentID
     * @return
     */
    Double getTodayByStudentID(Long studentID);

    /**
     * 根据一周的开始和结束返回本周的打卡记录
     * @param studentID
     * @param beginTime
     * @param endTime
     * @return
     */
    Double getWeekByStudentID(String beginTime, String endTime, Long studentID);

    void insert(PunchRecord punchRecord);

    PunchRecord getUnfinishPunchByStudnetID(Long studentID);

    void endPunchRecord(PunchRecord punchRecord);

    void deleteByStudentID(Long studentID);

    List<TimeRecordVO> getChartByTimeAndUser(@Param("userId")Long userId, @Param("start")Date start, @Param("end")Date end);
}
