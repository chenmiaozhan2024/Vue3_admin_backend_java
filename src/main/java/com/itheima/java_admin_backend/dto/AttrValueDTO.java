package com.itheima.java_admin_backend.dto;

import lombok.Data;

/**
 * 属性值入参（id 为空表示新增）
 */
@Data
public class AttrValueDTO {
    private Long id;
    private String valueName;
}
