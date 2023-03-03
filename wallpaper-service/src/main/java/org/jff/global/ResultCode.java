package org.jff.global;

import lombok.Getter;

@Getter
public enum ResultCode implements ResultEnumerable{
    SUCCESS(200, "操作成功"),
    FAILED(999, "响应失败"),

    SET_IS_NOT_FOUND(1001, "未找到该集合"),

    AUTHORIZATION_ERROR(2001, "您没有该权限");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
