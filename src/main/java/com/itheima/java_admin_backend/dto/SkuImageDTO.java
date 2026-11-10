package com.itheima.java_admin_backend.dto;

import lombok.Data;

/**
 * SKU 图片入参
 */
@Data
public class SkuImageDTO {
    private String imgUrl;
    private Long spuImgId;
    private String isDefault;
}
