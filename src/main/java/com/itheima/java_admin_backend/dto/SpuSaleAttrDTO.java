package com.itheima.java_admin_backend.dto;

import lombok.Data;

import java.util.List;

/**
 * SPU 销售属性入参
 */
@Data
public class SpuSaleAttrDTO {
    private Long baseSaleAttrId;
    private String saleAttrName;
    private List<SpuSaleAttrValueDTO> spuSaleAttrValueList;
}
