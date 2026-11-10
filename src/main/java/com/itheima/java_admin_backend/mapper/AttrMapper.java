package com.itheima.java_admin_backend.mapper;

import com.itheima.java_admin_backend.vo.AttrValueVO;
import com.itheima.java_admin_backend.vo.AttrVO;
import com.itheima.java_admin_backend.vo.CategoryVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 属性管理模块（三级分类 + 属性 + 属性值）
 */
@Mapper
public interface AttrMapper {

    /*
    * 获取一级分类列表
    * */
    @Select("SELECT category1_id AS id, name FROM category1")
    List<CategoryVO> selectCategory1();

    /*
    * 获取某个一级分类下的二级分类列表
    * */
    @Select("SELECT category2_id AS id, name, category1_id AS category1Id " +
            "FROM category2 WHERE category1_id = #{category1Id}")
    List<CategoryVO> selectCategory2(@Param("category1Id") Long category1Id);

    /*
    * 获取某个二级分类下的三级分类列表
    * */
    @Select("SELECT category3_id AS id, name, category2_id AS category2Id " +
            "FROM category3 WHERE category2_id = #{category2Id}")
    List<CategoryVO> selectCategory3(@Param("category2Id") Long category2Id);

    /*
    * 查询三级分类下的属性列表（join 校验一二三级分类链路，与 Node 版一致）
    * */
    @Select("SELECT c.attr_id AS id, c.attr_name AS attrName, " +
            "c.category_id AS categoryId, c.category_level AS categoryLevel " +
            "FROM attr c " +
            "JOIN category3 a ON c.category_id = a.category3_id " +
            "JOIN category2 b ON a.category2_id = b.category2_id " +
            "WHERE b.category1_id = #{category1Id} " +
            "AND a.category2_id = #{category2Id} " +
            "AND a.category3_id = #{category3Id}")
    List<AttrVO> selectAttrListByCategory(@Param("category1Id") Long category1Id,
                                          @Param("category2Id") Long category2Id,
                                          @Param("category3Id") Long category3Id);

    /*
    * 查询某个属性下的属性值列表
    * */
    @Select("SELECT attr_value_id AS id, value_name AS valueName, attr_id AS attrId " +
            "FROM attr_value WHERE attr_id = #{attrId}")
    List<AttrValueVO> selectAttrValuesByAttrId(@Param("attrId") Long attrId);

    /*
    * 新增属性
    * */
    @Insert("INSERT INTO attr (attr_id, attr_name, category_id, category_level) " +
            "VALUES (#{attrId}, #{attrName}, #{categoryId}, #{categoryLevel})")
    int insertAttr(@Param("attrId") Long attrId,
                   @Param("attrName") String attrName,
                   @Param("categoryId") Long categoryId,
                   @Param("categoryLevel") Integer categoryLevel);

    /*
    * 修改属性名
    * */
    @Update("UPDATE attr SET attr_name = #{attrName} WHERE attr_id = #{attrId}")
    int updateAttrName(@Param("attrId") Long attrId, @Param("attrName") String attrName);

    /*
    * 新增属性值
    * */
    @Insert("INSERT INTO attr_value (attr_value_id, value_name, attr_id) " +
            "VALUES (#{attrValueId}, #{valueName}, #{attrId})")
    int insertAttrValue(@Param("attrValueId") Long attrValueId,
                        @Param("valueName") String valueName,
                        @Param("attrId") Long attrId);

    /*
    * 修改属性值名
    * */
    @Update("UPDATE attr_value SET value_name = #{valueName} WHERE attr_value_id = #{attrValueId}")
    int updateAttrValueName(@Param("attrValueId") Long attrValueId, @Param("valueName") String valueName);

    /*
    * 删除单个属性值
    * */
    @Delete("DELETE FROM attr_value WHERE attr_value_id = #{attrValueId}")
    int deleteAttrValueById(@Param("attrValueId") Long attrValueId);

    /*
    * 删除属性（删除属性前需先删除其下属性值）
    * */
    @Delete("DELETE FROM attr WHERE attr_id = #{attrId}")
    int deleteAttrByAttrId(@Param("attrId") Long attrId);
}
