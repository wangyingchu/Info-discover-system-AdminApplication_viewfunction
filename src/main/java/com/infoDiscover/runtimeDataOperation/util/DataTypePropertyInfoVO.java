package com.infoDiscover.runtimeDataOperation.util;

public class DataTypePropertyInfoVO {

    private String propertyName;
    private Object propertyValue;

    public DataTypePropertyInfoVO(){}

    public DataTypePropertyInfoVO(String propertyName,Object propertyValue){
        this.propertyName=propertyName;
        this.propertyValue=propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }
}
