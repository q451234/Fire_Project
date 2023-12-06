package com.fire.util;

import com.fire.entity.Role;
import com.fire.entity.User;
import com.fire.manager.RoleManager;
import com.fire.manager.UserRoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class UserUtil {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RoleManager roleManager;

    @Autowired
    UserRoleManager userRoleManager;
    public User getLoginUser(HttpServletRequest request){
        String token = request.getHeader("X-Token");
        User loginUser = jwtUtil.parseToken(token, User.class);
        List<Integer> roleIdList = userRoleManager.getRoleIdByUserId(loginUser.getId());
        loginUser.setRoleIdList(roleIdList);

        return loginUser;
    }

    public Integer getAccess(Role role){
        int access = 0;
        List<Integer> menuIdList = role.getMenuIdList();
        if(menuIdList.contains(2)){
            access = 1;
        }
        if(menuIdList.contains(3)){
            access = 2;
        }
        return access;
    }

    public Integer getAuth(User user){
        Role role = roleManager.getRoleById(user.getRoleIdList().get(0));
        return role.getAccess();
    }
}
