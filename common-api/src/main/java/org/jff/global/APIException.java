package org.jff.global;

import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private final int code;
    private final String message;


    public APIException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public APIException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }


    public APIException(String message) {
        this(1001, message);
    }

}
