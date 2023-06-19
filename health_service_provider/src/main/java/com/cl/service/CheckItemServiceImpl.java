package com.cl.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.cl.constant.MessageConstant;
import com.cl.dao.CheckItemDao;
import com.cl.entity.PageResult;
import com.cl.entity.QueryPageBean;
import com.cl.pojo.CheckItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/17
 * @description:检查项服务
 * @version:1.0
 */
@Service
@Transactional
public class CheckItemServiceImpl implements CheckItemService{

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     * @param checkItem 检查项相关信息
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页查询
     * @param queryPageBean 查询条件
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //设置分页条件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem> page = checkItemDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }


    /**
     * 根据id删除检查项信息
     * @param id 检查项id
     */
    @Override
    public void deleteById(Integer id) {
        //查询检查项目是否跟检查组已经关联了，关联了就不能删除了
        //查询当前检查项是否和检查组关联
        long count = checkItemDao.findCountByCheckItemId(id);

        if (count > 0){
            throw new RuntimeException(MessageConstant.CHECKITEMHASASSOCIATION);
        }else {
            checkItemDao.deleteById(id);
        }
    }

    /**
     * 根据检查项id查询检查项信息
     * @param id 检查项id
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 编辑检查项
     * @param checkItem 检查项相关信息
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 查询所有检查项信息
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
