package com.yrmjhtdjxh.punch.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class AnnouncementPublishForm {

    /**
     * 公告标题
     */
    @Length(max = 50, message = "字数不能超过50字")
    @NotNull(message = "标题不能为空")
    private String title;

    /**
     * 公告内容,html字串
     */
    @NotNull(message = "公告内容不能为空")
    private String content;
}
