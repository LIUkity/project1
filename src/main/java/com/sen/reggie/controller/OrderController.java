package com.sen.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sen.reggie.common.BaseContext;
import com.sen.reggie.common.R;
import com.sen.reggie.entity.Orders;
import com.sen.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);

        return R.success("支付成功");
    }
    @GetMapping("/userPage")
    public R<Page> page(int page,int pageSize){
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Orders::getUserId,currentId);
        lqw.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo,lqw);
        return R.success(pageInfo);

    }
}
