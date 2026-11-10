package com.itheima.java_admin_backend.mapper;

import com.itheima.java_admin_backend.vo.SkuAttrValueVO;
import com.itheima.java_admin_backend.vo.SkuImageVO;
import com.itheima.java_admin_backend.vo.SkuInfoVO;
import com.itheima.java_admin_backend.vo.SkuSaleAttrValueVO;
import com.itheima.java_admin_backend.vo.SkuVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * SKU 管理模块
 */
@Mapper
public interface SkuMapper {

    /*
    * 分页查询 SKU 列表
    * */
    @Select("SELECT sku_id AS id, spu_id AS spuId, category_3_id AS category3Id, " +
            "tm_id AS tmId, sku_name AS skuName, weight, price, " +
            "sku_desc AS skuDesc, sku_default_img AS skuDefaultImg, is_sale AS isSale " +
            "FROM sku ORDER BY sku_id LIMIT #{limit} OFFSET #{offset}")
    List<SkuVO> selectSkuPage(@Param("offset") Long offset, @Param("limit") Long limit);

    /*
    * 统计 SKU 总数
    * */
    @Select("SELECT COUNT(*) FROM sku")
    Long countSku();

    /*
    * 查询 SKU 当前的上架状态（上架/下架前校验商品是否存在）
    * */
    @Select("SELECT is_sale AS isSale FROM sku WHERE sku_id = #{skuId}")
    Integer selectIsSaleBySkuId(@Param("skuId") Long skuId);

    /*
    * 上架：is_sale 置为 1
    * */
    @Update("UPDATE sku SET is_sale = 1 WHERE sku_id = #{skuId}")
    int updateIsSaleOn(@Param("skuId") Long skuId);

    /*
    * 下架：is_sale 置为 0
    * */
    @Update("UPDATE sku SET is_sale = 0 WHERE sku_id = #{skuId}")
    int updateIsSaleOff(@Param("skuId") Long skuId);

    /*
    * 查询 SKU 基本信息
    * */
    @Select("SELECT sku_id AS id, spu_id AS spuId, category_3_id AS category3Id, " +
            "tm_id AS tmId, sku_name AS skuName, weight, price, " +
            "sku_desc AS skuDesc, sku_default_img AS skuDefaultImg, is_sale AS isSale " +
            "FROM sku WHERE sku_id = #{skuId}")
    SkuInfoVO selectSkuBySkuId(@Param("skuId") Long skuId);

    /*
    * 查询 SKU 的平台属性值列表
    * */
    @Select("SELECT sku_attr_value_id AS id, attr_id AS attrId, value_id AS valueId, " +
            "value_name AS valueName, attr_name AS attrName, sku_id AS skuId " +
            "FROM sku_attr_value WHERE sku_id = #{skuId}")
    List<SkuAttrValueVO> selectSkuAttrValues(@Param("skuId") Long skuId);

    /*
    * 查询 SKU 的销售属性值列表
    * */
    @Select("SELECT sku_sale_attr_value_id AS id, sale_attr_id AS saleAttrId, " +
            "sale_attr_value_id AS saleAttrValueId, sale_attr_name AS saleAttrName, " +
            "sale_attr_value_name AS saleAttrValueName, sku_id AS skuId " +
            "FROM sku_sale_attr_value WHERE sku_id = #{skuId}")
    List<SkuSaleAttrValueVO> selectSkuSaleAttrValues(@Param("skuId") Long skuId);

    /*
    * 查询 SKU 的图片列表
    * */
    @Select("SELECT image_id AS id, sku_id AS skuId, image_url AS imgUrl, " +
            "spu_image_id AS spuImgId, is_default AS isDefault " +
            "FROM sku_image WHERE sku_id = #{skuId}")
    List<SkuImageVO> selectSkuImages(@Param("skuId") Long skuId);

    /*
    * 新增 SKU（sku_id 业务主键由 service 用时间戳生成）
    * */
    @Insert("INSERT INTO sku (sku_id, spu_id, category_3_id, tm_id, sku_name, weight, price, " +
            "sku_desc, sku_default_img, is_sale) " +
            "VALUES (#{skuId}, #{spuId}, #{category3Id}, #{tmId}, #{skuName}, " +
            "#{weight}, #{price}, #{skuDesc}, #{skuDefaultImg}, #{isSale})")
    int insertSku(@Param("skuId") Long skuId,
                  @Param("spuId") Long spuId,
                  @Param("category3Id") Long category3Id,
                  @Param("tmId") Long tmId,
                  @Param("skuName") String skuName,
                  @Param("weight") String weight,
                  @Param("price") Long price,
                  @Param("skuDesc") String skuDesc,
                  @Param("skuDefaultImg") String skuDefaultImg,
                  @Param("isSale") Integer isSale);

    /*
    * 新增 SKU 图片
    * */
    @Insert("INSERT INTO sku_image (image_id, sku_id, image_url, spu_image_id, is_default) " +
            "VALUES (#{imageId}, #{skuId}, #{imgUrl}, #{spuImgId}, #{isDefault})")
    int insertSkuImage(@Param("imageId") Long imageId,
                       @Param("skuId") Long skuId,
                       @Param("imgUrl") String imgUrl,
                       @Param("spuImgId") Long spuImgId,
                       @Param("isDefault") String isDefault);

    /*
    * 新增 SKU 平台属性值
    * */
    @Insert("INSERT INTO sku_attr_value (sku_attr_value_id, attr_id, value_id, value_name, attr_name, sku_id) " +
            "VALUES (#{skuAttrValueId}, #{attrId}, #{valueId}, #{valueName}, #{attrName}, #{skuId})")
    int insertSkuAttrValue(@Param("skuAttrValueId") Long skuAttrValueId,
                           @Param("attrId") Long attrId,
                           @Param("valueId") Long valueId,
                           @Param("valueName") String valueName,
                           @Param("attrName") String attrName,
                           @Param("skuId") Long skuId);

    /*
    * 新增 SKU 销售属性值
    * */
    @Insert("INSERT INTO sku_sale_attr_value (sku_sale_attr_value_id, sale_attr_id, sale_attr_value_id, " +
            "sale_attr_name, sale_attr_value_name, sku_id) " +
            "VALUES (#{skuSaleAttrValueId}, #{saleAttrId}, #{saleAttrValueId}, " +
            "#{saleAttrName}, #{saleAttrValueName}, #{skuId})")
    int insertSkuSaleAttrValue(@Param("skuSaleAttrValueId") Long skuSaleAttrValueId,
                               @Param("saleAttrId") Long saleAttrId,
                               @Param("saleAttrValueId") Long saleAttrValueId,
                               @Param("saleAttrName") String saleAttrName,
                               @Param("saleAttrValueName") String saleAttrValueName,
                               @Param("skuId") Long skuId);

    /*
    * 查询平台属性名（保存 SKU 时冗余存储用）
    * */
    @Select("SELECT attr_name FROM attr WHERE attr_id = #{attrId}")
    String selectAttrName(@Param("attrId") Long attrId);

    /*
    * 查询平台属性值名（保存 SKU 时冗余存储用）
    * */
    @Select("SELECT value_name FROM attr_value WHERE attr_value_id = #{valueId}")
    String selectAttrValueName(@Param("valueId") Long valueId);

    /*
    * 查询 SPU 销售属性名（保存 SKU 时冗余存储用）
    * */
    @Select("SELECT sale_attr_name FROM spu_sale_attr WHERE spu_sale_attr_id = #{spuSaleAttrId}")
    String selectSaleAttrName(@Param("spuSaleAttrId") Long spuSaleAttrId);

    /*
    * 查询销售属性值名（保存 SKU 时冗余存储用）
    * */
    @Select("SELECT sale_attr_value_name FROM sale_attr_value WHERE sale_attr_value_id = #{saleAttrValueId}")
    String selectSaleAttrValueName(@Param("saleAttrValueId") Long saleAttrValueId);

    /*
    * 删除 SKU 图片
    * */
    @Delete("DELETE FROM sku_image WHERE sku_id = #{skuId}")
    int deleteSkuImages(@Param("skuId") Long skuId);

    /*
    * 删除 SKU 平台属性值
    * */
    @Delete("DELETE FROM sku_attr_value WHERE sku_id = #{skuId}")
    int deleteSkuAttrValues(@Param("skuId") Long skuId);

    /*
    * 删除 SKU 销售属性值
    * */
    @Delete("DELETE FROM sku_sale_attr_value WHERE sku_id = #{skuId}")
    int deleteSkuSaleAttrValues(@Param("skuId") Long skuId);

    /*
    * 删除 SKU
    * */
    @Delete("DELETE FROM sku WHERE sku_id = #{skuId}")
    int deleteSkuBySkuId(@Param("skuId") Long skuId);
}
