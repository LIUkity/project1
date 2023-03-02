package com.sen.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sen.reggie.common.R;
import com.sen.reggie.dto.DishDto;
import com.sen.reggie.dto.SetmealDto;
import com.sen.reggie.entity.Category;
import com.sen.reggie.entity.Dish;
import com.sen.reggie.entity.DishFlavor;
import com.sen.reggie.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success(dishDto.getName()+"保存成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto=new DishDto();
        Dish dish = dishService.getById(id);
        LambdaQueryWrapper<DishFlavor> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(lqw);
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");


    }
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status,@RequestParam("ids") List<Long> list){
        for (Long id : list) {
            Dish dish = dishService.getById(id);
            if (status==0){
                dish.setStatus(0);
                dishService.updateById(dish);

            }else {
                dish.setStatus(1);
                dishService.updateById(dish);
            }

        }
        return R.success("修改成功");

    }
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> list){
        for (Long id : list) {
        dishService.removeById(id);
        LambdaQueryWrapper<DishFlavor> lwq=new LambdaQueryWrapper<>();
        lwq.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(lwq);
        }
        return R.success("删除成功");

    }
    @GetMapping("/list")
    public R<List<DishDto>> list(DishDto dishDto){
        LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(dishDto.getCategoryId()!=null,Dish::getCategoryId,dishDto.getCategoryId());
        lqw.orderByAsc(Dish::getSort);
        lqw.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(lqw);
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto1 = new DishDto();
            BeanUtils.copyProperties(item, dishDto1);
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(DishFlavor::getDishId, id);
            List<DishFlavor> list1 = dishFlavorService.list(lqw1);
            dishDto1.setFlavors(list1);
            return dishDto1;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);


    }




}
