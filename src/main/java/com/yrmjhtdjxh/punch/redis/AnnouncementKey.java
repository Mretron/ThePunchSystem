package com.yrmjhtdjxh.punch.redis;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 */
public class AnnouncementKey extends BasePrefix{
    public AnnouncementKey(String prefix) {
        super(prefix);
    }

    public AnnouncementKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AnnouncementKey getById = new AnnouncementKey("announcementId");
    public static AnnouncementKey getClickTimesById = new AnnouncementKey("announcementClickTimes");
}
