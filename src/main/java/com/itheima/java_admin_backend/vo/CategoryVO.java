package com.itheima.java_admin_backend.vo;

import lombok.Data;

/**
 * 分类（一/二/三级共用，parent 相关字段按层级填充）
 */
@Data
public class CategoryVO {
    private Long id;
    private String name;
    private Long category1Id;
    private Long category2Id;
}
