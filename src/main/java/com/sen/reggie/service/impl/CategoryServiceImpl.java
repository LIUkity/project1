package com.sen.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sen.reggie.common.CustomException;
import com.sen.reggie.common.R;
import com.sen.reggie.entity.Category;
import com.sen.reggie.entity.Dish;
import com.sen.reggie.entity.Setmeal;
import com.sen.reggie.mapper.CategoryMapper;
import com.sen.reggie.service.CategoryService;
import com.sen.reggie.service.DishService;
import com.sen.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    public void remove(Long id){
        LambdaQueryWrapper<Dish> lqw1=new LambdaQueryWrapper<>();
        lqw1.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(lqw1);
        if(count1>0){
            throw new CustomException("当前分类下关联了菜品，不能删除");

        }
        LambdaQueryWrapper<Setmeal> lqw2=new LambdaQueryWrapper<>();
        lqw2.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(lqw2);
        if(count2>0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除
        super.removeById(id);


    }
}
