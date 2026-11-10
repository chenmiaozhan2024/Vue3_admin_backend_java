package com.itheima.java_admin_backend.vo;

import lombok.Data;

/**
 * SKU 平台属性值
 */
@Data
public class SkuAttrValueVO {
    private Long id;
    private Long attrId;
    private Long valueId;
    private String valueName;
    private String attrName;
    private Long skuId;
}
