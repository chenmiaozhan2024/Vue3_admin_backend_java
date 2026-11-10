package com.itheima.java_admin_backend.mapper;

import com.itheima.java_admin_backend.vo.MenuVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 菜单管理模块（菜单与按钮）
 */
@Mapper
public interface MenuMapper {

    @Select("SELECT menu_id AS id, pid, name, code, to_code AS toCode, type, " +
            "IFNULL(status, '') AS status, level, " +
            "DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:%s') AS createTime, " +
            "DATE_FORMAT(update_time, '%Y-%m-%d %H:%i:%s') AS updateTime " +
            "FROM menu ORDER BY menu_id")
    List<MenuVO> selectAllMenus();

    @Select("SELECT COUNT(*) FROM menu WHERE name = #{name}")
    Long countByName(@Param("name") String name);

    @Select("SELECT COUNT(*) FROM menu WHERE name = #{name} AND menu_id != #{menuId}")
    Long countByNameExcludeId(@Param("name") String name, @Param("menuId") Long menuId);

    @Insert("INSERT INTO menu (menu_id, name, pid, code, to_code, type, status, level) " +
            "VALUES (#{menuId}, #{name}, #{pid}, #{code}, '', #{type}, '0', #{level})")
    int insertMenu(@Param("menuId") Long menuId,
                   @Param("name") String name,
                   @Param("pid") Long pid,
                   @Param("code") String code,
                   @Param("type") Integer type,
                   @Param("level") Integer level);

    @Update("UPDATE menu SET name = #{name}, pid = #{pid}, code = #{code}, level = #{level} " +
            "WHERE menu_id = #{menuId}")
    int updateMenu(@Param("menuId") Long menuId,
                   @Param("name") String name,
                   @Param("pid") Long pid,
                   @Param("code") String code,
                   @Param("level") Integer level);

    @Select("SELECT COUNT(*) FROM menu WHERE pid = #{menuId}")
    Long countChildren(@Param("menuId") Long menuId);

    @Delete("DELETE FROM role_menu WHERE menu_id = #{menuId}")
    int deleteRoleMenuByMenuId(@Param("menuId") Long menuId);

    @Delete("DELETE FROM menu WHERE menu_id = #{menuId}")
    int deleteMenuByMenuId(@Param("menuId") Long menuId);
}
