package com.sen.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Component
@Slf4j
public class MyMetaObjectHandle implements MetaObjectHandler {
    @Autowired
    private HttpServletRequest request;
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime",LocalDateTime.now());
        //metaObject.setValue("createUser",request.getSession().getAttribute("employee"));
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //metaObject.setValue("updateUser",request.getSession().getAttribute("employee"));
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        long id = Thread.currentThread().getId();
        log.info("线程id："+id);
        metaObject.setValue("updateTime", LocalDateTime.now());
        //metaObject.setValue("updateUser",request.getSession().getAttribute("employee"));
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
