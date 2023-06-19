package com.cl.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cl.constant.MessageConstant;
import com.cl.entity.PageResult;
import com.cl.entity.QueryPageBean;
import com.cl.entity.Result;
import com.cl.pojo.CheckItem;
import com.cl.service.CheckItemService;
import org.apache.poi.util.ArrayUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/17
 * @description:检查项的管理
 * @version:1.0
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        System.out.println("checkout: "+checkItem);
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return  new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);

    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }

    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            checkItemService.deleteById(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return  new Result(false, e.getMessage());
        }catch (Exception e){
            return  new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return  new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);

    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        CheckItem CheckItemById= null;
        try {
            CheckItemById = checkItemService.findById(id);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, CheckItemById);
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return  new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);

    }


    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckItem> list;
        try {
            list = checkItemService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);

    }
}
