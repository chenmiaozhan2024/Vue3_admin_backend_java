package com.itheima.java_admin_backend.vo;

import lombok.Data;

import java.util.List;

/**
 * 菜单/按钮（树形结构，字段与前端 MenuData 类型一致）
 */
@Data
public class MenuVO {
    private Long id;
    private Long pid;
    private String name;
    private String code;
    private String toCode;
    private Integer type;
    private String status;
    private Integer level;
    /** 当前角色是否已分配该菜单 */
    private Boolean select;
    private String createTime;
    private String updateTime;
    private List<MenuVO> children;
}
