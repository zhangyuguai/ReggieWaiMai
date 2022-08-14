package com.xiong.reggiewaimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiong.reggiewaimai.bean.AddressBook;
import com.xiong.reggiewaimai.bean.OrderDetail;
import com.xiong.reggiewaimai.bean.Orders;
import com.xiong.reggiewaimai.bean.User;
import com.xiong.reggiewaimai.common.R;
import com.xiong.reggiewaimai.dto.OrderDto;
import com.xiong.reggiewaimai.service.AddressBookService;
import com.xiong.reggiewaimai.service.OrderDetailService;
import com.xiong.reggiewaimai.service.OrdersService;
import com.xiong.reggiewaimai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xsy
 * @date 2022/8/14
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @PostMapping("submit")
    public R<String> submit(@RequestBody Orders order){
        ordersService.submit(order);
        return R.success("下单成功");
    }

    /**
     * 管理端和用户端查询订单共用
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping({"/userPage","/page"})
    public R<Page> page(Integer page, Integer pageSize, Long number, String  beginTime,String endTime){

        Page<Orders> ordersPage = new Page<>(page,pageSize);


        Page<OrderDto> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.like(number!=null,Orders::getNumber,number)
                        .between(beginTime!=null&&endTime!=null,Orders::getCheckoutTime,beginTime,endTime);
        ordersService.page(ordersPage,ordersLambdaQueryWrapper);

        BeanUtils.copyProperties(ordersPage,pageInfo,"records");

        List<Orders> records = ordersPage.getRecords();
        List<OrderDto> orderDtoList=new ArrayList<>();
        for (Orders record : records) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(record,orderDto);
            Long orderId = record.getId();

            //查询订单明细
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(orderId!=null,OrderDetail::getOrderId,orderId);

            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
            orderDto.setOrderDetails(orderDetails);

            //设置用户姓名
            Long userId = record.getUserId();
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(userId!=null,User::getId,userId);
            User user = userService.getOne(userLambdaQueryWrapper);
            if(user!=null){
                String userName = user.getName();
                orderDto.setUserName(userName);
            }

            //查询收件人姓名
            Long addressBookId = record.getAddressBookId();
            LambdaQueryWrapper<AddressBook> addressQueryWrapper = new LambdaQueryWrapper<>();
            addressQueryWrapper.eq(addressBookId!=null,AddressBook::getId,addressBookId);
            AddressBook addressBook = addressBookService.getOne(addressQueryWrapper);
            if(addressBook!=null){
                String consignee = addressBook.getConsignee();
                orderDto.setConsignee(consignee);
            }



            //添加到订单列表
            orderDtoList.add(orderDto);
        }

        pageInfo.setRecords(orderDtoList);


        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> changeStatus(@RequestBody Orders orders){
        LambdaUpdateWrapper<Orders> ordersUpdateWrapper = new LambdaUpdateWrapper<>();
        ordersUpdateWrapper.eq(orders.getId()!=null,Orders::getId,orders.getId())
                .set(orders.getStatus()!=null,Orders::getStatus,orders.getStatus());
        if(ordersService.update(ordersUpdateWrapper)){
            return R.success("状态修改成功");
        }
        return R.error("操作失败");

    }
}
