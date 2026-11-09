package com.itheima.java_admin_backend.mapper;

import com.itheima.java_admin_backend.vo.RoleVO;
import com.itheima.java_admin_backend.vo.UserVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户管理模块（与认证用的 AuthMapper 分开）
 */
@Mapper
public interface UserMapper {

    /*
    * 分页查询用户列表（含角色名）
    * */
    @Select("SELECT " +
            "u.user_id AS id, " +
            "u.username, " +
            "u.password, " +
            "u.name, " +
            "IFNULL(u.phone, '') AS phone, " +
            "IFNULL(GROUP_CONCAT(DISTINCT r.role_name SEPARATOR ','), '') AS roleName, " +
            "DATE_FORMAT(u.create_time, '%Y-%m-%d %H:%i:%s') AS createTime, " +
            "DATE_FORMAT(u.update_time, '%Y-%m-%d %H:%i:%s') AS updateTime " +
            "FROM user u " +
            "LEFT JOIN user_role ur ON u.user_id = ur.user_id " +
            "LEFT JOIN role r ON ur.role_id = r.role_id " +
            "GROUP BY u.user_id " +
            "ORDER BY u.user_id " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<UserVO> selectUserPage(@Param("offset") Long offset, @Param("limit") Long limit);

    /*
    * 统计用户总数
    * */
    @Select("SELECT COUNT(DISTINCT u.user_id) FROM user u")
    Long countUser();

    /*
    * 新增一个用户
    * */
    @Insert("INSERT INTO user (user_id, username, name, password) " +
            "VALUES (#{userId}, #{username}, #{name}, #{password})")
    int insertUser(@Param("userId") Long userId,
                   @Param("username") String username,
                   @Param("name") String name,
                   @Param("password") String password);

    /*
    * 更新一个用户的用户名和昵称
    * */
    @Update("UPDATE user SET username = #{username}, name = #{name} " +
            "WHERE user_id = #{userId}")
    int updateUser(@Param("userId") Long userId,
                   @Param("username") String username,
                   @Param("name") String name);

    /*
    * 删除用户与角色的关联关系
    * */
    @Delete("DELETE FROM user_role WHERE user_id = #{userId}")
    int deleteUserRoleByUserId(@Param("userId") Long userId);

    /*
    * 删除用户
    * */
    @Delete("DELETE FROM user WHERE user_id = #{userId}")
    int deleteUserByUserId(@Param("userId") Long userId);

    /*
    * 查询某个用户已分配的角色
    * */
    @Select("SELECT r.role_id AS id, " +
            "r.role_name AS roleName, " +
            "IFNULL(r.remark, '') AS remark, " +
            "DATE_FORMAT(r.create_time, '%Y-%m-%d %H:%i:%s') AS createTime, " +
            "DATE_FORMAT(r.update_time, '%Y-%m-%d %H:%i:%s') AS updateTime " +
            "FROM user_role ur " +
            "JOIN role r ON ur.role_id = r.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<RoleVO> selectAssignRolesByUserId(@Param("userId") Long userId);

    /*
    * 查询全部角色
    * */
    @Select("SELECT role_id AS id, " +
            "role_name AS roleName, " +
            "IFNULL(remark, '') AS remark, " +
            "DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:%s') AS createTime, " +
            "DATE_FORMAT(update_time, '%Y-%m-%d %H:%i:%s') AS updateTime " +
            "FROM role")
    List<RoleVO> selectAllRoles();
}
