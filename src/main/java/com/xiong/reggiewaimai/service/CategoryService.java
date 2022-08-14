package com.xiong.reggiewaimai.service;

import com.xiong.reggiewaimai.bean.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LENOVO
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2022-08-11 18:23:45
*/
public interface CategoryService extends IService<Category> {
        void remove(Long ids);
}
