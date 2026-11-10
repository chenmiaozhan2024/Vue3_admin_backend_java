package com.itheima.java_admin_backend.controller;

import com.itheima.java_admin_backend.common.Result;
import com.itheima.java_admin_backend.dto.AttrSaveDTO;
import com.itheima.java_admin_backend.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 属性管理（三级分类 + 属性 + 属性值）
 */
@RestController
public class AttrController {
    @Autowired
    private AttrService attrService;

    /*
    * 获取一级分类
    * */
    @GetMapping("/admin/product/getCategory1")
    public Result getCategory1() {
        return Result.success(attrService.getCategory1());
    }

    /*
    * 获取二级分类
    * */
    @GetMapping("/admin/product/getCategory2/{category1}")
    public Result getCategory2(@PathVariable Long category1) {
        return Result.success(attrService.getCategory2(category1));
    }

    /*
    * 获取三级分类
    * */
    @GetMapping("/admin/product/getCategory3/{category2}")
    public Result getCategory3(@PathVariable Long category2) {
        return Result.success(attrService.getCategory3(category2));
    }

    /*
    * 获取分类下已有的属性与属性值
    * */
    @GetMapping("/admin/product/attrInfoList/{category1}/{category2}/{category3}")
    public Result getAttrInfoList(@PathVariable Long category1,
                                  @PathVariable Long category2,
                                  @PathVariable Long category3) {
        return Result.success(attrService.getAttrInfoList(category1, category2, category3));
    }

    /*
    * 添加或修改属性（根据有无 id 判断）
    * */
    @PostMapping("/admin/product/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody AttrSaveDTO attrSaveDTO) {
        attrService.saveAttrInfo(attrSaveDTO);
        return Result.success();
    }

    /*
    * 删除某一个已有的属性
    * */
    @DeleteMapping("/admin/product/deleteAttr/{attrId}")
    public Result deleteAttr(@PathVariable Long attrId) {
        attrService.deleteAttr(attrId);
        return Result.success();
    }
}
