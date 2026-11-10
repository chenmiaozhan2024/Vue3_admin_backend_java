package com.itheima.java_admin_backend.service;

import com.itheima.java_admin_backend.dto.MenuSaveDTO;
import com.itheima.java_admin_backend.dto.MenuUpdateDTO;
import com.itheima.java_admin_backend.vo.MenuVO;

import java.util.List;

public interface MenuService {

    List<MenuVO> getMenuTree();

    void addMenu(MenuSaveDTO menuSaveDTO);

    void updateMenu(MenuUpdateDTO menuUpdateDTO);

    void removeMenu(Long menuId);
}
