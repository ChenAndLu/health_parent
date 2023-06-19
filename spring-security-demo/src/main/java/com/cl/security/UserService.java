package com.cl.security;

import com.cl.pojo.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/30
 * @description:
 * @version:1.0
 */

public class UserService implements UserDetailsService {
    //模拟数据库中的用户数据
    public  static Map<String, User> map = new HashMap<>();
    static {
        com.cl.pojo.User user1 = new com.cl.pojo.User();
        user1.setUsername("admin");
        user1.setPassword("{noop}admin");

        com.cl.pojo.User user2 = new com.cl.pojo.User();
        user2.setUsername("xiaoming");
        user2.setPassword("{noop}1234");

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }
    /**
     * 根据用户名加载用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       if (username == null || username.equals("")){
           return null;
       }

        User user = map.get(username);
        String passwordInDb = user.getPassword();

        if (user == null){
           return null;
       }

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(username,passwordInDb,list);
        return user1;
    }
}
