package com.sen.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sen.reggie.common.CustomException;
import com.sen.reggie.common.R;
import com.sen.reggie.dto.SetmealDto;
import com.sen.reggie.entity.*;
import com.sen.reggie.service.CategoryService;
import com.sen.reggie.service.SetmealDishService;
import com.sen.reggie.service.SetmealService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage=new Page<>();
        LambdaQueryWrapper<Setmeal> lwq=new LambdaQueryWrapper<>();
        lwq.like(name!=null,Setmeal::getName,name);
        lwq.orderByAsc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, lwq);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());

            }return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);


    }
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
    setmealService.saveWithSetmealDish(setmealDto);
    return R.success("新增成功");
    }
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto=new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        Category category = categoryService.getById(setmeal.getCategoryId());
        String categoryName = category.getName();
        setmealDto.setCategoryName(categoryName);
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(lqw);
        setmealDto.setSetmealDishes(setmealDishList);
        return R.success(setmealDto);
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithSetmealDish(setmealDto);
        return R.success("修改成功");
    }
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status,@RequestParam("ids") List<Long> ids){

            for (Long id : ids) {
                Setmeal setmeal = setmealService.getById(id);
            if (status==0){
                setmeal.setStatus(0);
                setmealService.updateById(setmeal);

            }
            if(status==1){
                setmeal.setStatus(1);
                setmealService.updateById(setmeal);
        }

        }
        return R.success("修改成功");

    }
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> list){
     /*   for (Long id : list) {
            setmealService.removeById(id);
            LambdaQueryWrapper<SetmealDish> lwq=new LambdaQueryWrapper<>();
            lwq.eq(SetmealDish::getSetmealId,id);
            setmealDishService.remove(lwq);
        }
        return R.success("删除成功");
*/   setmealService.removeWithDish(list);
     return R.success("删除成功");
    }
    @GetMapping("/dish/{id}")
    public R<SetmealDto> dish(@PathVariable Long id){
        SetmealDto setmealDto=new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(lqw);
        setmealDto.setSetmealDishes(setmealDishList);
        Long categoryId = setmeal.getCategoryId();
        Category category = categoryService.getById(categoryId);
        setmealDto.setCategoryName(category.getName());
        return R.success(setmealDto);

    }

}
