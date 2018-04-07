package com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo;

/**
 * Created by wangychu on 6/26/17.
 */
public class DimensionTypeDefinitionVO {

    private String typeName;
    private String typeAliasName;
    private String solutionName;
    private String parentTypeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeAliasName() {
        return typeAliasName;
    }

    public void setTypeAliasName(String typeAliasName) {
        this.typeAliasName = typeAliasName;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getParentTypeName() {
        return parentTypeName;
    }

    public void setParentTypeName(String parentTypeName) {
        this.parentTypeName = parentTypeName;
    }
}
