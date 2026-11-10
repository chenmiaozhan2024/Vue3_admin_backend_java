package com.itheima.java_admin_backend.service.impl;

import com.itheima.java_admin_backend.dto.AttrSaveDTO;
import com.itheima.java_admin_backend.dto.AttrValueDTO;
import com.itheima.java_admin_backend.mapper.AttrMapper;
import com.itheima.java_admin_backend.service.AttrService;
import com.itheima.java_admin_backend.vo.AttrVO;
import com.itheima.java_admin_backend.vo.AttrValueVO;
import com.itheima.java_admin_backend.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    private AttrMapper attrMapper;

    @Override
    public List<CategoryVO> getCategory1() {
        return attrMapper.selectCategory1();
    }

    @Override
    public List<CategoryVO> getCategory2(Long category1Id) {
        if (category1Id == null) {
            throw new RuntimeException("一级分类id不能为空");
        }
        return attrMapper.selectCategory2(category1Id);
    }

    @Override
    public List<CategoryVO> getCategory3(Long category2Id) {
        if (category2Id == null) {
            throw new RuntimeException("二级分类id不能为空");
        }
        return attrMapper.selectCategory3(category2Id);
    }

    @Override
    public List<AttrVO> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        if (category1Id == null || category2Id == null || category3Id == null) {
            throw new RuntimeException("分类id不能为空");
        }
        // 查询属性列表，并为每个属性组装属性值列表
        List<AttrVO> attrList = attrMapper.selectAttrListByCategory(category1Id, category2Id, category3Id);
        for (AttrVO attr : attrList) {
            attr.setAttrValueList(attrMapper.selectAttrValuesByAttrId(attr.getId()));
        }
        return attrList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttrInfo(AttrSaveDTO attrSaveDTO) {
        if (attrSaveDTO == null
                || attrSaveDTO.getAttrName() == null || attrSaveDTO.getAttrName().isBlank()
                || attrSaveDTO.getCategoryId() == null || attrSaveDTO.getCategoryLevel() == null) {
            throw new RuntimeException("属性名称、分类id、分类层级不能为空");
        }
        Long targetAttrId;
        if (attrSaveDTO.getId() == null) {
            // 没有 id，新增属性；业务主键 attr_id 采用时间戳生成，
            targetAttrId = System.currentTimeMillis();
            attrMapper.insertAttr(targetAttrId, attrSaveDTO.getAttrName(),
                    attrSaveDTO.getCategoryId(), attrSaveDTO.getCategoryLevel());
        } else {
            // 有 id，修改属性名
            targetAttrId = attrSaveDTO.getId();
            int rows = attrMapper.updateAttrName(targetAttrId, attrSaveDTO.getAttrName());
            if (rows == 0) {
                throw new RuntimeException("属性不存在");
            }
        }

        // 处理属性值：有 id 的更新，没有 id 的新增
        List<Long> keepValueIds = new ArrayList<>();
        List<AttrValueDTO> attrValueList = attrSaveDTO.getAttrValueList();
        if (attrValueList != null) {
            for (AttrValueDTO value : attrValueList) {
                if (value.getValueName() == null || value.getValueName().isBlank()) {
                    throw new RuntimeException("属性值名称不能为空");
                }
                if (value.getId() == null) {
                    Long newValueId = System.currentTimeMillis();
                    attrMapper.insertAttrValue(newValueId, value.getValueName(), targetAttrId);
                    keepValueIds.add(newValueId);
                } else {
                    attrMapper.updateAttrValueName(value.getId(), value.getValueName());
                    keepValueIds.add(value.getId());
                }
            }
        }

        // 删除前端已移除、数据库中已不存在的属性值
        attrMapper.deleteAttrValuesNotIn(targetAttrId, keepValueIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttr(Long attrId) {
        if (attrId == null) {
            throw new RuntimeException("属性id不能为空");
        }
        // 1. 先删除属性值
        attrMapper.deleteAttrValuesNotIn(attrId, new ArrayList<>());
        // 2. 再删除属性
        int rows = attrMapper.deleteAttrByAttrId(attrId);
        if (rows == 0) {
            throw new RuntimeException("属性不存在");
        }
    }
}
