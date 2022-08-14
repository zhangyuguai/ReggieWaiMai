package com.xiong.reggiewaimai.service;

import com.xiong.reggiewaimai.bean.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiong.reggiewaimai.dto.DishDto;

/**
* @author LENOVO
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2022-08-11 19:29:15
*/
public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    void updateWithFlavor(DishDto dishDto);

    DishDto getWithFlavorById(Long id);
}
