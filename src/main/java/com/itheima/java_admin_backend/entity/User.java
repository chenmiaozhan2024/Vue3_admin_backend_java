package com.itheima.java_admin_backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private Long userId;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
