package com.itheima.java_admin_backend.service.impl;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.SkuAttrValueDTO;
import com.itheima.java_admin_backend.dto.SkuImageDTO;
import com.itheima.java_admin_backend.dto.SkuSaleAttrValueDTO;
import com.itheima.java_admin_backend.dto.SkuSaveDTO;
import com.itheima.java_admin_backend.mapper.SkuMapper;
import com.itheima.java_admin_backend.service.SkuService;
import com.itheima.java_admin_backend.vo.SkuAttrValueVO;
import com.itheima.java_admin_backend.vo.SkuImageVO;
import com.itheima.java_admin_backend.vo.SkuInfoVO;
import com.itheima.java_admin_backend.vo.SkuSaleAttrValueVO;
import com.itheima.java_admin_backend.vo.SkuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    /*
    * 业务主键生成：时间戳 + 随机数，与 Node 版保持一致
    * */
    private Long generateId() {
        return System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(1000);
    }

    @Override
    public PageResult<SkuVO> getSkuPage(Long page, Long pageSize) {
        if (page == null || page < 1) {
            page = 1L;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10L;
        }
        Long offset = (page - 1) * pageSize;
        List<SkuVO> records = skuMapper.selectSkuPage(offset, pageSize);
        Long total = skuMapper.countSku();
        return PageResult.of(records, total, page, pageSize);
    }

    @Override
    public void onSale(Long skuId) {
        if (skuId == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        // 校验商品是否存在（Node 版 putGoosOnSale 的校验逻辑）
        if (skuMapper.selectIsSaleBySkuId(skuId) == null) {
            throw new RuntimeException("商品不存在");
        }
        skuMapper.updateIsSaleOn(skuId);
    }

    @Override
    public void cancelSale(Long skuId) {
        if (skuId == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        if (skuMapper.selectIsSaleBySkuId(skuId) == null) {
            throw new RuntimeException("商品不存在");
        }
        skuMapper.updateIsSaleOff(skuId);
    }

    @Override
    public SkuInfoVO getSkuInfo(Long skuId) {
        if (skuId == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        // SKU 基本信息
        SkuInfoVO skuInfo = skuMapper.selectSkuBySkuId(skuId);
        if (skuInfo == null) {
            throw new RuntimeException("商品不存在");
        }
        // 平台属性、销售属性、图片（按 sku_id 过滤，修正 Node 版未过滤的问题）
        List<SkuAttrValueVO> attrValueList = skuMapper.selectSkuAttrValues(skuId);
        List<SkuSaleAttrValueVO> saleAttrValueList = skuMapper.selectSkuSaleAttrValues(skuId);
        List<SkuImageVO> imageList = skuMapper.selectSkuImages(skuId);
        skuInfo.setSkuAttrValueList(attrValueList);
        skuInfo.setSkuSaleAttrValueList(saleAttrValueList);
        skuInfo.setSkuImageList(imageList);
        return skuInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSku(Long skuId) {
        if (skuId == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        if (skuMapper.selectIsSaleBySkuId(skuId) == null) {
            throw new RuntimeException("商品不存在");
        }
        // 1. 删除图片 2. 删除平台属性值 3. 删除销售属性值 4. 删除 SKU 本体
        skuMapper.deleteSkuImages(skuId);
        skuMapper.deleteSkuAttrValues(skuId);
        skuMapper.deleteSkuSaleAttrValues(skuId);
        skuMapper.deleteSkuBySkuId(skuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSkuInfo(SkuSaveDTO dto) {
        if (dto == null
                || dto.getSpuId() == null
                || dto.getCategory3Id() == null
                || dto.getTmId() == null
                || dto.getSkuName() == null || dto.getSkuName().isBlank()) {
            throw new RuntimeException("缺少必要参数");
        }
        Long skuId = generateId();
        Integer isSale = dto.getIsSale() == null ? 0 : dto.getIsSale();
        Long price = null;
        if (dto.getPrice() != null && !dto.getPrice().isBlank()) {
            price = Long.parseLong(dto.getPrice());
        }
        // 1. 插入 SKU 主表
        skuMapper.insertSku(skuId, dto.getSpuId(), dto.getCategory3Id(), dto.getTmId(),
                dto.getSkuName(), dto.getWeight(), price, dto.getSkuDesc(),
                dto.getSkuDefaultImg(), isSale);

        // 2. 插入图片列表
        List<SkuImageDTO> imageList = dto.getSkuImageList();
        if (imageList != null) {
            for (SkuImageDTO image : imageList) {
                if (image.getImgUrl() != null && !image.getImgUrl().isBlank()) {
                    String isDefault = "1".equals(image.getIsDefault()) ? "1" : "0";
                    skuMapper.insertSkuImage(generateId(), skuId, image.getImgUrl(),
                            image.getSpuImgId(), isDefault);
                }
            }
        }

        // 3. 插入平台属性值（attr_name / value_name 由 attr、attr_value 表反查后冗余存储）
        List<SkuAttrValueDTO> attrValueList = dto.getSkuAttrValueList();
        if (attrValueList != null) {
            for (SkuAttrValueDTO attrValue : attrValueList) {
                if (attrValue.getAttrId() == null || attrValue.getValueId() == null) {
                    continue;
                }
                String attrName = skuMapper.selectAttrName(attrValue.getAttrId());
                String valueName = skuMapper.selectAttrValueName(attrValue.getValueId());
                if (attrName != null && valueName != null) {
                    skuMapper.insertSkuAttrValue(generateId(), attrValue.getAttrId(),
                            attrValue.getValueId(), valueName, attrName, skuId);
                }
            }
        }

        // 4. 插入销售属性值（sale_attr_name / sale_attr_value_name 由 spu_sale_attr、sale_attr_value 反查后冗余存储）
        List<SkuSaleAttrValueDTO> saleAttrValueList = dto.getSkuSaleAttrValueList();
        if (saleAttrValueList != null) {
            for (SkuSaleAttrValueDTO saleAttrValue : saleAttrValueList) {
                if (saleAttrValue.getSaleAttrId() == null || saleAttrValue.getSaleAttrValueId() == null) {
                    continue;
                }
                String saleAttrName = skuMapper.selectSaleAttrName(saleAttrValue.getSaleAttrId());
                String saleAttrValueName = skuMapper.selectSaleAttrValueName(saleAttrValue.getSaleAttrValueId());
                if (saleAttrName != null && saleAttrValueName != null) {
                    skuMapper.insertSkuSaleAttrValue(generateId(), saleAttrValue.getSaleAttrId(),
                            saleAttrValue.getSaleAttrValueId(), saleAttrName, saleAttrValueName, skuId);
                }
            }
        }
    }
}
