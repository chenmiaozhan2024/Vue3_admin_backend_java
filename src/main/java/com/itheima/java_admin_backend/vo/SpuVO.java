package com.itheima.java_admin_backend.vo;

import lombok.Data;

/**
 * SPU 列表项
 */
@Data
public class SpuVO {
    private Long id;
    private String spuName;
    private String description;
    private Long category3Id;
    private Long tmId;
}
