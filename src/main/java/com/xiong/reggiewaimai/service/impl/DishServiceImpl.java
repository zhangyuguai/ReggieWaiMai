package com.xiong.reggiewaimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.reggiewaimai.bean.Category;
import com.xiong.reggiewaimai.bean.Dish;
import com.xiong.reggiewaimai.bean.DishFlavor;
import com.xiong.reggiewaimai.dto.DishDto;
import com.xiong.reggiewaimai.mapper.DishMapper;
import com.xiong.reggiewaimai.service.CategoryService;
import com.xiong.reggiewaimai.service.DishFlavorService;
import com.xiong.reggiewaimai.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author LENOVO
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2022-08-11 19:29:15
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWithFlavor(DishDto dishDto) {

        //保存菜品
        this.save(dishDto);


        //给菜品口味设置菜品id
        List<DishFlavor> flavors = dishDto.getFlavors();
        Long dishId = dishDto.getId();
        for (DishFlavor dishFlavor : flavors) {
            dishFlavor.setDishId(dishId);
        }


        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        Long dishId = dishDto.getId();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto getWithFlavorById(Long id) {
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);



        //查询分类
        Long categoryId = dish.getCategoryId();
        Category category = categoryService.getById(categoryId);

        if(category!=null){
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
        }

        List<DishFlavor> dishFlavorList = dishFlavorService.getFlavorListByDishId(id);

        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }
}




