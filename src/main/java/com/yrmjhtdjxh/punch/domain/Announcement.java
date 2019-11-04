package com.yrmjhtdjxh.punch.domain;

import lombok.Data;

import java.util.Date;


/**
 * @author dengg
 */
@Data
public class Announcement {

    private Long id;

    private Long publisherId;

    private String title;

    private String content;

    private Date publishTime;

    private Date updateTime;

    private Integer status;

}