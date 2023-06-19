package com.cl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.cl.constant.RedisConstant;
import com.cl.dao.SetmealDao;
import com.cl.entity.PageResult;
import com.cl.pojo.CheckGroup;
import com.cl.pojo.CheckItem;
import com.cl.pojo.Setmeal;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/23
 * @description:
 * @version:1.0
 */

@Service
@Transactional
public class SetmealServiceImpl implements SetmealService {


    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private SetmealDao setmealDao;

    private String OriginalPic = null;

    /**
     * 新增检查套餐
     * @param setmeal 检查套餐信息
     * @param checkgroupIds 检查组id
     */
    //新增套餐
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        if(checkgroupIds != null && checkgroupIds.length > 0){
            //绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }

        savePic2Redis(setmeal.getImg());
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据检查套餐id查询检查套餐信息
     * @param id 检查套餐id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.findById(id);
        OriginalPic = setmeal.getImg();
        return setmeal;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdBySetmealId(id);
    }

    /**
     * 编辑检查套餐
     * @param checkgroupIds 检查组id
     * @param setmeal 检查套餐信息
     */
    @Override
    public void edit(Integer[] checkgroupIds, Setmeal setmeal) {
        //清理检查组和检查项的关联关系
        setmealDao.deleteAssociation(setmeal.getId());
        //重新简历关系
        setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        //修改基本信息
        setmealDao.edit(setmeal);
        if (setmeal.getImg().equals(OriginalPic) == false){
            deletePic2Redis(OriginalPic);
        }
        savePic2Redis(setmeal.getImg());
    }

    /**
     * 根据id删除检查套餐
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //清理检查组和检查项的关联关系
        setmealDao.deleteAssociation(id);
        Setmeal setmeal = setmealDao.findById(id);
        String pic = setmeal.getImg();
        //清理检查组信息
        setmealDao.deleteById(id);
        //清理图片信息
        deletePic2Redis(pic);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findByIdForMobile(Integer id) {
        return setmealDao.findByIdForMobile(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    /**
     * 将图片信息保存到Redis
     * @param pic
     */
    private void savePic2Redis(String pic) {
        Jedis jedis = jedisPool.getResource();
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
        jedis.close();
    }

    /**
     * 删除Redis数据库中的图片信息
     * @param pic
     */
    private void deletePic2Redis(String pic) {
        Jedis jedis = jedisPool.getResource();
        jedis.srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
        jedis.close();
    }

    /**
     * 绑定套餐和检查组的多对多关系
     * @param id 套餐id
     * @param checkgroupIds 检查组id
     */
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmealId",id);
            map.put("checkgroupId",checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }
}
