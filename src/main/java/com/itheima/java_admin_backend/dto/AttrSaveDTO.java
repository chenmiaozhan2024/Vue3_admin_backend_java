package com.itheima.java_admin_backend.dto;

import lombok.Data;

import java.util.List;

/**
 * 保存属性入参（新增与修改共用：id 为空表示新增）
 */
@Data
public class AttrSaveDTO {
    /** 属性业务 id，为空表示新增 */
    private Long id;
    private String attrName;
    private Long categoryId;
    private Integer categoryLevel;
    private List<AttrValueDTO> attrValueList;
}
