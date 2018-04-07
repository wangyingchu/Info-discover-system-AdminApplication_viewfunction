package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo;

/**
 * Created by wangychu on 10/9/16.
 */
public class PropertyTypeVO {
    private String propertyName;
    private String propertyAliasName;
    private String propertyType;
    private String propertySourceOwner;
    private boolean mandatory;
    private boolean readOnly;
    private boolean nullable;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getPropertySourceOwner() {
        return propertySourceOwner;
    }

    public void setPropertySourceOwner(String propertySourceOwner) {
        this.propertySourceOwner = propertySourceOwner;
    }

    public String getPropertyAliasName() {
        return propertyAliasName;
    }

    public void setPropertyAliasName(String propertyAliasName) {
        this.propertyAliasName = propertyAliasName;
    }
}
