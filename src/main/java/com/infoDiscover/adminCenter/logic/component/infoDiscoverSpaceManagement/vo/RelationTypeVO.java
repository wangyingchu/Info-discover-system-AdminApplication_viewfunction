package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo;

import java.util.List;

/**
 * Created by wangychu on 10/25/16.
 */
public class RelationTypeVO {

    private String typeName;
    private String typeAliasName;
    private long typeDataRecordCount;
    private int descendantRelationTypesNumber;
    private List<RelationTypeVO> childRelationTypesVOList;

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

    public int getDescendantRelationTypesNumber() {
        return descendantRelationTypesNumber;
    }

    public void setDescendantRelationTypesNumber(int descendantRelationTypesNumber) {
        this.descendantRelationTypesNumber = descendantRelationTypesNumber;
    }

    public List<RelationTypeVO> getChildRelationTypesVOList() {
        return childRelationTypesVOList;
    }

    public void setChildRelationTypesVOList(List<RelationTypeVO> childDimensionTypesVO) {
        this.childRelationTypesVOList = childDimensionTypesVO;
    }

    public String getTypeAliasName() {
        return typeAliasName;
    }

    public void setTypeAliasName(String typeAliasName) {
        this.typeAliasName = typeAliasName;
    }
}
