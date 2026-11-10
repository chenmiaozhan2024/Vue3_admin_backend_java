package com.itheima.java_admin_backend.dto;

import lombok.Data;

import java.util.List;

/**
 * 保存 SKU 入参（字段名与前端 SkuForm.vue 提交的参数一致）
 */
@Data
public class SkuSaveDTO {
    private Long spuId;
    private Long category3Id;
    private Long tmId;
    private String skuName;
    private String price;
    private String weight;
    private String skuDesc;
    private String skuDefaultImg;
    private Integer isSale;
    private List<SkuAttrValueDTO> skuAttrValueList;
    private List<SkuSaleAttrValueDTO> skuSaleAttrValueList;
    private List<SkuImageDTO> skuImageList;
}
