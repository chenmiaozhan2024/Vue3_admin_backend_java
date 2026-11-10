package com.itheima.java_admin_backend.service;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.RoleSaveDTO;
import com.itheima.java_admin_backend.dto.RoleUpdateDTO;
import com.itheima.java_admin_backend.vo.MenuVO;
import com.itheima.java_admin_backend.vo.RoleVO;

import java.util.List;

public interface RoleService {

    PageResult<RoleVO> getRolePage(Long page, Long limit, String roleName);

    void addRole(RoleSaveDTO roleSaveDTO);

    void updateRole(RoleUpdateDTO roleUpdateDTO);

    void removeRole(Long roleId);

    List<MenuVO> getPermissionTree(Long roleId);

    void doAssignPermission(Long roleId, String permissionId);
}
