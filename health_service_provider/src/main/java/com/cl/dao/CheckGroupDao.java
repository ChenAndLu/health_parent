package com.cl.dao;

import com.cl.pojo.CheckGroup;
import com.cl.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/19
 * @description:
 * @version:1.0
 */
public interface CheckGroupDao {

    public void setCheckGroupAndCheckItem(Map map);
    public void add(CheckGroup checkGroup);

    Page<CheckGroup> selectByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void deleteAssociation(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteById(Integer id);

    List<CheckGroup> findAll();
}
