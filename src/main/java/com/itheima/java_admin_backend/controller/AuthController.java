package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.Result;
import com.itheima.java_admin_backend.dto.LoginDTO;
import com.itheima.java_admin_backend.service.AuthService;
import com.itheima.java_admin_backend.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;
    /*
    * 登录
    * */
    @PostMapping("/admin/acl/index/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        String token= authService.login(loginDTO);
        return Result.success(token);
    }
    /*
    * 获取用户信息
    * */
    @GetMapping("/admin/acl/index/info")
    public Result getUserInfo(@RequestHeader("token") String token){
     UserInfoVO vo= authService.getUserInfo(token);
     return Result.success(vo);
    }
    /*
    * 退出登录
    * */
    @PostMapping("/admin/acl/index/logout")
    public Result logout(@RequestHeader("token") String token){
        authService.logout(token);
        return Result.success();
    }
}
