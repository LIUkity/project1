package com.sen.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sen.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);

}
