package com.itheima.java_admin_backend.dto;

import lombok.Data;

/**
 * 修改菜单/按钮入参
 */
@Data
public class MenuUpdateDTO {
    private Long id;
    private String name;
    private Long pid;
    private String code;
    private Integer level;
}
