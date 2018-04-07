package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo;

import java.util.List;

/**
 * Created by wangychu on 12/8/16.
 */
public class RelationValueVO {

    private String id;
    private String discoverSpaceName;
    private String relationTypeName;
    private RelationableValueVO fromRelationable;
    private RelationableValueVO toRelationable;
    private List<PropertyValueVO> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public String getRelationTypeName() {
        return relationTypeName;
    }

    public void setRelationTypeName(String relationTypeName) {
        this.relationTypeName = relationTypeName;
    }

    public RelationableValueVO getFromRelationable() {
        return fromRelationable;
    }

    public void setFromRelationable(RelationableValueVO fromRelationable) {
        this.fromRelationable = fromRelationable;
    }

    public RelationableValueVO getToRelationable() {
        return toRelationable;
    }

    public void setToRelationable(RelationableValueVO toRelationable) {
        this.toRelationable = toRelationable;
    }

    public List<PropertyValueVO> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyValueVO> properties) {
        this.properties = properties;
    }
}
