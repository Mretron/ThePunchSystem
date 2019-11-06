package com.yrmjhtdjxh.punch.VO;

import com.yrmjhtdjxh.punch.enums.AnnouncementStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dengg
 */
@Data
public class AnnouncementListVO implements Serializable {
    /**
     * 公告id
     */
    private Long announcementId;
    /**
     * 标题
     */
    private String title;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 状态 1，已发布 2，未发布  {@link AnnouncementStatus}
     */
    private Integer status;
}
