package com.cl.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.cl.constant.MessageConstant;
import com.cl.constant.RedisConstant;
import com.cl.entity.Result;
import com.cl.pojo.Order;
import com.cl.service.OrderService;
import com.cl.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/27
 * @description:
 * @version:1.0
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result Submit(@RequestBody Map map){
        String telephone = (String)map.get("telephone");
        String validateCode = (String) map.get("validateCode");

        Jedis jedis = jedisPool.getResource();
        String codeRedis = jedis.get(telephone + RedisConstant.SENDTYPE_ORDER);
        if (codeRedis == null || validateCode == null || !validateCode.equals(codeRedis)){
            //验证码校验失败
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //校验通过
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false,MessageConstant.ORDER_FAIL);
        }
        if (result.isFlag()){
            //预约成功，发送短信通知
//            String orderDate = (String) map.get("orderDate");
//            try {
//                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
//            } catch (ClientException e) {
//                e.printStackTrace();
//            }
        }
        return result;
    }

    @RequestMapping("findById")
    public Result findById(Integer id){
        try{
            Map map = orderService.findById(id);
            //查询预约信息成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
