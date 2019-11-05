package com.yrmjhtdjxh.punch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PunchRecord {

    /**
     * 作为自增主键
     */
    @Id
    private int id;

    /**
     * 外键关联到student表
     */
    private Long studentID;

    /**
     * 这次打卡的开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginPunchTime;

    /**
     * 这次打卡的结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endPunchTime;

    /**
     * 记录的这次的打卡时间，如3.6个小时
     */
    private Double recordTime;

    /**
     * 这次打卡的日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;


}
