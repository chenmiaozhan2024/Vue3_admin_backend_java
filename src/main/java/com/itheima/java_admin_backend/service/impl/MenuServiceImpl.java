package com.itheima.java_admin_backend.service.impl;

import com.itheima.java_admin_backend.dto.MenuSaveDTO;
import com.itheima.java_admin_backend.dto.MenuUpdateDTO;
import com.itheima.java_admin_backend.mapper.MenuMapper;
import com.itheima.java_admin_backend.service.MenuService;
import com.itheima.java_admin_backend.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuVO> getMenuTree() {
        // 1. 查询全部菜单
        List<MenuVO> menuList = menuMapper.selectAllMenus();
        Map<Long, MenuVO> menuMap = new HashMap<>();
        for (MenuVO menu : menuList) {
            menu.setSelect(false);
            menu.setChildren(new ArrayList<>());
            menuMap.put(menu.getId(), menu);
        }
        // 2. 按 pid 组装成树，pid 为 0 的是根节点（与 Node 版 getMenuList 一致）
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
    public void addMenu(MenuSaveDTO menuSaveDTO) {
        if (menuSaveDTO == null || menuSaveDTO.getName() == null || menuSaveDTO.getName().isBlank()) {
            throw new RuntimeException("菜单名称不能为空");
        }
        if (menuSaveDTO.getPid() == null) {
            throw new RuntimeException("父菜单id不能为空");
        }
        // 菜单名称唯一校验（与 Node 版 permissionSave 一致）
        if (menuMapper.countByName(menuSaveDTO.getName()) > 0) {
            throw new RuntimeException("菜单名称已存在");
        }
        menuMapper.insertMenu(System.currentTimeMillis(), menuSaveDTO.getName(),
                menuSaveDTO.getPid(), menuSaveDTO.getCode(),
                menuSaveDTO.getType(), menuSaveDTO.getLevel());
    }

    @Override
    public void updateMenu(MenuUpdateDTO menuUpdateDTO) {
        if (menuUpdateDTO == null || menuUpdateDTO.getId() == null
                || menuUpdateDTO.getName() == null || menuUpdateDTO.getName().isBlank()) {
            throw new RuntimeException("菜单id、菜单名称不能为空");
        }
        // 修改时排除自身，避免误判重名
        if (menuMapper.countByNameExcludeId(menuUpdateDTO.getName(), menuUpdateDTO.getId()) > 0) {
            throw new RuntimeException("菜单名称已存在");
        }
        int rows = menuMapper.updateMenu(menuUpdateDTO.getId(), menuUpdateDTO.getName(),
                menuUpdateDTO.getPid(), menuUpdateDTO.getCode(), menuUpdateDTO.getLevel());
        if (rows == 0) {
            throw new RuntimeException("菜单不存在或数据未发生变化");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMenu(Long menuId) {
        if (menuId == null) {
            throw new RuntimeException("菜单id不能为空");
        }
        // 存在子菜单时不允许删除（与 Node 版 permissionDelete 一致）
        if (menuMapper.countChildren(menuId) > 0) {
            throw new RuntimeException("该菜单下存在子菜单，不能删除");
        }
        // 清理角色与菜单的关联，避免残留脏数据
        menuMapper.deleteRoleMenuByMenuId(menuId);
        int rows = menuMapper.deleteMenuByMenuId(menuId);
        if (rows == 0) {
            throw new RuntimeException("菜单不存在");
        }
    }
}
