package com.itheima.java_admin_backend.service;

import com.itheima.java_admin_backend.dto.LoginDTO;
import com.itheima.java_admin_backend.vo.UserInfoVO;

public interface AuthService {
    String login(LoginDTO loginDTO);

    UserInfoVO getUserInfo(String token);

    void logout(String token);
}
