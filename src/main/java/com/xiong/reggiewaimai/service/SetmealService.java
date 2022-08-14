package com.xiong.reggiewaimai.service;

import com.xiong.reggiewaimai.bean.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiong.reggiewaimai.dto.SetmealDto;

/**
* @author LENOVO
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-08-13 16:19:15
*/
public interface SetmealService extends IService<Setmeal> {

    void saveWithdish(SetmealDto setmealDto);

    SetmealDto getWithDishes(SetmealDto setmealDto);

    void updateWithDishes(SetmealDto setmealDto);
}
