package com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo;

/**
 * Created by wangychu on 5/6/17.
 */
public class SolutionTypePropertyTypeDefinitionVO {

    private String solutionName;
    private String propertyName;
    private String propertyAliasName;
    private String propertyType;
    private boolean mandatory;
    private boolean readOnly;
    private boolean nullable;
    private String propertyTypeKind;
    private String propertyTypeName;
    private String propertySourceOwner;

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

    public String getPropertyAliasName() {
        return propertyAliasName;
    }

    public void setPropertyAliasName(String propertyAliasName) {
        this.propertyAliasName = propertyAliasName;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getPropertyTypeKind() {
        return propertyTypeKind;
    }

    public void setPropertyTypeKind(String propertyTypeKind) {
        this.propertyTypeKind = propertyTypeKind;
    }

    public String getPropertyTypeName() {
        return propertyTypeName;
    }

    public void setPropertyTypeName(String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
    }

    public String getPropertySourceOwner() {
        return propertySourceOwner;
    }

    public void setPropertySourceOwner(String propertySourceOwner) {
        this.propertySourceOwner = propertySourceOwner;
    }
}
