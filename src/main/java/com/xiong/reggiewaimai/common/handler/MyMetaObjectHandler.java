package com.xiong.reggiewaimai.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xiong.reggiewaimai.common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author xsy
 * @date 2022/8/11
 * <p>
 * 插入或更新操作的同时，自动填充公共字段
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert已执行");
        if(metaObject.hasSetter("updateUser")){
            metaObject.setValue("updateUser", BaseContext.getCurrentId());
        }
        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime", LocalDateTime.now());
        }
        if(metaObject.hasSetter("createUser")){
            metaObject.setValue("createUser", BaseContext.getCurrentId());
        }
        if(metaObject.hasSetter("createTime")){
            metaObject.setValue("createTime", LocalDateTime.now());
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update已执行");
        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime", LocalDateTime.now());
        }
        if(metaObject.hasSetter("updateUser")) {
            metaObject.setValue("updateUser", BaseContext.getCurrentId());
        }
    }
}
