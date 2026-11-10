package com.itheima.java_admin_backend.vo;

import lombok.Data;

/**
 * 品牌列表项（字段与 Node 版 trademarkMapper.js 的返回保持一致）
 */
@Data
public class TrademarkVO {
    private Long id;
    private String tmName;
    private String logoUrl;
    private String createTime;
    private String updateTime;
}
