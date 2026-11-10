package com.itheima.java_admin_backend.service;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.SpuSaveDTO;
import com.itheima.java_admin_backend.vo.SaleAttrVO;
import com.itheima.java_admin_backend.vo.SkuVO;
import com.itheima.java_admin_backend.vo.SpuImageVO;
import com.itheima.java_admin_backend.vo.SpuSaleAttrVO;
import com.itheima.java_admin_backend.vo.SpuVO;
import com.itheima.java_admin_backend.vo.TrademarkVO;

import java.util.List;

public interface SpuService {

    PageResult<SpuVO> getSpuPage(Long page, Long limit, Long category3Id);

    List<TrademarkVO> getTrademarkList();

    List<SpuImageVO> getSpuImageList(Long spuId);

    List<SpuSaleAttrVO> getSpuSaleAttrList(Long spuId);

    List<SaleAttrVO> getBaseSaleAttrList();

    List<SkuVO> getSkusBySpuId(Long spuId);

    void deleteSpu(Long spuId);

    void saveSpuInfo(SpuSaveDTO dto);

    void updateSpuInfo(SpuSaveDTO dto);
}
