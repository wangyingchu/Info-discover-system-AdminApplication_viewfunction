package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement;

import com.infoDiscover.adminCenter.logic.common.SystemConfigUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.*;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.*;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.RuntimeEnvironmentUtil;
import com.infoDiscover.adminCenter.ui.util.StringUnicodeSerializer;
import com.infoDiscover.infoDiscoverEngine.dataMart.*;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.NullValueFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverAdminSpace;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.config.RuntimeEnvironmentHandler;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticHelper;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.infoDiscover.solution.builder.SolutionConstants;
import com.infoDiscover.solution.template.BatchExecution;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.util.*;

/**
 * Created by wangychu on 9/30/16.
 */
public class InfoDiscoverSpaceOperationUtil {
    public static final String TYPEKIND_DIMENSION="TYPEKIND_DIMENSION";
    public static final String TYPEKIND_FACT="TYPEKIND_FACT";
    public static final String TYPEKIND_RELATION="TYPEKIND_RELATION";

    public static final String RELATION_DIRECTION_FROM="FROM";
    public static final String RELATION_DIRECTION_TO="TO";
    public static final String RELATION_DIRECTION_BOTH="BOTH";
    public static final String TYPEKIND_AliasNameFactType="TypeKind_AliasName";
    public static final String TYPEPROPERTY_AliasNameFactType="TypeProperty_AliasName";
    public static final String CUSTOMPROPERTY_AliasNameFactType="CustomProperty_AliasName";

    public static final String MetaConfig_PropertyName_DiscoverSpace="discoverSpace";
    public static final String MetaConfig_PropertyName_TypeKind="typeKind";
    public static final String MetaConfig_PropertyName_TypeName="typeName";
    public static final String MetaConfig_PropertyName_TypeAliasName="typeAliasName";
    public static final String MetaConfig_PropertyName_TypePropertyName="typePropertyName";
    public static final String MetaConfig_PropertyName_TypePropertyAliasName="typePropertyAliasName";
    public static final String MetaConfig_PropertyName_CustomPropertyName="customPropertyName";
    public static final String MetaConfig_PropertyName_CustomPropertyType="customPropertyType";
    public static final String MetaConfig_PropertyName_CustomPropertyAliasName="customPropertyAliasName";
    public static final String ExistedPropertyAliasNameHandleMethod_IGNORE="IGNORE";
    public static final String ExistedPropertyAliasNameHandleMethod_REPLACE="REPLACE";

    public static final String DATAMAPPING_SpaceDataRelationMappingDefinitionFactType="DataMapping_SpaceDataRelationMappingDefinition";
    public static final String DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType="DataMapping_SpaceDataDateDimensionMappingDefinition";
    public static final String DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType="DataMapping_SpaceDataPropertiesDuplicateMappingDefinition";

    public static final String MetaConfig_PropertyName_FactTypeName="factTypeName";
    public static final String MetaConfig_PropertyName_FactTypeAliasName="factTypeAliasName";

    public static final String MetaConfig_PropertyName_DimensionTypeName="dimensionTypeName";
    public static final String MetaConfig_PropertyName_DimensionTypeAliasName="dimensionTypeAliasName";
    public static final String MetaConfig_PropertyName_ParentDimensionTypeName="parentDimensionTypeName";

    public static final String MetaConfig_PropertyName_RelationTypeName="relationTypeName";
    public static final String MetaConfig_PropertyName_RelationTypeAliasName="relationTypeAliasName";
    public static final String MetaConfig_PropertyName_ParentRelationTypeName="parentRelationTypeName";

    public static final String MetaConfig_PropertyName_PropertyName="propertyName";
    public static final String MetaConfig_PropertyName_PropertyAliasName="propertyAliasName";
    public static final String MetaConfig_PropertyName_PropertyType="propertyType";
    public static final String MetaConfig_PropertyName_PropertyTypeKind="propertyTypeKind";
    public static final String MetaConfig_PropertyName_PropertyTypeName="propertyTypeName";
    public static final String MetaConfig_PropertyName_PropertySourceOwner="propertySourceOwner";
    public static final String MetaConfig_PropertyName_IsMandatoryProperty="isMandatory";
    public static final String MetaConfig_PropertyName_IsNullableProperty="isNullable";
    public static final String MetaConfig_PropertyName_IsReadOnlyProperty="isReadOnly";

    public static final String MetaConfig_PropertyName_SourceDataTypeName="sourceDataTypeName";
    public static final String MetaConfig_PropertyName_SourceDataTypeKind="sourceDataTypeKind";
    public static final String MetaConfig_PropertyName_SourceDataPropertyName="sourceDataPropertyName";
    public static final String MetaConfig_PropertyName_SourceDataPropertyType="sourceDataPropertyType";
    public static final String MetaConfig_PropertyName_RelationDirection="relationDirection";
    public static final String MetaConfig_PropertyName_MappingNotExistHandleMethod="mappingNotExistHandleMethod";
    public static final String MetaConfig_PropertyName_TargetDataTypeName="targetDataTypeName";
    public static final String MetaConfig_PropertyName_TargetDataTypeKind="targetDataTypeKind";
    public static final String MetaConfig_PropertyName_TargetDataPropertyName="targetDataPropertyName";
    public static final String MetaConfig_PropertyName_TargetDataPropertyValue="targetDataPropertyValue";
    public static final String MetaConfig_PropertyName_TargetDataPropertyType="targetDataPropertyType";
    public static final String MetaConfig_PropertyName_MappingMinValue="minValue";
    public static final String MetaConfig_PropertyName_MappingMaxValue="maxValue";
    public static final String MetaConfig_PropertyName_ExistingPropertyHandleMethod="existingPropertyHandleMethod";
    public static final String MetaConfig_PropertyName_DateDimensionTypePrefix="dateDimensionTypePrefix";

    private static HashMap<String,String> TYPEKIND_AliasNameMap=new HashMap<>();
    private static HashMap<String,String> TypeProperty_AliasNameMap=new HashMap<>();
    private static HashMap<String,String> CustomProperty_AliasNameMap=new HashMap<>();

    public static boolean checkDiscoverSpaceExistence(String spaceName){
        return DiscoverEngineComponentFactory.checkDiscoverSpaceExistence(spaceName);
    }

    public static boolean createDiscoverSpace(String spaceName){
        return DiscoverEngineComponentFactory.createInfoDiscoverSpace(spaceName);
    }

    public static boolean deleteDiscoverSpace(String spaceName){
        return DiscoverEngineComponentFactory.deleteInfoDiscoverSpace(spaceName);
    }

    public static List<String> getExistDiscoverSpace(){
        return DiscoverEngineComponentFactory.getDiscoverSpacesListInEngine();
    }

    public static List<String> getExistDiscoverSpace(String[] excludedDiscoverSpaces){
        List<String>  existingSpaceList= DiscoverEngineComponentFactory
                .getDiscoverSpacesListInEngine();
        for(String excludedSpace: excludedDiscoverSpaces) {
            existingSpaceList.remove(excludedSpace);
        }
        return existingSpaceList;
    }

    public static DiscoverSpaceStatisticMetrics getDiscoverSpaceStatisticMetrics(String spaceName){
        DiscoverSpaceStatisticHelper discoverSpaceStatisticHelper= DiscoverEngineComponentFactory.getDiscoverSpaceStatisticHelper();
        return discoverSpaceStatisticHelper.getDiscoverSpaceStatisticMetrics(spaceName);
    }

    public static DimensionTypeVO retrieveRootDimensionTypeRuntimeInfo(String spaceName, DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        List<DataTypeStatisticMetrics> dimensionsStatisticMetrics=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
        DimensionTypeVO rootDimensionTypeVO=new DimensionTypeVO();
        rootDimensionTypeVO.setTypeName(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME);
        rootDimensionTypeVO.setTypeDataRecordCount(discoverSpaceStatisticMetrics.getSpaceDimensionDataCount());
        if(dimensionsStatisticMetrics!=null){
            rootDimensionTypeVO.setDescendantDimensionTypesNumber(dimensionsStatisticMetrics.size());
        }
        InfoDiscoverSpace targetSpace=null;
        try {
            List<DimensionTypeVO> childDimensionTypesVOList=new ArrayList<DimensionTypeVO>();
            rootDimensionTypeVO.setChildDimensionTypesVOList(childDimensionTypesVOList);

            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            List<String> rootDimensionTypesList=targetSpace.getRootDimensionTypesList();
            if(rootDimensionTypesList!=null){
                for(String currentDimensionTypeName:rootDimensionTypesList){
                    DimensionType currentDimensionType=targetSpace.getDimensionType(currentDimensionTypeName);
                    DimensionTypeVO currentDimensionTypeVO=retrieveDimensionTypeRuntimeInfo(spaceName,currentDimensionType,dimensionsStatisticMetrics);
                    childDimensionTypesVOList.add(currentDimensionTypeVO);
                }
            }
            return rootDimensionTypeVO;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static RelationTypeVO retrieveRootRelationTypeRuntimeInfo(String spaceName, DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        List<DataTypeStatisticMetrics> relationsStatisticMetrics=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
        RelationTypeVO rootRelationTypeVO=new RelationTypeVO();
        rootRelationTypeVO.setTypeName(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME);
        rootRelationTypeVO.setTypeDataRecordCount(discoverSpaceStatisticMetrics.getSpaceRelationDataCount());
        if(relationsStatisticMetrics!=null){
            rootRelationTypeVO.setDescendantRelationTypesNumber(relationsStatisticMetrics.size());
        }
        InfoDiscoverSpace targetSpace=null;
        try {
            List<RelationTypeVO> childRelationTypesVOList=new ArrayList<RelationTypeVO>();
            rootRelationTypeVO.setChildRelationTypesVOList(childRelationTypesVOList);

            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            List<String> rootRelationTypesList=targetSpace.getRootRelationTypesList();
            if(rootRelationTypesList!=null){
                for(String currentRelationTypeName:rootRelationTypesList){
                    RelationType currentRelationType=targetSpace.getRelationType(currentRelationTypeName);
                    RelationTypeVO currentRelationTypeVO=retrieveRelationTypeRuntimeInfo(spaceName,currentRelationType, relationsStatisticMetrics);
                    childRelationTypesVOList.add(currentRelationTypeVO);
                }
            }
            return rootRelationTypeVO;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static DimensionTypeVO retrieveDimensionTypeRuntimeInfo(String spaceName, String dimensionTypeName,DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasDimensionType(dimensionTypeName)){
                return null;
            }else{
                List<DataTypeStatisticMetrics> dimensionsStatisticMetrics=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
                DimensionType currentDimensionType=targetSpace.getDimensionType(dimensionTypeName);
                DimensionTypeVO currentDimensionTypeVO=retrieveDimensionTypeRuntimeInfo(spaceName,currentDimensionType,dimensionsStatisticMetrics);
                return currentDimensionTypeVO;
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static RelationTypeVO retrieveRelationTypeRuntimeInfo(String spaceName, String relationTypeName,DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasRelationType(relationTypeName)){
                return null;
            }else{
                List<DataTypeStatisticMetrics> relationsStatisticMetrics=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
                RelationType currentRelationType=targetSpace.getRelationType(relationTypeName);
                RelationTypeVO currentRelationTypeVO=retrieveRelationTypeRuntimeInfo(spaceName,currentRelationType, relationsStatisticMetrics);
                return currentRelationTypeVO;
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    private static DimensionTypeVO retrieveDimensionTypeRuntimeInfo(String discoverSpaceName,DimensionType targetDimensionType,List<DataTypeStatisticMetrics> dimensionsStatisticMetrics){
        DimensionTypeVO targetDimensionTypeVO=new DimensionTypeVO();
        targetDimensionTypeVO.setTypeName(targetDimensionType.getTypeName());
        targetDimensionTypeVO.setTypeAliasName(getTypeKindAliasName(discoverSpaceName,TYPEKIND_DIMENSION,targetDimensionType.getTypeName()));
        if(targetDimensionType.getDescendantDimensionTypes()!=null){
            targetDimensionTypeVO.setDescendantDimensionTypesNumber(targetDimensionType.getDescendantDimensionTypes().size());
        }
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:dimensionsStatisticMetrics){
            String currentDimensionTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION,"");
            if(currentDimensionTypeName.equals(targetDimensionType.getTypeName())){
                targetDimensionTypeVO.setTypeDataRecordCount(currentDataTypeStatisticMetrics.getTypeDataCount());
                break;
            }
        }
        List<DimensionTypeVO> childDimensionTypesVOList=new ArrayList<DimensionTypeVO>();
        targetDimensionTypeVO.setChildDimensionTypesVOList(childDimensionTypesVOList);
        List<DimensionType> childDimensionTypesList=targetDimensionType.getChildDimensionTypes();
        if(childDimensionTypesList!=null){
            for(DimensionType currentDimensionType:childDimensionTypesList){
                DimensionTypeVO currentDimensionTypeVO=retrieveDimensionTypeRuntimeInfo(discoverSpaceName,currentDimensionType,dimensionsStatisticMetrics);
                childDimensionTypesVOList.add(currentDimensionTypeVO);
            }
        }
        return targetDimensionTypeVO;
    }

    private static RelationTypeVO retrieveRelationTypeRuntimeInfo(String discoverSpaceName,RelationType targetRelationType,List<DataTypeStatisticMetrics> relationsStatisticMetrics){
        RelationTypeVO targetRelationTypeVO=new RelationTypeVO();
        targetRelationTypeVO.setTypeName(targetRelationType.getTypeName());
        targetRelationTypeVO.setTypeAliasName(getTypeKindAliasName(discoverSpaceName,TYPEKIND_RELATION,targetRelationType.getTypeName()));
        if(targetRelationType.getDescendantRelationTypes()!=null){
            targetRelationTypeVO.setDescendantRelationTypesNumber(targetRelationType.getDescendantRelationTypes().size());
        }
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:relationsStatisticMetrics){
            String currentRelationTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            if(currentRelationTypeName.equals(targetRelationType.getTypeName())){
                targetRelationTypeVO.setTypeDataRecordCount(currentDataTypeStatisticMetrics.getTypeDataCount());
                break;
            }
        }
        List<RelationTypeVO> childRelationTypesVOList=new ArrayList<RelationTypeVO>();
        targetRelationTypeVO.setChildRelationTypesVOList(childRelationTypesVOList);
        List<RelationType> childRelationTypesList=targetRelationType.getChildRelationTypes();
        if(childRelationTypesList!=null){
            for(RelationType currentRelationType:childRelationTypesList){
                RelationTypeVO currentRelationTypeVO=retrieveRelationTypeRuntimeInfo(discoverSpaceName,currentRelationType, relationsStatisticMetrics);
                childRelationTypesVOList.add(currentRelationTypeVO);
            }
        }
        return targetRelationTypeVO;
    }

    public static List<PropertyTypeVO> retrieveDimensionTypePropertiesInfo(String spaceName, String dimensionTypeName){
        List<PropertyTypeVO> propertyTypeVOs=new ArrayList<PropertyTypeVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            DimensionType targetDimensionType=targetSpace.getDimensionType(dimensionTypeName);
            if(targetDimensionType!=null){
                List<TypeProperty> propertiesList=targetDimensionType.getTypeProperties();
                if(propertiesList!=null){
                    for(TypeProperty currentTypeProperty:propertiesList){
                        currentTypeProperty.getPropertyName();
                        currentTypeProperty.getPropertyType();
                        currentTypeProperty.isMandatory();
                        currentTypeProperty.isReadOnly();
                        currentTypeProperty.isNullable();
                        PropertyTypeVO currentPropertyTypeVO=new PropertyTypeVO();
                        currentPropertyTypeVO.setPropertyName(currentTypeProperty.getPropertyName());
                        currentPropertyTypeVO.setPropertyAliasName(getTypePropertyAliasName(spaceName,
                                TYPEKIND_DIMENSION,dimensionTypeName,currentTypeProperty.getPropertyName()));
                        currentPropertyTypeVO.setPropertyType(""+currentTypeProperty.getPropertyType());
                        currentPropertyTypeVO.setMandatory(currentTypeProperty.isMandatory());
                        currentPropertyTypeVO.setNullable(currentTypeProperty.isNullable());
                        currentPropertyTypeVO.setReadOnly(currentTypeProperty.isReadOnly());
                        currentPropertyTypeVO.setPropertySourceOwner(currentTypeProperty.getPropertySourceOwner());
                        propertyTypeVOs.add(currentPropertyTypeVO);
                    }
                }
            }
            return propertyTypeVOs;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static List<PropertyTypeVO> retrieveRelationTypePropertiesInfo(String spaceName, String relationTypeName){
        List<PropertyTypeVO> propertyTypeVOs=new ArrayList<PropertyTypeVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            RelationType targetRelationType=targetSpace.getRelationType(relationTypeName);
            if(targetRelationType!=null){
                List<TypeProperty> propertiesList=targetRelationType.getTypeProperties();
                if(propertiesList!=null){
                    for(TypeProperty currentTypeProperty:propertiesList){
                        currentTypeProperty.getPropertyName();
                        currentTypeProperty.getPropertyType();
                        currentTypeProperty.isMandatory();
                        currentTypeProperty.isReadOnly();
                        currentTypeProperty.isNullable();
                        PropertyTypeVO currentPropertyTypeVO=new PropertyTypeVO();
                        currentPropertyTypeVO.setPropertyName(currentTypeProperty.getPropertyName());
                        currentPropertyTypeVO.setPropertyAliasName(getTypePropertyAliasName(spaceName,
                                TYPEKIND_RELATION,relationTypeName,currentTypeProperty.getPropertyName()));
                        currentPropertyTypeVO.setPropertyType(""+currentTypeProperty.getPropertyType());
                        currentPropertyTypeVO.setMandatory(currentTypeProperty.isMandatory());
                        currentPropertyTypeVO.setNullable(currentTypeProperty.isNullable());
                        currentPropertyTypeVO.setReadOnly(currentTypeProperty.isReadOnly());
                        currentPropertyTypeVO.setPropertySourceOwner(currentTypeProperty.getPropertySourceOwner());
                        propertyTypeVOs.add(currentPropertyTypeVO);
                    }
                }
            }
            return propertyTypeVOs;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static List<PropertyTypeVO> retrieveFactTypePropertiesInfo(String spaceName, String factTypeName){
        List<PropertyTypeVO> propertyTypeVOs=new ArrayList<PropertyTypeVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            FactType targetFactType=targetSpace.getFactType(factTypeName);
            if(targetFactType!=null){
                List<TypeProperty> propertiesList=targetFactType.getTypeProperties();
                if(propertiesList!=null){
                    for(TypeProperty currentTypeProperty:propertiesList){
                        currentTypeProperty.getPropertyName();
                        currentTypeProperty.getPropertyType();
                        currentTypeProperty.isMandatory();
                        currentTypeProperty.isReadOnly();
                        currentTypeProperty.isNullable();
                        PropertyTypeVO currentPropertyTypeVO=new PropertyTypeVO();
                        currentPropertyTypeVO.setPropertyName(currentTypeProperty.getPropertyName());
                        currentPropertyTypeVO.setPropertyAliasName(getTypePropertyAliasName(spaceName,
                                TYPEKIND_FACT,factTypeName,currentTypeProperty.getPropertyName()));
                        currentPropertyTypeVO.setPropertyType(""+currentTypeProperty.getPropertyType());
                        currentPropertyTypeVO.setMandatory(currentTypeProperty.isMandatory());
                        currentPropertyTypeVO.setNullable(currentTypeProperty.isNullable());
                        currentPropertyTypeVO.setReadOnly(currentTypeProperty.isReadOnly());
                        currentPropertyTypeVO.setPropertySourceOwner(currentTypeProperty.getPropertySourceOwner());
                        propertyTypeVOs.add(currentPropertyTypeVO);
                    }
                }
            }
            return propertyTypeVOs;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean checkDimensionTypeExistence(String spaceName, String dimensionTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            return targetSpace.hasDimensionType(dimensionTypeName);
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean checkRelationTypeExistence(String spaceName, String relationTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            return targetSpace.hasRelationType(relationTypeName);
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean checkFactTypeExistence(String spaceName, String factTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            return targetSpace.hasFactType(factTypeName);
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean createDimensionType(String spaceName, String dimensionTypeName,String dimensionTypeAliasName,String parentDimensionTypeName){
        boolean createResult=false;
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(targetSpace.hasDimensionType(dimensionTypeName)){
                return false;
            }else{
                DimensionType targetDimensionType=null;
                if(parentDimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
                    targetDimensionType=targetSpace.addDimensionType(dimensionTypeName);
                }else{
                    targetDimensionType=targetSpace.addChildDimensionType(dimensionTypeName,parentDimensionTypeName);
                }
                if(targetDimensionType!=null&&targetDimensionType.getTypeName().equals(dimensionTypeName)){
                    createResult = true;
                }else{
                    createResult = false;
                }
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        if(createResult){
            recordTypeKindAliasName(spaceName,TYPEKIND_DIMENSION,dimensionTypeName,dimensionTypeAliasName);
        }
        return createResult;
    }

    public static boolean createRelationType(String spaceName, String relationTypeName,String relationTypeAliasName,String parentRelationTypeName){
        boolean createResult=false;
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(targetSpace.hasRelationType(relationTypeName)){
                return false;
            }else{
                RelationType targetRelationType=null;
                if(parentRelationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
                    targetRelationType=targetSpace.addRelationType(relationTypeName);
                }else{
                    targetRelationType=targetSpace.addChildRelationType(relationTypeName, parentRelationTypeName);
                }
                if(targetRelationType!=null&&targetRelationType.getTypeName().equals(relationTypeName)){
                    createResult = true;
                }else{
                    createResult = false;
                }
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        if(createResult){
            recordTypeKindAliasName(spaceName,TYPEKIND_RELATION,relationTypeName,relationTypeAliasName);
        }
        return createResult;
    }

    public static boolean createFactType(String spaceName, String factTypeName,String factTypeAliasName){
        boolean createResult=false;
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(targetSpace.hasFactType(factTypeName)) {
                return false;
            }else{
                FactType targetFactType=targetSpace.addFactType(factTypeName);
                if(targetFactType!=null&&targetFactType.getTypeName().equals(factTypeName)){
                    createResult= true;
                }else{
                    createResult= false;
                }
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        if(createResult){
            recordTypeKindAliasName(spaceName,TYPEKIND_FACT,factTypeName,factTypeAliasName);
        }
        return createResult;
    }

    public static boolean deleteDimensionType(String spaceName, String dimensionTypeName){
        boolean removeResult=false;
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasDimensionType(dimensionTypeName)){
                return false;
            }else{
                removeResult=targetSpace.removeDimensionType(dimensionTypeName);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        if(removeResult) {
            deleteTypeKindAliasName(spaceName, TYPEKIND_DIMENSION, dimensionTypeName);
        }
        return removeResult;
    }

    public static boolean deleteRelationType(String spaceName, String relationTypeName){
        boolean removeResult=false;
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasRelationType(relationTypeName)){
                return false;
            }else{
                removeResult=targetSpace.removeRelationType(relationTypeName);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        if(removeResult) {
            deleteTypeKindAliasName(spaceName, TYPEKIND_RELATION, relationTypeName);
        }
        return removeResult;
    }

    public static boolean deleteFactType(String spaceName, String factTypeName){
        boolean removeResult=false;
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasFactType(factTypeName)){
                return false;
            }else{
                removeResult=targetSpace.removeFactType(factTypeName);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        if(removeResult) {
            deleteTypeKindAliasName(spaceName, TYPEKIND_FACT, factTypeName);
        }
        return removeResult;
    }

    public static boolean checkTypePropertyExistence(String spaceName, String typeKind,String typeName,String propertyName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(TYPEKIND_DIMENSION.equals(typeKind)){
                if(!targetSpace.hasDimensionType(typeName)){
                    return false;
                }else{
                    DimensionType targetDimensionType=targetSpace.getDimensionType(typeName);
                    return targetDimensionType.hasTypeProperty(propertyName);
                }
            }
            if(TYPEKIND_FACT.equals(typeKind)){
                if(!targetSpace.hasFactType(typeName)){
                    return false;
                }else{
                    FactType targetFactType=targetSpace.getFactType(typeName);
                    return targetFactType.hasTypeProperty(propertyName);
                }
            }
            if(TYPEKIND_RELATION.equals(typeKind)){
                if(!targetSpace.hasRelationType(typeName)){
                    return false;
                }else {
                    RelationType targetRelationType = targetSpace.getRelationType(typeName);
                    return targetRelationType.hasTypeProperty(propertyName);
                }
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createTypeProperty(String spaceName, String typeKind,String typeName,
                                             String propertyName,String propertyAliasName,String propertyType,boolean isMandatory,boolean isReadOnly,boolean isNullable){
        boolean createResult=false;
        PropertyType targetPropertyType=null;
        if(ApplicationConstant.DataFieldType_BOOLEAN.equals(propertyType)){
            targetPropertyType=PropertyType.BOOLEAN;
        }
        if(ApplicationConstant.DataFieldType_INT.equals(propertyType)){
            targetPropertyType=PropertyType.INT;
        }
        if(ApplicationConstant.DataFieldType_SHORT.equals(propertyType)){
            targetPropertyType=PropertyType.SHORT;
        }
        if(ApplicationConstant.DataFieldType_LONG.equals(propertyType)){
            targetPropertyType=PropertyType.LONG;
        }
        if(ApplicationConstant.DataFieldType_FLOAT.equals(propertyType)){
            targetPropertyType=PropertyType.FLOAT;
        }
        if(ApplicationConstant.DataFieldType_DOUBLE.equals(propertyType)){
            targetPropertyType=PropertyType.DOUBLE;
        }
        if(ApplicationConstant.DataFieldType_DATE.equals(propertyType)){
            targetPropertyType=PropertyType.DATE;
        }
        if(ApplicationConstant.DataFieldType_STRING.equals(propertyType)){
            targetPropertyType=PropertyType.STRING;
        }
        if(ApplicationConstant.DataFieldType_BINARY.equals(propertyType)){
            targetPropertyType=PropertyType.BINARY;
        }
        if(ApplicationConstant.DataFieldType_BYTE.equals(propertyType)){
            targetPropertyType=PropertyType.BYTE;
        }
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(TYPEKIND_DIMENSION.equals(typeKind)){
                if(!targetSpace.hasDimensionType(typeName)){
                    return false;
                }else{
                    DimensionType targetDimensionType=targetSpace.getDimensionType(typeName);
                    TypeProperty targetTypeProperty=targetDimensionType.addTypeProperty(propertyName,targetPropertyType);
                    targetTypeProperty.setMandatory(isMandatory);
                    targetTypeProperty.setReadOnly(isReadOnly);
                    targetTypeProperty.setNullable(isNullable);
                    createResult= targetTypeProperty.getPropertyName().equals(propertyName);
                }
            }
            if(TYPEKIND_FACT.equals(typeKind)){
                if(!targetSpace.hasFactType(typeName)){
                    return false;
                }else{
                    FactType targetFactType=targetSpace.getFactType(typeName);
                    TypeProperty targetTypeProperty=targetFactType.addTypeProperty(propertyName,targetPropertyType);
                    targetTypeProperty.setMandatory(isMandatory);
                    targetTypeProperty.setReadOnly(isReadOnly);
                    targetTypeProperty.setNullable(isNullable);
                    createResult= targetTypeProperty.getPropertyName().equals(propertyName);
                }
            }
            if(TYPEKIND_RELATION.equals(typeKind)){
                if(!targetSpace.hasRelationType(typeName)){
                    return false;
                }else {
                    RelationType targetRelationType = targetSpace.getRelationType(typeName);
                    TypeProperty targetTypeProperty=targetRelationType.addTypeProperty(propertyName,targetPropertyType);
                    targetTypeProperty.setMandatory(isMandatory);
                    targetTypeProperty.setReadOnly(isReadOnly);
                    targetTypeProperty.setNullable(isNullable);
                    createResult= targetTypeProperty.getPropertyName().equals(propertyName);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        if(createResult){
            recordTypePropertyAliasName(spaceName,typeKind,typeName,propertyName,propertyAliasName);
        }
        return createResult;
    }

    public static boolean deleteDimensionTypeProperty(String spaceName, String dimensionTypeName,String propertyName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasDimensionType(dimensionTypeName)){
                return false;
            }else{
                DimensionType targetDimensionType=targetSpace.getDimensionType(dimensionTypeName);
                if(!targetDimensionType.hasTypeProperty(propertyName)){
                    return false;
                }
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        boolean removeResult=false;
        InfoDiscoverAdminSpace adminSpace=null;
        try {
            adminSpace=DiscoverEngineComponentFactory.connectInfoDiscoverAdminSpace(spaceName);
            removeResult=adminSpace.removeDimensionTypeProperty(dimensionTypeName,propertyName);
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(adminSpace!=null){
                adminSpace.closeSpace();
            }
        }
        if(removeResult){
            deleteTypePropertyAliasName(spaceName,TYPEKIND_DIMENSION,dimensionTypeName,propertyName);
        }
        return removeResult;
    }

    public static boolean deleteRelationTypeProperty(String spaceName, String relationTypeName,String propertyName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasRelationType(relationTypeName)){
                return false;
            }else{
                RelationType targetRelationType=targetSpace.getRelationType(relationTypeName);
                if(!targetRelationType.hasTypeProperty(propertyName)){
                    return false;
                }
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        boolean removeResult=false;
        InfoDiscoverAdminSpace adminSpace=null;
        try {
            adminSpace=DiscoverEngineComponentFactory.connectInfoDiscoverAdminSpace(spaceName);
            removeResult=adminSpace.removeRelationTypeProperty(relationTypeName, propertyName);
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(adminSpace!=null){
                adminSpace.closeSpace();
            }
        }
        if(removeResult){
            deleteTypePropertyAliasName(spaceName,TYPEKIND_RELATION,relationTypeName,propertyName);
        }
        return removeResult;
    }

    public static boolean deleteFactTypeProperty(String spaceName, String factTypeName,String propertyName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasFactType(factTypeName)){
                return false;
            }else{
                FactType targetFactType=targetSpace.getFactType(factTypeName);
                if(!targetFactType.hasTypeProperty(propertyName)){
                    return false;
                }
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        boolean removeResult=false;
        InfoDiscoverAdminSpace adminSpace=null;
        try {
            adminSpace=DiscoverEngineComponentFactory.connectInfoDiscoverAdminSpace(spaceName);
            removeResult=adminSpace.removeFactTypeProperty(factTypeName,propertyName);
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(adminSpace!=null){
                adminSpace.closeSpace();
            }
        }
        if(removeResult){
            deleteTypePropertyAliasName(spaceName,TYPEKIND_FACT,factTypeName,propertyName);
        }
        return removeResult;
    }

    public static List<String> retrieveChildDimensionTypesRuntimeInfo(String spaceName, String parentDimensionTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(parentDimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
                List<String> rootDimensionTypesList=targetSpace.getRootDimensionTypesList();
                if(rootDimensionTypesList!=null){
                    return rootDimensionTypesList;
                }else{
                    return new ArrayList<String>();
                }
            }
            List<String> childDimensionTypeNamesList=new ArrayList<String>();
            if(targetSpace.hasDimensionType(parentDimensionTypeName)){
                List<DimensionType> childDimensionTypesList=targetSpace.getDimensionType(parentDimensionTypeName).getChildDimensionTypes();
                if(childDimensionTypesList!=null){
                    for(DimensionType currentDimensionType:childDimensionTypesList){
                        childDimensionTypeNamesList.add(currentDimensionType.getTypeName());
                    }
                }
            }
            return childDimensionTypeNamesList;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static List<String> retrieveChildRelationTypesRuntimeInfo(String spaceName, String parentRelationTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(parentRelationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
                List<String> rootRelationTypesList=targetSpace.getRootRelationTypesList();
                if(rootRelationTypesList!=null){
                    return rootRelationTypesList;
                }else{
                    return new ArrayList<String>();
                }
            }
            List<String> childRelationTypeNamesList=new ArrayList<String>();
            if(targetSpace.hasRelationType(parentRelationTypeName)){
                List<RelationType> childRelationTypesList=targetSpace.getRelationType(parentRelationTypeName).getChildRelationTypes();
                if(childRelationTypesList!=null){
                    for(RelationType currentRelationType:childRelationTypesList){
                        childRelationTypeNamesList.add(currentRelationType.getTypeName());
                    }
                }
            }
            return childRelationTypeNamesList;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean createDimension(String spaceName, String dimensionTypeName,List<PropertyValueVO> dimensionProperties){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasDimensionType(dimensionTypeName)){
                return false;
            }else{
                Dimension targetDimension=DiscoverEngineComponentFactory.createDimension(dimensionTypeName);
                setMeasurableInitProperties(targetDimension,dimensionProperties);
                Dimension resultDimension=targetSpace.addDimension(targetDimension);
                if(resultDimension!=null){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean createFact(String spaceName, String factTypeName,List<PropertyValueVO> factProperties){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasFactType(factTypeName)){
                return false;
            }else{
                Fact targetFact=DiscoverEngineComponentFactory.createFact(factTypeName);
                setMeasurableInitProperties(targetFact,factProperties);
                Fact resultFact=targetSpace.addFact(targetFact);
                if(resultFact!=null){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    private static void setMeasurableInitProperties(Measurable measurable,List<PropertyValueVO> properties){
        for(PropertyValueVO currentPropertyValueVO:properties){
            String propertyName=currentPropertyValueVO.getPropertyName();
            String propertyType=currentPropertyValueVO.getPropertyType();
            Object propertyValue=currentPropertyValueVO.getPropertyValue();
            if(propertyValue!=null){
                switch(propertyType){
                    case ApplicationConstant.DataFieldType_STRING:
                        String stringValue=propertyValue.toString();
                        measurable.setInitProperty(propertyName,stringValue);
                        break;
                    case ApplicationConstant.DataFieldType_BOOLEAN:
                        String valueText=propertyValue.toString();
                        boolean booleanValue=Boolean.parseBoolean(valueText);
                        measurable.setInitProperty(propertyName,booleanValue);
                        break;
                    case ApplicationConstant.DataFieldType_DATE:
                        Date dateValue=(Date)propertyValue;
                        measurable.setInitProperty(propertyName,dateValue);
                        break;
                    case ApplicationConstant.DataFieldType_INT:
                        int intValue=((Integer)propertyValue).intValue();
                        measurable.setInitProperty(propertyName,intValue);
                        break;
                    case ApplicationConstant.DataFieldType_LONG:
                        long longValue=((Long)propertyValue).longValue();
                        measurable.setInitProperty(propertyName,longValue);
                        break;
                    case ApplicationConstant.DataFieldType_DOUBLE:
                        double doubleValue=((Double)propertyValue).doubleValue();
                        measurable.setInitProperty(propertyName,doubleValue);
                        break;
                    case ApplicationConstant.DataFieldType_FLOAT:
                        float floatValue=((Float)propertyValue).floatValue();
                        measurable.setInitProperty(propertyName,floatValue);
                        break;
                    case ApplicationConstant.DataFieldType_SHORT:
                        short shortValue=((Short)propertyValue).shortValue();
                        measurable.setInitProperty(propertyName,shortValue);
                        break;
                case ApplicationConstant.DataFieldType_BYTE:
                        byte byteValue=((Byte)propertyValue).byteValue();
                        measurable.setInitProperty(propertyName,byteValue);
                        break;
                case ApplicationConstant.DataFieldType_BINARY:
                    byte[] binaryValue=(byte[])propertyValue;
                    measurable.setInitProperty(propertyName,binaryValue);
                    break;
                }
            }
        }
    }

    public static List<MeasurableValueVO> queryDimensions(String spaceName,ExploreParameters exploreParameters){
        List<MeasurableValueVO> measurableValueList=new ArrayList<MeasurableValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Dimension> resultDimensionsList=ie.discoverDimensions(exploreParameters);
            if(resultDimensionsList!=null) {
                for (Dimension currentDimension : resultDimensionsList) {
                    MeasurableValueVO currentMeasurableValueVO=new MeasurableValueVO();
                    measurableValueList.add(currentMeasurableValueVO);
                    currentMeasurableValueVO.setDiscoverSpaceName(spaceName);
                    currentMeasurableValueVO.setId(currentDimension.getId());
                    currentMeasurableValueVO.setMeasurableTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
                    currentMeasurableValueVO.setMeasurableTypeName(currentDimension.getType());
                    List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                    currentMeasurableValueVO.setProperties(propertyValueVOList);
                    List<Property> propertiesList=currentDimension.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return measurableValueList;
    }

    public static List<MeasurableValueVO> queryFacts(String spaceName,ExploreParameters exploreParameters){
        List<MeasurableValueVO> measurableValueList=new ArrayList<MeasurableValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Fact> resultFactsList=ie.discoverFacts(exploreParameters);
            if(resultFactsList!=null) {
                for (Fact currentFact : resultFactsList) {
                    MeasurableValueVO currentMeasurableValueVO=new MeasurableValueVO();
                    measurableValueList.add(currentMeasurableValueVO);
                    currentMeasurableValueVO.setDiscoverSpaceName(spaceName);
                    currentMeasurableValueVO.setId(currentFact.getId());
                    currentMeasurableValueVO.setMeasurableTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT);
                    currentMeasurableValueVO.setMeasurableTypeName(currentFact.getType());
                    List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                    currentMeasurableValueVO.setProperties(propertyValueVOList);
                    List<Property> propertiesList=currentFact.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return measurableValueList;
    }

    public static List<MeasurableValueVO> queryRelations(String spaceName,ExploreParameters exploreParameters){
        List<MeasurableValueVO> measurableValueList=new ArrayList<MeasurableValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Relation> resultRelationsList=ie.discoverRelations(exploreParameters);
            if(resultRelationsList!=null) {
                for (Relation currentRelation : resultRelationsList) {
                    MeasurableValueVO currentMeasurableValueVO=new MeasurableValueVO();
                    measurableValueList.add(currentMeasurableValueVO);
                    currentMeasurableValueVO.setDiscoverSpaceName(spaceName);
                    currentMeasurableValueVO.setId(currentRelation.getId());
                    currentMeasurableValueVO.setMeasurableTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION);
                    currentMeasurableValueVO.setMeasurableTypeName(currentRelation.getType());
                    List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                    currentMeasurableValueVO.setProperties(propertyValueVOList);
                    List<Property> propertiesList=currentRelation.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return measurableValueList;
    }

    public static boolean updateMeasurableProperties(String spaceName,String measurableId,List<PropertyValueVO> propertiesValue){
        if(propertiesValue==null){
            return false;
        }
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Measurable targetMeasurable = targetSpace.getMeasurableById(measurableId);
            if(targetMeasurable==null){
                return false;
            }
            List<String> currentPropertiesNameList= targetMeasurable.getPropertyNames();
            //remove properties not contained in new PropertyValue List
            if(currentPropertiesNameList!=null){
                for(String currentPropertyName:currentPropertiesNameList){
                    if(!propertyContainedInPropertyValueList(currentPropertyName,propertiesValue)){
                        boolean resultPropertyResult=targetMeasurable.removeProperty(currentPropertyName);
                        if(!resultPropertyResult){
                            return false;
                        }
                    }
                }
            }

            Map<String, Object> propertiesNeedAddMap=new HashMap<String, Object>();
            Map<String, Object> propertiesNeedAddUpdate=new HashMap<String, Object>();

            for(PropertyValueVO currentNewPropertyValue:propertiesValue){
                String propertyName=currentNewPropertyValue.getPropertyName();
                if(!targetMeasurable.hasProperty(propertyName)){
                    //add new property value
                    propertiesNeedAddMap.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
                }else{
                    //update existing property value
                    String propertyType=currentNewPropertyValue.getPropertyType();
                    Property measurableCurrentProperty=targetMeasurable.getProperty(propertyName);
                    PropertyType currentPropertyType=measurableCurrentProperty.getPropertyType();
                    if(!currentPropertyType.toString().equals(propertyType)){
                        targetMeasurable.removeProperty(propertyName);
                        propertiesNeedAddMap.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
                    }else{
                        propertiesNeedAddUpdate.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
                    }
                }
            }
            targetMeasurable.addProperties(propertiesNeedAddMap);
            targetMeasurable.updateProperties(propertiesNeedAddUpdate);

        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return true;
    }

    private static boolean propertyContainedInPropertyValueList(String propertyName,List<PropertyValueVO> propertiesValue){
        if(propertiesValue==null){
            return false;
        }
        for(PropertyValueVO currentPropertyValue:propertiesValue){
            if(currentPropertyValue.getPropertyName().equals(propertyName)){
                return true;
            }
        }
        return false;
    }

    private static Object getMeasurablePropertyValue(PropertyValueVO currentPropertyValueVO){
        String propertyType=currentPropertyValueVO.getPropertyType();
        Object propertyValue=currentPropertyValueVO.getPropertyValue();
        Object measurablePropertyValue=null;
        if(propertyValue!=null){
            switch(propertyType){
                case ApplicationConstant.DataFieldType_STRING:
                    measurablePropertyValue=propertyValue.toString();
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    String valueText=propertyValue.toString();
                    measurablePropertyValue=Boolean.parseBoolean(valueText);
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    measurablePropertyValue=(Date)propertyValue;
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    measurablePropertyValue=((Integer)propertyValue).intValue();
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    measurablePropertyValue=((Long)propertyValue).longValue();
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    measurablePropertyValue=((Double)propertyValue).doubleValue();
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    measurablePropertyValue=((Float)propertyValue).floatValue();
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    measurablePropertyValue=((Short)propertyValue).shortValue();
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    measurablePropertyValue=((Byte)propertyValue).byteValue();
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    measurablePropertyValue=(byte[])propertyValue;
                    break;
            }
        }
        return measurablePropertyValue;
    }

    public static void addMeasurableSingleProperty(Measurable measurable,PropertyValueVO currentPropertyValueVO) throws InfoDiscoveryEngineRuntimeException {
        String propertyName=currentPropertyValueVO.getPropertyName();
        String propertyType=currentPropertyValueVO.getPropertyType();
        Object propertyValue=currentPropertyValueVO.getPropertyValue();
        if(propertyValue!=null){
            switch(propertyType){
                case ApplicationConstant.DataFieldType_STRING:
                    String stringValue=propertyValue.toString();
                    measurable.addProperty(propertyName,stringValue);
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    String valueText=propertyValue.toString();
                    boolean booleanValue=Boolean.parseBoolean(valueText);
                    measurable.addProperty(propertyName,booleanValue);
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    Date dateValue=(Date)propertyValue;
                    measurable.addProperty(propertyName,dateValue);
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    int intValue=((Integer)propertyValue).intValue();
                    measurable.addProperty(propertyName,intValue);
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    long longValue=((Long)propertyValue).longValue();
                    measurable.addProperty(propertyName,longValue);
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    double doubleValue=((Double)propertyValue).doubleValue();
                    measurable.addProperty(propertyName,doubleValue);
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    float floatValue=((Float)propertyValue).floatValue();
                    measurable.addProperty(propertyName,floatValue);
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    short shortValue=((Short)propertyValue).shortValue();
                    measurable.addProperty(propertyName,shortValue);
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    byte byteValue=((Byte)propertyValue).byteValue();
                    measurable.addProperty(propertyName,byteValue);
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    byte[] binaryValue=(byte[])propertyValue;
                    measurable.addProperty(propertyName,binaryValue);
                    break;
            }
        }
    }

    public static void addMeasurableMultiProperties(Measurable measurable,List<PropertyValueVO> propertiesValueList){
        Map<String, Object> propertiesNeedAddMap=new HashMap<String, Object>();
        for(PropertyValueVO currentNewPropertyValue:propertiesValueList){
            String propertyName=currentNewPropertyValue.getPropertyName();
            propertiesNeedAddMap.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
        }
        measurable.addProperties(propertiesNeedAddMap);
    }

    public static void updateMeasurableSingleProperty(Measurable measurable,PropertyValueVO currentPropertyValueVO) throws InfoDiscoveryEngineRuntimeException {
        String propertyName=currentPropertyValueVO.getPropertyName();
        String propertyType=currentPropertyValueVO.getPropertyType();
        Object propertyValue=currentPropertyValueVO.getPropertyValue();

        Property measurableCurrentProperty=measurable.getProperty(propertyName);
        PropertyType currentPropertyType=measurableCurrentProperty.getPropertyType();

        if(!currentPropertyType.toString().equals(propertyType)){
            measurable.removeProperty(propertyName);
            addMeasurableSingleProperty(measurable,currentPropertyValueVO);
        }else{
            if(propertyValue!=null){
                switch(propertyType){
                    case ApplicationConstant.DataFieldType_STRING:
                        String stringValue=propertyValue.toString();
                        measurable.updateProperty(propertyName,stringValue);
                        break;
                    case ApplicationConstant.DataFieldType_BOOLEAN:
                        String valueText=propertyValue.toString();
                        boolean booleanValue=Boolean.parseBoolean(valueText);
                        measurable.updateProperty(propertyName,booleanValue);
                        break;
                    case ApplicationConstant.DataFieldType_DATE:
                        Date dateValue=(Date)propertyValue;
                        measurable.updateProperty(propertyName,dateValue);
                        break;
                    case ApplicationConstant.DataFieldType_INT:
                        int intValue=((Integer)propertyValue).intValue();
                        measurable.updateProperty(propertyName,intValue);
                        break;
                    case ApplicationConstant.DataFieldType_LONG:
                        long longValue=((Long)propertyValue).longValue();
                        measurable.updateProperty(propertyName,longValue);
                        break;
                    case ApplicationConstant.DataFieldType_DOUBLE:
                        double doubleValue=((Double)propertyValue).doubleValue();
                        measurable.updateProperty(propertyName,doubleValue);
                        break;
                    case ApplicationConstant.DataFieldType_FLOAT:
                        float floatValue=((Float)propertyValue).floatValue();
                        measurable.updateProperty(propertyName,floatValue);
                        break;
                    case ApplicationConstant.DataFieldType_SHORT:
                        short shortValue=((Short)propertyValue).shortValue();
                        measurable.updateProperty(propertyName,shortValue);
                        break;
                    case ApplicationConstant.DataFieldType_BYTE:
                        byte byteValue=((Byte)propertyValue).byteValue();
                        measurable.updateProperty(propertyName,byteValue);
                        break;
                    case ApplicationConstant.DataFieldType_BINARY:
                        byte[] binaryValue=(byte[])propertyValue;
                        measurable.updateProperty(propertyName,binaryValue);
                        break;
                }
            }
        }
    }

    public static List<RelationValueVO> getRelationableRelationsById(String spaceName,String relationableTypeKind,String relationableId){
        List<RelationValueVO> resultRelationValueList=new ArrayList<RelationValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            List<Relation> relationsList=null;
            if(TYPEKIND_DIMENSION.equals(relationableTypeKind)){
                Dimension targetDimension=targetSpace.getDimensionById(relationableId);
                if(targetDimension!=null){
                    relationsList= targetDimension.getAllRelations();
                }
            }
            if(TYPEKIND_FACT.equals(relationableTypeKind)){
                Fact targetFact=targetSpace.getFactById(relationableId);
                if(targetFact!=null){
                    relationsList= targetFact.getAllRelations();
                }
            }
            if(relationsList!=null){
                for(Relation currentRelation:relationsList){
                    RelationValueVO currentRelationValueVO=new RelationValueVO();
                    currentRelationValueVO.setId(currentRelation.getId());
                    currentRelationValueVO.setRelationTypeName(currentRelation.getType());
                    currentRelationValueVO.setDiscoverSpaceName(spaceName);

                    Relationable fromRelationable=currentRelation.getFromRelationable();
                    if(fromRelationable!=null){
                        RelationableValueVO fromRelationableValueVO=new RelationableValueVO();
                        fromRelationableValueVO.setDiscoverSpaceName(spaceName);
                        fromRelationableValueVO.setId(fromRelationable.getId());
                        if(fromRelationable instanceof Dimension){
                            Dimension dimensionRelationable=(Dimension)fromRelationable;
                            fromRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                            fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                        }
                        if(fromRelationable instanceof Fact){
                            Fact factRelationable=(Fact)fromRelationable;
                            fromRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                            fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                        }
                        currentRelationValueVO.setFromRelationable(fromRelationableValueVO);
                    }

                    Relationable toRelationable=currentRelation.getToRelationable();
                    if(toRelationable!=null){
                        RelationableValueVO toRelationableValueVO=new RelationableValueVO();
                        toRelationableValueVO.setDiscoverSpaceName(spaceName);
                        toRelationableValueVO.setId(toRelationable.getId());
                        if(toRelationable instanceof Dimension){
                            Dimension dimensionRelationable=(Dimension)toRelationable;
                            toRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                            toRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                        }
                        if(toRelationable instanceof Fact){
                            Fact factRelationable=(Fact)toRelationable;
                            toRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                            toRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                        }
                        currentRelationValueVO.setToRelationable(toRelationableValueVO);
                    }

                    List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                    currentRelationValueVO.setProperties(propertyValueVOList);
                    /* for better performance but get properties's value here
                    List<Property> propertiesList=currentRelation.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                    */
                    resultRelationValueList.add(currentRelationValueVO);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return resultRelationValueList;
    }

    public static List<PropertyValueVO> getMeasurablePropertiesById(String spaceName,String measurableId){
        List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Measurable targetMeasurable=targetSpace.getMeasurableById(measurableId);
            if(targetMeasurable!=null){
                List<Property> propertiesList=targetMeasurable.getProperties();
                if(propertiesList!=null){
                    for(Property currentProperty:propertiesList){
                        if(currentProperty.getPropertyType()!=null){
                            PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                            currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                            currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                            currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                            propertyValueVOList.add(currentPropertyValueVO);
                        }
                    }
                }
            }
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return propertyValueVOList;
    }

    public static MeasurableValueVO getMeasurableValueById(String spaceName,String measurableId){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Measurable targetMeasurable=targetSpace.getMeasurableById(measurableId);
            if(targetMeasurable!=null){
                MeasurableValueVO currentMeasurableValueVO=new MeasurableValueVO();
                currentMeasurableValueVO.setDiscoverSpaceName(spaceName);
                if(targetMeasurable instanceof Dimension){
                    Dimension dimensionRelationable=(Dimension)targetMeasurable;
                    currentMeasurableValueVO.setMeasurableTypeName(dimensionRelationable.getType());
                    currentMeasurableValueVO.setMeasurableTypeKind(TYPEKIND_DIMENSION);
                    currentMeasurableValueVO.setId(dimensionRelationable.getId());
                }
                if(targetMeasurable instanceof Fact){
                    Fact factRelationable=(Fact)targetMeasurable;
                    currentMeasurableValueVO.setMeasurableTypeName(factRelationable.getType());
                    currentMeasurableValueVO.setMeasurableTypeKind(TYPEKIND_FACT);
                    currentMeasurableValueVO.setId(factRelationable.getId());
                }
                if(targetMeasurable instanceof Relation){
                    Relation relationRelationable=(Relation)targetMeasurable;
                    currentMeasurableValueVO.setMeasurableTypeName(relationRelationable.getType());
                    currentMeasurableValueVO.setMeasurableTypeKind(TYPEKIND_RELATION);
                    currentMeasurableValueVO.setId(relationRelationable.getId());
                }
                List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                currentMeasurableValueVO.setProperties(propertyValueVOList);
                List<Property> propertiesList=targetMeasurable.getProperties();
                if(propertiesList!=null){
                    for(Property currentProperty:propertiesList){
                        if(currentProperty.getPropertyType()!=null){
                            PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                            currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                            currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                            currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                            propertyValueVOList.add(currentPropertyValueVO);
                        }
                    }
                }
                return currentMeasurableValueVO;
            }
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static boolean createRelations(RelationableValueVO sourceRelationableValue,String relationTypeName,
                                          String relationDirection,List<PropertyValueVO> relationProperties,
                                          List<String> targetDimensionsIdList, List<String> targetFactsIdList){
        String discoverSpaceName=sourceRelationableValue.getDiscoverSpaceName();
        String relationableTypeKind=sourceRelationableValue.getRelationableTypeKind();
        String sourceRelationablId=sourceRelationableValue.getId();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
            Relationable sourceRelationable=null;
            if(TYPEKIND_DIMENSION.equals(relationableTypeKind)){
                sourceRelationable=targetSpace.getDimensionById(sourceRelationablId);
            }
            if(TYPEKIND_FACT.equals(relationableTypeKind)){
                sourceRelationable=targetSpace.getFactById(sourceRelationablId);
            }
            if(sourceRelationable==null){
                return false;
            }else{
                Relationable targetRelationable=null;
                if(targetDimensionsIdList!=null) {
                    for (String currentDimensionId:targetDimensionsIdList) {
                        targetRelationable=targetSpace.getDimensionById(currentDimensionId);
                        if(targetRelationable!=null){
                            addRelationWithProperties(sourceRelationable,targetRelationable,relationTypeName,relationDirection,relationProperties);
                        }
                    }
                }
                if(targetFactsIdList!=null) {
                    for (String currentFactId:targetFactsIdList) {
                        targetRelationable=targetSpace.getFactById(currentFactId);
                        if(targetRelationable!=null){
                            addRelationWithProperties(sourceRelationable,targetRelationable,relationTypeName,relationDirection,relationProperties);
                        }
                    }
                }
            }
            return true;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    private static void addRelationWithProperties(Relationable sourceRelationable,
                                                  Relationable targetRelationable,String relationTypeName,String relationDirection,
                                                  List<PropertyValueVO> relationProperties) throws InfoDiscoveryEngineRuntimeException {
        try {
            Map<String, Object> propertiesNeedAddMap=new HashMap<String, Object>();
            for(PropertyValueVO currentNewPropertyValue:relationProperties){
                String propertyName=currentNewPropertyValue.getPropertyName();
                propertiesNeedAddMap.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
            }
            if (RELATION_DIRECTION_BOTH.equals(relationDirection)) {
                sourceRelationable.addFromRelation(targetRelationable, relationTypeName,propertiesNeedAddMap);
                sourceRelationable.addToRelation(targetRelationable, relationTypeName,propertiesNeedAddMap);
            }
            if (RELATION_DIRECTION_FROM.equals(relationDirection)) {
                sourceRelationable.addFromRelation(targetRelationable, relationTypeName,propertiesNeedAddMap);
            }
            if (RELATION_DIRECTION_TO.equals(relationDirection)) {
                sourceRelationable.addToRelation(targetRelationable, relationTypeName,propertiesNeedAddMap);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new InfoDiscoveryEngineRuntimeException();
        }
    }

    public static boolean removeMeasurableById(String spaceName,String measurableType,String measurableId){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(measurableType)){
                return targetSpace.removeDimension(measurableId);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(measurableType)){
                return targetSpace.removeFact(measurableId);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(measurableType)){
                return targetSpace.removeRelation(measurableId);
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static RelationValueVO getRelationById(String spaceName,String relationId){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Relation targetRelation = targetSpace.getRelationById(relationId);
            if(targetRelation!=null){
                RelationValueVO currentRelationValueVO=new RelationValueVO();
                currentRelationValueVO.setId(targetRelation.getId());
                currentRelationValueVO.setRelationTypeName(targetRelation.getType());
                currentRelationValueVO.setDiscoverSpaceName(spaceName);

                Relationable fromRelationable=targetRelation.getFromRelationable();
                if(fromRelationable!=null){
                    RelationableValueVO fromRelationableValueVO=new RelationableValueVO();
                    fromRelationableValueVO.setDiscoverSpaceName(spaceName);
                    fromRelationableValueVO.setId(fromRelationable.getId());
                    if(fromRelationable instanceof Dimension){
                        Dimension dimensionRelationable=(Dimension)fromRelationable;
                        fromRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                        fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                    }
                    if(fromRelationable instanceof Fact){
                        Fact factRelationable=(Fact)fromRelationable;
                        fromRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                        fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                    }
                    currentRelationValueVO.setFromRelationable(fromRelationableValueVO);
                }

                Relationable toRelationable=targetRelation.getToRelationable();
                if(toRelationable!=null){
                    RelationableValueVO toRelationableValueVO=new RelationableValueVO();
                    toRelationableValueVO.setDiscoverSpaceName(spaceName);
                    toRelationableValueVO.setId(toRelationable.getId());
                    if(toRelationable instanceof Dimension){
                        Dimension dimensionRelationable=(Dimension)toRelationable;
                        toRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                        toRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                    }
                    if(toRelationable instanceof Fact){
                        Fact factRelationable=(Fact)toRelationable;
                        toRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                        toRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                    }
                    currentRelationValueVO.setToRelationable(toRelationableValueVO);
                }

                List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                currentRelationValueVO.setProperties(propertyValueVOList);
                    /* for better performance but get properties's value here
                    List<Property> propertiesList=currentRelation.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                    */
                return currentRelationValueVO;
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static boolean initCountriesAndRegionsDimensionData(String discoverSpaceName,String dimensionTypePerfix){
        String continentsDimensionTypeName=dimensionTypePerfix+"_"+"geo_Continents";
        String countriesAndRegionsDimensionTypeName=dimensionTypePerfix+"_"+"geo_CountriesAndRegions";
        String geoBelongsToRelationTypeName=dimensionTypePerfix+"_"+"geo_BelongsTo";
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
            if(!targetSpace.hasDimensionType(continentsDimensionTypeName)){
                DimensionType continentsDimensionType= targetSpace.addDimensionType(continentsDimensionTypeName);
                TypeProperty continentChineseNameTypeProperty= continentsDimensionType.addTypeProperty("continentChineseName",PropertyType.STRING);
                continentChineseNameTypeProperty.setMandatory(true);
                TypeProperty continentEnglishNameTypeProperty=continentsDimensionType.addTypeProperty("continentEnglishName",PropertyType.STRING);
                continentEnglishNameTypeProperty.setMandatory(true);
                TypeProperty continentChineseFullNameTypeProperty=continentsDimensionType.addTypeProperty("continentChineseFullName",PropertyType.STRING);
                continentChineseFullNameTypeProperty.setMandatory(false);
            }
            if(!targetSpace.hasDimensionType(countriesAndRegionsDimensionTypeName)){
                DimensionType countriesAndRegionsDimensionType= targetSpace.addDimensionType(countriesAndRegionsDimensionTypeName);
                TypeProperty _2bitCodeTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("2bitCode",PropertyType.STRING);
                _2bitCodeTypeProperty.setMandatory(true);
                TypeProperty _3bitCodeTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("3bitCode",PropertyType.STRING);
                _3bitCodeTypeProperty.setMandatory(true);
                TypeProperty numberTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("number",PropertyType.STRING);
                numberTypeProperty.setMandatory(true);
                TypeProperty ISO3122_2CodeTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("ISO3122_2Code",PropertyType.STRING);
                ISO3122_2CodeTypeProperty.setMandatory(true);
                TypeProperty _EnglishNameTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("EnglishName",PropertyType.STRING);
                _EnglishNameTypeProperty.setMandatory(true);
                TypeProperty _ChineseNameTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("ChineseName",PropertyType.STRING);
                _ChineseNameTypeProperty.setMandatory(true);
                TypeProperty belongedContinentTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("belongedContinent",PropertyType.STRING);
                belongedContinentTypeProperty.setMandatory(false);
                TypeProperty capitalChineseNameTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("capitalChineseName",PropertyType.STRING);
                capitalChineseNameTypeProperty.setMandatory(false);
                TypeProperty capitalEnglishNameTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("capitalEnglishName",PropertyType.STRING);
                capitalEnglishNameTypeProperty.setMandatory(false);
            }
            if(!targetSpace.hasRelationType(geoBelongsToRelationTypeName)){
                targetSpace.addRelationType(geoBelongsToRelationTypeName);
            }

            //Add Continents dimension
            Dimension _AsiaDimension=null;
            Dimension _EuropeDimension=null;
            Dimension _NorthAmericaDimension=null;
            Dimension _SouthAmericaDimension=null;
            Dimension _AfricaDimension=null;
            Dimension _OceaniaDimension=null;
            Dimension _AntarcticaDimension=null;

            File continentsInfoFile =new File(RuntimeEnvironmentHandler.getApplicationRootPath()+"ContinentsInfo.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(continentsInfoFile),"UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String[] continentsInfoValueArray = lineTxt.split("\\|");
                Dimension targetContinentDimension=DiscoverEngineComponentFactory.createDimension(continentsDimensionTypeName);
                targetContinentDimension.setInitProperty("continentChineseName",continentsInfoValueArray[0]);
                targetContinentDimension.setInitProperty("continentEnglishName",continentsInfoValueArray[2]);
                if(!"-".equals(continentsInfoValueArray[1])){
                    targetContinentDimension.setInitProperty("continentChineseFullName",continentsInfoValueArray[1]);
                }
                targetContinentDimension=targetSpace.addDimension(targetContinentDimension);
                if("Asia".equals(continentsInfoValueArray[2])){
                    _AsiaDimension=targetContinentDimension;
                }
                if("Europe".equals(continentsInfoValueArray[2])){
                    _EuropeDimension=targetContinentDimension;
                }
                if("North America".equals(continentsInfoValueArray[2])){
                    _NorthAmericaDimension=targetContinentDimension;
                }
                if("South America".equals(continentsInfoValueArray[2])){
                    _SouthAmericaDimension=targetContinentDimension;
                }
                if("Africa".equals(continentsInfoValueArray[2])){
                    _AfricaDimension=targetContinentDimension;
                }
                if("Oceania".equals(continentsInfoValueArray[2])){
                    _OceaniaDimension=targetContinentDimension;
                }
                if("Antarctica".equals(continentsInfoValueArray[2])){
                    _AntarcticaDimension=targetContinentDimension;
                }
            }
            br.close();

            File countriesAndRegionInfoFile =new File(RuntimeEnvironmentHandler.getApplicationRootPath()+"ISO_3166_2_CountriesAndRegions.txt");
            BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(countriesAndRegionInfoFile),"UTF-8"));
            while ((lineTxt = br1.readLine()) != null) {
                String[] countriesAndRegionInfoValueArray = lineTxt.split("\\|");
                Dimension targetCountryOrRegionDimension=DiscoverEngineComponentFactory.createDimension(countriesAndRegionsDimensionTypeName);
                targetCountryOrRegionDimension.setInitProperty("2bitCode",countriesAndRegionInfoValueArray[0].trim());
                targetCountryOrRegionDimension.setInitProperty("3bitCode",countriesAndRegionInfoValueArray[1].trim());
                targetCountryOrRegionDimension.setInitProperty("number",countriesAndRegionInfoValueArray[2].trim());
                targetCountryOrRegionDimension.setInitProperty("ISO3122_2Code",countriesAndRegionInfoValueArray[3].trim());
                targetCountryOrRegionDimension.setInitProperty("EnglishName",countriesAndRegionInfoValueArray[4].trim());
                targetCountryOrRegionDimension.setInitProperty("ChineseName",countriesAndRegionInfoValueArray[5].trim());
                if(!"-".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.setInitProperty("belongedContinent",countriesAndRegionInfoValueArray[6].trim());
                }
                if(!"-".equals(countriesAndRegionInfoValueArray[7].trim())){
                    targetCountryOrRegionDimension.setInitProperty("capitalChineseName",countriesAndRegionInfoValueArray[7].trim());
                }
                if(!"-".equals(countriesAndRegionInfoValueArray[8].trim())){
                    targetCountryOrRegionDimension.setInitProperty("capitalEnglishName",countriesAndRegionInfoValueArray[8].trim());
                }

                targetCountryOrRegionDimension=targetSpace.addDimension(targetCountryOrRegionDimension);

                if("Asia".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_AsiaDimension,geoBelongsToRelationTypeName);
                }
                if("Europe".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_EuropeDimension,geoBelongsToRelationTypeName);
                }
                if("North America".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_NorthAmericaDimension,geoBelongsToRelationTypeName);
                }
                if("South America".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_SouthAmericaDimension,geoBelongsToRelationTypeName);
                }
                if("Africa".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_AfricaDimension,geoBelongsToRelationTypeName);
                }
                if("Oceania".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_OceaniaDimension,geoBelongsToRelationTypeName);
                }
                if("Antarctica".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_AntarcticaDimension,geoBelongsToRelationTypeName);
                }
            }
            br1.close();
            return true;
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean initChineseAdministrativeDivisionDimensionData(String discoverSpaceName,String dimensionTypePerfix,String chinaDimensionId){
        String countriesAndRegionsDimensionTypeName=dimensionTypePerfix+"_"+"geo_CountriesAndRegions";
        String geoBelongsToRelationTypeName=dimensionTypePerfix+"_"+"geo_BelongsTo";

        String administrativeDivisionDimensionTypeName=dimensionTypePerfix+"_"+"geo_AdministrativeDivision";
        String provincialLevelDimensionTypeName=dimensionTypePerfix+"_"+"geo_ProvincialLevel";
        String cityLevelDimensionTypeName=dimensionTypePerfix+"_"+"geo_CityLevel";
        String districtLevelDimensionTypeName=dimensionTypePerfix+"_"+"geo_DistrictLevel";
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);

            Dimension _ChinaDimension=targetSpace.getDimensionById(chinaDimensionId);
            if(_ChinaDimension==null){
                return false;
            }
            if(!_ChinaDimension.getType().equals(countriesAndRegionsDimensionTypeName)){
                return false;
            }
            if(!targetSpace.hasDimensionType(administrativeDivisionDimensionTypeName)){
                DimensionType adminDivisionDimensionType =targetSpace.addDimensionType(administrativeDivisionDimensionTypeName);
                TypeProperty divisionCodeTypeProperty= adminDivisionDimensionType.addTypeProperty("divisionCode",PropertyType.STRING);
                divisionCodeTypeProperty.setMandatory(true);
                TypeProperty divisionNameTypeProperty= adminDivisionDimensionType.addTypeProperty("divisionName",PropertyType.STRING);
                divisionNameTypeProperty.setMandatory(true);
            }

            if(!targetSpace.hasDimensionType(provincialLevelDimensionTypeName)){
                targetSpace.addChildDimensionType(provincialLevelDimensionTypeName,administrativeDivisionDimensionTypeName);
            }
            if(!targetSpace.hasDimensionType(cityLevelDimensionTypeName)){
                targetSpace.addChildDimensionType(cityLevelDimensionTypeName,administrativeDivisionDimensionTypeName);
            }
            if(!targetSpace.hasDimensionType(districtLevelDimensionTypeName)){
                targetSpace.addChildDimensionType(districtLevelDimensionTypeName,administrativeDivisionDimensionTypeName);
            }

            Map<String,Dimension> existDimensionMap=new HashMap<>();

            File continentsInfoFile =new File(RuntimeEnvironmentHandler.getApplicationRootPath()+"China_MinistryOfCivilAffairs_AdministrativeDivision_Code.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(continentsInfoFile),"UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String administrativeDivision_CodeInfoStr=lineTxt.trim();
                String[] codeInfoValueArray = administrativeDivision_CodeInfoStr.split(" ");
                int infoLength=codeInfoValueArray.length;

                if(infoLength==2){
                    //省,自治区，直辖市，港澳台，钓鱼岛等
                    String divisionCode=codeInfoValueArray[0].trim();
                    String divisionName=codeInfoValueArray[1].trim();

                    Dimension targetProvincialLevelDimension=DiscoverEngineComponentFactory.createDimension(provincialLevelDimensionTypeName);
                    targetProvincialLevelDimension.setInitProperty("divisionCode",divisionCode);
                    targetProvincialLevelDimension.setInitProperty("divisionName",divisionName);

                    targetProvincialLevelDimension=targetSpace.addDimension(targetProvincialLevelDimension);
                    targetProvincialLevelDimension.addToRelation(_ChinaDimension,geoBelongsToRelationTypeName);

                    existDimensionMap.put(divisionName,targetProvincialLevelDimension);

                }
                if(infoLength==3){
                    String divisionCode=codeInfoValueArray[0].trim();
                    String divisionNameStr=codeInfoValueArray[2].trim();
                    String[] divisionNameArray=divisionNameStr.split("-");
                    if(divisionNameArray.length==2){
                        //市级
                        String cityLevelDivisionName=divisionNameArray[1].trim();
                        String parentLevelName=divisionNameArray[0].trim();

                        Dimension targetCityLevelDimension=DiscoverEngineComponentFactory.createDimension(cityLevelDimensionTypeName);
                        targetCityLevelDimension.setInitProperty("divisionCode",divisionCode);
                        targetCityLevelDimension.setInitProperty("divisionName",cityLevelDivisionName);

                        targetCityLevelDimension=targetSpace.addDimension(targetCityLevelDimension);
                        if(existDimensionMap.get(parentLevelName)!=null){
                            targetCityLevelDimension.addToRelation(existDimensionMap.get(parentLevelName),geoBelongsToRelationTypeName);
                        }
                        existDimensionMap.put(parentLevelName+"_"+cityLevelDivisionName,targetCityLevelDimension);
                    }
                    if(divisionNameArray.length==3){
                        //区级
                        String districtLevelDivisionName=divisionNameArray[2].trim();
                        String parentLevelName=divisionNameArray[0].trim()+"_"+divisionNameArray[1].trim();

                        Dimension targetDistrictLevelDimension=DiscoverEngineComponentFactory.createDimension(districtLevelDimensionTypeName);
                        targetDistrictLevelDimension.setInitProperty("divisionCode",divisionCode);
                        targetDistrictLevelDimension.setInitProperty("divisionName",districtLevelDivisionName);

                        targetDistrictLevelDimension=targetSpace.addDimension(targetDistrictLevelDimension);
                        if(existDimensionMap.get(parentLevelName)!=null){
                            targetDistrictLevelDimension.addToRelation(existDimensionMap.get(parentLevelName),geoBelongsToRelationTypeName);
                        }
                    }
                }
            }
            br.close();
            return true;
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static long countDimensionRelationsById(String spaceName,String dimensionId,String relationType,RelationDirection relationDirection){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Dimension targetDimension = targetSpace.getDimensionById(dimensionId);
            if (targetDimension == null) {
                return Long.MIN_VALUE;
            } else {
                List<Relation> relatedDataList = targetDimension.getSpecifiedRelations(relationType, relationDirection);
                return relatedDataList.size();
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return Long.MIN_VALUE;
    }

    public static List<RelationableValueVO> getSimilarRelationableConnectedSameDimensions(String spaceName,String sourceRelationableId,List<String> relatedDimensionsList,String filteringPattern){
        List<RelationableValueVO> relationableValueVOList=new ArrayList<>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Relationable> similarRelationableList=null;
            if("ALL".equals(filteringPattern)){
                similarRelationableList=ie.discoverSimilarRelationablesRelatedToSameDimensions(sourceRelationableId,relatedDimensionsList,InformationExplorer.FilteringPattern.AND);
            }
            if("ANY".equals(filteringPattern)){
                similarRelationableList=ie.discoverSimilarRelationablesRelatedToSameDimensions(sourceRelationableId,relatedDimensionsList,InformationExplorer.FilteringPattern.OR);
            }
            if(similarRelationableList!=null) {
                for (Relationable currentRelationable : similarRelationableList) {
                    RelationableValueVO currentRelationableValueVO=new RelationableValueVO();
                    if(currentRelationable instanceof Fact){
                        Fact currentFact=(Fact)currentRelationable;
                        currentRelationableValueVO.setDiscoverSpaceName(spaceName);
                        currentRelationableValueVO.setId(currentFact.getId());
                        currentRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                        currentRelationableValueVO.setRelationableTypeName(currentFact.getType());
                        currentRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,TYPEKIND_FACT,currentFact.getType()));
                    }
                    if(currentRelationable instanceof Dimension){
                        Dimension currentDimension=(Dimension)currentRelationable;
                        currentRelationableValueVO.setDiscoverSpaceName(spaceName);
                        currentRelationableValueVO.setId(currentDimension.getId());
                        currentRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                        currentRelationableValueVO.setRelationableTypeName(currentDimension.getType());
                        currentRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,TYPEKIND_DIMENSION,currentDimension.getType()));
                    }
                    relationableValueVOList.add(currentRelationableValueVO);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return relationableValueVOList;
    }

    public static boolean hasShortestPathBetweenTwoRelationables(String spaceName, String relationable1Id, String relationable2Id){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            Stack<Relation> shortestPathRelationsStack=ie.discoverRelationablesShortestPath(relationable1Id,relationable2Id);

            if(shortestPathRelationsStack!=null){
                return true;
            }else{
                return false;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static RelationablesPathVO getShortestPathBetweenTwoRelationables(String spaceName, String relationable1Id, String relationable2Id){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            Stack<Relation> shortestPathRelationsStack=ie.discoverRelationablesShortestPath(relationable1Id,relationable2Id);

            if(shortestPathRelationsStack!=null){
                RelationablesPathVO currentRelationablesPathVO=new RelationablesPathVO();
                currentRelationablesPathVO.setEndPointRelationableAId(relationable1Id);
                currentRelationablesPathVO.setEndPointRelationableBId(relationable2Id);
                Stack<RelationValueVO> pathRelationRouteStack=new Stack();
                currentRelationablesPathVO.setPathRelationRoute(pathRelationRouteStack);
                for(int i=0;i<shortestPathRelationsStack.size();i++){
                    Relation currentRelation=shortestPathRelationsStack.elementAt(i);
                    RelationValueVO currentRelationValueVO=generateRelationValueVO(spaceName,currentRelation);
                    pathRelationRouteStack.push(currentRelationValueVO);
                }
                return currentRelationablesPathVO;
            }else{
                return null;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static List<RelationablesPathVO> getAllPathsBetweenTwoRelationables(String spaceName, String relationable1Id, String relationable2Id){
        List<RelationablesPathVO> relationablesPathList=new ArrayList<>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Stack<Relation>> relationStackList=ie.discoverRelationablesAllPaths(relationable1Id,relationable2Id);
            if(relationStackList!=null){
                for(Stack<Relation> currentpathStack:relationStackList){
                    RelationablesPathVO currentRelationablesPathVO=new RelationablesPathVO();
                    currentRelationablesPathVO.setEndPointRelationableAId(relationable1Id);
                    currentRelationablesPathVO.setEndPointRelationableBId(relationable2Id);

                    Stack<RelationValueVO> pathRelationRouteStack=new Stack();
                    currentRelationablesPathVO.setPathRelationRoute(pathRelationRouteStack);
                    for(int i=0;i<currentpathStack.size();i++){
                        Relation currentRelation=currentpathStack.elementAt(i);
                        RelationValueVO currentRelationValueVO=generateRelationValueVO(spaceName,currentRelation);
                        pathRelationRouteStack.push(currentRelationValueVO);
                    }
                    relationablesPathList.add(currentRelationablesPathVO);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return relationablesPathList;
    }

    public static List<RelationablesPathVO> getShortestPathsBetweenTwoRelationables(String spaceName, String relationable1Id, String relationable2Id,int pathNumber){
        List<RelationablesPathVO> relationablesPathList=new ArrayList<>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Stack<Relation>> relationStackList=ie.discoverRelationablesShortestPaths(relationable1Id,relationable2Id,pathNumber);
            if(relationStackList!=null){
                for(Stack<Relation> currentpathStack:relationStackList){
                    RelationablesPathVO currentRelationablesPathVO=new RelationablesPathVO();
                    currentRelationablesPathVO.setEndPointRelationableAId(relationable1Id);
                    currentRelationablesPathVO.setEndPointRelationableBId(relationable2Id);

                    Stack<RelationValueVO> pathRelationRouteStack=new Stack();
                    currentRelationablesPathVO.setPathRelationRoute(pathRelationRouteStack);
                    for(int i=0;i<currentpathStack.size();i++){
                        Relation currentRelation=currentpathStack.elementAt(i);
                        RelationValueVO currentRelationValueVO=generateRelationValueVO(spaceName,currentRelation);
                        pathRelationRouteStack.push(currentRelationValueVO);
                    }
                    relationablesPathList.add(currentRelationablesPathVO);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return relationablesPathList;
    }

    public static List<RelationablesPathVO> getLongestPathsBetweenTwoRelationables(String spaceName, String relationable1Id, String relationable2Id,int pathNumber){
        List<RelationablesPathVO> relationablesPathList=new ArrayList<>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Stack<Relation>> relationStackList=ie.discoverRelationablesLongestPaths(relationable1Id,relationable2Id,pathNumber);
            if(relationStackList!=null){
                for(Stack<Relation> currentpathStack:relationStackList){
                    RelationablesPathVO currentRelationablesPathVO=new RelationablesPathVO();
                    currentRelationablesPathVO.setEndPointRelationableAId(relationable1Id);
                    currentRelationablesPathVO.setEndPointRelationableBId(relationable2Id);

                    Stack<RelationValueVO> pathRelationRouteStack=new Stack();
                    currentRelationablesPathVO.setPathRelationRoute(pathRelationRouteStack);
                    for(int i=0;i<currentpathStack.size();i++){
                        Relation currentRelation=currentpathStack.elementAt(i);
                        RelationValueVO currentRelationValueVO=generateRelationValueVO(spaceName,currentRelation);
                        pathRelationRouteStack.push(currentRelationValueVO);
                    }
                    relationablesPathList.add(currentRelationablesPathVO);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return relationablesPathList;
    }

    public static List<RelationablesPathVO> getPathsContainPointedDatasBetweenTwoRelationables(String spaceName, String relationable1Id, String relationable2Id,List<String> pathDataIdList){
        List<RelationablesPathVO> relationablesPathList=new ArrayList<>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Stack<Relation>> relationStackList=ie.discoverPathsConnectedWithSpecifiedRelationables(relationable1Id,relationable2Id,pathDataIdList);
            if(relationStackList!=null){
                for(Stack<Relation> currentpathStack:relationStackList){
                    RelationablesPathVO currentRelationablesPathVO=new RelationablesPathVO();
                    currentRelationablesPathVO.setEndPointRelationableAId(relationable1Id);
                    currentRelationablesPathVO.setEndPointRelationableBId(relationable2Id);

                    Stack<RelationValueVO> pathRelationRouteStack=new Stack();
                    currentRelationablesPathVO.setPathRelationRoute(pathRelationRouteStack);
                    for(int i=0;i<currentpathStack.size();i++){
                        Relation currentRelation=currentpathStack.elementAt(i);
                        RelationValueVO currentRelationValueVO=generateRelationValueVO(spaceName,currentRelation);
                        pathRelationRouteStack.push(currentRelationValueVO);
                    }
                    relationablesPathList.add(currentRelationablesPathVO);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return relationablesPathList;
    }

    private static RelationValueVO generateRelationValueVO(String spaceName,Relation currentRelation){
        RelationValueVO currentRelationValueVO=new RelationValueVO();
        currentRelationValueVO.setId(currentRelation.getId());
        currentRelationValueVO.setRelationTypeName(currentRelation.getType());
        currentRelationValueVO.setDiscoverSpaceName(spaceName);

        Relationable fromRelationable=currentRelation.getFromRelationable();
        if(fromRelationable!=null){
            RelationableValueVO fromRelationableValueVO=new RelationableValueVO();
            fromRelationableValueVO.setDiscoverSpaceName(spaceName);
            fromRelationableValueVO.setId(fromRelationable.getId());
            if(fromRelationable instanceof Dimension){
                Dimension dimensionRelationable=(Dimension)fromRelationable;
                fromRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
            }
            if(fromRelationable instanceof Fact){
                Fact factRelationable=(Fact)fromRelationable;
                fromRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
            }
            currentRelationValueVO.setFromRelationable(fromRelationableValueVO);
        }

        Relationable toRelationable=currentRelation.getToRelationable();
        if(toRelationable!=null){
            RelationableValueVO toRelationableValueVO=new RelationableValueVO();
            toRelationableValueVO.setDiscoverSpaceName(spaceName);
            toRelationableValueVO.setId(toRelationable.getId());
            if(toRelationable instanceof Dimension){
                Dimension dimensionRelationable=(Dimension)toRelationable;
                toRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                toRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
            }
            if(toRelationable instanceof Fact){
                Fact factRelationable=(Fact)toRelationable;
                toRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                toRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
            }
            currentRelationValueVO.setToRelationable(toRelationableValueVO);
        }

        List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
        currentRelationValueVO.setProperties(propertyValueVOList);
        /*
        List<Property> propertiesList=currentRelation.getProperties();
        if(propertiesList!=null){
            for(Property currentProperty:propertiesList){
                if(currentProperty.getPropertyType()!=null){
                    PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                    currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                    currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                    currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                    propertyValueVOList.add(currentPropertyValueVO);
                }
            }
        }
        */
        return currentRelationValueVO;
    }

    public static boolean initMetaConfigDiscoverSpace(){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        boolean metaConfigSpaceAlreadyExist=DiscoverEngineComponentFactory.checkDiscoverSpaceExistence(metaConfigSpaceName);
        if(!metaConfigSpaceAlreadyExist){
            return DiscoverEngineComponentFactory.createInfoDiscoverSpace(metaConfigSpaceName);
        }else{
            return true;
        }
    }

    private static boolean recordTypeKindAliasName(String spaceName,String typeKind,String typeName,String typeAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(TYPEKIND_AliasNameFactType)){
                FactType typeKindAliasNameFactType=metaConfigSpace.addFactType(TYPEKIND_AliasNameFactType);
                TypeProperty discoverSpaceProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_DiscoverSpace,PropertyType.STRING);
                discoverSpaceProperty.setMandatory(true);
                TypeProperty typeKindProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeKind,PropertyType.STRING);
                typeKindProperty.setMandatory(true);
                TypeProperty typeNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeName,PropertyType.STRING);
                typeNameProperty.setMandatory(true);
                TypeProperty typeAliasNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeAliasName,PropertyType.STRING);
                typeAliasNameProperty.setMandatory(true);
            }
            Fact typeKind_AliasNameFact=DiscoverEngineComponentFactory.createFact(TYPEKIND_AliasNameFactType);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_DiscoverSpace,spaceName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeKind,typeKind);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeName,typeName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeAliasName,typeAliasName);
            Fact resultRecord=metaConfigSpace.addFact(typeKind_AliasNameFact);
            if(resultRecord!=null){
                String recordKey=spaceName+"_"+typeKind+"_"+typeName;
                TYPEKIND_AliasNameMap.put(recordKey,typeAliasName);
                return true;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    private static void deleteTypeKindAliasName(String spaceName,String typeKind,String typeName) {
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEKIND_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1000000);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typeAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(typeAliasRecordFact!=null) {
                for (Fact currentRecordFact : typeAliasRecordFact) {
                    metaConfigSpace.removeFact(currentRecordFact.getId());
                }
            }
            String recordKey=spaceName+"_"+typeKind+"_"+typeName;
            TYPEKIND_AliasNameMap.remove(recordKey);

            typeKindRecordEP.setType(TYPEPROPERTY_AliasNameFactType);
            List<Fact> propertyAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(propertyAliasRecordFact!=null) {
                for (Fact currentRecordFact : propertyAliasRecordFact) {
                    String propertyName=currentRecordFact.getProperty(MetaConfig_PropertyName_TypePropertyName).getPropertyValue().toString();
                    String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+propertyName;
                    metaConfigSpace.removeFact(currentRecordFact.getId());
                    TypeProperty_AliasNameMap.remove(propertyRecordKey);
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
    }

    public static boolean updateTypeKindAliasName(String spaceName,String typeKind,String typeName,String typeAliasName) {
        boolean existAliasName=false;
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEKIND_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1000000);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typeAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(typeAliasRecordFact!=null) {
                if(typeAliasRecordFact.size()>0){
                    existAliasName=true;
                }
                for (Fact currentRecordFact : typeAliasRecordFact) {
                    currentRecordFact.updateProperty(MetaConfig_PropertyName_TypeAliasName,typeAliasName);
                }
            }
            String recordKey=spaceName+"_"+typeKind+"_"+typeName;
            TYPEKIND_AliasNameMap.remove(recordKey);
            TYPEKIND_AliasNameMap.put(recordKey,typeAliasName);
            if(existAliasName){
                return true;
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        if(!existAliasName){
            return recordTypeKindAliasName(spaceName,typeKind,typeName,typeAliasName);
        }else{
            return false;
        }
    }

    private static boolean recordTypePropertyAliasName(String spaceName,String typeKind,String typeName,String typePropertyName,String typePropertyAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(TYPEPROPERTY_AliasNameFactType)){
                FactType typeKindAliasNameFactType=metaConfigSpace.addFactType(TYPEPROPERTY_AliasNameFactType);
                TypeProperty discoverSpaceProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_DiscoverSpace,PropertyType.STRING);
                discoverSpaceProperty.setMandatory(true);
                TypeProperty typeKindProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeKind,PropertyType.STRING);
                typeKindProperty.setMandatory(true);
                TypeProperty typeNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeName,PropertyType.STRING);
                typeNameProperty.setMandatory(true);
                TypeProperty typePropertyNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypePropertyName,PropertyType.STRING);
                typePropertyNameProperty.setMandatory(true);
                TypeProperty typePropertyAliasNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypePropertyAliasName,PropertyType.STRING);
                typePropertyAliasNameProperty.setMandatory(true);

            }
            Fact typeKind_AliasNameFact=DiscoverEngineComponentFactory.createFact(TYPEPROPERTY_AliasNameFactType);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_DiscoverSpace,spaceName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeKind,typeKind);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeName,typeName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypePropertyName,typePropertyName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypePropertyAliasName,typePropertyAliasName);
            Fact resultRecord=metaConfigSpace.addFact(typeKind_AliasNameFact);
            if(resultRecord!=null){
                String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
                TypeProperty_AliasNameMap.put(propertyRecordKey,typePropertyAliasName);
                return true;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    private static void deleteTypePropertyAliasName(String spaceName,String typeKind,String typeName,String typePropertyName) {
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEPROPERTY_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypePropertyName, typePropertyName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1000000);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typeAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(typeAliasRecordFact!=null) {
                for (Fact currentRecordFact : typeAliasRecordFact) {
                    metaConfigSpace.removeFact(currentRecordFact.getId());
                }
            }
            String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
            TypeProperty_AliasNameMap.remove(propertyRecordKey);
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
    }

    public static boolean updateTypePropertyAliasName(String spaceName,String typeKind,String typeName,String typePropertyName,String typePropertyAliasName) {
        boolean existAliasName=false;
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEPROPERTY_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypePropertyName, typePropertyName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1000000);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typeAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(typeAliasRecordFact!=null) {
                if(typeAliasRecordFact.size()>0){
                    existAliasName=true;
                }
                for (Fact currentRecordFact : typeAliasRecordFact) {
                    currentRecordFact.updateProperty(MetaConfig_PropertyName_TypePropertyAliasName,typePropertyAliasName);
                }
            }
            String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
            TypeProperty_AliasNameMap.remove(propertyRecordKey);
            TypeProperty_AliasNameMap.put(propertyRecordKey,typePropertyAliasName);
            if(existAliasName){
                return true;
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        if(!existAliasName){
            return recordTypePropertyAliasName(spaceName,typeKind,typeName,typePropertyName,typePropertyAliasName);
        }else{
            return false;
        }
    }

    public static String getTypeKindAliasName(String spaceName,String typeKind,String typeName){
        String typeKindRecordKey=spaceName+"_"+typeKind+"_"+typeName;
        if(TYPEKIND_AliasNameMap.get(typeKindRecordKey)!=null){
            return TYPEKIND_AliasNameMap.get(typeKindRecordKey);
        }else{
            return "";
        }
    }

    public static String getTypePropertyAliasName(String spaceName,String typeKind,String typeName,String typePropertyName) {
        String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
        if(TypeProperty_AliasNameMap.get(propertyRecordKey)!=null){
            return TypeProperty_AliasNameMap.get(propertyRecordKey);
        }else{
            return "";
        }
    }

    public static void clearItemAliasNameCache(){
        TYPEKIND_AliasNameMap.clear();
        TypeProperty_AliasNameMap.clear();
        CustomProperty_AliasNameMap.clear();
    }

    public static void refreshItemAliasNameCache(){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(TYPEKIND_AliasNameFactType)){
                ExploreParameters typeKindRecordEP = new ExploreParameters();
                typeKindRecordEP.setType(TYPEKIND_AliasNameFactType);
                typeKindRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> typeAliasRecordFactsList = ie.discoverFacts(typeKindRecordEP);
                if(typeAliasRecordFactsList!=null){
                    String spaceName=null;
                    String typeKind=null;
                    String typeName=null;
                    for(Fact currentFact:typeAliasRecordFactsList){
                        Property _DiscoverSpace=currentFact.getProperty(MetaConfig_PropertyName_DiscoverSpace);
                        if(_DiscoverSpace!=null){
                            spaceName=_DiscoverSpace.getPropertyValue().toString();
                        }else{
                            spaceName=null;
                        }
                        Property _TypeKind=currentFact.getProperty(MetaConfig_PropertyName_TypeKind);
                        if(_TypeKind!=null){
                            typeKind=_TypeKind.getPropertyValue().toString();
                        }else{
                            typeKind=null;
                        }
                        Property _TypeName=currentFact.getProperty(MetaConfig_PropertyName_TypeName);
                        if(_TypeName!=null){
                            typeName=_TypeName.getPropertyValue().toString();
                        }else{
                            typeName=null;
                        }
                        Property _TypeAliasName=currentFact.getProperty(MetaConfig_PropertyName_TypeAliasName);
                        if(_TypeAliasName!=null){
                            String typeKindRecordKey=spaceName+"_"+typeKind+"_"+typeName;
                            String typeAliasName=_TypeAliasName.getPropertyValue().toString();
                            if(TYPEKIND_AliasNameMap.containsKey(typeKindRecordKey)) {
                                TYPEKIND_AliasNameMap.remove(typeKindRecordKey);
                            }
                            TYPEKIND_AliasNameMap.put(typeKindRecordKey,typeAliasName);
                        }
                    }
                }
            }

            if(metaConfigSpace.hasFactType(TYPEPROPERTY_AliasNameFactType)){
                ExploreParameters typeKindRecordEP = new ExploreParameters();
                typeKindRecordEP.setType(TYPEPROPERTY_AliasNameFactType);
                typeKindRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> typePropertyAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
                if(typePropertyAliasRecordFact!=null) {
                    String spaceName=null;
                    String typeKind=null;
                    String typeName=null;
                    String typePropertyName=null;
                    for(Fact currentFact:typePropertyAliasRecordFact){
                        Property _DiscoverSpace=currentFact.getProperty(MetaConfig_PropertyName_DiscoverSpace);
                        if(_DiscoverSpace!=null){
                            spaceName=_DiscoverSpace.getPropertyValue().toString();
                        }else{
                            spaceName=null;
                        }
                        Property _TypeKind=currentFact.getProperty(MetaConfig_PropertyName_TypeKind);
                        if(_TypeKind!=null){
                            typeKind=_TypeKind.getPropertyValue().toString();
                        }else{
                            typeKind=null;
                        }
                        Property _TypeName=currentFact.getProperty(MetaConfig_PropertyName_TypeName);
                        if(_TypeName!=null){
                            typeName=_TypeName.getPropertyValue().toString();
                        }else{
                            typeName=null;
                        }
                        Property _TypePropertyName=currentFact.getProperty(MetaConfig_PropertyName_TypePropertyName);
                        if(_TypePropertyName!=null){
                            typePropertyName=_TypePropertyName.getPropertyValue().toString();
                        }else{
                            typePropertyName=null;
                        }
                        Property _TypePropertyAliasName=currentFact.getProperty(MetaConfig_PropertyName_TypePropertyAliasName);
                        if(_TypePropertyAliasName!=null){
                            String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
                            String typePropertyAliasName=_TypePropertyAliasName.getPropertyValue().toString();
                            if(TypeProperty_AliasNameMap.containsKey(propertyRecordKey)) {
                                TypeProperty_AliasNameMap.remove(propertyRecordKey);
                            }
                            TypeProperty_AliasNameMap.put(propertyRecordKey,typePropertyAliasName);
                        }
                    }
                }
            }

            if(metaConfigSpace.hasFactType(CUSTOMPROPERTY_AliasNameFactType)){
                ExploreParameters typeKindRecordEP = new ExploreParameters();
                typeKindRecordEP.setType(CUSTOMPROPERTY_AliasNameFactType);
                typeKindRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> typePropertyAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
                if(typePropertyAliasRecordFact!=null) {
                    String spaceName=null;
                    String customPropertyName=null;
                    String customPropertyType=null;
                    String customPropertyAliasName=null;
                    for(Fact currentFact:typePropertyAliasRecordFact){
                        Property _DiscoverSpace=currentFact.getProperty(MetaConfig_PropertyName_DiscoverSpace);
                        if(_DiscoverSpace!=null){
                            spaceName=_DiscoverSpace.getPropertyValue().toString();
                        }else{
                            spaceName=null;
                        }
                        Property _CustomPropertyName=currentFact.getProperty(MetaConfig_PropertyName_CustomPropertyName);
                        if(_CustomPropertyName!=null){
                            customPropertyName=_CustomPropertyName.getPropertyValue().toString();
                        }else{
                            customPropertyName=null;
                        }
                        Property _CustomPropertyType=currentFact.getProperty(MetaConfig_PropertyName_CustomPropertyType);
                        if(_CustomPropertyType!=null){
                            customPropertyType=_CustomPropertyType.getPropertyValue().toString();
                        }else{
                            customPropertyType=null;
                        }
                        Property _CustomPropertyAliasName=currentFact.getProperty(MetaConfig_PropertyName_CustomPropertyAliasName);
                        if(_CustomPropertyAliasName!=null){
                            customPropertyAliasName=_CustomPropertyAliasName.getPropertyValue().toString();
                        }else{
                            customPropertyAliasName=null;
                        }
                        if(_CustomPropertyAliasName!=null){
                            String propertyRecordKey=spaceName+"_"+customPropertyName+"_"+customPropertyType;
                            String typePropertyAliasName=customPropertyAliasName;
                            if(CustomProperty_AliasNameMap.containsKey(propertyRecordKey)) {
                                CustomProperty_AliasNameMap.remove(propertyRecordKey);
                            }
                            CustomProperty_AliasNameMap.put(propertyRecordKey,typePropertyAliasName);
                        }
                    }
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
    }

    public static boolean checkCustomPropertyAliasNameExistence(String spaceName, String customPropertyName,String customPropertyType){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(CUSTOMPROPERTY_AliasNameFactType)){
                ExploreParameters customPropertyAliasNameRecordEP = new ExploreParameters();
                customPropertyAliasNameRecordEP.setType(CUSTOMPROPERTY_AliasNameFactType);
                customPropertyAliasNameRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
                customPropertyAliasNameRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_CustomPropertyName, customPropertyName), ExploreParameters.FilteringLogic.AND);
                customPropertyAliasNameRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_CustomPropertyType, customPropertyType), ExploreParameters.FilteringLogic.AND);
                customPropertyAliasNameRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> customPropertyAliasRecordFact = ie.discoverFacts(customPropertyAliasNameRecordEP);
                if(customPropertyAliasRecordFact!=null&&customPropertyAliasRecordFact.size()>0){
                    return true;
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
                e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
                e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                    metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean recordCustomPropertyAliasName(String spaceName, String customPropertyName,String customPropertyType,String customPropertyAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(CUSTOMPROPERTY_AliasNameFactType)){
                FactType customPropertyAliasNameFactType=metaConfigSpace.addFactType(CUSTOMPROPERTY_AliasNameFactType);
                TypeProperty discoverSpaceProperty=customPropertyAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_DiscoverSpace,PropertyType.STRING);
                discoverSpaceProperty.setMandatory(true);
                TypeProperty customPropertyNameProperty=customPropertyAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_CustomPropertyName,PropertyType.STRING);
                customPropertyNameProperty.setMandatory(true);
                TypeProperty customPropertyTypeProperty=customPropertyAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_CustomPropertyType,PropertyType.STRING);
                customPropertyTypeProperty.setMandatory(true);
                TypeProperty customPropertyAliasNameNameProperty=customPropertyAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_CustomPropertyAliasName,PropertyType.STRING);
                customPropertyAliasNameNameProperty.setMandatory(true);
            }
            Fact typeKind_AliasNameFact=DiscoverEngineComponentFactory.createFact(CUSTOMPROPERTY_AliasNameFactType);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_DiscoverSpace,spaceName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_CustomPropertyName,customPropertyName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_CustomPropertyType,customPropertyType);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_CustomPropertyAliasName,customPropertyAliasName);
            Fact resultRecord=metaConfigSpace.addFact(typeKind_AliasNameFact);
            if(resultRecord!=null){
                String propertyRecordKey=spaceName+"_"+customPropertyName+"_"+customPropertyType;
                CustomProperty_AliasNameMap.put(propertyRecordKey,customPropertyAliasName);
                return true;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static List<CustomPropertyAliasNameVO> queryCustomPropertyAliasNames(String discoverSpaceName){
        List<CustomPropertyAliasNameVO> resultList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(CUSTOMPROPERTY_AliasNameFactType)){
                ExploreParameters customPropertyAliasNameRecordEP = new ExploreParameters();
                customPropertyAliasNameRecordEP.setType(CUSTOMPROPERTY_AliasNameFactType);
                customPropertyAliasNameRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, discoverSpaceName));
                customPropertyAliasNameRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> customPropertyAliasRecordFact = ie.discoverFacts(customPropertyAliasNameRecordEP);
                if(customPropertyAliasRecordFact!=null){
                    String spaceName=null;
                    String customPropertyName=null;
                    String customPropertyType=null;
                    String customPropertyAliasName=null;
                    for(Fact currentFact:customPropertyAliasRecordFact){
                        Property _DiscoverSpace=currentFact.getProperty(MetaConfig_PropertyName_DiscoverSpace);
                        if(_DiscoverSpace!=null){
                            spaceName=_DiscoverSpace.getPropertyValue().toString();
                        }else{
                            spaceName=null;
                        }
                        Property _CustomPropertyName=currentFact.getProperty(MetaConfig_PropertyName_CustomPropertyName);
                        if(_CustomPropertyName!=null){
                            customPropertyName=_CustomPropertyName.getPropertyValue().toString();
                        }else{
                            customPropertyName=null;
                        }
                        Property _CustomPropertyType=currentFact.getProperty(MetaConfig_PropertyName_CustomPropertyType);
                        if(_CustomPropertyType!=null){
                            customPropertyType=_CustomPropertyType.getPropertyValue().toString();
                        }else{
                            customPropertyType=null;
                        }
                        Property _CustomPropertyAliasName=currentFact.getProperty(MetaConfig_PropertyName_CustomPropertyAliasName);
                        if(_CustomPropertyAliasName!=null){
                            customPropertyAliasName=_CustomPropertyAliasName.getPropertyValue().toString();
                        }else{
                            customPropertyAliasName=null;
                        }
                        if(_CustomPropertyAliasName!=null){
                            CustomPropertyAliasNameVO currentCustomPropertyAliasNameVO=new CustomPropertyAliasNameVO();
                            currentCustomPropertyAliasNameVO.setDiscoverSpaceName(spaceName);
                            currentCustomPropertyAliasNameVO.setCustomPropertyName(customPropertyName);
                            currentCustomPropertyAliasNameVO.setCustomPropertyType(customPropertyType);
                            currentCustomPropertyAliasNameVO.setCustomPropertyAliasName(customPropertyAliasName);
                            resultList.add(currentCustomPropertyAliasNameVO);
                        }
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return resultList;
    }

    public static boolean deleteCustomPropertyAliasName(String spaceName,String customPropertyName,String customPropertyType) {
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(CUSTOMPROPERTY_AliasNameFactType)){
                ExploreParameters customPropertyAliasNameRecordEP = new ExploreParameters();
                customPropertyAliasNameRecordEP.setType(CUSTOMPROPERTY_AliasNameFactType);
                customPropertyAliasNameRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
                customPropertyAliasNameRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_CustomPropertyName, customPropertyName), ExploreParameters.FilteringLogic.AND);
                customPropertyAliasNameRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_CustomPropertyType, customPropertyType), ExploreParameters.FilteringLogic.AND);
                customPropertyAliasNameRecordEP.setResultNumber(1000000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> customPropertyAliasNameRecordFact = ie.discoverFacts(customPropertyAliasNameRecordEP);
                if(customPropertyAliasNameRecordFact!=null) {
                    for (Fact currentRecordFact : customPropertyAliasNameRecordFact) {
                        metaConfigSpace.removeFact(currentRecordFact.getId());
                    }
                }
                String propertyRecordKey=spaceName+"_"+customPropertyName+"_"+customPropertyType;
                CustomProperty_AliasNameMap.remove(propertyRecordKey);
                return true;
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static String getCustomPropertyAliasName(String spaceName,String customPropertyName,String customPropertyType) {
        String propertyRecordKey=spaceName+"_"+customPropertyName+"_"+customPropertyType;
        if(CustomProperty_AliasNameMap.get(propertyRecordKey)!=null){
            return CustomProperty_AliasNameMap.get(propertyRecordKey);
        }else{
            return "";
        }
    }

    private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();

    public static File generateCustomPropertyAliasNamesJsonFile(String discoverSpaceName){
        File customPropertyAliasNamesFile=new File(tempFileDir+discoverSpaceName+"_"+"CustomPropertyAliasNames.json");
        List<CustomPropertyAliasNameVO> aliasNamesList=queryCustomPropertyAliasNames(discoverSpaceName);
        ObjectMapper mapper = new ObjectMapper();
        //当找不到对应的序列化器时 忽略此字段
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //使Jackson JSON支持Unicode编码非ASCII字符
        CustomSerializerFactory serializerFactory= new CustomSerializerFactory();
        serializerFactory.addSpecificMapping(String.class, new StringUnicodeSerializer());
        mapper.setSerializerFactory(serializerFactory);
        //支持结束
        String json = null;
        try {
            json = mapper.writeValueAsString(aliasNamesList);
            try(PrintStream ps = new PrintStream(customPropertyAliasNamesFile)) {
                ps.println(json);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customPropertyAliasNamesFile;
    }

    public static boolean importCustomPropertyAliasNamesFromJsonFile(String discoverSpaceName,String aliasNameDataFileLocation,String existAliasNameHandleMethod){
        File file = new File(aliasNameDataFileLocation);
        if(file.exists()){
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode dataNode=mapper.readTree(file);
                if(dataNode.isArray()){
                    Iterator<JsonNode> aliasDataIterator = dataNode.getElements();
                    while(aliasDataIterator.hasNext()){
                        JsonNode currentAliasDefinition=aliasDataIterator.next();
                        if(!currentAliasDefinition.isArray()){
                            JsonNode spaceNameProp=currentAliasDefinition.get("discoverSpaceName");
                            JsonNode customPropertyNameProp=currentAliasDefinition.get("customPropertyName");
                            JsonNode customPropertyTypeProp=currentAliasDefinition.get("customPropertyType");
                            JsonNode customPropertyAliasNameProp=currentAliasDefinition.get("customPropertyAliasName");
                            if(spaceNameProp!=null&&customPropertyNameProp!=null&&customPropertyTypeProp!=null&&customPropertyAliasNameProp!=null){
                                String spaceName=spaceNameProp.getTextValue();
                                if(spaceName.equals(discoverSpaceName)){
                                    String customPropertyName=customPropertyNameProp.getTextValue();
                                    String customPropertyType=customPropertyTypeProp.getTextValue();
                                    String customPropertyAliasName=customPropertyAliasNameProp.getTextValue();
                                    if(getCustomPropertyAliasName(spaceName,customPropertyName,customPropertyType)!=null){
                                        if(ExistedPropertyAliasNameHandleMethod_REPLACE.equals(existAliasNameHandleMethod)){
                                            deleteCustomPropertyAliasName(spaceName, customPropertyName,customPropertyType);
                                            recordCustomPropertyAliasName(spaceName, customPropertyName,customPropertyType,customPropertyAliasName);
                                        }
                                    }else{
                                        recordCustomPropertyAliasName(spaceName, customPropertyName,customPropertyType,customPropertyAliasName);
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }else{
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    FileUtils.forceDelete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean applyBusinessSolution(String discoverSpaceName,String businessSolutionName){
        if(!checkDiscoverSpaceExistence(discoverSpaceName)){
            return false;
        }
        if(!BusinessSolutionOperationUtil.checkBusinessSolutionExistence(businessSolutionName)){
            return false;
        }
        //Apply Fact Type Data
        List<FactTypeDefinitionVO> factTypeDefinitionList=BusinessSolutionOperationUtil.getBusinessSolutionFactTypeList(businessSolutionName);
        if(factTypeDefinitionList!=null) {
            InfoDiscoverSpace targetSpace=null;
            try {
                targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
                for (FactTypeDefinitionVO currentFactTypeDefinitionVO:factTypeDefinitionList) {
                    String factTypeName=currentFactTypeDefinitionVO.getTypeName();
                    if(!targetSpace.hasFactType(factTypeName)){
                        targetSpace.addFactType(factTypeName);
                    }
                }
            } catch (InfoDiscoveryEngineDataMartException e) {
                e.printStackTrace();
            }finally {
                if(targetSpace!=null){
                    targetSpace.closeSpace();
                }
            }
            for (FactTypeDefinitionVO currentFactTypeDefinitionVO:factTypeDefinitionList) {
                String factTypeAliasName=currentFactTypeDefinitionVO.getTypeAliasName();
                String factTypeName=currentFactTypeDefinitionVO.getTypeName();
                recordTypeKindAliasName(discoverSpaceName,TYPEKIND_FACT,factTypeName,factTypeAliasName);
            }
            Map<String, List<SolutionTypePropertyTypeDefinitionVO>> factTypePropertiesMap=new HashMap<>();
            for(FactTypeDefinitionVO currentFactTypeDefinitionVO:factTypeDefinitionList) {
               String factType= currentFactTypeDefinitionVO.getTypeName();
                List<SolutionTypePropertyTypeDefinitionVO> factPropertiesList=BusinessSolutionOperationUtil.getSolutionTypePropertiesInfo(businessSolutionName,InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,factType);
                factTypePropertiesMap.put(factType,factPropertiesList);
                if(factPropertiesList!=null){
                    targetSpace=null;
                    try {
                        targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
                        FactType targetFactType=targetSpace.getFactType(factType);
                        if(targetFactType!=null){
                            for (SolutionTypePropertyTypeDefinitionVO currentSolutionTypePropertyTypeDefinitionVO:factPropertiesList){
                                String propertyName=currentSolutionTypePropertyTypeDefinitionVO.getPropertyName();
                                PropertyType propertyType=getPropertyTypeByCode(currentSolutionTypePropertyTypeDefinitionVO.getPropertyType());
                                TypeProperty currentTypeProperty=targetFactType.addTypeProperty(propertyName,propertyType);
                                currentTypeProperty.setMandatory(currentSolutionTypePropertyTypeDefinitionVO.isMandatory());
                                currentTypeProperty.setNullable(currentSolutionTypePropertyTypeDefinitionVO.isNullable());
                                currentTypeProperty.setReadOnly(currentSolutionTypePropertyTypeDefinitionVO.isReadOnly());
                            }
                        }
                    } catch (InfoDiscoveryEngineRuntimeException e) {
                        e.printStackTrace();
                    } finally {
                        if(targetSpace!=null){
                            targetSpace.closeSpace();
                        }
                    }
                }
            }
            Set<String> factTypeSet=factTypePropertiesMap.keySet();
            Iterator<String> factTypeIterator= factTypeSet.iterator();
            while(factTypeIterator.hasNext()){
                String currentFactType=factTypeIterator.next();
                List<SolutionTypePropertyTypeDefinitionVO> factTypePropertiesList=factTypePropertiesMap.get(currentFactType);
                if(factTypePropertiesList!=null){
                    for(SolutionTypePropertyTypeDefinitionVO currentSolutionTypePropertyTypeDefinitionVO:factTypePropertiesList){
                        String propertyName=currentSolutionTypePropertyTypeDefinitionVO.getPropertyName();
                        String propertyAlias=currentSolutionTypePropertyTypeDefinitionVO.getPropertyAliasName();
                        recordTypePropertyAliasName(discoverSpaceName,TYPEKIND_FACT,currentFactType,propertyName,propertyAlias);
                    }
                }
            }
        }
        //Apply Dimension Type Data
        List<DimensionTypeDefinitionVO> dimensionTypeDefinitionList=BusinessSolutionOperationUtil.getBusinessSolutionDimensionTypeList(businessSolutionName);
        if(dimensionTypeDefinitionList!=null) {
            List<String> alreadyRenderedTypeList=new ArrayList<>();
            InfoDiscoverSpace targetSpace=null;
            try {
                targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
                for(DimensionTypeDefinitionVO currentDimensionTypeDefinitionVO:dimensionTypeDefinitionList){
                    applyDimensionTypeData(targetSpace,alreadyRenderedTypeList,InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME,currentDimensionTypeDefinitionVO,dimensionTypeDefinitionList);
                }
            } catch (InfoDiscoveryEngineDataMartException e) {
                e.printStackTrace();
            }finally {
                if(targetSpace!=null){
                    targetSpace.closeSpace();
                }
            }
            for (DimensionTypeDefinitionVO currentDimensionTypeDefinitionVO:dimensionTypeDefinitionList) {
                String dimensionTypeAliasName=currentDimensionTypeDefinitionVO.getTypeAliasName();
                String dimensionTypeName=currentDimensionTypeDefinitionVO.getTypeName();
                recordTypeKindAliasName(discoverSpaceName,TYPEKIND_DIMENSION,dimensionTypeName,dimensionTypeAliasName);
            }
            Map<String, List<SolutionTypePropertyTypeDefinitionVO>> dimensionTypePropertiesMap=new HashMap<>();
            for(DimensionTypeDefinitionVO currentDimensionTypeDefinitionVO:dimensionTypeDefinitionList) {
                String dimensionType= currentDimensionTypeDefinitionVO.getTypeName();
                List<SolutionTypePropertyTypeDefinitionVO> dimensionPropertiesList=BusinessSolutionOperationUtil.getSolutionTypePropertiesInfo(businessSolutionName,InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION,dimensionType);
                dimensionTypePropertiesMap.put(dimensionType,dimensionPropertiesList);
                if(dimensionPropertiesList!=null){
                    targetSpace=null;
                    try {
                        targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
                        DimensionType targetDimensionType=targetSpace.getDimensionType(dimensionType);
                        if(targetDimensionType!=null){
                            for (SolutionTypePropertyTypeDefinitionVO currentSolutionTypePropertyTypeDefinitionVO:dimensionPropertiesList){
                                String propertyName=currentSolutionTypePropertyTypeDefinitionVO.getPropertyName();
                                PropertyType propertyType=getPropertyTypeByCode(currentSolutionTypePropertyTypeDefinitionVO.getPropertyType());
                                TypeProperty currentTypeProperty=targetDimensionType.addTypeProperty(propertyName,propertyType);
                                currentTypeProperty.setMandatory(currentSolutionTypePropertyTypeDefinitionVO.isMandatory());
                                currentTypeProperty.setNullable(currentSolutionTypePropertyTypeDefinitionVO.isNullable());
                                currentTypeProperty.setReadOnly(currentSolutionTypePropertyTypeDefinitionVO.isReadOnly());
                            }
                        }
                    } catch (InfoDiscoveryEngineRuntimeException e) {
                        e.printStackTrace();
                    } finally {
                        if(targetSpace!=null){
                            targetSpace.closeSpace();
                        }
                    }
                }
            }
            Set<String> dimensionTypeSet=dimensionTypePropertiesMap.keySet();
            Iterator<String> dimensionTypeIterator= dimensionTypeSet.iterator();
            while(dimensionTypeIterator.hasNext()){
                String currentDimensionType=dimensionTypeIterator.next();
                List<SolutionTypePropertyTypeDefinitionVO> dimensionTypePropertiesList=dimensionTypePropertiesMap.get(currentDimensionType);
                if(dimensionTypePropertiesList!=null){
                    for(SolutionTypePropertyTypeDefinitionVO currentSolutionTypePropertyTypeDefinitionVO:dimensionTypePropertiesList){
                        String propertyName=currentSolutionTypePropertyTypeDefinitionVO.getPropertyName();
                        String propertyAlias=currentSolutionTypePropertyTypeDefinitionVO.getPropertyAliasName();
                        recordTypePropertyAliasName(discoverSpaceName,TYPEKIND_DIMENSION,currentDimensionType,propertyName,propertyAlias);
                    }
                }
            }
        }
        //Apply Relation Type Data
        List<RelationTypeDefinitionVO> relationDefinitionList=BusinessSolutionOperationUtil.getBusinessSolutionRelationTypeList(businessSolutionName);
        if(relationDefinitionList!=null) {
            List<String> alreadyRenderedTypeList=new ArrayList<>();
            InfoDiscoverSpace targetSpace=null;
            try {
                targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
                for(RelationTypeDefinitionVO currentRelationTypeDefinitionVO:relationDefinitionList){
                    applyRelationTypeData(targetSpace,alreadyRenderedTypeList,InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME,currentRelationTypeDefinitionVO,relationDefinitionList);
                }
            } catch (InfoDiscoveryEngineDataMartException e) {
                e.printStackTrace();
            }finally {
                if(targetSpace!=null){
                    targetSpace.closeSpace();
                }
            }
            for (RelationTypeDefinitionVO currentRelationTypeDefinitionVO:relationDefinitionList) {
                String relationTypeAliasName=currentRelationTypeDefinitionVO.getTypeAliasName();
                String relationTypeName=currentRelationTypeDefinitionVO.getTypeName();
                recordTypeKindAliasName(discoverSpaceName,TYPEKIND_RELATION,relationTypeName,relationTypeAliasName);
            }
            Map<String, List<SolutionTypePropertyTypeDefinitionVO>> relationTypePropertiesMap=new HashMap<>();
            for(RelationTypeDefinitionVO currentRelationTypeDefinitionVO:relationDefinitionList) {
                String relationType= currentRelationTypeDefinitionVO.getTypeName();
                List<SolutionTypePropertyTypeDefinitionVO> relationPropertiesList=BusinessSolutionOperationUtil.getSolutionTypePropertiesInfo(businessSolutionName,InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,relationType);
                relationTypePropertiesMap.put(relationType,relationPropertiesList);
                if(relationPropertiesList!=null){
                    targetSpace=null;
                    try {
                        targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
                        RelationType targetRelationType=targetSpace.getRelationType(relationType);
                        if(targetRelationType!=null){
                            for (SolutionTypePropertyTypeDefinitionVO currentSolutionTypePropertyTypeDefinitionVO:relationPropertiesList){
                                String propertyName=currentSolutionTypePropertyTypeDefinitionVO.getPropertyName();
                                PropertyType propertyType=getPropertyTypeByCode(currentSolutionTypePropertyTypeDefinitionVO.getPropertyType());
                                TypeProperty currentTypeProperty=targetRelationType.addTypeProperty(propertyName,propertyType);
                                currentTypeProperty.setMandatory(currentSolutionTypePropertyTypeDefinitionVO.isMandatory());
                                currentTypeProperty.setNullable(currentSolutionTypePropertyTypeDefinitionVO.isNullable());
                                currentTypeProperty.setReadOnly(currentSolutionTypePropertyTypeDefinitionVO.isReadOnly());
                            }
                        }
                    } catch (InfoDiscoveryEngineRuntimeException e) {
                        e.printStackTrace();
                    } finally {
                        if(targetSpace!=null){
                            targetSpace.closeSpace();
                        }
                    }
                }
            }
            Set<String> relationTypeSet=relationTypePropertiesMap.keySet();
            Iterator<String> relationTypeIterator= relationTypeSet.iterator();
            while(relationTypeIterator.hasNext()){
                String currentRelationType=relationTypeIterator.next();
                List<SolutionTypePropertyTypeDefinitionVO> relationTypePropertiesList=relationTypePropertiesMap.get(currentRelationType);
                if(relationTypePropertiesList!=null){
                    for(SolutionTypePropertyTypeDefinitionVO currentSolutionTypePropertyTypeDefinitionVO:relationTypePropertiesList){
                        String propertyName=currentSolutionTypePropertyTypeDefinitionVO.getPropertyName();
                        String propertyAlias=currentSolutionTypePropertyTypeDefinitionVO.getPropertyAliasName();
                        recordTypePropertyAliasName(discoverSpaceName,TYPEKIND_RELATION,currentRelationType,propertyName,propertyAlias);
                    }
                }
            }
            //Apply Custom Property Alias Data
            List<CustomPropertyAliasDefinitionVO> customPropertyAliasNameList=BusinessSolutionOperationUtil.getSolutionCustomPropertyAliasNames(businessSolutionName);
            if(customPropertyAliasNameList!=null){
                for(CustomPropertyAliasDefinitionVO currentCustomPropertyAliasDefinitionVO:customPropertyAliasNameList){
                    recordCustomPropertyAliasName(discoverSpaceName,currentCustomPropertyAliasDefinitionVO.getCustomPropertyName(),
                            currentCustomPropertyAliasDefinitionVO.getCustomPropertyType(),currentCustomPropertyAliasDefinitionVO.getCustomPropertyAliasName());
                }
            }
        }
        //Apply Data Relation Mapping And Duplicate Data
        List<DataMappingDefinitionVO>  dataRelationMappingList=BusinessSolutionOperationUtil.getCommonDataRelationMappingDefinitionList(businessSolutionName);
        if(dataRelationMappingList!=null){
            for(DataMappingDefinitionVO currentDataMappingDefinitionVO:dataRelationMappingList){
                createCommonDataRelationMapping(discoverSpaceName,currentDataMappingDefinitionVO);
            }
        }
        List<DataMappingDefinitionVO>  dataDateDimensionMappingList=BusinessSolutionOperationUtil.getDataDateDimensionMappingDefinitionList(businessSolutionName);
        if(dataDateDimensionMappingList!=null){
            for(DataMappingDefinitionVO currentDataMappingDefinitionVO:dataDateDimensionMappingList){
                createDataDateDimensionMappingDefinition(discoverSpaceName,currentDataMappingDefinitionVO);
            }
        }
        List<DataMappingDefinitionVO>  dataPropertiesDuplicateMappingList=BusinessSolutionOperationUtil.getDataPropertiesDuplicateMappingDefinitionList(businessSolutionName);
        if(dataPropertiesDuplicateMappingList!=null){
            for(DataMappingDefinitionVO currentDataMappingDefinitionVO:dataPropertiesDuplicateMappingList){
                createDataPropertiesDuplicateMappingDefinition(discoverSpaceName,currentDataMappingDefinitionVO);
            }
        }
        return true;
    }

    private static void applyDimensionTypeData(InfoDiscoverSpace targetSpace,List<String> alreadyRenderedTypeList,String parentDimensionType,DimensionTypeDefinitionVO currentDimensionTypeVO,List<DimensionTypeDefinitionVO> dimensionTypeDefinitionsList) throws InfoDiscoveryEngineDataMartException {
        if(!currentDimensionTypeVO.getParentTypeName().equals(parentDimensionType)){
            return;
        }
        if(alreadyRenderedTypeList.contains(currentDimensionTypeVO.getTypeName())){
            return;
        }
        if(!targetSpace.hasDimensionType(currentDimensionTypeVO.getTypeName())){
            if(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME.equals(parentDimensionType)){
                targetSpace.addDimensionType(currentDimensionTypeVO.getTypeName());
            }else{
                targetSpace.addChildDimensionType(currentDimensionTypeVO.getTypeName(),parentDimensionType);
            }
            alreadyRenderedTypeList.add(currentDimensionTypeVO.getTypeName());
            List<DimensionTypeDefinitionVO> childDimensionTypesList= getChildDimensionTypeList(currentDimensionTypeVO.getTypeName(),dimensionTypeDefinitionsList);
            for(DimensionTypeDefinitionVO currentChildDimensionType:childDimensionTypesList){
                applyDimensionTypeData(targetSpace,alreadyRenderedTypeList,currentDimensionTypeVO.getTypeName(),currentChildDimensionType,dimensionTypeDefinitionsList);
            }
        }
    }

    private static List<DimensionTypeDefinitionVO> getChildDimensionTypeList(String dimensionTypeName, List<DimensionTypeDefinitionVO> dimensionTypeDefinitionsList){
        List<DimensionTypeDefinitionVO> childDimensionTypeList=new ArrayList<>();
        if(dimensionTypeDefinitionsList==null){
            return childDimensionTypeList;
        }else{
            for(DimensionTypeDefinitionVO currentTypeDefinition:dimensionTypeDefinitionsList){
                if(currentTypeDefinition.getParentTypeName().equals(dimensionTypeName)){
                    childDimensionTypeList.add(currentTypeDefinition);
                }
            }
            return childDimensionTypeList;
        }
    }

    private static void applyRelationTypeData(InfoDiscoverSpace targetSpace,List<String> alreadyRenderedTypeList,String parentRelationType,RelationTypeDefinitionVO currentRelationTypeVO,List<RelationTypeDefinitionVO> relationTypeDefinitionsList) throws InfoDiscoveryEngineDataMartException {
        if(!currentRelationTypeVO.getParentTypeName().equals(parentRelationType)){
            return;
        }
        if(alreadyRenderedTypeList.contains(currentRelationTypeVO.getTypeName())){
            return;
        }
        if(!targetSpace.hasRelationType(currentRelationTypeVO.getTypeName())){
            if(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME.equals(parentRelationType)){
                targetSpace.addRelationType(currentRelationTypeVO.getTypeName());
            }else{
                targetSpace.addChildRelationType(currentRelationTypeVO.getTypeName(),parentRelationType);
            }
            alreadyRenderedTypeList.add(currentRelationTypeVO.getTypeName());
            List<RelationTypeDefinitionVO> childRelationTypesList= getChildRelationTypeList(currentRelationTypeVO.getTypeName(),relationTypeDefinitionsList);
            for(RelationTypeDefinitionVO currentChildRelationType:childRelationTypesList){
                applyRelationTypeData(targetSpace,alreadyRenderedTypeList,currentRelationTypeVO.getTypeName(),currentChildRelationType,relationTypeDefinitionsList);
            }
        }
    }

    private static List<RelationTypeDefinitionVO> getChildRelationTypeList(String relationTypeName, List<RelationTypeDefinitionVO> relationTypeDefinitionsList){
        List<RelationTypeDefinitionVO> childRelationTypeList=new ArrayList<>();
        if(relationTypeDefinitionsList==null){
            return childRelationTypeList;
        }else{
            for(RelationTypeDefinitionVO currentTypeDefinition:relationTypeDefinitionsList){
                if(currentTypeDefinition.getParentTypeName().equals(relationTypeName)){
                    childRelationTypeList.add(currentTypeDefinition);
                }
            }
            return childRelationTypeList;
        }
    }

    private static PropertyType getPropertyTypeByCode(String propertyType){
        PropertyType targetPropertyType=null;
        if(ApplicationConstant.DataFieldType_BOOLEAN.equals(propertyType)){
            targetPropertyType=PropertyType.BOOLEAN;
        }
        if(ApplicationConstant.DataFieldType_INT.equals(propertyType)){
            targetPropertyType=PropertyType.INT;
        }
        if(ApplicationConstant.DataFieldType_SHORT.equals(propertyType)){
            targetPropertyType=PropertyType.SHORT;
        }
        if(ApplicationConstant.DataFieldType_LONG.equals(propertyType)){
            targetPropertyType=PropertyType.LONG;
        }
        if(ApplicationConstant.DataFieldType_FLOAT.equals(propertyType)){
            targetPropertyType=PropertyType.FLOAT;
        }
        if(ApplicationConstant.DataFieldType_DOUBLE.equals(propertyType)){
            targetPropertyType=PropertyType.DOUBLE;
        }
        if(ApplicationConstant.DataFieldType_DATE.equals(propertyType)){
            targetPropertyType=PropertyType.DATE;
        }
        if(ApplicationConstant.DataFieldType_STRING.equals(propertyType)){
            targetPropertyType=PropertyType.STRING;
        }
        if(ApplicationConstant.DataFieldType_BINARY.equals(propertyType)){
            targetPropertyType=PropertyType.BINARY;
        }
        if(ApplicationConstant.DataFieldType_BYTE.equals(propertyType)){
            targetPropertyType=PropertyType.BYTE;
        }
        return targetPropertyType;
    }

    public static List<TypeSummaryVO> getTypeDataSummary(String discoverSpaceName,String typeKind){
        List<TypeSummaryVO> typeSummaryVOList=new ArrayList<>();
        DiscoverSpaceStatisticMetrics  discoverSpaceStatisticMetrics=getDiscoverSpaceStatisticMetrics(discoverSpaceName);
        List<DataTypeStatisticMetrics> dataTypeStatisticMetricsList=null;
        String dataTypePerfix="";
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(typeKind)){
            dataTypeStatisticMetricsList=discoverSpaceStatisticMetrics.getFactsStatisticMetrics();
            dataTypePerfix= InfoDiscoverEngineConstant.CLASSPERFIX_FACT;
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(typeKind)){
            dataTypeStatisticMetricsList=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
            dataTypePerfix= InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION;
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(typeKind)){
            dataTypeStatisticMetricsList=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
            dataTypePerfix= InfoDiscoverEngineConstant.CLASSPERFIX_RELATION;
        }
        if(dataTypeStatisticMetricsList!=null){
            for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:dataTypeStatisticMetricsList){
                String typeNameString=currentDataTypeStatisticMetrics.getDataTypeName();
                String typeName=typeNameString.replaceFirst(dataTypePerfix,"");
                String typeAliasName=getTypeKindAliasName(discoverSpaceName,typeKind,typeName);
                TypeSummaryVO currentTypeSummaryVO=new TypeSummaryVO();
                currentTypeSummaryVO.setTypeAliasName(typeAliasName);
                currentTypeSummaryVO.setTypeName(typeName);
                typeSummaryVOList.add(currentTypeSummaryVO);
            }
        }
        return typeSummaryVOList;
    }

    public static boolean createCommonDataRelationMapping(String spaceName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType)){
                FactType dataRelationMappingFactType=metaConfigSpace.addFactType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType);
                TypeProperty spaceNameProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_DiscoverSpace, PropertyType.STRING);
                spaceNameProperty.setMandatory(true);
                TypeProperty sourceDataTypeNameProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_SourceDataTypeName, PropertyType.STRING);
                sourceDataTypeNameProperty.setMandatory(true);
                TypeProperty sourceDataTypeKindProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_SourceDataTypeKind, PropertyType.STRING);
                sourceDataTypeKindProperty.setMandatory(true);
                TypeProperty sourceDataPropertyNameProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_SourceDataPropertyName, PropertyType.STRING);
                sourceDataPropertyNameProperty.setMandatory(true);
                TypeProperty sourceDataPropertyTypeProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_SourceDataPropertyType, PropertyType.STRING);
                sourceDataPropertyTypeProperty.setMandatory(true);
                TypeProperty relationTypeNameProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_RelationTypeName, PropertyType.STRING);
                relationTypeNameProperty.setMandatory(true);
                TypeProperty relationDirectionProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_RelationDirection, PropertyType.STRING);
                relationDirectionProperty.setMandatory(true);
                TypeProperty mappingNotExistHandleMethodProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_MappingNotExistHandleMethod, PropertyType.STRING);
                mappingNotExistHandleMethodProperty.setMandatory(true);
                TypeProperty targetDataTypeNameProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_TargetDataTypeName, PropertyType.STRING);
                targetDataTypeNameProperty.setMandatory(false);
                TypeProperty targetDataTypeKindProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_TargetDataTypeKind, PropertyType.STRING);
                targetDataTypeKindProperty.setMandatory(true);
                TypeProperty targetDataPropertyNameProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_TargetDataPropertyName, PropertyType.STRING);
                targetDataPropertyNameProperty.setMandatory(true);
                TypeProperty targetDataPropertyTypeProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_TargetDataPropertyType, PropertyType.STRING);
                targetDataPropertyTypeProperty.setMandatory(true);
                TypeProperty mappingMinValueProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_MappingMinValue, PropertyType.STRING);
                mappingMinValueProperty.setMandatory(false);
                TypeProperty mappingMaxValueProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_MappingMaxValue, PropertyType.STRING);
                mappingMaxValueProperty.setMandatory(false);
                TypeProperty targetPropertyValueProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_TargetDataPropertyValue, PropertyType.STRING);
                targetPropertyValueProperty.setMandatory(false);
            }

            Fact dataRelationMappingDefinitionFact=DiscoverEngineComponentFactory.createFact(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType);
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_DiscoverSpace,spaceName);
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_SourceDataTypeName,dataMappingDefinitionVO.getSourceDataTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_SourceDataTypeKind,dataMappingDefinitionVO.getSourceDataTypeKind());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_SourceDataPropertyName,dataMappingDefinitionVO.getSourceDataPropertyName());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_SourceDataPropertyType,dataMappingDefinitionVO.getSourceDataPropertyType());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_RelationTypeName,dataMappingDefinitionVO.getRelationTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_RelationDirection,dataMappingDefinitionVO.getRelationDirection());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_MappingNotExistHandleMethod,dataMappingDefinitionVO.getMappingNotExistHandleMethod());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_TargetDataTypeName,dataMappingDefinitionVO.getTargetDataTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_TargetDataTypeKind,dataMappingDefinitionVO.getTargetDataTypeKind());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_TargetDataPropertyName,dataMappingDefinitionVO.getTargetDataPropertyName());
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_TargetDataPropertyType,dataMappingDefinitionVO.getTargetDataPropertyType());
            if(dataMappingDefinitionVO.getMinValue()!=null){
                dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_MappingMinValue,dataMappingDefinitionVO.getMinValue());
            }
            if(dataMappingDefinitionVO.getMaxValue()!=null){
                dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_MappingMaxValue,dataMappingDefinitionVO.getMaxValue());
            }
            if(dataMappingDefinitionVO.getRangeResult()!=null){
                dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_TargetDataPropertyValue,dataMappingDefinitionVO.getRangeResult());
            }
            Fact resultRecord=metaConfigSpace.addFact(dataRelationMappingDefinitionFact);
            if(resultRecord!=null){
                return true;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static List<DataMappingDefinitionVO> getCommonDataRelationMappingDefinitionList(String businessSolutionName){
        List<DataMappingDefinitionVO> dataRelationMappingDefinitionList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType)){
                ExploreParameters dataMaooingDefinitionRecordEP = new ExploreParameters();
                dataMaooingDefinitionRecordEP.setType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType);
                dataMaooingDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, businessSolutionName));
                dataMaooingDefinitionRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> dataMappingDefinitionRecordFactsList = ie.discoverFacts(dataMaooingDefinitionRecordEP);

                if(dataMappingDefinitionRecordFactsList!=null){
                    for(Fact currentFact:dataMappingDefinitionRecordFactsList){
                        DataMappingDefinitionVO currentDataMappingDefinitionVO=new DataMappingDefinitionVO();
                        currentDataMappingDefinitionVO.setSolutionName(businessSolutionName);
                        currentDataMappingDefinitionVO.setSourceDataTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setSourceDataTypeKind(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setSourceDataPropertyType(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setSourceDataPropertyName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName).getPropertyValue().toString());

                        currentDataMappingDefinitionVO.setRelationTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setRelationDirection(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationDirection).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setMappingNotExistHandleMethod(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingNotExistHandleMethod).getPropertyValue().toString());

                        currentDataMappingDefinitionVO.setTargetDataTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setTargetDataTypeKind(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeKind).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setTargetDataPropertyType(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setTargetDataPropertyName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName).getPropertyValue().toString());

                        if(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMinValue)!=null){
                            currentDataMappingDefinitionVO.setMinValue(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMinValue).getPropertyValue().toString());
                        }
                        if(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMaxValue)!=null){
                            currentDataMappingDefinitionVO.setMaxValue(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMaxValue).getPropertyValue().toString());
                        }
                        if(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyValue)!=null){
                            currentDataMappingDefinitionVO.setRangeResult(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyValue).getPropertyValue().toString());
                        }
                        dataRelationMappingDefinitionList.add(currentDataMappingDefinitionVO);
                    }
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return dataRelationMappingDefinitionList;
    }

    public static boolean deleteCommonDataRelationMappingDefinition(String discoverSpace, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType)){
                return false;
            }
            ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
            solutionDefinitionRecordEP.setType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType);
            solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, discoverSpace));
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName, dataMappingDefinitionVO.getSourceDataTypeName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind, dataMappingDefinitionVO.getSourceDataTypeKind()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType, dataMappingDefinitionVO.getSourceDataPropertyType()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName, dataMappingDefinitionVO.getSourceDataPropertyName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName, dataMappingDefinitionVO.getRelationTypeName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationDirection, dataMappingDefinitionVO.getRelationDirection()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingNotExistHandleMethod, dataMappingDefinitionVO.getMappingNotExistHandleMethod()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName, dataMappingDefinitionVO.getTargetDataTypeName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeKind, dataMappingDefinitionVO.getTargetDataTypeKind()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType, dataMappingDefinitionVO.getTargetDataPropertyType()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName, dataMappingDefinitionVO.getTargetDataPropertyName()), ExploreParameters.FilteringLogic.AND);
            if(dataMappingDefinitionVO.getMinValue()!=null&&!dataMappingDefinitionVO.getMinValue().equals("")){
                solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMinValue, dataMappingDefinitionVO.getMinValue()), ExploreParameters.FilteringLogic.AND);
            }
            else{
                solutionDefinitionRecordEP.addFilteringItem(new NullValueFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMinValue),ExploreParameters.FilteringLogic.AND);
            }
            if(dataMappingDefinitionVO.getMaxValue()!=null&&!dataMappingDefinitionVO.getMaxValue().equals("")){
                solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMaxValue, dataMappingDefinitionVO.getMaxValue()), ExploreParameters.FilteringLogic.AND);
            }
            else{
                solutionDefinitionRecordEP.addFilteringItem(new NullValueFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMaxValue),ExploreParameters.FilteringLogic.AND);
            }
            if(dataMappingDefinitionVO.getRangeResult()!=null&&!dataMappingDefinitionVO.getRangeResult().equals("")){
                solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyValue, dataMappingDefinitionVO.getRangeResult()), ExploreParameters.FilteringLogic.AND);
            }
            else{
                solutionDefinitionRecordEP.addFilteringItem(new NullValueFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyValue),ExploreParameters.FilteringLogic.AND);
            }
            solutionDefinitionRecordEP.setResultNumber(10000);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
            if(solutionDefinitionRecordFactsList!=null){
                for(Fact currentFact:solutionDefinitionRecordFactsList){
                    metaConfigSpace.removeFact(currentFact.getId());
                }
            }
            return true;
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createDataDateDimensionMappingDefinition(String discoverSpaceName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType)){
                FactType dataRelationMappingFactType=metaConfigSpace.addFactType(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType);
                TypeProperty discoverSpaceProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_DiscoverSpace, PropertyType.STRING);
                discoverSpaceProperty.setMandatory(true);
                TypeProperty sourceDataTypeNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName, PropertyType.STRING);
                sourceDataTypeNameProperty.setMandatory(true);
                TypeProperty sourceDataTypeKindProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind, PropertyType.STRING);
                sourceDataTypeKindProperty.setMandatory(true);
                TypeProperty sourceDataPropertyNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName, PropertyType.STRING);
                sourceDataPropertyNameProperty.setMandatory(true);
                TypeProperty relationTypeNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName, PropertyType.STRING);
                relationTypeNameProperty.setMandatory(true);
                TypeProperty relationDirectionProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationDirection, PropertyType.STRING);
                relationDirectionProperty.setMandatory(true);
                TypeProperty dateDimensionPerfixNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DateDimensionTypePrefix, PropertyType.STRING);
                dateDimensionPerfixNameProperty.setMandatory(true);
            }
            Fact dataRelationMappingDefinitionFact=DiscoverEngineComponentFactory.createFact(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType);
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_DiscoverSpace,discoverSpaceName);
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName,dataMappingDefinitionVO.getSourceDataTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind,dataMappingDefinitionVO.getSourceDataTypeKind());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName,dataMappingDefinitionVO.getSourceDataPropertyName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName,dataMappingDefinitionVO.getRelationTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationDirection,dataMappingDefinitionVO.getRelationDirection());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DateDimensionTypePrefix,dataMappingDefinitionVO.getDateDimensionTypePrefix());
            Fact resultRecord=metaConfigSpace.addFact(dataRelationMappingDefinitionFact);
            if(resultRecord!=null){
                return true;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static List<DataMappingDefinitionVO> getDataDateDimensionMappingDefinitionList(String discoverSpaceName){
        List<DataMappingDefinitionVO> dataRelationMappingDefinitionList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType)){
                ExploreParameters dataMaooingDefinitionRecordEP = new ExploreParameters();
                dataMaooingDefinitionRecordEP.setType(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType);
                dataMaooingDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, discoverSpaceName));
                dataMaooingDefinitionRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> dataMappingDefinitionRecordFactsList = ie.discoverFacts(dataMaooingDefinitionRecordEP);

                if(dataMappingDefinitionRecordFactsList!=null){
                    for(Fact currentFact:dataMappingDefinitionRecordFactsList){
                        DataMappingDefinitionVO currentDataMappingDefinitionVO=new DataMappingDefinitionVO();
                        currentDataMappingDefinitionVO.setSolutionName(discoverSpaceName);
                        currentDataMappingDefinitionVO.setSourceDataTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setSourceDataTypeKind(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setSourceDataPropertyName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setRelationTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setRelationDirection(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationDirection).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setDateDimensionTypePrefix(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DateDimensionTypePrefix).getPropertyValue().toString());
                        dataRelationMappingDefinitionList.add(currentDataMappingDefinitionVO);
                    }
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return dataRelationMappingDefinitionList;
    }

    public static boolean deleteDataDateDimensionMappingDefinition(String discoverSpaceName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType)){
                return false;
            }
            ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
            solutionDefinitionRecordEP.setType(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType);
            solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, discoverSpaceName));
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName, dataMappingDefinitionVO.getSourceDataTypeName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind, dataMappingDefinitionVO.getSourceDataTypeKind()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName, dataMappingDefinitionVO.getSourceDataPropertyName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName, dataMappingDefinitionVO.getRelationTypeName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationDirection, dataMappingDefinitionVO.getRelationDirection()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DateDimensionTypePrefix, dataMappingDefinitionVO.getDateDimensionTypePrefix()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.setResultNumber(10000);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
            if(solutionDefinitionRecordFactsList!=null){
                for(Fact currentFact:solutionDefinitionRecordFactsList){
                    metaConfigSpace.removeFact(currentFact.getId());
                }
            }
            return true;
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createDataPropertiesDuplicateMappingDefinition(String discoverSpaceName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType)){
                FactType dataRelationMappingFactType=metaConfigSpace.addFactType(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType);
                TypeProperty discoverSpaceProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_DiscoverSpace, PropertyType.STRING);
                discoverSpaceProperty.setMandatory(true);
                TypeProperty sourceDataTypeNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName, PropertyType.STRING);
                sourceDataTypeNameProperty.setMandatory(true);
                TypeProperty sourceDataTypeKindProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind, PropertyType.STRING);
                sourceDataTypeKindProperty.setMandatory(true);
                TypeProperty sourceDataPropertyNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName, PropertyType.STRING);
                sourceDataPropertyNameProperty.setMandatory(true);
                TypeProperty sourceDataPropertyTypeProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType, PropertyType.STRING);
                sourceDataPropertyTypeProperty.setMandatory(true);
                TypeProperty existingPropertyHandleMethodProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ExistingPropertyHandleMethod, PropertyType.STRING);
                existingPropertyHandleMethodProperty.setMandatory(true);
                TypeProperty targetDataTypeNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName, PropertyType.STRING);
                targetDataTypeNameProperty.setMandatory(false);
                TypeProperty targetDataPropertyNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName, PropertyType.STRING);
                targetDataPropertyNameProperty.setMandatory(true);
                TypeProperty targetDataPropertyTypeProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType, PropertyType.STRING);
                targetDataPropertyTypeProperty.setMandatory(true);
            }

            Fact dataRelationMappingDefinitionFact=DiscoverEngineComponentFactory.createFact(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType);
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_DiscoverSpace,discoverSpaceName);
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName,dataMappingDefinitionVO.getSourceDataTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind,dataMappingDefinitionVO.getSourceDataTypeKind());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName,dataMappingDefinitionVO.getSourceDataPropertyName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType,dataMappingDefinitionVO.getSourceDataPropertyType());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ExistingPropertyHandleMethod,dataMappingDefinitionVO.getExistingPropertyHandleMethod());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName,dataMappingDefinitionVO.getTargetDataTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName,dataMappingDefinitionVO.getTargetDataPropertyName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType,dataMappingDefinitionVO.getTargetDataPropertyType());
            Fact resultRecord=metaConfigSpace.addFact(dataRelationMappingDefinitionFact);
            if(resultRecord!=null){
                return true;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static List<DataMappingDefinitionVO> getDataPropertiesDuplicateMappingDefinitionList(String discoverSpaceName){
        List<DataMappingDefinitionVO> dataRelationMappingDefinitionList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType)){
                ExploreParameters dataMaooingDefinitionRecordEP = new ExploreParameters();
                dataMaooingDefinitionRecordEP.setType(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType);
                dataMaooingDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, discoverSpaceName));
                dataMaooingDefinitionRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> dataMappingDefinitionRecordFactsList = ie.discoverFacts(dataMaooingDefinitionRecordEP);

                if(dataMappingDefinitionRecordFactsList!=null){
                    for(Fact currentFact:dataMappingDefinitionRecordFactsList){
                        DataMappingDefinitionVO currentDataMappingDefinitionVO=new DataMappingDefinitionVO();
                        currentDataMappingDefinitionVO.setSolutionName(discoverSpaceName);
                        currentDataMappingDefinitionVO.setSourceDataTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setSourceDataTypeKind(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setSourceDataPropertyType(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setSourceDataPropertyName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setExistingPropertyHandleMethod(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ExistingPropertyHandleMethod).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setTargetDataTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setTargetDataPropertyType(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType).getPropertyValue().toString());
                        currentDataMappingDefinitionVO.setTargetDataPropertyName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName).getPropertyValue().toString());
                        dataRelationMappingDefinitionList.add(currentDataMappingDefinitionVO);
                    }
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return dataRelationMappingDefinitionList;
    }

    public static boolean deleteDataPropertiesDuplicateMappingDefinition(String discoverSpaceName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType)){
                return false;
            }
            ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
            solutionDefinitionRecordEP.setType(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType);
            solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, discoverSpaceName));
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName, dataMappingDefinitionVO.getSourceDataTypeName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind, dataMappingDefinitionVO.getSourceDataTypeKind()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType, dataMappingDefinitionVO.getSourceDataPropertyType()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName, dataMappingDefinitionVO.getSourceDataPropertyName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ExistingPropertyHandleMethod, dataMappingDefinitionVO.getExistingPropertyHandleMethod()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName, dataMappingDefinitionVO.getTargetDataTypeName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType, dataMappingDefinitionVO.getTargetDataPropertyType()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName, dataMappingDefinitionVO.getTargetDataPropertyName()), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP.setResultNumber(10000);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
            if(solutionDefinitionRecordFactsList!=null){
                for(Fact currentFact:solutionDefinitionRecordFactsList){
                    metaConfigSpace.removeFact(currentFact.getId());
                }
            }
            return true;
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean checkInfoUsedInCommonDataRelationMappingDefinition(String businessSolutionName,String dataTypeKind,String dataTypeName,String dataPropertyName,String dataPropertyType){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType)){
                return false;
            }
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();

            ExploreParameters solutionDefinitionRecordEP1 = new ExploreParameters();
            solutionDefinitionRecordEP1.setType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType);
            solutionDefinitionRecordEP1.setResultNumber(1);
            solutionDefinitionRecordEP1.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, businessSolutionName));

            solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName,dataTypeName), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind, dataTypeKind), ExploreParameters.FilteringLogic.AND);
            if(dataPropertyType!=null) {
                solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType, dataPropertyType), ExploreParameters.FilteringLogic.AND);
            }
            if(dataPropertyName!=null){
                solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName,dataPropertyName), ExploreParameters.FilteringLogic.AND);
            }
            List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP1);
            if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                return true;
            }

            ExploreParameters solutionDefinitionRecordEP2 = new ExploreParameters();
            solutionDefinitionRecordEP2.setType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType);
            solutionDefinitionRecordEP2.setResultNumber(1);
            solutionDefinitionRecordEP2.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, businessSolutionName));

            solutionDefinitionRecordEP2.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName,dataTypeName), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP2.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeKind, dataTypeKind), ExploreParameters.FilteringLogic.AND);
            if(dataPropertyType!=null) {
                solutionDefinitionRecordEP2.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType, dataPropertyType), ExploreParameters.FilteringLogic.AND);
            }
            if(dataPropertyName!=null){
                solutionDefinitionRecordEP2.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName,dataPropertyName), ExploreParameters.FilteringLogic.AND);
            }
            solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP2);
            if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                return true;
            }

            ExploreParameters solutionDefinitionRecordEP3 = new ExploreParameters();
            solutionDefinitionRecordEP3.setType(DATAMAPPING_SpaceDataRelationMappingDefinitionFactType);
            solutionDefinitionRecordEP3.setResultNumber(1);
            solutionDefinitionRecordEP3.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, businessSolutionName));
            solutionDefinitionRecordEP3.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName,dataTypeName), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP3);
            if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                return true;
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean checkInfoUsedInDataDateDimensionMappingDefinition(String businessSolutionName,String dataTypeKind,String dataTypeName,String dataPropertyName){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType)){
                return false;
            }
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            ExploreParameters solutionDefinitionRecordEP1 = new ExploreParameters();
            solutionDefinitionRecordEP1.setType(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType);
            solutionDefinitionRecordEP1.setResultNumber(1);
            solutionDefinitionRecordEP1.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, businessSolutionName));
            solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName, dataTypeName), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind,dataTypeKind), ExploreParameters.FilteringLogic.AND);
            if(dataPropertyName!=null){
                solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName, dataPropertyName), ExploreParameters.FilteringLogic.AND);
            }
            List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP1);
            if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                return true;
            }

            ExploreParameters solutionDefinitionRecordEP2 = new ExploreParameters();
            solutionDefinitionRecordEP2.setType(DATAMAPPING_SpaceDataDateDimensionMappingDefinitionFactType);
            solutionDefinitionRecordEP2.setResultNumber(1);
            solutionDefinitionRecordEP2.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, businessSolutionName));
            solutionDefinitionRecordEP2.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName,dataTypeName), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP2);
            if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                return true;
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean checkInfoUsedInDataPropertiesDuplicateMappingDefinition(String businessSolutionName,String dataTypeKind,String dataTypeName,String dataPropertyName,String dataPropertyType){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType)){
                return false;
            }
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();

            ExploreParameters solutionDefinitionRecordEP1 = new ExploreParameters();
            solutionDefinitionRecordEP1.setType(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType);
            solutionDefinitionRecordEP1.setResultNumber(1);
            solutionDefinitionRecordEP1.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, businessSolutionName));
            solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName, dataTypeName), ExploreParameters.FilteringLogic.AND);
            solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind,dataTypeKind), ExploreParameters.FilteringLogic.AND);
            if(dataPropertyName!=null){
                solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName,dataPropertyName), ExploreParameters.FilteringLogic.AND);
            }
            if(dataPropertyType!=null){
                solutionDefinitionRecordEP1.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType, dataPropertyType), ExploreParameters.FilteringLogic.AND);
            }
            List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP1);
            if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                return true;
            }
            if(dataTypeKind.equals("FACT")){
                ExploreParameters solutionDefinitionRecordEP2 = new ExploreParameters();
                solutionDefinitionRecordEP2.setType(DATAMAPPING_SpaceDataPropertiesDuplicateMappingDefinitionFactType);
                solutionDefinitionRecordEP2.setResultNumber(1);
                solutionDefinitionRecordEP2.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, businessSolutionName));
                solutionDefinitionRecordEP2.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName, dataTypeName), ExploreParameters.FilteringLogic.AND);
                if (dataPropertyName != null) {
                    solutionDefinitionRecordEP2.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName, dataPropertyName), ExploreParameters.FilteringLogic.AND);
                }
                if (dataPropertyType != null) {
                    solutionDefinitionRecordEP2.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType, dataPropertyType), ExploreParameters.FilteringLogic.AND);
                }
                solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP2);
                if (solutionDefinitionRecordFactsList != null && solutionDefinitionRecordFactsList.size() > 0) {
                    return true;
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean runCommonDataRelationMappingDefinition(String discoverSpaceName,DataMappingDefinitionVO definition){
        Runnable runRuleDefinitionRunnable=new Runnable() {
            @Override
            public void run() {
                try {
                    //Call run definition job in background thread
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    ObjectMapper mapper=new ObjectMapper();
                    if(definition.getSourceDataTypeKind().equals(SolutionConstants.FACT_TYPE)){
                        if(definition.getTargetDataTypeKind().equals(SolutionConstants.FACT_TYPE)){
                            dataMap.put("mappingType",SolutionConstants.JSON_FACT_TO_FACT_MAPPING);
                        }
                        if(definition.getTargetDataTypeKind().equals(SolutionConstants.DIMENSION_TYPE)){
                            dataMap.put("mappingType",SolutionConstants.JSON_FACT_TO_DIMENSION_MAPPING);
                        }
                    }
                    if(definition.getSourceDataTypeKind().equals(SolutionConstants.DIMENSION_TYPE)){
                        if(definition.getTargetDataTypeKind().equals(SolutionConstants.FACT_TYPE)){
                            dataMap.put("mappingType",SolutionConstants.JSON_DIMENSION_TO_FACT_MAPPING);
                        }
                        if(definition.getTargetDataTypeKind().equals(SolutionConstants.DIMENSION_TYPE)){
                            dataMap.put("mappingType",SolutionConstants.JSON_DIMENSION_TO_DIMENSION_MAPPING);
                        }
                    }
                    dataMap.put("sourceDataTypeName",definition.getSourceDataTypeName());
                    dataMap.put("sourceDataTypeKind",definition.getSourceDataTypeKind());
                    dataMap.put("sourceDataPropertyType",definition.getSourceDataPropertyType());
                    dataMap.put("sourceDataPropertyName",definition.getSourceDataPropertyName());
                    if(definition.getMinValue()!=null&&!definition.getMinValue().equals("")){
                        dataMap.put("minValue",definition.getMinValue());
                    }
                    if(definition.getMaxValue()!=null&&!definition.getMaxValue().equals("")){
                        dataMap.put("maxValue",definition.getMaxValue());
                    }
                    if(definition.getRangeResult()!=null&&!definition.getRangeResult().equals("")){
                        dataMap.put("targetDataPropertyValue",definition.getRangeResult());
                    }
                    dataMap.put("relationTypeName",definition.getRelationTypeName());
                    dataMap.put("relationDirection",definition.getRelationDirection());
                    dataMap.put("mappingNotExistHandleMethod",definition.getMappingNotExistHandleMethod());
                    dataMap.put("targetDataTypeKind",definition.getTargetDataTypeKind());
                    dataMap.put("targetDataTypeName",definition.getTargetDataTypeName());
                    dataMap.put("targetDataPropertyName",definition.getTargetDataPropertyName());
                    dataMap.put("targetDataPropertyType",definition.getTargetDataPropertyType());

                    String jsonString=mapper.writeValueAsString(dataMap);
                    BatchExecution batchExecuteImportor = new BatchExecution(discoverSpaceName);
                    String batchOperationHandledDataCount=batchExecuteImportor.batchUpdateWithRule(jsonString);
                    Label confirmMessage=new Label("<span style='font-weight:bold;'>"+ FontAwesome.INFO.getHtml()+
                            "   <b style='color:#333333;'>数据属性关联映射规则运行结束</b>。</span>"+
                            "<br/>处理数据数量: "+batchOperationHandledDataCount+
                            "<br/>完成时间: "+new Date().toString()+
                            "<br/><br/>规则定义信息"+
                            "<br/>源数据类型:"+definition.getSourceDataTypeKind()+
                            "<br/>源类型名称:"+definition.getSourceDataTypeName()+
                            "<br/>源属性名称:"+definition.getSourceDataPropertyName()+
                            "<br/>源属性数据类型:"+definition.getSourceDataPropertyType()+
                            "<br/>源属性最小值:"+definition.getMinValue()+
                            "<br/>源属性最大值:"+definition.getMaxValue()+
                            "<br/>目标属性值:"+definition.getRangeResult()+
                            "<br/>关联关系类型:"+definition.getRelationTypeName()+
                            "<br/>数据关联方向:"+definition.getRelationDirection()+
                            "<br/>不存在映射处理策略:"+definition.getMappingNotExistHandleMethod()+
                            "<br/>目标数据类型:"+definition.getTargetDataTypeKind()+
                            "<br/>目标类型名称:"+definition.getTargetDataTypeName()+
                            "<br/>目标属性名称:"+definition.getTargetDataPropertyName()+
                            "<br/>目标属性数据类型:"+definition.getTargetDataPropertyType()
                            , ContentMode.HTML);
                    SystemConfigUtil.showServerPushConfirmDialog(confirmMessage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch(JsonProcessingException e){
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread backgroundThread = new Thread(runRuleDefinitionRunnable);
        backgroundThread.setDaemon(true);  //设为后台线程
        backgroundThread.start();
        return true;
    }

    public static boolean runDataDateDimensionMappingDefinition(String discoverSpaceName,DataMappingDefinitionVO definition){
        Runnable runRuleDefinitionRunnable=new Runnable() {
            @Override
            public void run() {
                try {
                    //Call run definition job in background thread
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    ObjectMapper mapper=new ObjectMapper();
                    if(definition.getSourceDataTypeKind().equals(SolutionConstants.FACT_TYPE)){
                        dataMap.put("mappingType",SolutionConstants.JSON_FACT_TO_DATE_DIMENSION_MAPPING);
                    }
                    if(definition.getSourceDataTypeKind().equals(SolutionConstants.DIMENSION_TYPE)){
                        dataMap.put("mappingType",SolutionConstants.JSON_DIMENSION_TO_DATE_DIMENSION_MAPPING);
                    }
                    dataMap.put("sourceDataTypeName",definition.getSourceDataTypeName());
                    dataMap.put("sourceDataTypeKind",definition.getSourceDataTypeKind());
                    dataMap.put("sourceDataPropertyName",definition.getSourceDataPropertyName());
                    dataMap.put("relationTypeName",definition.getRelationTypeName());
                    dataMap.put("relationDirection",definition.getRelationDirection());
                    dataMap.put("dateDimensionTypePrefix",definition.getDateDimensionTypePrefix());
                    String jsonString=mapper.writeValueAsString(dataMap);

                    BatchExecution batchExecuteImportor = new BatchExecution(discoverSpaceName);
                    String batchOperationHandledDataCount=batchExecuteImportor.batchUpdateWithRule(jsonString);
                    Label confirmMessage=new Label("<span style='font-weight:bold;'>"+ FontAwesome.INFO.getHtml()+
                            "   <b style='color:#333333;'>数据与时间维度关联定义规则运行结束</b>。</span>"+
                            "<br/>处理数据数量: "+ batchOperationHandledDataCount+
                            "<br/>完成时间: "+new Date().toString()+
                            "<br/><br/>规则定义信息"+
                            "<br/>源数据类型:"+definition.getSourceDataTypeKind()+
                            "<br/>源类型名称:"+definition.getSourceDataTypeName()+
                            "<br/>源属性名称:"+definition.getSourceDataPropertyName()+
                            "<br/>关联关系类型:"+definition.getRelationTypeName()+
                            "<br/>数据关联方向:"+definition.getRelationDirection()+
                            "<br/>时间维度类型前缀:"+definition.getDateDimensionTypePrefix()
                            , ContentMode.HTML);
                    SystemConfigUtil.showServerPushConfirmDialog(confirmMessage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch(JsonProcessingException e){
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread backgroundThread = new Thread(runRuleDefinitionRunnable);
        backgroundThread.setDaemon(true);  //设为后台线程
        backgroundThread.start();
        return true;
    }

    public static boolean runDataPropertiesDuplicateMappingDefinition(String discoverSpaceName,DataMappingDefinitionVO definition){
        Runnable runRuleDefinitionRunnable=new Runnable() {
            @Override
            public void run() {
                try {
                    //Call run definition job in background thread and show result message dialog
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    ObjectMapper mapper=new ObjectMapper();
                    if(definition.getSourceDataTypeKind().equals(SolutionConstants.FACT_TYPE)){
                        dataMap.put("mappingType",SolutionConstants.JSON_FACT_DUPLICATE_COPY_MAPPING);
                    }
                    if(definition.getSourceDataTypeKind().equals(SolutionConstants.DIMENSION_TYPE)){
                        dataMap.put("mappingType",SolutionConstants.JSON_DIMENSION_DUPLICATE_COPY_MAPPING);
                    }
                    dataMap.put("sourceDataTypeName",definition.getSourceDataTypeName());
                    dataMap.put("sourceDataTypeKind",definition.getSourceDataTypeKind());
                    dataMap.put("sourceDataPropertyType",definition.getSourceDataPropertyType());
                    dataMap.put("sourceDataPropertyName",definition.getSourceDataPropertyName());
                    dataMap.put("targetDataTypeName",definition.getTargetDataTypeName());
                    dataMap.put("targetDataPropertyName",definition.getTargetDataPropertyName());
                    dataMap.put("targetDataPropertyType",definition.getTargetDataPropertyType());
                    dataMap.put("existingPropertyHandleMethod",definition.getExistingPropertyHandleMethod());

                    String jsonString=mapper.writeValueAsString(dataMap);
                    BatchExecution batchExecuteImportor = new BatchExecution(discoverSpaceName);
                    String batchOperationHandledDataCount=batchExecuteImportor.batchUpdateWithRule(jsonString);
                    Label confirmMessage=new Label("<span style='font-weight:bold;'>"+ FontAwesome.INFO.getHtml()+
                            "   <b style='color:#333333;'>数据属性复制规则运行结束</b>。</span>"+
                            "<br/>处理数据数量: "+batchOperationHandledDataCount+
                            "<br/>完成时间: "+new Date().toString()+
                            "<br/><br/>规则定义信息"+
                            "<br/>源数据类型:"+definition.getSourceDataTypeKind()+
                            "<br/>源类型名称:"+definition.getSourceDataTypeName()+
                            "<br/>数据匹配外键属性:"+definition.getSourceDataPropertyName()+
                            "<br/>源属性数据类型:"+definition.getSourceDataPropertyType()+
                            "<br/>已存在属性处理策略:"+definition.getExistingPropertyHandleMethod()+
                            "<br/>目标事实类型名称:"+definition.getTargetDataTypeName()+
                            "<br/>数据匹配主键属性:"+definition.getTargetDataPropertyName()+
                            "<br/>目标属性数据类型:"+definition.getTargetDataPropertyType()
                            , ContentMode.HTML);
                    SystemConfigUtil.showServerPushConfirmDialog(confirmMessage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch(JsonProcessingException e){
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread backgroundThread = new Thread(runRuleDefinitionRunnable);
        backgroundThread.setDaemon(true);  //设为后台线程
        backgroundThread.start();
        return true;
    }
}
