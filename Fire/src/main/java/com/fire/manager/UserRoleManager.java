package com.fire.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fire.entity.UserRole;
import com.fire.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class UserRoleManager {

    @Autowired
    UserRoleMapper userRoleMapper;

    public List<String> getRoleNameListByUserId(Integer userId){
        return userRoleMapper.getRoleNameByUserId(userId);
    }

    public List<Integer> getRoleIdByUserId(Integer userId){
        return userRoleMapper.getRoleIdByUserId(userId);
    }

    @Transactional
    public void saveUserRole(UserRole userRole){
        userRoleMapper.insert(userRole);
    }

    public List<UserRole> getUserRoleListByUserId(Integer userId){
        LambdaQueryWrapper<UserRole> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(UserRole::getUserId, userId);
        return userRoleMapper.selectList(Wrapper);
    }

    @Transactional
    public void deleteByUserId(Integer userId){
        LambdaQueryWrapper<UserRole> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(UserRole::getUserId,userId);
        userRoleMapper.delete(Wrapper);
    }

    public List<Integer> getUserIdByRoleId(Integer roleId){
        return userRoleMapper.getUserIdByRoleId(roleId);
    }
}
