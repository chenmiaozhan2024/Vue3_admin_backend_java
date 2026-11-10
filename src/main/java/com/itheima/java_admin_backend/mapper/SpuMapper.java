package com.itheima.java_admin_backend.mapper;

import com.itheima.java_admin_backend.vo.SaleAttrVO;
import com.itheima.java_admin_backend.vo.SkuVO;
import com.itheima.java_admin_backend.vo.SpuImageVO;
import com.itheima.java_admin_backend.vo.SpuSaleAttrValueVO;
import com.itheima.java_admin_backend.vo.SpuSaleAttrVO;
import com.itheima.java_admin_backend.vo.SpuVO;
import com.itheima.java_admin_backend.vo.TrademarkVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * SPU 管理模块
 */
@Mapper
public interface SpuMapper {

    @Select("<script>SELECT spu_id AS id, spu_name AS spuName, description, " +
            "category3_id AS category3Id, tm_id AS tmId FROM spu " +
            "<if test='category3Id != null'>WHERE category3_id = #{category3Id}</if> " +
            "ORDER BY spu_id LIMIT #{limit} OFFSET #{offset}</script>")
    List<SpuVO> selectSpuPage(@Param("offset") Long offset,
                              @Param("limit") Long limit,
                              @Param("category3Id") Long category3Id);

    @Select("<script>SELECT COUNT(*) FROM spu " +
            "<if test='category3Id != null'>WHERE category3_id = #{category3Id}</if></script>")
    Long countSpu(@Param("category3Id") Long category3Id);

    @Select("SELECT tm_id AS id, tm_name AS tmName, logo_url AS logoUrl FROM trademark")
    List<TrademarkVO> selectAllTrademarks();

    @Select("SELECT image_id AS id, image_name AS imgName, image_url AS imgUrl, spu_id AS spuId " +
            "FROM spu_image_list WHERE spu_id = #{spuId}")
    List<SpuImageVO> selectSpuImages(@Param("spuId") Long spuId);

    @Select("SELECT spu_sale_attr_id AS id, base_sale_attr_id AS baseSaleAttrId, " +
            "sale_attr_name AS saleAttrName, spu_id AS spuId " +
            "FROM spu_sale_attr WHERE spu_id = #{spuId}")
    List<SpuSaleAttrVO> selectSpuSaleAttrs(@Param("spuId") Long spuId);

    @Select("SELECT sale_attr_value_id AS id, sale_attr_value_name AS saleAttrValueName, " +
            "sale_attr_id AS baseSaleAttrId, spu_id AS spuId " +
            "FROM sale_attr_value WHERE spu_id = #{spuId} AND sale_attr_id = #{baseSaleAttrId}")
    List<SpuSaleAttrValueVO> selectSpuSaleAttrValues(@Param("spuId") Long spuId,
                                                     @Param("baseSaleAttrId") Long baseSaleAttrId);

    @Select("SELECT sale_attr_id AS id, sale_attr_name AS name FROM sale_attr")
    List<SaleAttrVO> selectBaseSaleAttrs();

    @Select("SELECT sku_id AS id, spu_id AS spuId, category_3_id AS category3Id, " +
            "tm_id AS tmId, sku_name AS skuName, weight, price, " +
            "sku_desc AS skuDesc, sku_default_img AS skuDefaultImg, is_sale AS isSale " +
            "FROM sku WHERE spu_id = #{spuId}")
    List<SkuVO> selectSkusBySpuId(@Param("spuId") Long spuId);

    @Insert("INSERT INTO spu (spu_id, spu_name, description, category3_id, tm_id) " +
            "VALUES (#{spuId}, #{spuName}, #{description}, #{category3Id}, #{tmId})")
    int insertSpu(@Param("spuId") Long spuId, @Param("spuName") String spuName,
                  @Param("description") String description,
                  @Param("category3Id") Long category3Id, @Param("tmId") Long tmId);

    @Insert("INSERT INTO spu_image_list (image_id, image_name, image_url, spu_id) " +
            "VALUES (#{imageId}, #{imgName}, #{imgUrl}, #{spuId})")
    int insertSpuImage(@Param("imageId") Long imageId, @Param("imgName") String imgName,
                       @Param("imgUrl") String imgUrl, @Param("spuId") Long spuId);

    @Insert("INSERT INTO spu_sale_attr (spu_sale_attr_id, base_sale_attr_id, sale_attr_name, spu_id) " +
            "VALUES (#{spuSaleAttrId}, #{baseSaleAttrId}, #{saleAttrName}, #{spuId})")
    int insertSpuSaleAttr(@Param("spuSaleAttrId") Long spuSaleAttrId,
                          @Param("baseSaleAttrId") Long baseSaleAttrId,
                          @Param("saleAttrName") String saleAttrName,
                          @Param("spuId") Long spuId);

    @Insert("INSERT INTO sale_attr_value (sale_attr_value_id, sale_attr_value_name, sale_attr_id, spu_id) " +
            "VALUES (#{saleAttrValueId}, #{saleAttrValueName}, #{baseSaleAttrId}, #{spuId})")
    int insertSpuSaleAttrValue(@Param("saleAttrValueId") Long saleAttrValueId,
                               @Param("saleAttrValueName") String saleAttrValueName,
                               @Param("baseSaleAttrId") Long baseSaleAttrId,
                               @Param("spuId") Long spuId);

    @Update("UPDATE spu SET spu_name = #{spuName}, description = #{description}, " +
            "category3_id = #{category3Id}, tm_id = #{tmId} WHERE spu_id = #{spuId}")
    int updateSpu(@Param("spuId") Long spuId, @Param("spuName") String spuName,
                  @Param("description") String description,
                  @Param("category3Id") Long category3Id, @Param("tmId") Long tmId);

    @Delete("DELETE FROM spu_image_list WHERE spu_id = #{spuId}")
    int deleteSpuImages(@Param("spuId") Long spuId);

    @Delete("DELETE FROM spu_sale_attr WHERE spu_id = #{spuId}")
    int deleteSpuSaleAttrs(@Param("spuId") Long spuId);

    @Delete("DELETE FROM sale_attr_value WHERE spu_id = #{spuId}")
    int deleteSpuSaleAttrValues(@Param("spuId") Long spuId);

    @Delete("DELETE FROM spu WHERE spu_id = #{spuId}")
    int deleteSpuBySpuId(@Param("spuId") Long spuId);

    @Select("SELECT COUNT(*) FROM sku WHERE spu_id = #{spuId}")
    Long countSkusBySpuId(@Param("spuId") Long spuId);
}
