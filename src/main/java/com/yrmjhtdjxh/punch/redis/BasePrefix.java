package com.yrmjhtdjxh.punch.redis;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 实现缓存前缀
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix) {
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return this.expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+prefix;
    }
}
