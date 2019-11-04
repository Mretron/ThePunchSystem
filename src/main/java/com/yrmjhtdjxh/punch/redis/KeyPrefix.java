package com.yrmjhtdjxh.punch.redis;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 */
public interface KeyPrefix {
    /**
     * 获取过期时间
     * @return
     */
    int expireSeconds();

    /**
     * 获取key前缀
     * @return
     */
    String getPrefix();
}
