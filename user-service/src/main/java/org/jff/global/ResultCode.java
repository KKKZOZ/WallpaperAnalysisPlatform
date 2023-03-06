package org.jff.global;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(999, "响应失败"),

    TOKEN_INVALID(5000,"Token非法"),
    LOGIN_FAILED(1000,"用户名或者密码错误"),
    LOGIN_SUCCESS(1001,"登录成功"),
    LOGOUT_SUCCESS(1002,"退出成功"),

    ACTIVATION_CODE_EXPIRED(2000,"激活码已过期"),
    USER_NOT_ACTIVATED(2001,"用户未激活，请查看邮箱通过验证码激活"),
    USERNAME_ALREADY_EXIST(2002,"用户名已存在"),
    ;


    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
