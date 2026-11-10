package com.itheima.java_admin_backend.service;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.UserRoleAssignDTO;
import com.itheima.java_admin_backend.dto.UserSaveDTO;
import com.itheima.java_admin_backend.dto.UserUpdateDTO;
import com.itheima.java_admin_backend.vo.RoleAssignVO;
import com.itheima.java_admin_backend.vo.UserVO;

import java.util.List;

public interface UserService {
    PageResult<UserVO> getUserPage(Long page, Long limit, String username);

    void addUser(UserSaveDTO userSaveDTO);

    void updateUser(UserUpdateDTO userUpdateDTO);

    void removeUser(Long userId);

    void batchRemoveUsers(List<Long> userIds);

    RoleAssignVO toAssign(Long userId);

    void doAssignRole(UserRoleAssignDTO userRoleAssignDTO);
}
