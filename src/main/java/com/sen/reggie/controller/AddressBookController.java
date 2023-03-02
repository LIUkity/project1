package com.sen.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sen.reggie.common.BaseContext;
import com.sen.reggie.common.R;
import com.sen.reggie.entity.AddressBook;
import com.sen.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;
    /*@GetMapping("/list")
    public R<List> list(AddressBook addressBook){
        LambdaQueryWrapper<AddressBook> lqw=new LambdaQueryWrapper<>();
        lqw.orderByDesc(AddressBook::getUpdateTime);
        lqw.eq(addressBook.getUserId()!=null,AddressBook::getUserId,addressBook.getUserId());
        List<AddressBook> addressBookList = addressBookService.list(lqw);
        return R.success(addressBookList);
    }
    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook){
        Long id = addressBook.getId();
        log.info("{id为}"+id);
        List<AddressBook> list = addressBookService.list();
        List<AddressBook> addressBookList = list.stream().map((item) -> {
            if (item.getId().equals(id)) {
                log.info("{item.id为}"+item.getId());
                item.setIsDefault(1);
                log.info("");
            } else {
                item.setIsDefault(0);
            }
            return item;
        }).collect(Collectors.toList());
        addressBookService.updateBatchById(addressBookList);
       return R.success("设置成功");
    }
    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        if (addressBook!=null){
            addressBook.setUserId(BaseContext.getCurrentId());
            addressBookService.save(addressBook);
            return R.success("保存成功");
        }
        return R.error("保存失败");



    }
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }*/
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        if (addressBook!=null){
            addressBook.setUserId(BaseContext.getCurrentId());
            addressBookService.updateById(addressBook);
            return R.success("保存成功");
        }
        return R.error("保存失败");
    }

    /**
     * 新增
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list(queryWrapper));
    }
}
