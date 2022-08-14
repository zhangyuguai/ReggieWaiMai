package com.xiong.reggiewaimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiong.reggiewaimai.bean.User;
import com.xiong.reggiewaimai.common.R;
import com.xiong.reggiewaimai.dto.UserDto;
import com.xiong.reggiewaimai.service.UserService;
import com.xiong.reggiewaimai.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xsy
 * @date 2022/8/13
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/sendMsg")
    public R<String> senPhoneCode(@RequestBody User user, HttpServletRequest request) {

        //生成一个验证码
        String codeString = ValidateCodeUtils.generateValidateCode4String(4);
        //通过短信服务发送给指定手机
        //将code存在session中
        request.getSession().setAttribute("code", codeString);
        log.info("手机验证码{}", codeString);
        return R.success("手机验证码发送成功");
    }


    @PostMapping("/login")
    public R<UserDto> login(@RequestBody UserDto userDto, HttpServletRequest request) {

        //查询数据库中是否有这个号码
        String phone = userDto.getPhone();
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(userDto.getPhone() != null, User::getPhone, phone);

        String code = (String) request.getSession().getAttribute("code");
        String userDtoCode = userDto.getCode();
        User user = userService.getOne(lambdaQueryWrapper);
        //数据库中有且验证码正确
        if (user != null && code.equals(userDtoCode)){
            request.getSession().setAttribute("user",user.getId());
            return R.success(userDto);
            //第一次登陆数据库中没有
        } else if (code.equals(userDtoCode)){
            user=new User();
            BeanUtils.copyProperties(userDto,user);
            userService.save(user);
            request.getSession().setAttribute("user",user.getId());
            return R.success(userDto);
        }
        return R.error("登陆失败");
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("成功退出");
    }
}
