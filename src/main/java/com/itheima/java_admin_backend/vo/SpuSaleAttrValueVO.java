package com.itheima.java_admin_backend.vo;

import lombok.Data;

/**
 * SPU 销售属性值
 */
@Data
public class SpuSaleAttrValueVO {
    private Long id;
    private String saleAttrValueName;
    private Long baseSaleAttrId;
    private Long spuId;
}
