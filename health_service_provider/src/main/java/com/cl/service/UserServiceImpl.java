package com.cl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.cl.dao.PermissionDao;
import com.cl.dao.RoleDao;
import com.cl.dao.UserDao;
import com.cl.pojo.Permission;
import com.cl.pojo.Role;
import com.cl.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/30
 * @description:
 * @version:1.0
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    //根据用户查询用户名查询数据库中的用户信息，还需要查询用户对应的角色和权限
    @Override
    public User findByUsername(String username) {
        //查询用户
        User user = userDao.findByUsername(username);
        //查询角色
        if (user != null){
            //根据用户查询对象角色
            Set<Role> roles = roleDao.findByUserId(user.getId());

            if (roles !=null && roles.size() > 0){
                for (Role role : roles){
                    Set<Permission> permissions = permissionDao.findByRoleId(role.getId());
                    role.setPermissions(permissions);
                }
            }

            user.setRoles(roles);
        }
        return user;
    }
}
