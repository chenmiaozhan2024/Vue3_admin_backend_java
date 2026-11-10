package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.Result;
import com.itheima.java_admin_backend.dto.RoleSaveDTO;
import com.itheima.java_admin_backend.dto.RoleUpdateDTO;
import com.itheima.java_admin_backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理（含角色权限分配）
 */
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    /*
    * 分页获取角色列表（roleName 模糊搜索）
    * */
    @GetMapping("/admin/acl/role/{page}/{limit}")
    public Result getRolePage(@PathVariable Long page,
                              @PathVariable Long limit,
                              @RequestParam(required = false) String roleName) {
        return Result.success(roleService.getRolePage(page, limit, roleName));
    }

    /*
    * 新增角色
    * */
    @PostMapping("/admin/acl/role/save")
    public Result saveRole(@RequestBody RoleSaveDTO roleSaveDTO) {
        roleService.addRole(roleSaveDTO);
        return Result.success();
    }

    /*
    * 修改角色
    * */
    @PutMapping("/admin/acl/role/update")
    public Result updateRole(@RequestBody RoleUpdateDTO roleUpdateDTO) {
        roleService.updateRole(roleUpdateDTO);
        return Result.success();
    }

    /*
    * 删除角色
    * */
    @DeleteMapping("/admin/acl/role/remove/{roleId}")
    public Result removeRole(@PathVariable Long roleId) {
        roleService.removeRole(roleId);
        return Result.success();
    }

    /*
    * 获取全部菜单与按钮数据（树形，标记当前角色已分配的节点）
    * */
    @GetMapping("/admin/acl/permission/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        return Result.success(roleService.getPermissionTree(roleId));
    }

    /*
    * 给角色分配权限（permissionId 为逗号分隔的菜单 id 字符串）
    * */
    @PostMapping("/admin/acl/permission/doAssign")
    public Result doAssign(@RequestParam Long roleId,
                           @RequestParam(required = false) String permissionId) {
        roleService.doAssignPermission(roleId, permissionId);
        return Result.success();
    }
}
