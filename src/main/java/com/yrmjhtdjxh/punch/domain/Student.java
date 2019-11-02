package com.yrmjhtdjxh.punch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.util.Date;

@Data
@AllArgsConstructor
public class Student {
    /**
     * 作为自增主键
     */
    @Id
    @Column(keyColumn = "id")
    private int id;

    /**
     * 学号
     */
    private Long studentID;

    /**
     * 密码
     */
    private String password;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 性别 1：男 0：女
     */
    private int sex;

    /**
     * 学生的年级
     */
    private int grade;

    /**
     * 学生的初始照片
     */
    private String photo;

    /**
     * 学生的头像
     */
    private String avatar;

    /**
     * 创建当前学生的日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否在打卡
     */
    private boolean isPunch;




}
