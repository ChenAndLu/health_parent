package com.cl.service;

import com.cl.pojo.User;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/30
 * @description:
 * @version:1.0
 */
public interface UserService {
    User findByUsername(String username);
}
