package com.fire.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fire.entity.Menu;
import com.fire.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MenuManager {
    @Autowired
    MenuMapper menuMapper;

    public List<Menu> getMenuListByUserId(Integer userId, Integer pid){
        return menuMapper.getMenuListByUserId(userId, pid);
    }

    public List<Menu> getMenuListByParentId(Integer parentId){
        // 一级菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,parentId);

        return menuMapper.selectList(wrapper);
    }

}
