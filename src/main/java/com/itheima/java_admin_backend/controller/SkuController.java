package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.Result;
import com.itheima.java_admin_backend.dto.SkuSaveDTO;
import com.itheima.java_admin_backend.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * SKU 管理
 */
@RestController
public class SkuController {
    @Autowired
    private SkuService skuService;

    /*
    * 分页获取已有的商品的数据-SKU
    * */
    @GetMapping("/admin/product/list/{page}/{pageSize}")
    public Result getSkuPage(@PathVariable Long page, @PathVariable Long pageSize) {
        return Result.success(skuService.getSkuPage(page, pageSize));
    }

    /*
    * 商品上架
    * */
    @GetMapping("/admin/product/onSale/{skuId}")
    public Result onSale(@PathVariable Long skuId) {
        skuService.onSale(skuId);
        return Result.success();
    }

    /*
    * 商品下架
    * */
    @GetMapping("/admin/product/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable Long skuId) {
        skuService.cancelSale(skuId);
        return Result.success();
    }

    /*
    * 获取商品详情
    * */
    @GetMapping("/admin/product/getSkuInfo/{skuId}")
    public Result getSkuInfo(@PathVariable Long skuId) {
        return Result.success(skuService.getSkuInfo(skuId));
    }

    /*
    * 删除已有的商品
    * */
    @DeleteMapping("/admin/product/deleteSku/{skuId}")
    public Result deleteSku(@PathVariable Long skuId) {
        skuService.deleteSku(skuId);
        return Result.success();
    }

    /*
    * 保存 SKU 信息
    * */
    @PostMapping("/admin/product/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuSaveDTO skuSaveDTO) {
        skuService.saveSkuInfo(skuSaveDTO);
        return Result.success();
    }
}
