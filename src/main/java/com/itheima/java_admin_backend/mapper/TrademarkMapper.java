package com.itheima.java_admin_backend.mapper;

import com.itheima.java_admin_backend.vo.TrademarkVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 品牌管理模块
 */
@Mapper
public interface TrademarkMapper {

    /*
    * 分页查询品牌列表
    * */
    @Select("SELECT id, " +
            "tm_name AS tmName, " +
            "logo_url AS logoUrl, " +
            "DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:%s') AS createTime, " +
            "DATE_FORMAT(update_time, '%Y-%m-%d %H:%i:%s') AS updateTime " +
            "FROM trademark " +
            "ORDER BY id " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<TrademarkVO> selectTrademarkPage(@Param("offset") Long offset, @Param("limit") Long limit);

    /*
    * 统计品牌总数
    * */
    @Select("SELECT COUNT(*) FROM trademark")
    Long countTrademark();

    /*
    * 按品牌名统计（表中 tm_name 有唯一索引，新增前用于查重）
    * */
    @Select("SELECT COUNT(*) FROM trademark WHERE tm_name = #{tmName}")
    Long countByTmName(@Param("tmName") String tmName);

    /*
    * 新增品牌（tm_id 业务主键由 service 用时间戳生成）
    * */
    @Insert("INSERT INTO trademark (tm_id, tm_name, logo_url) " +
            "VALUES (#{tmId}, #{tmName}, #{logoUrl})")
    int insertTrademark(@Param("tmId") Long tmId,
                        @Param("tmName") String tmName,
                        @Param("logoUrl") String logoUrl);

    /*
    * 修改品牌
    * */
    @Update("UPDATE trademark SET tm_name = #{tmName}, logo_url = #{logoUrl} " +
            "WHERE id = #{id}")
    int updateTrademark(@Param("id") Long id,
                        @Param("tmName") String tmName,
                        @Param("logoUrl") String logoUrl);

    /*
    * 删除品牌
    * */
    @Delete("DELETE FROM trademark WHERE id = #{id}")
    int deleteTrademarkById(@Param("id") Long id);
}
