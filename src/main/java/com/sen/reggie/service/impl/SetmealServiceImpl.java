package com.sen.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sen.reggie.common.CustomException;
import com.sen.reggie.dto.SetmealDto;
import com.sen.reggie.entity.Category;
import com.sen.reggie.entity.Setmeal;
import com.sen.reggie.entity.SetmealDish;
import com.sen.reggie.mapper.CategoryMapper;
import com.sen.reggie.mapper.SetmealMapper;
import com.sen.reggie.service.CategoryService;
import com.sen.reggie.service.SetmealDishService;
import com.sen.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Transactional
    public void saveWithSetmealDish(SetmealDto setmealDto){
        this.save(setmealDto);
        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> setmealDishList = setmealDishes.stream().map((setmealDish) -> {
            setmealDish.setSetmealId(id);
            return setmealDish;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }
    @Transactional
    public void updateWithSetmealDish(SetmealDto setmealDto){
       this.updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();

        Long id = setmealDto.getId();
        lqw.eq(SetmealDish::getSetmealId,id);
        setmealDishService.remove(lqw);
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);


    }
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
