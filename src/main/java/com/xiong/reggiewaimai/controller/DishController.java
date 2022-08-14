package com.xiong.reggiewaimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiong.reggiewaimai.bean.Category;
import com.xiong.reggiewaimai.bean.Dish;
import com.xiong.reggiewaimai.bean.DishFlavor;
import com.xiong.reggiewaimai.common.R;
import com.xiong.reggiewaimai.dto.DishDto;
import com.xiong.reggiewaimai.service.CategoryService;
import com.xiong.reggiewaimai.service.DishFlavorService;
import com.xiong.reggiewaimai.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xsy
 * @date 2022/8/12
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto) {

        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page> getPage(Integer page, Integer pageSize, String name) {
        Page<Dish> pageDish = new Page<Dish>(page, pageSize);
        Page<DishDto> dtoPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Dish::getName, name)
                .orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        dishService.page(pageDish, queryWrapper);

        BeanUtils.copyProperties(pageDish, dtoPage, "records");

        List<Dish> dishList = pageDish.getRecords();
        List<DishDto> dishDtos = new ArrayList<>(dishList.size());
        for (Dish dish : dishList) {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(dish, dishDto);
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
                dishDtos.add(dishDto);
            }

        }
        dtoPage.setRecords(dishDtos);
        System.out.println("*****");
        return R.success(dtoPage);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> queryDishById(@PathVariable("id") Long id) {
        DishDto dishDto = dishService.getWithFlavorById(id);
        return R.success(dishDto);
    }

    /**
     * 改变菜品状态，单选和多选二合一
     *
     * @param status
     * @param dishIds
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable("status") Integer status, @RequestParam("ids") List<Long> dishIds) {
        List<Dish> dishList = new ArrayList<>(dishIds.size());
        for (int i = 0; i < dishIds.size(); i++) {
            Dish dish = new Dish();
            dish.setStatus(status);
            Long dishId = dishIds.get(i);
            dish.setId(dishId);
            dishList.add(dish);
        }
        dishService.updateBatchById(dishList);
        return R.success("修改成功");
    }

    /**
     * 逻辑删除菜品
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> dishIds) {
        List<Dish> dishList = new ArrayList<>(dishIds.size());
        for (int i = 0; i < dishIds.size(); i++) {
            Dish dish = new Dish();
            //逻辑删除顺带将状态改为停售
            dish.setStatus(0);
            Long dishId = dishIds.get(i);
            dish.setId(dishId);
            dishList.add(dish);
        }
        dishService.removeBatchByIds(dishList);
        return R.success("删除成功");
    }



//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId())
//                .eq(Dish::getStatus,1)
//                .orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId())
                .eq(Dish::getStatus,1)
                .orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList=new ArrayList<>();
        for (Dish dish1 : list) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            Long categoryId = dish1.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dish1Id = dish1.getId();
            LambdaQueryWrapper<DishFlavor> dishQueryWrapper = new LambdaQueryWrapper<>();

            dishQueryWrapper.eq(dish1Id!=null,DishFlavor::getDishId,dish1Id);

            List<DishFlavor> dishFlavors = dishFlavorService.list(dishQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            dishDtoList.add(dishDto);
        }
        return R.success(dishDtoList);
    }

}
