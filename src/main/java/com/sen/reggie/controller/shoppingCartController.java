package com.sen.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sen.reggie.common.BaseContext;
import com.sen.reggie.common.R;
import com.sen.reggie.entity.ShoppingCart;
import com.sen.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class shoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        Long usrId = BaseContext.getCurrentId();
        shoppingCart.setUserId(usrId);
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,usrId);
        Long dishId = shoppingCart.getDishId();
        if (dishId!=null){
            lqw.eq(ShoppingCart::getDishId,dishId);
        }else{
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
    }
    ShoppingCart one = shoppingCartService.getOne(lqw);
        if(one!=null){
            Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCartService.updateById(one);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one=shoppingCart;
        }

        return R.success(one);
    }
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        if (dishId!=null){
            lqw.eq(ShoppingCart::getDishId,dishId);
        }else {
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(lqw);
        Integer number = shoppingCartOne.getNumber();
        if(number!=1){
            shoppingCartOne.setNumber(number-1);
            shoppingCartService.updateById(shoppingCartOne);
        }else {
            shoppingCartService.removeById(shoppingCartOne);
        }
        return R.success("删除成功");
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        lqw.eq(ShoppingCart::getUserId,currentId);
        lqw.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);
    }
    @DeleteMapping("clean")
    public R<String> delete(){
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        lqw.eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(lqw);
        return R.success("清空购物车成功");
    }

}
