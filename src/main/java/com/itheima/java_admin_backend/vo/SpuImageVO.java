package com.itheima.java_admin_backend.vo;

import lombok.Data;

/**
 * SPU 图片
 */
@Data
public class SpuImageVO {
    private Long id;
    private String imgName;
    private String imgUrl;
    private Long spuId;
}
