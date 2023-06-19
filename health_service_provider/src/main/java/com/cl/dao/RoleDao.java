package com.cl.dao;

import com.cl.pojo.Role;

import java.util.Set;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/30
 * @description:
 * @version:1.0
 */
public interface RoleDao {
    public Set<Role> findByUserId(int id);
}
