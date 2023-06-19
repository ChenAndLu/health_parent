package com.cl.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cl.constant.MessageConstant;
import com.cl.constant.RedisConstant;
import com.cl.entity.Result;
import com.cl.pojo.Member;
import com.cl.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/28
 * @description:
 * @version:1.0
 */

@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;
    //会员手机快速登录
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");

        //从redis中获取验证码
        Jedis jedis = jedisPool.getResource();
        String codeInRedis = jedis.get(telephone + RedisConstant.SENDTYPE_LOGIN);
        //比较验证码
        if (codeInRedis == null || validateCode == null || !codeInRedis.equals(validateCode)){
            //校验不通过，返回
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //校验通过，判断是否为会员，如果不是会员自动完成注册
        //判断当前用户是否为会员
        Member member = memberService.findByTelephone(telephone);
        if (member == null){
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //将用户手机号保存到cookie
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");//路径
        cookie.setMaxAge(60*60*24*30);//有效期30天
        response.addCookie(cookie);
        //将用户信息保存到redis
        jedis.setex(telephone,60*30, JSON.toJSON(member).toString());
        jedis.close();
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
