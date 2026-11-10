package com.itheima.java_admin_backend.service;

import com.itheima.java_admin_backend.dto.AttrSaveDTO;
import com.itheima.java_admin_backend.vo.AttrVO;
import com.itheima.java_admin_backend.vo.CategoryVO;

import java.util.List;

public interface AttrService {

    List<CategoryVO> getCategory1();

    List<CategoryVO> getCategory2(Long category1Id);

    List<CategoryVO> getCategory3(Long category2Id);

    List<AttrVO> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id);

    void saveAttrInfo(AttrSaveDTO attrSaveDTO);

    void deleteAttr(Long attrId);
}
