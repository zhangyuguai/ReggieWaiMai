package com.xiong.reggiewaimai.dto;

import com.xiong.reggiewaimai.bean.OrderDetail;
import com.xiong.reggiewaimai.bean.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author xsy
 * @date 2022/8/14
 */
@Data
public class OrderDto extends Orders {

    //订单明细
    private List<OrderDetail> orderDetails;

    //收件人姓名
    private String consignee;
}
