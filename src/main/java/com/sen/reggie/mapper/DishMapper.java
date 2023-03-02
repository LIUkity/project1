package com.sen.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.reggie.dto.DishDto;
import com.sen.reggie.entity.Category;
import com.sen.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
