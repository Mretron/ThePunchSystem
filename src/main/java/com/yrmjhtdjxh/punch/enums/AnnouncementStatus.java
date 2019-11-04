package com.yrmjhtdjxh.punch.enums;

import lombok.Getter;

/**
 * @author dengg
 */

@Getter
public enum AnnouncementStatus {
    /**
     *
     */
    SAVE(2,"保存，未发布"),
    PUBLISHED(1,"已发布");

    private Integer value;

    private String tips;

    AnnouncementStatus(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }

}
