package com.itheima.java_admin_backend.service;

import com.itheima.java_admin_backend.common.PageResult;
import com.itheima.java_admin_backend.dto.TrademarkSaveDTO;
import com.itheima.java_admin_backend.dto.TrademarkUpdateDTO;
import com.itheima.java_admin_backend.vo.TrademarkVO;

public interface TrademarkService {

    PageResult<TrademarkVO> getTrademarkPage(Long page, Long limit);

    void addTrademark(TrademarkSaveDTO trademarkSaveDTO);

    void updateTrademark(TrademarkUpdateDTO trademarkUpdateDTO);

    void removeTrademark(Long id);
}
