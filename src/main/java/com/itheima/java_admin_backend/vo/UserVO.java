package com.itheima.java_admin_backend.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String roleName;
    private String createTime;
    private String updateTime;
}
