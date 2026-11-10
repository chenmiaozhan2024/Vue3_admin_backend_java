package com.itheima.java_admin_backend.service;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.SkuSaveDTO;
import com.itheima.java_admin_backend.vo.SkuInfoVO;
import com.itheima.java_admin_backend.vo.SkuVO;

public interface SkuService {

    PageResult<SkuVO> getSkuPage(Long page, Long pageSize);

    void onSale(Long skuId);

    void cancelSale(Long skuId);

    SkuInfoVO getSkuInfo(Long skuId);

    void deleteSku(Long skuId);

    void saveSkuInfo(SkuSaveDTO skuSaveDTO);
}
