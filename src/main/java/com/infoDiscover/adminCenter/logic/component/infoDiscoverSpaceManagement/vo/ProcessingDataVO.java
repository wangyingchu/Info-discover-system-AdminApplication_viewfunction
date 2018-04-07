package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo;

/**
 * Created by wangychu on 12/15/16.
 */
public class ProcessingDataVO {

    private String id;
    private String discoverSpaceName;
    private String dataTypeName;
    private String dataTypeKind;

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

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    public String getDataTypeKind() {
        return dataTypeKind;
    }

    public void setDataTypeKind(String dataTypeKind) {
        this.dataTypeKind = dataTypeKind;
    }
}
