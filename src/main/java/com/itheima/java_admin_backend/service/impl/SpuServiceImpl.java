package com.itheima.java_admin_backend.service.impl;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.SpuImageDTO;
import com.itheima.java_admin_backend.dto.SpuSaleAttrDTO;
import com.itheima.java_admin_backend.dto.SpuSaleAttrValueDTO;
import com.itheima.java_admin_backend.dto.SpuSaveDTO;
import com.itheima.java_admin_backend.mapper.SpuMapper;
import com.itheima.java_admin_backend.service.SpuService;
import com.itheima.java_admin_backend.vo.SaleAttrVO;
import com.itheima.java_admin_backend.vo.SkuVO;
import com.itheima.java_admin_backend.vo.SpuImageVO;
import com.itheima.java_admin_backend.vo.SpuSaleAttrVO;
import com.itheima.java_admin_backend.vo.SpuVO;
import com.itheima.java_admin_backend.vo.TrademarkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    /*
    * 业务主键生成：时间戳 + 随机数，与 Node 版保持一致
    * */
    private Long generateId() {
        return System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(1000);
    }

    @Override
    public PageResult<SpuVO> getSpuPage(Long page, Long limit, Long category3Id) {
        if (page == null || page < 1) {
            page = 1L;
        }
        if (limit == null || limit < 1) {
            limit = 10L;
        }
        Long offset = (page - 1) * limit;
        List<SpuVO> records = spuMapper.selectSpuPage(offset, limit, category3Id);
        Long total = spuMapper.countSpu(category3Id);
        return PageResult.of(records, total, page, limit);
    }

    @Override
    public List<TrademarkVO> getTrademarkList() {
        return spuMapper.selectAllTrademarks();
    }

    @Override
    public List<SpuImageVO> getSpuImageList(Long spuId) {
        if (spuId == null) {
            throw new RuntimeException("spuId不能为空");
        }
        return spuMapper.selectSpuImages(spuId);
    }

    @Override
    public List<SpuSaleAttrVO> getSpuSaleAttrList(Long spuId) {
        if (spuId == null) {
            throw new RuntimeException("spuId不能为空");
        }
        // 查询销售属性列表，并为每个属性组装属性值列表（与 Node 版 getSpuSaleAttrList 逻辑一致）
        List<SpuSaleAttrVO> saleAttrList = spuMapper.selectSpuSaleAttrs(spuId);
        for (SpuSaleAttrVO saleAttr : saleAttrList) {
            saleAttr.setSpuSaleAttrValueList(
                    spuMapper.selectSpuSaleAttrValues(saleAttr.getSpuId(), saleAttr.getBaseSaleAttrId()));
        }
        return saleAttrList;
    }

    @Override
    public List<SaleAttrVO> getBaseSaleAttrList() {
        return spuMapper.selectBaseSaleAttrs();
    }

    @Override
    public List<SkuVO> getSkusBySpuId(Long spuId) {
        if (spuId == null) {
            throw new RuntimeException("spuId不能为空");
        }
        return spuMapper.selectSkusBySpuId(spuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpu(Long spuId) {
        if (spuId == null) {
            throw new RuntimeException("spuId不能为空");
        }
        if (spuMapper.countSkusBySpuId(spuId) > 0) {
            throw new RuntimeException("该SPU下存在SKU，不能直接删除");
        }
        // 先删子表再删主表（与 Node 版 deleteSpuBySpuId 顺序一致）
        spuMapper.deleteSpuImages(spuId);
        spuMapper.deleteSpuSaleAttrs(spuId);
        spuMapper.deleteSpuSaleAttrValues(spuId);
        int rows = spuMapper.deleteSpuBySpuId(spuId);
        if (rows == 0) {
            throw new RuntimeException("SPU不存在");
        }
    }

    /*
    * 保存 SPU 的图片与销售属性（新增与修改共用）
    * */
    private void saveSpuChildren(Long spuId, SpuSaveDTO dto) {
        // 1. 图片列表
        List<SpuImageDTO> imageList = dto.getSpuImageList();
        if (imageList != null) {
            for (SpuImageDTO image : imageList) {
                if (image.getImgUrl() != null && !image.getImgUrl().isBlank()
                        && image.getImgName() != null && !image.getImgName().isBlank()) {
                    spuMapper.insertSpuImage(generateId(), image.getImgName(), image.getImgUrl(), spuId);
                }
            }
        }
        // 2. 销售属性及属性值
        List<SpuSaleAttrDTO> saleAttrList = dto.getSpuSaleAttrList();
        if (saleAttrList != null) {
            for (SpuSaleAttrDTO saleAttr : saleAttrList) {
                if (saleAttr.getBaseSaleAttrId() == null || saleAttr.getSaleAttrName() == null) {
                    continue;
                }
                spuMapper.insertSpuSaleAttr(generateId(), saleAttr.getBaseSaleAttrId(),
                        saleAttr.getSaleAttrName(), spuId);
                List<SpuSaleAttrValueDTO> valueList = saleAttr.getSpuSaleAttrValueList();
                if (valueList != null) {
                    for (SpuSaleAttrValueDTO value : valueList) {
                        if (value.getSaleAttrValueName() != null && !value.getSaleAttrValueName().isBlank()) {
                            spuMapper.insertSpuSaleAttrValue(generateId(), value.getSaleAttrValueName(),
                                    saleAttr.getBaseSaleAttrId(), spuId);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuSaveDTO dto) {
        if (dto == null || dto.getSpuName() == null || dto.getSpuName().isBlank()
                || dto.getCategory3Id() == null || dto.getTmId() == null) {
            throw new RuntimeException("缺少必要参数");
        }
        Long spuId = generateId();
        spuMapper.insertSpu(spuId, dto.getSpuName(), dto.getDescription(),
                dto.getCategory3Id(), dto.getTmId());
        saveSpuChildren(spuId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpuInfo(SpuSaveDTO dto) {
        if (dto == null || dto.getId() == null
                || dto.getSpuName() == null || dto.getSpuName().isBlank()
                || dto.getCategory3Id() == null || dto.getTmId() == null) {
            throw new RuntimeException("缺少必要参数");
        }
        int rows = spuMapper.updateSpu(dto.getId(), dto.getSpuName(), dto.getDescription(),
                dto.getCategory3Id(), dto.getTmId());
        if (rows == 0) {
            throw new RuntimeException("SPU不存在或数据未发生变化");
        }
        // 图片与销售属性整体重建：先删旧数据再插入（与 Node 版 updateSpuInfo 一致）
        spuMapper.deleteSpuImages(dto.getId());
        spuMapper.deleteSpuSaleAttrs(dto.getId());
        spuMapper.deleteSpuSaleAttrValues(dto.getId());
        saveSpuChildren(dto.getId(), dto);
    }
}
