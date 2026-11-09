package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public Result hello(){
       return Result.success("success");
    }

}
