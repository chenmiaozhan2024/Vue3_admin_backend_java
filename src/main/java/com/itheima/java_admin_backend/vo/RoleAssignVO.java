package com.itheima.java_admin_backend.vo;

import lombok.Data;

import java.util.List;

/**
 * 用户已分配角色 + 全部角色
 */
@Data
public class RoleAssignVO {
    private List<RoleVO> assignRoles;
    private List<RoleVO> allRolesList;
}
