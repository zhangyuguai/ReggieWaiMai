package com.xiong.reggiewaimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.reggiewaimai.bean.Setmeal;
import com.xiong.reggiewaimai.bean.SetmealDish;
import com.xiong.reggiewaimai.dto.SetmealDto;
import com.xiong.reggiewaimai.mapper.SetmealMapper;
import com.xiong.reggiewaimai.service.SetmealDishService;
import com.xiong.reggiewaimai.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author LENOVO
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2022-08-13 16:19:15
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 保存套餐，并且将相关联的菜品一同保存
     * @param setmealDto
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWithdish(SetmealDto setmealDto) {
        this.save(setmealDto);

        Long setmealDtoId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDtoId);
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据套餐id获取绑定有菜品的套餐
     * @param setmealDto
     * @return
     */
    @Override
    public SetmealDto getWithDishes(SetmealDto setmealDto) {

        Setmeal setmeal = this.getById(setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmealDto.getId()!=null,SetmealDish::getSetmealId,setmealDto.getId())
                        .orderByAsc(SetmealDish::getSort).orderByDesc(SetmealDish::getUpdateTime);


        BeanUtils.copyProperties(setmeal,setmealDto);
        List<SetmealDish> dishList = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(dishList);

        return setmealDto;
    }

    @Override
    public void updateWithDishes(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> dishList = setmealDto.getSetmealDishes();
        Long setmealId = setmealDto.getId();
        for (SetmealDish dish : dishList) {
            dish.setSetmealId(setmealId);
        }
        setmealDishService.saveBatch(dishList);
    }
}




