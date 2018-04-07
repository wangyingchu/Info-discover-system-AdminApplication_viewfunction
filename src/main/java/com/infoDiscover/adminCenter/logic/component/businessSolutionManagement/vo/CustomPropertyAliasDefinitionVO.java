package com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo;

/**
 * Created by wangychu on 6/28/17.
 */
public class CustomPropertyAliasDefinitionVO {

    private String solutionName;
    private String customPropertyName;
    private String customPropertyType;
    private String customPropertyAliasName;

    public String getCustomPropertyName() {
        return customPropertyName;
    }

    public void setCustomPropertyName(String customPropertyName) {
        this.customPropertyName = customPropertyName;
    }

    public String getCustomPropertyType() {
        return customPropertyType;
    }

    public void setCustomPropertyType(String customPropertyType) {
        this.customPropertyType = customPropertyType;
    }

    public String getCustomPropertyAliasName() {
        return customPropertyAliasName;
    }

    public void setCustomPropertyAliasName(String customPropertyAliasName) {
        this.customPropertyAliasName = customPropertyAliasName;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }
}
