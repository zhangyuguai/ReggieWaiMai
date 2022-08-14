package com.xiong.reggiewaimai.dto;

import com.xiong.reggiewaimai.bean.Setmeal;
import com.xiong.reggiewaimai.bean.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
