package com.xiong.reggiewaimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiong.reggiewaimai.bean.Orders;

/**
* @author LENOVO
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2022-08-14 22:15:20
*/
public interface OrdersService extends IService<Orders> {

    void submit(Orders order);
}
