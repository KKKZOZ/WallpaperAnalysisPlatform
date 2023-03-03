package org.jff.global;

import lombok.Getter;

@Getter
public class ResponseVO<T> {

    private final int code;
    private final String msg;
    private final T data;


    public ResponseVO(T data) {
        this(ResultCode.SUCCESS, data);
    }
    public ResponseVO(ResultEnumerable resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
        this.data = data;
    }

    public ResponseVO(int code, String message){
        this.code = code;
        this.msg = message;
        this.data = null;
    }
    public ResponseVO(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
        this.data = null;
    }



}
