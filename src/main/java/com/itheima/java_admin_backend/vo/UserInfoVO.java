package com.itheima.java_admin_backend.vo;

import lombok.Data;

import java.util.List;
@Data
public class UserInfoVO {
    private List<String> routes;
    private List<String> buttons;
    private List<String> roles;
    private String name;
    private String avatar;
}
