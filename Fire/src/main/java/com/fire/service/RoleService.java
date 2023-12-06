package com.fire.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.entity.Role;
import com.fire.entity.RoleMenu;
import com.fire.entity.User;
import com.fire.manager.RoleManager;
import com.fire.manager.RoleMenuManager;
import com.fire.manager.UserManager;
import com.fire.manager.UserRoleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@Slf4j
public class RoleService{

    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RoleMenuManager roleMenuManager;

    @Autowired
    private UserRoleManager userRoleManager;

    @Autowired
    private UserManager userManager;


    public void addRole(Role role) {
        roleManager.insertRole(role);
        if(role.getMenuIdList() != null){
            for(Integer menuId : role.getMenuIdList()){
                roleMenuManager.insertRoleMenu(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }


    public Role getRoleById(Integer id) {
        Role role = roleManager.getRoleById(id);
        List<Integer> menuIdList = roleMenuManager.getMenuIdListByRoleId(id);
        role.setMenuIdList(menuIdList);
        return role;
    }



    public void updateRole(Role role) {
        // 更新role表
        roleManager.updateRole(role);
        // 清除原有权限
        roleMenuManager.deleteByRoleId(role.getRoleId());
        //新增权限
        for (Integer menuId : role.getMenuIdList()) {
            roleMenuManager.insertRoleMenu(new RoleMenu(null,role.getRoleId(),menuId));
        }
    }


    public void deleteRoleById(Integer id) {
        roleManager.deleteRoleById(id);
        // 清除原有权限
        roleMenuManager.deleteByRoleId(id);

        //对应角色用户状态设置为禁用
        List<Integer> userIdList = userRoleManager.getUserIdByRoleId(id);
        for(Integer userId : userIdList){
            User user = userManager.getByUserId(userId);
            user.setStatus(0);
            userManager.updateUser(user);
            userRoleManager.deleteByUserId(userId);
        }
    }

    public List<Role> getALlRole(){
        return roleManager.getALlRole();
    }

    public Page<Role> getPage(String roleName, Long pageNo, Long pageSize){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName, roleName);
        wrapper.orderByDesc(Role::getRoleId);

        Page<Role> page = new Page<>(pageNo,pageSize);
        page = roleManager.getPage(page, wrapper);

        return page;
    }
}
