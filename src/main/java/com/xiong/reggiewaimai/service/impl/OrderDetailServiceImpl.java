package com.xiong.reggiewaimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.reggiewaimai.bean.OrderDetail;
import com.xiong.reggiewaimai.service.OrderDetailService;
import com.xiong.reggiewaimai.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author LENOVO
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-08-14 22:15:26
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




