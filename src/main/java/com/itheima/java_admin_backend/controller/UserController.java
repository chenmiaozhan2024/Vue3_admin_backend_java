package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.common.Result;
import com.itheima.java_admin_backend.dto.UserRoleAssignDTO;
import com.itheima.java_admin_backend.dto.UserSaveDTO;
import com.itheima.java_admin_backend.dto.UserUpdateDTO;
import com.itheima.java_admin_backend.service.UserService;
import com.itheima.java_admin_backend.vo.RoleAssignVO;
import com.itheima.java_admin_backend.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /*
    * 分页获取用户列表（页码、每页条数由前端传入）
    * */
    @GetMapping("/admin/acl/user/{page}/{limit}")
    public Result getUserPage(@PathVariable Long page,
                              @PathVariable(required = false) Long limit,
                              @RequestParam(required = false) String username){
        if (page == null || page < 1) {
            page = 1L;
        }
        if (limit == null || limit < 1) {
            limit = 10L;
        }
        PageResult<UserVO> pageResult = userService.getUserPage(page, limit, username);
        return Result.success(pageResult);
    }
    /*
    * 添加一个用户
    * */
    @PostMapping("/admin/acl/user/save")
    public Result saveUser(@RequestBody UserSaveDTO userSaveDTO){
        userService.addUser(userSaveDTO);
        return Result.success();
    }
    /*
    * 更新一个用户
    * */
    @PutMapping("/admin/acl/user/update")
    public Result updateUser(@RequestBody UserUpdateDTO userUpdateDTO){
        userService.updateUser(userUpdateDTO);
        return Result.success();
    }
    /*
    * 删除一个用户
    * */
    @DeleteMapping("/admin/acl/user/remove/{id}")
    public Result removeUser(@PathVariable Long id){
        userService.removeUser(id);
        return Result.success();
    }
    /*
    * 批量删除用户
    * */
    @DeleteMapping("/admin/acl/user/batchRemove")
    public Result batchRemoveUser(@RequestBody List<Long> userIds){
        userService.batchRemoveUsers(userIds);
        return Result.success();
    }

    /*
    * 获取当前账号拥有的角色，以及全部角色列表
    * */
    @GetMapping("/admin/acl/user/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        RoleAssignVO roleAssignVO = userService.toAssign(userId);
        return Result.success(roleAssignVO);
    }

    /*
    * 给已有用户分配角色
    * */
    @PostMapping("/admin/acl/user/doAssignRole")
    public Result doAssignRole(@RequestBody UserRoleAssignDTO userRoleAssignDTO){
        userService.doAssignRole(userRoleAssignDTO);
        return Result.success();
    }
}
