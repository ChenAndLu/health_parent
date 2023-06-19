package com.cl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.cl.dao.CheckGroupDao;
import com.cl.entity.PageResult;
import com.cl.entity.QueryPageBean;
import com.cl.pojo.CheckGroup;
import com.cl.pojo.CheckItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/19
 * @description:
 * @version:1.0
 */

@Service
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService{

    @Autowired
    private CheckGroupDao checkGroupDao;
    /**
     * 添加检查组，同时需要关联检查项信息
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的关联关系
        setCheckGroupAndCheckItem(checkitemIds, checkGroup.getId());
    }

    /**
     * 分页查询
     * @param queryPageBean 查询的分页条件
     * @return 查询数据
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //设置分页条件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询检查组信息
     * @param id 需要查询的检查组主键
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组ID查询检查项信息
     * @param id 检查组id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑更新检查组信息
     * @param checkitemIds 检查项id
     * @param checkGroup 检查组信息
     */
    @Override
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        //清理检查组和检查项的关联关系
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //重新简历关系
        setCheckGroupAndCheckItem(checkitemIds,checkGroup.getId());
        //修改基本信息
        checkGroupDao.edit(checkGroup);
    }

    /**
     * 根据id删除检查组信息
     * @param id 检查组id
     */
    @Override
    public void deleteById(Integer id) {
        //清理检查组和检查项的关联关系
        checkGroupDao.deleteAssociation(id);
        //清理检查组信息
        checkGroupDao.deleteById(id);
    }

    /**
     * 查询检查组列表
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 关联检查组下的所有检查项
     * @param checkitemIds 检查项Id
     * @param CheckGroupId 检查组id
     */
    private void setCheckGroupAndCheckItem(Integer[] checkitemIds, Integer CheckGroupId) {
        if(checkitemIds != null && checkitemIds.length > 0){
            for(Integer checkitemId : checkitemIds){
                Map map = new HashMap();
                map.put("checkitemId",checkitemId);
                map.put("CheckGroupId",CheckGroupId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

}
