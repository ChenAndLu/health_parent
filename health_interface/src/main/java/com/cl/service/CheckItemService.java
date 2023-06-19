package com.cl.service;

import com.cl.entity.PageResult;
import com.cl.entity.QueryPageBean;
import com.cl.pojo.CheckItem;

import java.util.List;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/17
 * @description:
 * @version:1.0
 */
public interface CheckItemService {
    public void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    public void deleteById(Integer id);

    public CheckItem findById(Integer id);

    public void edit(CheckItem checkItem);

    List<CheckItem> findAll();


}
