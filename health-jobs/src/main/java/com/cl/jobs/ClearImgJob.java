package com.cl.jobs;

import com.cl.constant.RedisConstant;
import com.cl.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/24
 * @description:
 * @version:1.0
 */

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        Jedis jedis = jedisPool.getResource();
        Set<String> set = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set != null && set.size() > 0){
            for (String imgName : set){
                QiniuUtils.deleteFileFromQiniu(imgName);
                jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
            }
        }
        jedis.close();
    }
}
