package com.sen.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sen.reggie.dto.DishDto;
import com.sen.reggie.dto.SetmealDto;
import com.sen.reggie.entity.Category;
import com.sen.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithSetmealDish(SetmealDto setmealDto);
    public void updateWithSetmealDish(SetmealDto SetmealDto);
    public void removeWithDish(List<Long> ids);
}
