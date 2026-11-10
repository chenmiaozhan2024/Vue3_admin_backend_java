package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.Result;
import com.itheima.java_admin_backend.dto.TrademarkSaveDTO;
import com.itheima.java_admin_backend.dto.TrademarkUpdateDTO;
import com.itheima.java_admin_backend.service.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 品牌管理
 */
@RestController
public class TrademarkController {
    @Autowired
    private TrademarkService trademarkService;

    /*
    * 分页获取已有品牌列表
    * */
    @GetMapping("/admin/product/baseTrademark/{page}/{pageSize}")
    public Result getTrademarkPage(@PathVariable Long page,
                                   @PathVariable Long pageSize) {
        return Result.success(trademarkService.getTrademarkPage(page, pageSize));
    }

    /*
    * 新增品牌
    * */
    @PostMapping("/admin/product/baseTrademark/save")
    public Result saveTrademark(@RequestBody TrademarkSaveDTO trademarkSaveDTO) {
        trademarkService.addTrademark(trademarkSaveDTO);
        return Result.success();
    }

    /*
    * 修改品牌
    * */
    @PutMapping("/admin/product/baseTrademark/update")
    public Result updateTrademark(@RequestBody TrademarkUpdateDTO trademarkUpdateDTO) {
        trademarkService.updateTrademark(trademarkUpdateDTO);
        return Result.success();
    }

    /*
    * 删除已有品牌
    * */
    @DeleteMapping("/admin/product/baseTrademark/remove/{id}")
    public Result removeTrademark(@PathVariable Long id) {
        trademarkService.removeTrademark(id);
        return Result.success();
    }
}
