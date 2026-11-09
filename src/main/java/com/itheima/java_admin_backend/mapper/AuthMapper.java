package com.itheima.java_admin_backend.mapper;

import com.itheima.java_admin_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AuthMapper {

    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);

    @Select("select * from user where id = #{id}")
    User selectById(Long id);

    /*
    * 通过用户id查询用户角色
    * */
    @Select("SELECT r.role_name AS roleName " +
            "FROM role r INNER JOIN user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> selectRoleNamesByUserId(Long userId);

    /*
    * 如果用户id查询用户菜单
    * */
    @Select("SELECT DISTINCT m.code AS code, m.type AS type " +
            "FROM menu m " +
            "INNER JOIN role_menu rm ON m.menu_id = rm.menu_id " +
            "INNER JOIN user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.type IN (1,2) AND m.code != ''")
    List<Map<String, Object>> selectMenusByUserId(Long userId);
}
