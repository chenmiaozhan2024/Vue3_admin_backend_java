package com.itheima.java_admin_backend.vo;

import lombok.Data;

/**
 * SKU 销售属性值
 */
@Data
public class SkuSaleAttrValueVO {
    private Long id;
    private Long saleAttrId;
    private Long saleAttrValueId;
    private String saleAttrName;
    private String saleAttrValueName;
    private Long skuId;
}
