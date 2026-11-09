package com.itheima.java_admin_backend.service.impl;

import com.itheima.java_admin_backend.common.JwtUtils;
import com.itheima.java_admin_backend.dto.LoginDTO;
import com.itheima.java_admin_backend.entity.User;
import com.itheima.java_admin_backend.mapper.AuthMapper;
import com.itheima.java_admin_backend.service.AuthService;
import com.itheima.java_admin_backend.vo.UserInfoVO;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public String login(LoginDTO loginDTO) {
        if (loginDTO == null
                || loginDTO.getUsername() == null || loginDTO.getUsername().isBlank()
                || loginDTO.getPassword() == null || loginDTO.getPassword().isBlank()) {
            throw new RuntimeException("用户名和密码不能为空");
        }
        User user = authMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        String password = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        return jwtUtils.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public UserInfoVO getUserInfo(String token) {
        // 1. 解析 token 拿用户 id
        Claims claims = jwtUtils.parseToken(token);

        Long userId = Long.valueOf(claims.getSubject());
        // 2. 查用户基本信息
        User user = authMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 3. 查角色（注意用 user_id，不是 id）
        List<String> roles = authMapper.selectRoleNamesByUserId(user.getUserId());
        // 4. 查菜单，按 type 拆分
        List<Map<String, Object>> menus = authMapper.selectMenusByUserId(user.getUserId());
        List<String> routes = new ArrayList<>();
        List<String> buttons = new ArrayList<>();
        for (Map<String, Object> m : menus) {
            String code = (String) m.get("code");
            Integer type = ((Number) m.get("type")).intValue();
            if (type == 1) {
                routes.add(code);
            } else if (type == 2 && code.startsWith("btn.")) {
                buttons.add(code);
            }
        }

        // 5. 组装 VO
        UserInfoVO vo = new UserInfoVO();
        vo.setRoutes(routes);
        vo.setButtons(buttons);
        vo.setRoles(roles);
        vo.setName(user.getUsername());
        vo.setAvatar(user.getAvatar());
        return vo;
    }

    @Override
    public void logout(String token) {
        // JWT 无状态，服务端不保存会话：这里只校验令牌是否有效，真正的退出由前端清除本地 token
        Claims claims = jwtUtils.parseToken(token);
        log.info("用户退出登录，userId={}", claims.getSubject());
    }
}
