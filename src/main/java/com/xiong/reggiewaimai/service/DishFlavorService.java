package com.xiong.reggiewaimai.service;

import com.xiong.reggiewaimai.bean.DishFlavor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author LENOVO
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service
* @createDate 2022-08-12 22:34:39
*/
public interface DishFlavorService extends IService<DishFlavor> {


    List<DishFlavor> getFlavorListByDishId(Long id);
}
