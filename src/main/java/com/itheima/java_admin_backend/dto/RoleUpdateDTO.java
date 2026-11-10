package com.itheima.java_admin_backend.dto;

import lombok.Data;

/**
 * 修改角色入参
 */
@Data
public class RoleUpdateDTO {
    private Long id;
    private String roleName;
    private String remark;
}
