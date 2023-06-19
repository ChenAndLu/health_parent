package com.cl.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cl.constant.MessageConstant;
import com.cl.constant.RedisConstant;
import com.cl.entity.PageResult;
import com.cl.entity.QueryPageBean;
import com.cl.entity.Result;
import com.cl.pojo.CheckGroup;
import com.cl.pojo.Setmeal;
import com.cl.service.SetmealService;
import com.cl.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/20
 * @description:
 * @version:1.0
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetmealService setmealService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        Jedis jedis = null;
        try {
            //原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(lastIndexOf);
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //完成后需要将文件名称保存到redis集合中
            jedis = jedisPool.getResource();
            jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[]  checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);

        }
    }


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeal);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    @RequestMapping("/findCheckGroupIdsBySetmealId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        List<Integer> chekitemIds;
        try {
            chekitemIds = setmealService.findCheckGroupIdBySetmealId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, chekitemIds);
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] checkgroupIds, @RequestBody Setmeal setmeal){
        try {
            setmealService.edit(checkgroupIds,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            setmealService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
