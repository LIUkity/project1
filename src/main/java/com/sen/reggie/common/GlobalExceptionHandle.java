package com.sen.reggie.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;


@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandle {
        @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
public R<String> exceptionHandle(SQLIntegrityConstraintViolationException e) {
        if(e.getMessage().contains("Duplicate entry")){
            String[] split = e.getMessage().split(" ");
            String msg=split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("失败了");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandle(CustomException e) {

        return R.error(e.getMessage());
    }
}
