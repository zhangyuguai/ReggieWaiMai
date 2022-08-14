package com.xiong.reggiewaimai.common.handler;

import com.xiong.reggiewaimai.common.R;
import com.xiong.reggiewaimai.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author xsy
 * @date 2022/8/10
 */
@ControllerAdvice(annotations = {RestController.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandle(SQLIntegrityConstraintViolationException exception) {
        log.info("错误信息{}", exception.toString());

        if(exception.getMessage().contains("Duplicate entry")){
            String[] split = exception.getMessage().split(" ");
            String msg=split[2]+"已经存在了";
            return R.error(msg);
        }
        return null;
    }

    @ExceptionHandler(value = CustomException.class)
    public R<String> exceptionHandle(CustomException exception) {
        log.info("错误信息{}", exception.toString());
        return R.error(exception.getMessage());
    }
}
