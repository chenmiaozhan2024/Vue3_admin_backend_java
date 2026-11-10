package com.itheima.java_admin_backend.dto;

import lombok.Data;

/**
 * 修改品牌入参
 */
@Data
public class TrademarkUpdateDTO {
    private Long id;
    private String tmName;
    private String logoUrl;
}
