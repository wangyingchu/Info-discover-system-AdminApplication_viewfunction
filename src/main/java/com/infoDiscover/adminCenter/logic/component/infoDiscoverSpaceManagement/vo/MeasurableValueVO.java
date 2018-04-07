package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangychu on 11/6/16.
 */
public class MeasurableValueVO {
    private String id;
    private String discoverSpaceName;
    private String measurableTypeName;
    private String measurableTypeKind;
    private List<PropertyValueVO> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPropertyNames() {
        List<String> propertiesNameList=new ArrayList<String>();
        if(getProperties()!=null){
            for(PropertyValueVO propertyValueVO:getProperties()){
                propertiesNameList.add(propertyValueVO.getPropertyName());
            }
        }
        return propertiesNameList;
    }

    public List<PropertyValueVO> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyValueVO> properties) {
        this.properties = properties;
    }

    public String getMeasurableTypeName() {
        return measurableTypeName;
    }

    public void setMeasurableTypeName(String measurableTypeName) {
        this.measurableTypeName = measurableTypeName;
    }

    public String getMeasurableTypeKind() {
        return measurableTypeKind;
    }

    public void setMeasurableTypeKind(String measurableTypeKind) {
        this.measurableTypeKind = measurableTypeKind;
    }

    public PropertyValueVO getPropertyValue(String propertyName){
        if(this.properties!=null){
            for(PropertyValueVO currentPropertyValueVO:this.properties){
                if(currentPropertyValueVO.getPropertyName().equals(propertyName)){
                   return currentPropertyValueVO;
                }
            }
            return null;
        }else{
            return null;
        }
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }
}
