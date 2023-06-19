package com.cl.service;

import com.cl.entity.PageResult;
import com.cl.entity.QueryPageBean;
import com.cl.pojo.CheckGroup;

import java.util.List;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/19
 * @description:
 * @version:1.0
 */
public interface CheckGroupService {
    public void add(Integer[] checkitemIds, CheckGroup checkGroup);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(Integer[] checkitemIds, CheckGroup checkGroup);

    void deleteById(Integer id);

    List<CheckGroup> findAll();
}
