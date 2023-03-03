package org.jff.global;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.kkkzoz"})
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVO<String> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return new ResponseVO<>(ResultCode.FAILED, objectError.getDefaultMessage());
    }


    @ExceptionHandler(APIException.class)
    public ResponseVO<String> APIExceptionHandler(APIException e) {
        return new ResponseVO<>(e.getCode(), e.getMessage());
    }

}
