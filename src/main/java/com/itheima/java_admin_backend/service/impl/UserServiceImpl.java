package com.itheima.java_admin_backend.service.impl;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.UserSaveDTO;
import com.itheima.java_admin_backend.dto.UserUpdateDTO;
import com.itheima.java_admin_backend.mapper.UserMapper;
import com.itheima.java_admin_backend.service.UserService;
import com.itheima.java_admin_backend.vo.RoleAssignVO;
import com.itheima.java_admin_backend.vo.RoleVO;
import com.itheima.java_admin_backend.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<UserVO> getUserPage(Long page, Long limit) {
        Long offset = (page - 1) * limit;
        List<UserVO> records = userMapper.selectUserPage(offset, limit);
        Long total = userMapper.countUser();
        return PageResult.of(records, total, page, limit);
    }

    @Override
    public void addUser(UserSaveDTO userSaveDTO) {
        if (userSaveDTO == null
                || userSaveDTO.getUsername() == null || userSaveDTO.getUsername().isBlank()
                || userSaveDTO.getName() == null || userSaveDTO.getName().isBlank()
                || userSaveDTO.getPassword() == null || userSaveDTO.getPassword().isBlank()) {
            throw new RuntimeException("用户名、昵称、密码不能为空");
        }
        // 与登录校验保持一致：入库前对密码做 MD5 加密
        String password = DigestUtils.md5DigestAsHex(userSaveDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        // 业务主键 user_id 采用时间戳生成
        userMapper.insertUser(System.currentTimeMillis(), userSaveDTO.getUsername(), userSaveDTO.getName(), password);
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO == null || userUpdateDTO.getId() == null
                || userUpdateDTO.getUsername() == null || userUpdateDTO.getUsername().isBlank()
                || userUpdateDTO.getName() == null || userUpdateDTO.getName().isBlank()) {
            throw new RuntimeException("用户id、用户名、昵称不能为空");
        }
        int rows = userMapper.updateUser(userUpdateDTO.getId(), userUpdateDTO.getUsername(), userUpdateDTO.getName());
        if (rows == 0) {
            throw new RuntimeException("用户不存在或数据未发生变化");
        }
    }

    @Override
    @Transactional
    public void removeUser(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户id不能为空");
        }
        // 先删除用户与角色的关联，避免残留脏数据；两步在同一事务中，任一步失败整体回滚
        userMapper.deleteUserRoleByUserId(userId);
        int rows = userMapper.deleteUserByUserId(userId);
        if (rows == 0) {
            throw new RuntimeException("用户不存在");
        }
    }

    @Override
    public RoleAssignVO toAssign(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户id不能为空");
        }
        RoleAssignVO vo = new RoleAssignVO();
        vo.setAssignRoles(userMapper.selectAssignRolesByUserId(userId));
        vo.setAllRolesList(userMapper.selectAllRoles());
        return vo;
    }
}
