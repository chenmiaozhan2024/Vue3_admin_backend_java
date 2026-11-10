package com.itheima.java_admin_backend.dto;

import lombok.Data;

/**
 * 新增菜单/按钮入参（字段与前端 MenuParams 一致）
 */
@Data
public class MenuSaveDTO {
    private String name;
    private Long pid;
    private String code;
    private Integer type;
    private Integer level;
}
