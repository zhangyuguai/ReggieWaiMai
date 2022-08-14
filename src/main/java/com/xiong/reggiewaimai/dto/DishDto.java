package com.xiong.reggiewaimai.dto;


import com.xiong.reggiewaimai.bean.Dish;
import com.xiong.reggiewaimai.bean.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
