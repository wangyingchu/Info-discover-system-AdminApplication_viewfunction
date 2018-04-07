package com.infoDiscover.runtimeDataOperation.util;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.solution.template.DataImporter;

public class DataTransferUtil {

    public static String transferTypeDataToJson(String dataTypeKind,String dataTypeName,List<DataTypePropertyInfoVO> dataPropertiesList){
        Map<String, Object> rootMap = new HashMap<String, Object>();
        List<Map> dataList=new ArrayList<Map>();
        rootMap.put("data", dataList);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataList.add(dataMap);

        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(dataTypeKind)){
            dataMap.put("type","Dimension");
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataTypeKind)){
            dataMap.put("type","Fact");
        }
        dataMap.put("typeName",dataTypeName);

        List<Map> propertiesMapList=new ArrayList<Map>();
        dataMap.put("properties", propertiesMapList);

        if(dataPropertiesList!=null){
            for(DataTypePropertyInfoVO currentProperty:dataPropertiesList){
                String propertyType=getPropertyTypeCode(currentProperty.getPropertyValue());
                if(propertyType!=null){
                    Map<String,Object> projectNameMap=new HashMap<String, Object>();
                    projectNameMap.put("propertyType", propertyType);
                    projectNameMap.put("propertyName", currentProperty.getPropertyName());

                    if(currentProperty.getPropertyValue() instanceof Date){
                        Long longDateValue=((Date)currentProperty.getPropertyValue()).getTime();
                        projectNameMap.put("propertyValue", longDateValue);
                    }else{
                        projectNameMap.put("propertyValue", currentProperty.getPropertyValue());
                    }
                    propertiesMapList.add(projectNameMap);
                }
            }
        }

        ObjectMapper mapper=new ObjectMapper();
        try {
            String jsonString=mapper.writeValueAsString(rootMap);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getPropertyTypeCode(Object propertyValue){
        if(propertyValue instanceof String){
            return "String";
        }
        if(propertyValue instanceof Integer){
            return "Int";
        }
        if(propertyValue instanceof Long){
            return "Long";
        }
        if(propertyValue instanceof Double){
            return "Double";
        }
        if(propertyValue instanceof Float){
            return "Float";
        }
        if(propertyValue instanceof Boolean){
            return "Boolean";
        }
        if(propertyValue instanceof Date){
            return "Date";
        }
        if(propertyValue instanceof Short){
            return "Short";
        }
        return null;
    }

    public static boolean importTypeDate(String discoverSpaceName,String dataTypeKind,String dataTypeName,List<DataTypePropertyInfoVO> dataPropertiesList){
        DataImporter importer = new DataImporter(discoverSpaceName);
        String jsonContent=transferTypeDataToJson(dataTypeKind,dataTypeName,dataPropertiesList);
        System.out.println(jsonContent);
        try {
            importer.importData(jsonContent,true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args){
        List<DataTypePropertyInfoVO> propertyList=new ArrayList<>();
        //propertyList.add(new DataTypePropertyInfoVO("fact001p2",new Boolean(false)));
/*
        propertyList.add(new DataTypePropertyInfoVO("fact001p2",new Date()));
        propertyList.add(new DataTypePropertyInfoVO("fact003p1",new Float(223399)));
        propertyList.add(new DataTypePropertyInfoVO("sourceStr","AAA"));
        propertyList.add(new DataTypePropertyInfoVO("strprop",new Long(2233)));
        propertyList.add(new DataTypePropertyInfoVO("date01",new Date()));
        propertyList.add(new DataTypePropertyInfoVO("booleanData",true));
*/
        //importTypeDate("testApply",InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,"fact001",propertyList);
        //importTypeDate("testApply",InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,"dupsource",propertyList);
        //importTypeDate("testApply",InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION,"dupsourceDim",propertyList);
        //importTypeDate("testApply",InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION,"dim001",propertyList);
        //importTypeDate("testApply",InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,"fact001shouldwroing11",propertyList);

        //propertyList.add(new DataTypePropertyInfoVO("strMatchKey","MATCHKEY-VALUE"));
        //propertyList.add(new DataTypePropertyInfoVO("intMatchKey",new Integer(150)));
        //propertyList.add(new DataTypePropertyInfoVO("longMatchKey",new Long(150)));
        //propertyList.add(new DataTypePropertyInfoVO("booleanMatchKey",new Boolean(true)));
        //propertyList.add(new DataTypePropertyInfoVO("doubleMatchKey",new Double(150)));
        //propertyList.add(new DataTypePropertyInfoVO("floatMatchKey",new Float(150)));
        propertyList.add(new DataTypePropertyInfoVO("shortMatchKey",(short)10));
        propertyList.add(new DataTypePropertyInfoVO("dateProp",new Date()));


        //importTypeDate("testApply",InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,"relSourceFact",propertyList);


        for(int i=0;i<500;i++){
            importTypeDate("testApply",InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION,"relSourceDimension",propertyList);



        }

    }
}
