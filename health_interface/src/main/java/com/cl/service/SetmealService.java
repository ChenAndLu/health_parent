package com.cl.service;

import com.cl.entity.PageResult;
import com.cl.pojo.CheckGroup;
import com.cl.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/23
 * @description:
 * @version:1.0
 */
public interface SetmealService {

    void add(Setmeal setmeal,Integer[] checkgroupIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdBySetmealId(Integer id);

    void edit(Integer[] checkgroupIds, Setmeal setmeal);

    void deleteById(Integer id);

    List<Setmeal> findAll();

    Setmeal findByIdForMobile(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
