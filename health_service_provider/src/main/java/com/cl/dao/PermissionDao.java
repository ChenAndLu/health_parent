package com.cl.dao;

import com.cl.pojo.Permission;

import java.util.Set;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/30
 * @description:
 * @version:1.0
 */
public interface PermissionDao {
    public Set<Permission> findByRoleId(int roleId);
}
