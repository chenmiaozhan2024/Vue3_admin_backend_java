package com.itheima.java_admin_backend.vo;

import lombok.Data;

import java.util.List;

/**
 * 属性（含属性值列表）
 */
@Data
public class AttrVO {
    private Long id;
    private String attrName;
    private Long categoryId;
    private Integer categoryLevel;
    private List<AttrValueVO> attrValueList;
}
