package com.cl.dao;

import com.cl.pojo.CheckGroup;
import com.cl.pojo.CheckItem;
import com.cl.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/23
 * @description:
 * @version:1.0
 */
public interface SetmealDao {
    public void add(Setmeal setmeal);
    public void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<CheckItem> selectByCondition(String queryString);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdBySetmealId(Integer id);

    void deleteAssociation(Integer id);

    void edit(Setmeal setmeal);

    void deleteById(Integer id);

    List<Setmeal> findAll();

    Setmeal findByIdForMobile(Integer id);

    List<Map<String, Object>> findSetmealCount();

    List<Map<String, Object>> findHotSetmeal();
}
