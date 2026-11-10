package com.itheima.java_admin_backend.mapper;

import com.itheima.java_admin_backend.vo.MenuVO;
import com.itheima.java_admin_backend.vo.RoleVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 角色管理模块（含角色权限分配）
 */
@Mapper
public interface RoleMapper {

    @Select("SELECT role_id AS id, role_name AS roleName, IFNULL(remark, '') AS remark, " +
            "DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:%s') AS createTime, " +
            "DATE_FORMAT(update_time, '%Y-%m-%d %H:%i:%s') AS updateTime " +
            "FROM role WHERE role_name LIKE CONCAT('%', #{roleName}, '%') " +
            "ORDER BY role_id LIMIT #{limit} OFFSET #{offset}")
    List<RoleVO> selectRolePage(@Param("offset") Long offset,
                                @Param("limit") Long limit,
                                @Param("roleName") String roleName);

    @Select("SELECT COUNT(*) FROM role WHERE role_name LIKE CONCAT('%', #{roleName}, '%')")
    Long countRole(@Param("roleName") String roleName);

    @Select("SELECT COUNT(*) FROM role WHERE role_name = #{roleName}")
    Long countByRoleName(@Param("roleName") String roleName);

    @Select("SELECT COUNT(*) FROM role WHERE role_name = #{roleName} AND role_id != #{roleId}")
    Long countByRoleNameExcludeId(@Param("roleName") String roleName, @Param("roleId") Long roleId);

    @Insert("INSERT INTO role (role_id, role_name, remark) VALUES (#{roleId}, #{roleName}, #{remark})")
    int insertRole(@Param("roleId") Long roleId,
                   @Param("roleName") String roleName,
                   @Param("remark") String remark);

    @Update("UPDATE role SET role_name = #{roleName}, remark = #{remark} WHERE role_id = #{roleId}")
    int updateRole(@Param("roleId") Long roleId,
                   @Param("roleName") String roleName,
                   @Param("remark") String remark);

    @Delete("DELETE FROM role WHERE role_id = #{roleId}")
    int deleteRoleByRoleId(@Param("roleId") Long roleId);

    @Delete("DELETE FROM role_menu WHERE role_id = #{roleId}")
    int deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

    @Delete("DELETE FROM user_role WHERE role_id = #{roleId}")
    int deleteUserRoleByRoleId(@Param("roleId") Long roleId);

    @Select("SELECT menu_id AS id, pid, name, code, to_code AS toCode, type, status, level, " +
            "DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:%s') AS createTime, " +
            "DATE_FORMAT(update_time, '%Y-%m-%d %H:%i:%s') AS updateTime " +
            "FROM menu ORDER BY menu_id")
    List<MenuVO> selectAllMenus();

    @Select("SELECT menu_id FROM role_menu WHERE role_id = #{roleId}")
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    @Insert("<script>INSERT INTO role_menu (role_id, menu_id) VALUES " +
            "<foreach collection='menuIds' item='menuId' separator=','>(#{roleId}, #{menuId})</foreach></script>")
    int insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
}
