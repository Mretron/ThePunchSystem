package com.yrmjhtdjxh.punch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 首页展示的domain数据类
 */
@Data
@AllArgsConstructor
public class IndexStudent {
    /**
     * 该student的头像
     */
    private String avatar;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年级
     */
    private int grade;

    /**
     * 本周打卡时间
     */
    private Double weekTime;

    /**
     * 今日打卡时间
     */
    private Double todayTime;

    /**
     * 本周剩余打卡时间
     */
    private Double weekLeftTime;

    /**
     * 是否正在打卡
     */
    private boolean isPunch;


    public IndexStudent() {
    }
}
