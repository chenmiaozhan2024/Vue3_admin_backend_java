package com.itheima.java_admin_backend.dto;

import lombok.Data;

/**
 * 新增角色入参
 */
@Data
public class RoleSaveDTO {
    private String roleName;
    private String remark;
}
