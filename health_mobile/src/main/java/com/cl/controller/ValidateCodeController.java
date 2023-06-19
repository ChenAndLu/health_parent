package com.cl.controller;

import com.aliyuncs.exceptions.ClientException;
import com.cl.constant.MessageConstant;
import com.cl.constant.RedisConstant;
import com.cl.entity.Result;
import com.cl.utils.SMSUtils;
import com.cl.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/27
 * @description:负责验证码发送
 * @version:1.0
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 在线预约验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result Send4Order(String telephone){
        //为用户随机生成一个6为的验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(6).toString();
        //调用阿里云短信服务为用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis中，设置有效时间为5分钟
        Jedis jedis = jedisPool.getResource();
        jedis.setex(telephone+ RedisConstant.SENDTYPE_ORDER,300,validateCode);
        jedis.close();
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //为用户随机生成一个6为的验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(6).toString();
        //调用阿里云短信服务为用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis中，设置有效时间为5分钟
        Jedis jedis = jedisPool.getResource();
        jedis.setex(telephone+ RedisConstant.SENDTYPE_LOGIN,300,validateCode);
        jedis.close();
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
