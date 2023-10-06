package com.example.demo.interception;

import com.example.demo.pojo.TUser;

/**
 * @ClassName UserContext
 * @Description TODO
 * @Date 2023/10/5 11:29
 */
public class UserContext {
    private static final ThreadLocal<TUser> userHolder = new ThreadLocal<>();

    public static void setUser(TUser user) {
        userHolder.set(user);
    }

    public static TUser getUser() {
        return userHolder.get();
    }

    public static void removeUser() {
        userHolder.remove();
    }
}

