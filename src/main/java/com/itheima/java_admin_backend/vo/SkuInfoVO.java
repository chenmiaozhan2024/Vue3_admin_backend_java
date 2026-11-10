package com.itheima.java_admin_backend.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * SKU 详情（含平台属性、销售属性、图片列表）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuInfoVO extends SkuVO {
    private List<SkuAttrValueVO> skuAttrValueList;
    private List<SkuSaleAttrValueVO> skuSaleAttrValueList;
    private List<SkuImageVO> skuImageList;
}
