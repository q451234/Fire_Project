package com.fire.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.common.Access;
import com.fire.common.AccessLevel;
import com.fire.common.Constant;
import com.fire.common.Result;
import com.fire.entity.Role;
import com.fire.service.RoleService;
import com.fire.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "角色相关接口列表")
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserUtil userUtil;

    @ApiOperation("获取所有角色信息")
    @Access(level = AccessLevel.MANAGER)
    @GetMapping("/all")
    public Result<List<Role>> getAllRole(){
        List<Role> roleList = roleService.getALlRole();
        return Result.success(roleList,"查询成功");
    }

    @ApiOperation("搜索角色")
    @Access(level = AccessLevel.ADMIN)
    @GetMapping("/list")
    public Result<Map<String,Object>> getRoleList(@RequestParam(value = "roleName",required = false) String roleName,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){

        Page<Role> page = roleService.getPage(roleName, pageNo, pageSize);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return Result.success(data);

    }

    @ApiOperation("添加角色")
    @Access(level = AccessLevel.ADMIN)
    @PostMapping
    public Result<?> addRole(@RequestBody Role role){
        try{
            role.setAccess(userUtil.getAccess(role));
            roleService.addRole(role);
        }catch (Exception SQLIntegrityConstraintViolationException){
            log.info("角色名已存在", role);
            return Result.fail(Constant.FAIL_CODE_3, "角色名已存在");
        }

        return Result.success("新增角色成功");
    }

    @ApiOperation("更新角色")
    @Access(level = AccessLevel.ADMIN)
    @PutMapping
    public Result<?> updateRole(@RequestBody Role role){
        role.setAccess(userUtil.getAccess(role));
        roleService.updateRole(role);
        return Result.success("修改角色成功");
    }

    @ApiOperation("查询角色")
    @Access(level = AccessLevel.ADMIN)
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id){
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    @ApiOperation("删除角色")
    @Access(level = AccessLevel.ADMIN)
    @DeleteMapping("/{id}")
    public Result<Role> deleteRoleById(@PathVariable("id") Integer id){
        roleService.deleteRoleById(id);
        return Result.success("删除角色成功");
    }
}
