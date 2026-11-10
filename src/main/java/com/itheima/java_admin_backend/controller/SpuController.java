package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.Result;
import com.itheima.java_admin_backend.dto.SpuSaveDTO;
import com.itheima.java_admin_backend.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * SPU 管理（saveSkuInfo 在 SkuController 中，此处不再重复注册）
 */
@RestController
public class SpuController {
    @Autowired
    private SpuService spuService;

    @GetMapping("/admin/product/{page}/{limit}")
    public Result getSpuPage(@PathVariable Long page,
                             @PathVariable Long limit,
                             @RequestParam(required = false) Long category3Id) {
        return Result.success(spuService.getSpuPage(page, limit, category3Id));
    }

    @GetMapping("/admin/product/baseTrademark/getTrademarkList")
    public Result getTrademarkList() {
        return Result.success(spuService.getTrademarkList());
    }

    @GetMapping("/admin/product/spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable Long spuId) {
        return Result.success(spuService.getSpuImageList(spuId));
    }

    @GetMapping("/admin/product/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable Long spuId) {
        return Result.success(spuService.getSpuSaleAttrList(spuId));
    }

    @GetMapping("/admin/product/baseSaleAttrList")
    public Result getBaseSaleAttrList() {
        return Result.success(spuService.getBaseSaleAttrList());
    }

    @GetMapping("/admin/product/findBySpuId/{spuId}")
    public Result getSkusBySpuId(@PathVariable Long spuId) {
        return Result.success(spuService.getSkusBySpuId(spuId));
    }

    @PostMapping("/admin/product/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuSaveDTO spuSaveDTO) {
        spuService.saveSpuInfo(spuSaveDTO);
        return Result.success();
    }

    @PostMapping("/admin/product/updateSpuInfo")
    public Result updateSpuInfo(@RequestBody SpuSaveDTO spuSaveDTO) {
        spuService.updateSpuInfo(spuSaveDTO);
        return Result.success();
    }

    @DeleteMapping("/admin/product/deleteSpu/{spuId}")
    public Result deleteSpu(@PathVariable Long spuId) {
        spuService.deleteSpu(spuId);
        return Result.success();
    }
}
