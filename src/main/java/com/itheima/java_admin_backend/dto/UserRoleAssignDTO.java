package com.itheima.java_admin_backend.dto;

import lombok.Data;

import java.util.List;

/**
 * 给用户分配角色入参
 */
@Data
public class UserRoleAssignDTO {
    private Long userId;
    private List<Long> roleIdList;
}
