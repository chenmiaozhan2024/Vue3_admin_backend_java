package com.itheima.java_admin_backend.vo;

import lombok.Data;

/**
 * SKU 列表项
 */
@Data
public class SkuVO {
    private Long id;
    private Long spuId;
    private Long category3Id;
    private Long tmId;
    private String skuName;
    private String weight;
    private Long price;
    private String skuDesc;
    private String skuDefaultImg;
    private Integer isSale;
}
