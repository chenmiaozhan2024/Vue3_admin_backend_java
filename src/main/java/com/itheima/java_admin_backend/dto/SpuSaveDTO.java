package com.itheima.java_admin_backend.dto;

import lombok.Data;

import java.util.List;

/**
 * 保存 SPU 入参（新增与修改共用，字段与前端提交参数一致）
 */
@Data
public class SpuSaveDTO {
    /** SPU 业务 id，修改时必传，新增时为空 */
    private Long id;
    private String spuName;
    private String description;
    private Long category3Id;
    private Long tmId;
    private List<SpuImageDTO> spuImageList;
    private List<SpuSaleAttrDTO> spuSaleAttrList;
}
