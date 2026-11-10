package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.Result;
import com.itheima.java_admin_backend.dto.MenuSaveDTO;
import com.itheima.java_admin_backend.dto.MenuUpdateDTO;
import com.itheima.java_admin_backend.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单管理（菜单与按钮；权限分配接口见 RoleController）
 */
@RestController
public class MenuController {
    @Autowired
    private MenuService menuService;

    /*
    * 获取全部菜单与按钮的标识数据（树形）
    * */
    @GetMapping("/admin/acl/permission")
    public Result getMenuTree() {
        return Result.success(menuService.getMenuTree());
    }

    /*
    * 新增子菜单
    * */
    @PostMapping("/admin/acl/permission/save")
    public Result saveMenu(@RequestBody MenuSaveDTO menuSaveDTO) {
        menuService.addMenu(menuSaveDTO);
        return Result.success();
    }

    /*
    * 修改菜单
    * */
    @PutMapping("/admin/acl/permission/update")
    public Result updateMenu(@RequestBody MenuUpdateDTO menuUpdateDTO) {
        menuService.updateMenu(menuUpdateDTO);
        return Result.success();
    }

    /*
    * 删除菜单
    * */
    @DeleteMapping("/admin/acl/permission/remove/{id}")
    public Result removeMenu(@PathVariable Long id) {
        menuService.removeMenu(id);
        return Result.success();
    }
}
