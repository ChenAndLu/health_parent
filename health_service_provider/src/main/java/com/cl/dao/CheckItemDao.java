package com.cl.dao;

import com.cl.entity.PageResult;
import com.cl.entity.QueryPageBean;
import com.cl.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/17
 * @description:
 * @version:1.0
 */
public interface CheckItemDao {
    public void add(CheckItem checkItem);
    public Page<CheckItem> selectByCondition(String queryString);
    public Long findCountByCheckItemId(Integer checkItemId);
    public void deleteById(Integer id);
    public CheckItem findById(Integer id);
    public void edit(CheckItem checkItem);
    public List<CheckItem> findAll();
}
