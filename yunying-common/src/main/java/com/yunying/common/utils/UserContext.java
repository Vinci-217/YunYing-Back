package com.yunying.common.utils;

/**
 * ThreadLocal存储当前登录用户信息
 */
public class UserContext {
    private static final ThreadLocal<Long> userIds = new ThreadLocal<>();

    /**
     * 保存当前登录用户信息到ThreadLocal
     *
     * @param userId 用户id
     */
    public static void setUserIds(Long userId) {
        userIds.set(userId);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户id
     */
    public static Long getUserIds() {
        return userIds.get();
    }

    /**
     * 移除当前登录用户信息
     */
    public static void removeUser() {
        userIds.remove();
    }
}