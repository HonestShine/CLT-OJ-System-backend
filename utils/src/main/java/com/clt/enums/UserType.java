package com.clt.enums;

public enum UserType {
    ADMIN(0),  //管理员
    USER(1);    //普通用户

    public final int code;

    UserType(int code) {
        this.code = code;
    }

    public static int getCode(UserType userType) {
        return userType.code;
    }
}
