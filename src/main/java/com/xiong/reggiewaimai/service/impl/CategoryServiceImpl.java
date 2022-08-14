package com.xiong.reggiewaimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.reggiewaimai.bean.Category;
import com.xiong.reggiewaimai.bean.Dish;
import com.xiong.reggiewaimai.bean.Setmeal;
import com.xiong.reggiewaimai.common.exception.CustomException;
import com.xiong.reggiewaimai.mapper.CategoryMapper;
import com.xiong.reggiewaimai.mapper.DishMapper;
import com.xiong.reggiewaimai.mapper.SetmealMapper;
import com.xiong.reggiewaimai.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author LENOVO
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2022-08-11 18:23:45
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 删除指定id的菜品分类
     * @param ids
     */
    @Override
    public void remove(Long ids) {
        //查询是否有相关联的菜品，如果有，拒绝删除，抛出业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        Long count1 = dishMapper.selectCount(dishLambdaQueryWrapper);

        if(count1>0){
            //关联有菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品,不能进行删除");
        }

        //查询是否有相关联的套餐，如果有，拒绝删除，抛出业务异常

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        Long count2 = setmealMapper.selectCount(setmealLambdaQueryWrapper);

        if(count2>0){
            //关联有菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品,不能进行删除");
        }

        //删除相关菜品分类
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getId,ids);
        remove(categoryLambdaQueryWrapper);

    }
}




