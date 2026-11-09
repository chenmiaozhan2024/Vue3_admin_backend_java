package com.itheima.java_admin_backend.vo;

import lombok.Data;

@Data
public class RoleVO {
    private Long id;
    private String roleName;
    private String remark;
    private String createTime;
    private String updateTime;
}
