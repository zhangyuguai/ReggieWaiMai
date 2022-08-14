package com.xiong.reggiewaimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiong.reggiewaimai.bean.ShoppingCart;
import com.xiong.reggiewaimai.common.BaseContext;
import com.xiong.reggiewaimai.common.R;
import com.xiong.reggiewaimai.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xsy
 * @date 2022/8/14
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long userId= BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId!= null,ShoppingCart::getUserId,userId)
                .orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        return R.success(shoppingCarts);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request){
        log.info("{}",shoppingCart);
        Long userId = (Long) request.getSession().getAttribute("user");
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId!=null,ShoppingCart::getUserId,userId)
                .eq(dishId!=null,ShoppingCart::getDishId,dishId)
                .eq(setmealId!=null,ShoppingCart::getSetmealId,setmealId);
        //数据库中查询到的cart
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        //增加份数
        if(cart!=null){
            Integer number = cart.getNumber();
            cart.setNumber(++number);
            shoppingCartService.updateById(cart);
            return R.success(cart);
        }

        if(userId!=null){
            shoppingCart.setUserId(userId);
            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }else {
            return R.error("操作失败");
        }

    }

    @PostMapping("/sub")
    public R<ShoppingCart> reducecar(@RequestBody ShoppingCart shoppingCart,HttpServletRequest request){

        Long userId = (Long) request.getSession().getAttribute("user");
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId!=null,ShoppingCart::getUserId,userId)
                .eq(dishId!=null,ShoppingCart::getDishId,dishId)
                .eq(setmealId!=null,ShoppingCart::getSetmealId,setmealId);
        //数据库中查询到的cart
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        //增加份数
        if(cart!=null){
            Integer number = cart.getNumber();
            cart.setNumber(--number);
            shoppingCartService.updateById(cart);
            return R.success(cart);
        }
        return R.error("修改失败");
    }

    @DeleteMapping("/clean")
    public R<String> cleanCar(HttpServletRequest request){

        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId!=null,ShoppingCart::getUserId,userId);

        if(shoppingCartService.remove(queryWrapper)){
         return R.success("清空购物车成功");
        }
        return R.error("操作失败");
    }
}
