package com.xiong.reggiewaimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiong.reggiewaimai.bean.Category;
import com.xiong.reggiewaimai.bean.Setmeal;
import com.xiong.reggiewaimai.bean.SetmealDish;
import com.xiong.reggiewaimai.common.R;
import com.xiong.reggiewaimai.dto.SetmealDto;
import com.xiong.reggiewaimai.service.CategoryService;
import com.xiong.reggiewaimai.service.SetmealDishService;
import com.xiong.reggiewaimai.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xsy
 * @date 2022/8/13
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> add(@RequestBody SetmealDto setmealDto){
        log.info("信息{}",setmealDto);

        setmealService.saveWithdish(setmealDto);
        return R.success("套餐保存成功");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);

        Page<SetmealDto> pageInfo=new Page<>(page,pageSize);




        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(name),Setmeal::getName,name)
                .orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(setmealPage,queryWrapper);

        BeanUtils.copyProperties(setmealPage,pageInfo,"records");
        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDto> setmealDtoRecords=new ArrayList<>();
        for (Setmeal record : records) {
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(record,setmealDto);
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            setmealDtoRecords.add(setmealDto);
        }
        pageInfo.setRecords(setmealDtoRecords);

        return R.success(pageInfo);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getById(SetmealDto setmealDto){
        setmealDto = setmealService.getWithDishes(setmealDto);


        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDishes(setmealDto);

        return R.success("修改成功");
    }

    /**
     * 改变套餐状态，单选和多选二合一
     *
     * @param status
     * @param setmealIds
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable("status") Integer status, @RequestParam("ids") List<Long> setmealIds) {
        List<Setmeal> setmealList = new ArrayList<>(setmealIds.size());
        for (int i = 0; i < setmealIds.size(); i++) {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            Long setmealId = setmealIds.get(i);
            setmeal.setId(setmealId);
            setmealList.add(setmeal);
        }
        setmealService.updateBatchById(setmealList);
        return R.success("修改成功");
    }

    /**
     * 逻辑删除套餐
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> setmealIds) {
        List<Setmeal> setmealList = new ArrayList<>(setmealIds.size());
        for (int i = 0; i < setmealIds.size(); i++) {
            Setmeal setmeal = new Setmeal();
            //逻辑删除顺带将状态改为停售
            setmeal.setStatus(0);
            Long setmealId = setmealIds.get(i);
            setmeal.setId(setmealId);
            setmealList.add(setmeal);
        }
        setmealService.removeBatchByIds(setmealList);
        return R.success("删除成功");
    }

    /**
     * 获取套餐列表
     * @param setmealDto
     * @return
     */
    @GetMapping("/list")
   public R<List<Setmeal>> list(SetmealDto setmealDto){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmealDto.getCategoryId()!=null,Setmeal::getCategoryId,setmealDto.getCategoryId())
                .eq(setmealDto.getStatus()!=null,Setmeal::getStatus,setmealDto.getStatus())
                .orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmealList = setmealService.list(queryWrapper);



        return R.success(setmealList);
   }

   //获取套餐的菜品
   @GetMapping("/dish/{id}")
   public R<List<SetmealDish>> getSetmealDishLsit(@PathVariable("id") Long setmealId){
       LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.eq(setmealId!=null,SetmealDish::getSetmealId,setmealId)
               .orderByAsc(SetmealDish::getSort).orderByDesc(SetmealDish::getUpdateTime);
       List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
       return R.success(setmealDishes);
   }
}
