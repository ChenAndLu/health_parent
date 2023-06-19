package com.cl.dao;

import com.cl.pojo.User;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/30
 * @description:
 * @version:1.0
 */
public interface UserDao {
    User findByUsername(String username);
}
