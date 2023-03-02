package com.sen.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sen.reggie.dto.DishDto;
import com.sen.reggie.entity.Category;
import com.sen.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    public void updateWithFlavor(DishDto dishDto);
}
