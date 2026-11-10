package com.itheima.java_admin_backend.vo;

import lombok.Data;

/**
 * SKU 图片
 */
@Data
public class SkuImageVO {
    private Long id;
    private Long skuId;
    private String imgUrl;
    private Long spuImgId;
    private String isDefault;
}
