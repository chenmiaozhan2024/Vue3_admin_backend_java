package com.itheima.java_admin_backend.service.impl;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.RoleSaveDTO;
import com.itheima.java_admin_backend.dto.RoleUpdateDTO;
import com.itheima.java_admin_backend.mapper.RoleMapper;
import com.itheima.java_admin_backend.service.RoleService;
import com.itheima.java_admin_backend.vo.MenuVO;
import com.itheima.java_admin_backend.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageResult<RoleVO> getRolePage(Long page, Long limit, String roleName) {
        if (page == null || page < 1) {
            page = 1L;
        }
        if (limit == null || limit < 1) {
            limit = 10L;
        }
        String keyword = roleName == null ? "" : roleName;
        Long offset = (page - 1) * limit;
        List<RoleVO> records = roleMapper.selectRolePage(offset, limit, keyword);
        Long total = roleMapper.countRole(keyword);
        return PageResult.of(records, total, page, limit);
    }

    @Override
    public void addRole(RoleSaveDTO roleSaveDTO) {
        if (roleSaveDTO == null || roleSaveDTO.getRoleName() == null || roleSaveDTO.getRoleName().isBlank()) {
            throw new RuntimeException("角色名称不能为空");
        }
        // 角色名称唯一性校验
        if (roleMapper.countByRoleName(roleSaveDTO.getRoleName()) > 0) {
            throw new RuntimeException("角色名称已存在");
        }
        roleMapper.insertRole(System.currentTimeMillis(), roleSaveDTO.getRoleName(), roleSaveDTO.getRemark());
    }

    @Override
    public void updateRole(RoleUpdateDTO roleUpdateDTO) {
        if (roleUpdateDTO == null || roleUpdateDTO.getId() == null
                || roleUpdateDTO.getRoleName() == null || roleUpdateDTO.getRoleName().isBlank()) {
            throw new RuntimeException("角色id、角色名称不能为空");
        }
        // 修改时排除自身，避免把当前角色名的唯一性误判为重复
        if (roleMapper.countByRoleNameExcludeId(roleUpdateDTO.getRoleName(), roleUpdateDTO.getId()) > 0) {
            throw new RuntimeException("角色名称已存在");
        }
        int rows = roleMapper.updateRole(roleUpdateDTO.getId(), roleUpdateDTO.getRoleName(),
                roleUpdateDTO.getRemark());
        if (rows == 0) {
            throw new RuntimeException("角色不存在或数据未发生变化");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRole(Long roleId) {
        if (roleId == null) {
            throw new RuntimeException("角色id不能为空");
        }
        // 先清理角色与菜单、用户的关联，再删除角色
        roleMapper.deleteRoleMenuByRoleId(roleId);
        roleMapper.deleteUserRoleByRoleId(roleId);
        int rows = roleMapper.deleteRoleByRoleId(roleId);
        if (rows == 0) {
            throw new RuntimeException("角色不存在");
        }
    }

    @Override
    public List<MenuVO> getPermissionTree(Long roleId) {
        if (roleId == null) {
            throw new RuntimeException("角色id不能为空");
        }
        // 1. 查询全部菜单，已分配给该角色的标记 select = true
        List<MenuVO> menuList = roleMapper.selectAllMenus();
        Set<Long> assignMenuIds = new HashSet<>(roleMapper.selectMenuIdsByRoleId(roleId));
        Map<Long, MenuVO> menuMap = new HashMap<>();
        for (MenuVO menu : menuList) {
            menu.setSelect(assignMenuIds.contains(menu.getId()));
            menu.setChildren(new ArrayList<>());
            menuMap.put(menu.getId(), menu);
        }
        // 2. 按 pid 组装成树，pid 为 0 的是根节点
        List<MenuVO> tree = new ArrayList<>();
        for (MenuVO menu : menuList) {
            if (menu.getPid() == null || menu.getPid() == 0L) {
                tree.add(menu);
            } else if (menuMap.containsKey(menu.getPid())) {
                menuMap.get(menu.getPid()).getChildren().add(menu);
            }
        }
        return tree;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doAssignPermission(Long roleId, String permissionId) {
        if (roleId == null) {
            throw new RuntimeException("角色id不能为空");
        }
        // 前端以逗号分隔字符串传递 permissionId，与 Node 版解析方式一致
        List<Long> menuIds = new ArrayList<>();
        if (permissionId != null && !permissionId.isBlank()) {
            for (String id : permissionId.split(",")) {
                String trimmed = id.trim();
                if (!trimmed.isEmpty()) {
                    menuIds.add(Long.parseLong(trimmed));
                }
            }
        }
        // 先清空原有权限，再按新列表重新分配
        roleMapper.deleteRoleMenuByRoleId(roleId);
        if (!menuIds.isEmpty()) {
            roleMapper.insertRoleMenus(roleId, menuIds);
        }
    }
}
