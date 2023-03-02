package com.sen.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sen.reggie.common.R;
import com.sen.reggie.entity.Category;
import com.sen.reggie.service.CategoryService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        Page pageinfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);
        Page page1 = categoryService.page(pageinfo, lqw);
        return R.success(page1);
    }

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增" + category.getName() + "成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getSort, category.getSort());
        int number = categoryService.count(lqw);
        if (number == 0) {
            categoryService.updateById(category);
            return R.success("修改成功");
        }
        return R.error("排序已存在");

    }

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id) {
        categoryService.remove(id);
        return R.success("删除成功");

    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> lqw=new LambdaQueryWrapper<>();
        lqw.eq(category.getType()!=null,Category::getType,category.getType());
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lqw);
        return R.success(list);
    }


}