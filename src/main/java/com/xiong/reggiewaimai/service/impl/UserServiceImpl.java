package com.xiong.reggiewaimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.reggiewaimai.bean.User;
import com.xiong.reggiewaimai.service.UserService;
import com.xiong.reggiewaimai.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author LENOVO
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-08-13 21:22:14
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




