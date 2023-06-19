package com.cl.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cl.constant.MessageConstant;
import com.cl.entity.PageResult;
import com.cl.entity.QueryPageBean;
import com.cl.entity.Result;
import com.cl.pojo.CheckGroup;
import com.cl.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.handler.MessageContext;
import java.util.List;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/19
 * @description:检查组管理
 * @version:1.0
 */
@RestController
@RequestMapping("checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public Result add(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.add(checkitemIds,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.findPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        if(checkGroup != null){
            Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroup);
            return result;
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @RequestMapping("findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        List<Integer> chekitemIds;
        try {
            chekitemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, chekitemIds);
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.edit(checkitemIds,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("deleteById")
    public Result deleteById(Integer id) {
        try {
            checkGroupService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        if(checkGroupList != null && checkGroupList.size() > 0){
            Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroupList);
            return result;
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }
}
