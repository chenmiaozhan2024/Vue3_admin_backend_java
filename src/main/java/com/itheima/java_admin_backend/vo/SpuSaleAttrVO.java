package com.itheima.java_admin_backend.vo;

import lombok.Data;

import java.util.List;

/**
 * SPU 销售属性（含属性值列表）
 */
@Data
public class SpuSaleAttrVO {
    private Long id;
    private Long baseSaleAttrId;
    private String saleAttrName;
    private Long spuId;
    private List<SpuSaleAttrValueVO> spuSaleAttrValueList;
}
