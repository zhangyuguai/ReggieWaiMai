package com.xiong.reggiewaimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.reggiewaimai.bean.DishFlavor;
import com.xiong.reggiewaimai.service.DishFlavorService;
import com.xiong.reggiewaimai.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author LENOVO
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-08-12 22:34:39
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{



    @Override
    public List<DishFlavor> getFlavorListByDishId(Long id) {
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(id!=null,DishFlavor::getDishId,id);

        List<DishFlavor> dishFlavorList = this.list(queryWrapper);

        return dishFlavorList;
    }
}




