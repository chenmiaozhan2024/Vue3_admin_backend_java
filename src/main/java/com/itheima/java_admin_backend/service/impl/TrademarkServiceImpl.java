package com.itheima.java_admin_backend.service.impl;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.TrademarkSaveDTO;
import com.itheima.java_admin_backend.dto.TrademarkUpdateDTO;
import com.itheima.java_admin_backend.mapper.TrademarkMapper;
import com.itheima.java_admin_backend.service.TrademarkService;
import com.itheima.java_admin_backend.vo.TrademarkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrademarkServiceImpl implements TrademarkService {
    @Autowired
    private TrademarkMapper trademarkMapper;

    @Override
    public PageResult<TrademarkVO> getTrademarkPage(Long page, Long limit) {
        if (page == null || page < 1) {
            page = 1L;
        }
        if (limit == null || limit < 1) {
            limit = 10L;
        }
        Long offset = (page - 1) * limit;
        List<TrademarkVO> records = trademarkMapper.selectTrademarkPage(offset, limit);
        Long total = trademarkMapper.countTrademark();
        return PageResult.of(records, total, page, limit);
    }

    @Override
    public void addTrademark(TrademarkSaveDTO trademarkSaveDTO) {
        if (trademarkSaveDTO == null
                || trademarkSaveDTO.getTmName() == null || trademarkSaveDTO.getTmName().isBlank()
                || trademarkSaveDTO.getLogoUrl() == null || trademarkSaveDTO.getLogoUrl().isBlank()) {
            throw new RuntimeException("品牌名称、品牌logo不能为空");
        }
        // 表中 tm_name 有唯一索引，先查重给出友好提示
        if (trademarkMapper.countByTmName(trademarkSaveDTO.getTmName()) > 0) {
            throw new RuntimeException("品牌名称已存在");
        }
        // 业务主键 tm_id 采用时间戳生成，与 Node 版保持一致
        trademarkMapper.insertTrademark(System.currentTimeMillis(),
                trademarkSaveDTO.getTmName(), trademarkSaveDTO.getLogoUrl());
    }

    @Override
    public void updateTrademark(TrademarkUpdateDTO trademarkUpdateDTO) {
        if (trademarkUpdateDTO == null || trademarkUpdateDTO.getId() == null
                || trademarkUpdateDTO.getTmName() == null || trademarkUpdateDTO.getTmName().isBlank()
                || trademarkUpdateDTO.getLogoUrl() == null || trademarkUpdateDTO.getLogoUrl().isBlank()) {
            throw new RuntimeException("品牌id、品牌名称、品牌logo不能为空");
        }
        int rows = trademarkMapper.updateTrademark(trademarkUpdateDTO.getId(),
                trademarkUpdateDTO.getTmName(), trademarkUpdateDTO.getLogoUrl());
        if (rows == 0) {
            throw new RuntimeException("品牌不存在或数据未发生变化");
        }
    }

    @Override
    public void removeTrademark(Long id) {
        if (id == null) {
            throw new RuntimeException("品牌id不能为空");
        }
        int rows = trademarkMapper.deleteTrademarkById(id);
        if (rows == 0) {
            throw new RuntimeException("品牌不存在");
        }
    }
}
