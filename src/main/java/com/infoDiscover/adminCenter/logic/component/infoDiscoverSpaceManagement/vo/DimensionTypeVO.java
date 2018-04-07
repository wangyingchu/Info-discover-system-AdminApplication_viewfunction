package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo;

import java.util.List;

/**
 * Created by wangychu on 10/8/16.
 */
public class DimensionTypeVO {

    private String typeName;
    private String typeAliasName;
    private long typeDataRecordCount;
    private int descendantDimensionTypesNumber;
    private List<DimensionTypeVO> childDimensionTypesVOList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getTypeDataRecordCount() {
        return typeDataRecordCount;
    }

    public void setTypeDataRecordCount(long typeDataRecordCount) {
        this.typeDataRecordCount = typeDataRecordCount;
    }

    public int getDescendantDimensionTypesNumber() {
        return descendantDimensionTypesNumber;
    }

    public void setDescendantDimensionTypesNumber(int descendantDimensionTypesNumber) {
        this.descendantDimensionTypesNumber = descendantDimensionTypesNumber;
    }

    public List<DimensionTypeVO> getChildDimensionTypesVOList() {
        return childDimensionTypesVOList;
    }

    public void setChildDimensionTypesVOList(List<DimensionTypeVO> childDimensionTypesVO) {
        this.childDimensionTypesVOList = childDimensionTypesVO;
    }

    public String getTypeAliasName() {
        return typeAliasName;
    }

    public void setTypeAliasName(String typeAliasName) {
        this.typeAliasName = typeAliasName;
    }
}
