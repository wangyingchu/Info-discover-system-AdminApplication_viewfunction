package com.infoDiscover.adminCenter.logic.component.businessSolutionManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.*;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.RuntimeEnvironmentUtil;
import com.infoDiscover.infoDiscoverEngine.dataMart.*;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.NullValueFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;
import com.infoDiscover.solution.template.TemplateExporter;
import com.infoDiscover.solution.template.TemplateImporter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangychu on 5/4/17.
 */
public class BusinessSolutionOperationUtil {

    public static final String BUSINESSSOLUTION_SolutionDefinitionFactType="BusinessSolution_SolutionDefinition";
    public static final String BUSINESSSOLUTION_SolutionFactTypeFactType="BusinessSolution_SolutionFactType";
    public static final String BUSINESSSOLUTION_SolutionDimensionTypeFactType="BusinessSolution_SolutionDimensionType";
    public static final String BUSINESSSOLUTION_SolutionRelationTypeFactType="BusinessSolution_SolutionRelationType";
    public static final String BUSINESSSOLUTION_SolutionTypePropertyFactType="BusinessSolution_SolutionTypePropertyType";
    public static final String BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType="BusinessSolution_SolutionCustomPropertyAliasType";
    public static final String BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType="BusinessSolution_SolutionDataRelationMappingDefinition";
    public static final String BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType="BusinessSolution_SolutionDataDateDimensionMappingDefinition";
    public static final String BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType="BusinessSolution_SolutionDataPropertiesDuplicateMappingDefinition";

    public static final String MetaConfig_PropertyName_SolutionName="solutionName";

    public static boolean checkBusinessSolutionExistence(String businessSolutionName){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDefinitionFactType)){
                ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
                solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDefinitionFactType);
                solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDefinitionRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
                if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
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

    public static boolean createBusinessSolution(String businessSolutionName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDefinitionFactType)){
                FactType solutionDefinitionFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionDefinitionFactType);
                TypeProperty solutionNameProperty=solutionDefinitionFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);
            }
            Fact solutionDefinitionFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionDefinitionFactType);
            solutionDefinitionFact.setInitProperty(MetaConfig_PropertyName_SolutionName,businessSolutionName);
            Fact resultRecord=metaConfigSpace.addFact(solutionDefinitionFact);
            if(resultRecord!=null){
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

    public static List<String> getExistBusinessSolutions(){
        List<String> businessSolutionsList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDefinitionFactType)){
                ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
                solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDefinitionFactType);
                solutionDefinitionRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
                for(Fact currentFact:solutionDefinitionRecordFactsList){
                    businessSolutionsList.add(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
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
        return businessSolutionsList;
    }

    public static boolean deleteBusinessSolutionDefinition(String businessSolutionName){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDefinitionFactType)){
                ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
                solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDefinitionFactType);
                solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDefinitionRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
                if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                    Fact targetBusinessSolution=solutionDefinitionRecordFactsList.get(0);
                    metaConfigSpace.removeFact(targetBusinessSolution.getId());
                    //also need delete other data related to this solution
                    //remove type properties
                    removeRecordByTypeProperty(metaConfigSpace,ie,BUSINESSSOLUTION_SolutionTypePropertyFactType,MetaConfig_PropertyName_SolutionName,businessSolutionName);
                    //remove type definition
                    removeRecordByTypeProperty(metaConfigSpace,ie,BUSINESSSOLUTION_SolutionFactTypeFactType,MetaConfig_PropertyName_SolutionName,businessSolutionName);
                    removeRecordByTypeProperty(metaConfigSpace,ie,BUSINESSSOLUTION_SolutionDimensionTypeFactType,MetaConfig_PropertyName_SolutionName,businessSolutionName);
                    removeRecordByTypeProperty(metaConfigSpace,ie,BUSINESSSOLUTION_SolutionRelationTypeFactType,MetaConfig_PropertyName_SolutionName,businessSolutionName);
                    //remove alias names definition
                    removeRecordByTypeProperty(metaConfigSpace,ie,BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType,MetaConfig_PropertyName_SolutionName,businessSolutionName);
                    //remove data mapping configuration items
                    removeRecordByTypeProperty(metaConfigSpace,ie,BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType,MetaConfig_PropertyName_SolutionName,businessSolutionName);
                    removeRecordByTypeProperty(metaConfigSpace,ie,BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType,MetaConfig_PropertyName_SolutionName,businessSolutionName);
                    removeRecordByTypeProperty(metaConfigSpace,ie,BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType,MetaConfig_PropertyName_SolutionName,businessSolutionName);
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
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

    private static void removeRecordByTypeProperty(InfoDiscoverSpace metaConfigSpace,InformationExplorer ie,String recordTypeName,String keyPropertyName,Object keyPropertyValue) throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
        if(metaConfigSpace.hasFactType(recordTypeName)){
            ExploreParameters solutionFactTypePropertyRecordEP = new ExploreParameters();
            solutionFactTypePropertyRecordEP.setType(recordTypeName);
            solutionFactTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(keyPropertyName, keyPropertyValue));
            solutionFactTypePropertyRecordEP.setResultNumber(10000);
            List<Fact> solutionFactTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypePropertyRecordEP);
            if(solutionFactTypePropertyDefinitionRecordFactsList!=null){
                for(Fact currentFact:solutionFactTypePropertyDefinitionRecordFactsList){
                    metaConfigSpace.removeFact(currentFact.getId());
                }
            }
        }
    }

    public static boolean checkSolutionFactTypeExistence(String businessSolutionName,String factTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                return false;
            }
            else{
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionFactTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeName, factTypeName), ExploreParameters.FilteringLogic.AND);
                solutionFactTypeRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionFactTypeDefinitionRecordFactsList!=null&&solutionFactTypeDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
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
        return false;
    }

    public static boolean createBusinessSolutionFactType(String businessSolutionName,String factTypeName,String factAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                FactType solutionFactTypeFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionFactTypeFactType);
                TypeProperty solutionNameProperty=solutionFactTypeFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);

                TypeProperty factTypeNameProperty=solutionFactTypeFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeName, PropertyType.STRING);
                factTypeNameProperty.setMandatory(true);

                TypeProperty factTypeAliasNameProperty=solutionFactTypeFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeAliasName, PropertyType.STRING);
                factTypeAliasNameProperty.setMandatory(true);
            }
            Fact solutionFactTypeFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionFactTypeFactType);
            solutionFactTypeFact.setInitProperty(MetaConfig_PropertyName_SolutionName,businessSolutionName);
            solutionFactTypeFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeName,factTypeName);
            solutionFactTypeFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeAliasName,factAliasName);
            Fact resultRecord=metaConfigSpace.addFact(solutionFactTypeFact);
            if(resultRecord!=null){
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

    public static boolean deleteBusinessSolutionFactType(String businessSolutionName,String factTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                return false;
            }else{
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionFactTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeName, factTypeName), ExploreParameters.FilteringLogic.AND);
                solutionFactTypeRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionFactTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionFactTypeDefinitionRecordFactsList){
                        metaConfigSpace.removeFact(currentFact.getId());
                    }
                }
                if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                    ExploreParameters solutionFactTypePropertyRecordEP = new ExploreParameters();
                    solutionFactTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                    solutionFactTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                    solutionFactTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeKind, InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT), ExploreParameters.FilteringLogic.AND);
                    solutionFactTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeName, factTypeName), ExploreParameters.FilteringLogic.AND);
                    solutionFactTypePropertyRecordEP.setResultNumber(10000);
                    List<Fact> solutionFactTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypePropertyRecordEP);
                    if(solutionFactTypePropertyDefinitionRecordFactsList!=null){
                        for(Fact currentFact:solutionFactTypePropertyDefinitionRecordFactsList){
                            metaConfigSpace.removeFact(currentFact.getId());
                        }
                    }
                }
                return true;
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
        return false;
    }

    public static List<FactTypeDefinitionVO> getBusinessSolutionFactTypeList(String businessSolutionName){
        List<FactTypeDefinitionVO> factTypeDefinitionList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionFactTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionFactTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionFactTypeDefinitionRecordFactsList){
                        FactTypeDefinitionVO currentFactTypeDefinitionVO=new FactTypeDefinitionVO();
                        currentFactTypeDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        currentFactTypeDefinitionVO.setTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeName).getPropertyValue().toString());
                        currentFactTypeDefinitionVO.setTypeAliasName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeAliasName).getPropertyValue().toString());
                        factTypeDefinitionList.add(currentFactTypeDefinitionVO);
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
        return factTypeDefinitionList;
    }

    public static List<DimensionTypeDefinitionVO> getBusinessSolutionDimensionTypeList(String businessSolutionName){
        List<DimensionTypeDefinitionVO> dimensionTypeDefinitionList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDimensionTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionDimensionTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionDimensionTypeDefinitionRecordFactsList){
                        DimensionTypeDefinitionVO currentDimensionTypeDefinitionVO=new DimensionTypeDefinitionVO();
                        currentDimensionTypeDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        currentDimensionTypeDefinitionVO.setTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeName).getPropertyValue().toString());
                        currentDimensionTypeDefinitionVO.setTypeAliasName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeAliasName).getPropertyValue().toString());
                        currentDimensionTypeDefinitionVO.setParentTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ParentDimensionTypeName).getPropertyValue().toString());
                        dimensionTypeDefinitionList.add(currentDimensionTypeDefinitionVO);
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
        return dimensionTypeDefinitionList;
    }

    public static boolean checkSolutionDimensionTypeExistence(String businessSolutionName,String dimensionTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                return false;
            }
            else{
                ExploreParameters solutionDimensionTypeRecordEP = new ExploreParameters();
                solutionDimensionTypeRecordEP.setType(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
                solutionDimensionTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDimensionTypeRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeName, dimensionTypeName), ExploreParameters.FilteringLogic.AND);
                solutionDimensionTypeRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypeDefinitionRecordFactsList = ie.discoverFacts(solutionDimensionTypeRecordEP);
                if(solutionFactTypeDefinitionRecordFactsList!=null&&solutionFactTypeDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
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
        return false;
    }

    public static boolean createBusinessSolutionDimensionType(String businessSolutionName,String parentDimensionTypeName,String dimensionTypeName,String dimensionTypeAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                FactType solutionFactTypeFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
                TypeProperty solutionNameProperty=solutionFactTypeFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);

                TypeProperty dimensionTypeNameProperty=solutionFactTypeFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeName, PropertyType.STRING);
                dimensionTypeNameProperty.setMandatory(true);

                TypeProperty dimensionTypeAliasNameProperty=solutionFactTypeFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeAliasName, PropertyType.STRING);
                dimensionTypeAliasNameProperty.setMandatory(true);

                TypeProperty parentDimensionTypeAliasNameProperty=solutionFactTypeFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ParentDimensionTypeName, PropertyType.STRING);
                parentDimensionTypeAliasNameProperty.setMandatory(true);
            }
            Fact solutionDimensionTypeFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
            solutionDimensionTypeFact.setInitProperty(MetaConfig_PropertyName_SolutionName,businessSolutionName);
            solutionDimensionTypeFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ParentDimensionTypeName,parentDimensionTypeName);
            solutionDimensionTypeFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeName,dimensionTypeName);
            solutionDimensionTypeFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeAliasName,dimensionTypeAliasName);
            Fact resultRecord=metaConfigSpace.addFact(solutionDimensionTypeFact);
            if(resultRecord!=null){
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

    public static boolean deleteBusinessSolutionDimensionType(String businessSolutionName,String dimensionTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                return false;
            }else{
                ExploreParameters solutionDimensionTypeRecordEP = new ExploreParameters();
                solutionDimensionTypeRecordEP.setType(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
                solutionDimensionTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDimensionTypeRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeName, dimensionTypeName), ExploreParameters.FilteringLogic.AND);
                solutionDimensionTypeRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDimensionTypeDefinitionRecordFactsList = ie.discoverFacts(solutionDimensionTypeRecordEP);
                if(solutionDimensionTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionDimensionTypeDefinitionRecordFactsList){
                        metaConfigSpace.removeFact(currentFact.getId());
                    }
                }
                if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                    ExploreParameters solutionDimensionTypePropertyRecordEP = new ExploreParameters();
                    solutionDimensionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                    solutionDimensionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                    solutionDimensionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeKind, InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION), ExploreParameters.FilteringLogic.AND);
                    solutionDimensionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeName, dimensionTypeName), ExploreParameters.FilteringLogic.AND);
                    solutionDimensionTypePropertyRecordEP.setResultNumber(10000);
                    List<Fact> solutionDimensionTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionDimensionTypePropertyRecordEP);
                    if(solutionDimensionTypePropertyDefinitionRecordFactsList!=null){
                        for(Fact currentFact:solutionDimensionTypePropertyDefinitionRecordFactsList){
                            metaConfigSpace.removeFact(currentFact.getId());
                        }
                    }
                }
                return true;
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
        return false;
    }

    public static List<RelationTypeDefinitionVO> getBusinessSolutionRelationTypeList(String businessSolutionName){
        List<RelationTypeDefinitionVO> relationTypeDefinitionList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionRelationTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionRelationTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionRelationTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionRelationTypeDefinitionRecordFactsList){
                        RelationTypeDefinitionVO currentRelationTypeDefinitionVO=new RelationTypeDefinitionVO();
                        currentRelationTypeDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        currentRelationTypeDefinitionVO.setTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName).getPropertyValue().toString());
                        currentRelationTypeDefinitionVO.setTypeAliasName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeAliasName).getPropertyValue().toString());
                        currentRelationTypeDefinitionVO.setParentTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ParentRelationTypeName).getPropertyValue().toString());
                        relationTypeDefinitionList.add(currentRelationTypeDefinitionVO);
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
        return relationTypeDefinitionList;
    }

    public static boolean checkSolutionRelationTypeExistence(String businessSolutionName,String relationTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                return false;
            }
            else{
                ExploreParameters solutionRelationTypeRecordEP = new ExploreParameters();
                solutionRelationTypeRecordEP.setType(BUSINESSSOLUTION_SolutionRelationTypeFactType);
                solutionRelationTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionRelationTypeRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName, relationTypeName), ExploreParameters.FilteringLogic.AND);
                solutionRelationTypeRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionRelationTypeDefinitionRecordFactsList = ie.discoverFacts(solutionRelationTypeRecordEP);
                if(solutionRelationTypeDefinitionRecordFactsList!=null&&solutionRelationTypeDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
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
        return false;
    }

    public static boolean createBusinessSolutionRelationType(String businessSolutionName,String parentRelationTypeName,String relationTypeName,String relationTypeAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                FactType solutionRelationTypeFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType);
                TypeProperty solutionNameProperty=solutionRelationTypeFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);

                TypeProperty relationTypeNameProperty=solutionRelationTypeFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName, PropertyType.STRING);
                relationTypeNameProperty.setMandatory(true);

                TypeProperty relationTypeAliasNameProperty=solutionRelationTypeFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeAliasName, PropertyType.STRING);
                relationTypeAliasNameProperty.setMandatory(true);

                TypeProperty parentRelationTypeAliasNameProperty=solutionRelationTypeFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ParentRelationTypeName, PropertyType.STRING);
                parentRelationTypeAliasNameProperty.setMandatory(true);
            }
            Fact solutionRelationTypeFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionRelationTypeFactType);
            solutionRelationTypeFact.setInitProperty(MetaConfig_PropertyName_SolutionName,businessSolutionName);
            solutionRelationTypeFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ParentRelationTypeName,parentRelationTypeName);
            solutionRelationTypeFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName,relationTypeName);
            solutionRelationTypeFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeAliasName,relationTypeAliasName);
            Fact resultRecord=metaConfigSpace.addFact(solutionRelationTypeFact);
            if(resultRecord!=null){
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

    public static boolean deleteBusinessSolutionRelationType(String businessSolutionName,String relationTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                return false;
            }else{
                ExploreParameters solutionRelationTypeRecordEP = new ExploreParameters();
                solutionRelationTypeRecordEP.setType(BUSINESSSOLUTION_SolutionRelationTypeFactType);
                solutionRelationTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionRelationTypeRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName, relationTypeName), ExploreParameters.FilteringLogic.AND);
                solutionRelationTypeRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionRelationTypeDefinitionRecordFactsList = ie.discoverFacts(solutionRelationTypeRecordEP);
                if(solutionRelationTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionRelationTypeDefinitionRecordFactsList){
                        metaConfigSpace.removeFact(currentFact.getId());
                    }
                }
                if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                    ExploreParameters solutionRelationTypePropertyRecordEP = new ExploreParameters();
                    solutionRelationTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                    solutionRelationTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                    solutionRelationTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeKind, InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION), ExploreParameters.FilteringLogic.AND);
                    solutionRelationTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeName, relationTypeName), ExploreParameters.FilteringLogic.AND);
                    solutionRelationTypePropertyRecordEP.setResultNumber(10000);
                    List<Fact> solutionRelationTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionRelationTypePropertyRecordEP);
                    if(solutionRelationTypePropertyDefinitionRecordFactsList!=null){
                        for(Fact currentFact:solutionRelationTypePropertyDefinitionRecordFactsList){
                            metaConfigSpace.removeFact(currentFact.getId());
                        }
                    }
                }
                return true;
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
        return false;
    }

    public static boolean updateSolutionTypeAliasName(String solutionName, String solutionTypeKind,String solutionTypeName,String solutionTypeAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            String dataFactType=null;
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(solutionTypeKind)){
                if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                    return false;
                }
                dataFactType=BUSINESSSOLUTION_SolutionDimensionTypeFactType;
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(solutionTypeKind)){
                if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                    return false;
                }
                dataFactType=BUSINESSSOLUTION_SolutionFactTypeFactType;
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(solutionTypeKind)){
                if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                    return false;
                }
                dataFactType=BUSINESSSOLUTION_SolutionRelationTypeFactType;
            }
            ExploreParameters solutionTypeRecordEP = new ExploreParameters();
            solutionTypeRecordEP.setType(dataFactType);
            solutionTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(solutionTypeKind)){
                solutionTypeRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeName, solutionTypeName), ExploreParameters.FilteringLogic.AND);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(solutionTypeKind)){
                solutionTypeRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeName, solutionTypeName), ExploreParameters.FilteringLogic.AND);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(solutionTypeKind)) {
                solutionTypeRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName, solutionTypeName), ExploreParameters.FilteringLogic.AND);
            }
            solutionTypeRecordEP.setResultNumber(100000);

            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> solutionTypeDefinitionRecordFactsList = ie.discoverFacts(solutionTypeRecordEP);
            for(Fact currentFact:solutionTypeDefinitionRecordFactsList){
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(solutionTypeKind)){
                    currentFact.updateProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeAliasName,solutionTypeAliasName);
                }
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(solutionTypeKind)){
                    currentFact.updateProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeAliasName,solutionTypeAliasName);
                }
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(solutionTypeKind)) {
                    currentFact.updateProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeAliasName,solutionTypeAliasName);
                }
            }
            return true;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static List<SolutionTypePropertyTypeDefinitionVO> getSolutionTypePropertiesInfo(String solutionName, String propertyTypeKind,String propertyTypeName){
        List<SolutionTypePropertyTypeDefinitionVO> solutionFactPropertyTypeDefinitionVOList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){

                ExploreParameters solutionTypePropertyRecordEP = new ExploreParameters();
                solutionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                solutionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeKind, propertyTypeKind), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeName, propertyTypeName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.setResultNumber(100000);

                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionTypePropertyRecordEP);
                if(solutionFactTypePropertyDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionFactTypePropertyDefinitionRecordFactsList){
                        SolutionTypePropertyTypeDefinitionVO currentSolutionFactPropertyTypeDefinitionVO=new SolutionTypePropertyTypeDefinitionVO();
                        currentSolutionFactPropertyTypeDefinitionVO.setSolutionName(solutionName);
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyTypeKind(propertyTypeKind);
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyTypeName(propertyTypeName);
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyName).getPropertyValue().toString());
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyAliasName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyAliasName).getPropertyValue().toString());
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyType(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyType).getPropertyValue().toString());
                        Boolean isMandatory=(Boolean)currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_IsMandatoryProperty).getPropertyValue();
                        Boolean isNullable=(Boolean)currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_IsNullableProperty).getPropertyValue();
                        Boolean isReadOnly=(Boolean)currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_IsReadOnlyProperty).getPropertyValue();
                        currentSolutionFactPropertyTypeDefinitionVO.setMandatory(isMandatory.booleanValue());
                        currentSolutionFactPropertyTypeDefinitionVO.setNullable(isNullable.booleanValue());
                        currentSolutionFactPropertyTypeDefinitionVO.setReadOnly(isReadOnly.booleanValue());
                        if(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertySourceOwner)!=null) {
                            currentSolutionFactPropertyTypeDefinitionVO.setPropertySourceOwner(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertySourceOwner).getPropertyValue().toString());
                        }
                        solutionFactPropertyTypeDefinitionVOList.add(currentSolutionFactPropertyTypeDefinitionVO);
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
        return solutionFactPropertyTypeDefinitionVOList;
    }

    public static boolean checkSolutionTypePropertyDefinitionExistence(String solutionName, String propertyTypeKind, String propertyTypeName, String propertyName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                ExploreParameters solutionTypePropertyRecordEP = new ExploreParameters();
                solutionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                solutionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeKind, propertyTypeKind), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeName, propertyTypeName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyName, propertyName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionTypePropertyRecordEP);

                if(solutionTypePropertyDefinitionRecordFactsList!=null&&solutionTypePropertyDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
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
        return false;
    }

    public static boolean createSolutionTypePropertyDefinition(String solutionName, String propertyTypeKind,String propertyTypeName, String propertyName,
                                                               String propertyAliasName,String propertyType,boolean isMandatory,boolean isReadOnly,boolean isNullable,String sourceOwner){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                FactType solutionTypePropertyFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                TypeProperty solutionNameProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);
                TypeProperty propertyTypeKindProperty=solutionTypePropertyFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeKind, PropertyType.STRING);
                propertyTypeKindProperty.setMandatory(true);
                TypeProperty propertyTypeNameProperty=solutionTypePropertyFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeName, PropertyType.STRING);
                propertyTypeNameProperty.setMandatory(true);
                TypeProperty propertyNameProperty=solutionTypePropertyFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyName, PropertyType.STRING);
                propertyNameProperty.setMandatory(true);
                TypeProperty propertyAliasNameProperty=solutionTypePropertyFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyAliasName, PropertyType.STRING);
                propertyAliasNameProperty.setMandatory(true);
                TypeProperty propertyTypeProperty=solutionTypePropertyFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyType, PropertyType.STRING);
                propertyTypeProperty.setMandatory(true);
                TypeProperty propertySourceOwnerProperty=solutionTypePropertyFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertySourceOwner, PropertyType.STRING);
                propertySourceOwnerProperty.setMandatory(false);
                TypeProperty isMandatoryProperty=solutionTypePropertyFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_IsMandatoryProperty, PropertyType.BOOLEAN);
                isMandatoryProperty.setMandatory(true);
                TypeProperty isNullableProperty=solutionTypePropertyFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_IsNullableProperty, PropertyType.BOOLEAN);
                isNullableProperty.setMandatory(true);
                TypeProperty isReadOnlyProperty=solutionTypePropertyFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_IsReadOnlyProperty, PropertyType.BOOLEAN);
                isReadOnlyProperty.setMandatory(true);
            }
            Fact solutionTypePropertyFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionTypePropertyFactType);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_SolutionName,solutionName);
            solutionTypePropertyFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeKind,propertyTypeKind);
            solutionTypePropertyFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeName,propertyTypeName);
            solutionTypePropertyFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyName,propertyName);
            solutionTypePropertyFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyAliasName,propertyAliasName);
            solutionTypePropertyFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyType,propertyType);
            solutionTypePropertyFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_IsMandatoryProperty,isMandatory);
            solutionTypePropertyFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_IsReadOnlyProperty,isReadOnly);
            solutionTypePropertyFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_IsNullableProperty,isNullable);
            if(sourceOwner!=null) {
                solutionTypePropertyFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertySourceOwner, sourceOwner);
            }
            Fact resultRecord=metaConfigSpace.addFact(solutionTypePropertyFact);
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

    public static boolean deleteSolutionTypePropertyDefinition(String solutionName, String propertyTypeKind,String propertyTypeName, String propertyName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                ExploreParameters solutionTypePropertyRecordEP = new ExploreParameters();
                solutionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                solutionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeKind, propertyTypeKind), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeName, propertyTypeName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyName, propertyName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionTypePropertyRecordEP);
                if(solutionTypePropertyDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionTypePropertyDefinitionRecordFactsList){
                        metaConfigSpace.removeFact(currentFact.getId());
                    }
                }
                return true;
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
        return false;
    }

    public static boolean updateSolutionTypePropertyAliasName(String solutionName, String propertyTypeKind,String propertyTypeName, String propertyName,String propertyAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                ExploreParameters solutionTypePropertyRecordEP = new ExploreParameters();
                solutionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                solutionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeKind, propertyTypeKind), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyTypeName, propertyTypeName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyName, propertyName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionTypePropertyRecordEP);
                if(solutionTypePropertyDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionTypePropertyDefinitionRecordFactsList){
                        currentFact.updateProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_PropertyAliasName,propertyAliasName);
                    }
                }
                return true;
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
        return false;
    }

    public static List<CustomPropertyAliasDefinitionVO> getSolutionCustomPropertyAliasNames(String solutionName){
        List<CustomPropertyAliasDefinitionVO> resultList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType)){
                ExploreParameters customPropertyAliasNameRecordEP = new ExploreParameters();
                customPropertyAliasNameRecordEP.setType(BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType);
                customPropertyAliasNameRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                customPropertyAliasNameRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> customPropertyAliasRecordFact = ie.discoverFacts(customPropertyAliasNameRecordEP);
                if(customPropertyAliasRecordFact!=null){
                    for(Fact currentFact:customPropertyAliasRecordFact){
                        CustomPropertyAliasDefinitionVO currentCustomPropertyAliasDefinitionVO=new CustomPropertyAliasDefinitionVO();
                        currentCustomPropertyAliasDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        currentCustomPropertyAliasDefinitionVO.setCustomPropertyName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyName).getPropertyValue().toString());
                        currentCustomPropertyAliasDefinitionVO.setCustomPropertyType(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyType).getPropertyValue().toString());
                        currentCustomPropertyAliasDefinitionVO.setCustomPropertyAliasName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyAliasName).getPropertyValue().toString());
                        resultList.add(currentCustomPropertyAliasDefinitionVO);
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

    public static boolean checkCustomPropertyAliasDefinitionExistence(String solutionName, String customPropertyName,String customPropertyType){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType)){
                ExploreParameters customPropertyAliasNameRecordEP = new ExploreParameters();
                customPropertyAliasNameRecordEP.setType(BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType);
                customPropertyAliasNameRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                customPropertyAliasNameRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyName, customPropertyName), ExploreParameters.FilteringLogic.AND);
                customPropertyAliasNameRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyType, customPropertyType), ExploreParameters.FilteringLogic.AND);
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

    public static boolean addCustomPropertyAliasDefinition(String solutionName, String customPropertyName,String customPropertyType,String customPropertyAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType)){
                FactType customPropertyAliasNameFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType);
                TypeProperty solutionNameProperty=customPropertyAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName,PropertyType.STRING);
                solutionNameProperty.setMandatory(true);
                TypeProperty customPropertyNameProperty=customPropertyAliasNameFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyName,PropertyType.STRING);
                customPropertyNameProperty.setMandatory(true);
                TypeProperty customPropertyTypeProperty=customPropertyAliasNameFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyType,PropertyType.STRING);
                customPropertyTypeProperty.setMandatory(true);
                TypeProperty customPropertyAliasNameNameProperty=customPropertyAliasNameFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyAliasName,PropertyType.STRING);
                customPropertyAliasNameNameProperty.setMandatory(true);
            }
            Fact typeKind_AliasNameFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_SolutionName,solutionName);
            typeKind_AliasNameFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyName,customPropertyName);
            typeKind_AliasNameFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyType,customPropertyType);
            typeKind_AliasNameFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyAliasName,customPropertyAliasName);
            Fact resultRecord=metaConfigSpace.addFact(typeKind_AliasNameFact);
            if(resultRecord!=null){
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

    public static boolean deleteCustomPropertyAliasDefinition(String solutionName,String customPropertyName,String customPropertyType) {
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType)){
                ExploreParameters customPropertyAliasNameRecordEP = new ExploreParameters();
                customPropertyAliasNameRecordEP.setType(BUSINESSSOLUTION_SolutionCustomPropertyAliasFactType);
                customPropertyAliasNameRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                customPropertyAliasNameRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyName, customPropertyName), ExploreParameters.FilteringLogic.AND);
                customPropertyAliasNameRecordEP.addFilteringItem(new EqualFilteringItem(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_CustomPropertyType, customPropertyType), ExploreParameters.FilteringLogic.AND);
                customPropertyAliasNameRecordEP.setResultNumber(1000000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> customPropertyAliasNameRecordFact = ie.discoverFacts(customPropertyAliasNameRecordEP);
                if(customPropertyAliasNameRecordFact!=null) {
                    for (Fact currentRecordFact : customPropertyAliasNameRecordFact) {
                        metaConfigSpace.removeFact(currentRecordFact.getId());
                    }
                }
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

    public static List<FactTypeDefinitionVO> getFactTypeDefinitionList(String businessSolutionName){
        List<FactTypeDefinitionVO> factTypeDefinitionList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
                solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionFactTypeFactType);
                solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDefinitionRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
                if(solutionDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionDefinitionRecordFactsList){
                        FactTypeDefinitionVO currentFactTypeDefinitionVO=new FactTypeDefinitionVO();
                        currentFactTypeDefinitionVO.setTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeName).getPropertyValue().toString());
                        currentFactTypeDefinitionVO.setTypeAliasName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_FactTypeAliasName).getPropertyValue().toString());
                        currentFactTypeDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        factTypeDefinitionList.add(currentFactTypeDefinitionVO);
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
        return factTypeDefinitionList;
    }

    public static List<DimensionTypeDefinitionVO> getDimensionTypeDefinitionList(String businessSolutionName){
        List<DimensionTypeDefinitionVO> dimensionTypeDefinitionList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
                solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
                solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDefinitionRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
                if(solutionDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionDefinitionRecordFactsList){
                        DimensionTypeDefinitionVO currentDimensionTypeDefinitionVO=new DimensionTypeDefinitionVO();
                        currentDimensionTypeDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        currentDimensionTypeDefinitionVO.setTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeName).getPropertyValue().toString());
                        currentDimensionTypeDefinitionVO.setTypeAliasName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_DimensionTypeAliasName).getPropertyValue().toString());
                        currentDimensionTypeDefinitionVO.setParentTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ParentDimensionTypeName).getPropertyValue().toString());
                        dimensionTypeDefinitionList.add(currentDimensionTypeDefinitionVO);
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
        return dimensionTypeDefinitionList;
    }

    public static List<RelationTypeDefinitionVO> getRelationTypeDefinitionList(String businessSolutionName){
        List<RelationTypeDefinitionVO> relationTypeDefinitionList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
                solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionRelationTypeFactType);
                solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDefinitionRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
                if(solutionDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionDefinitionRecordFactsList){
                        RelationTypeDefinitionVO currentRelationTypeDefinitionVO=new RelationTypeDefinitionVO();
                        currentRelationTypeDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        currentRelationTypeDefinitionVO.setTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName).getPropertyValue().toString());
                        currentRelationTypeDefinitionVO.setTypeAliasName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeAliasName).getPropertyValue().toString());
                        currentRelationTypeDefinitionVO.setParentTypeName(currentFact.getProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_ParentRelationTypeName).getPropertyValue().toString());
                        relationTypeDefinitionList.add(currentRelationTypeDefinitionVO);
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
        return relationTypeDefinitionList;
    }

    public static boolean createCommonDataRelationMappingDefinition(String solutionName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType)){
                FactType dataRelationMappingFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType);
                TypeProperty solutionNameProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);
                TypeProperty sourceDataTypeNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName, PropertyType.STRING);
                sourceDataTypeNameProperty.setMandatory(true);
                TypeProperty sourceDataTypeKindProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind, PropertyType.STRING);
                sourceDataTypeKindProperty.setMandatory(true);
                TypeProperty sourceDataPropertyNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName, PropertyType.STRING);
                sourceDataPropertyNameProperty.setMandatory(true);
                TypeProperty sourceDataPropertyTypeProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType, PropertyType.STRING);
                sourceDataPropertyTypeProperty.setMandatory(true);
                TypeProperty relationTypeNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName, PropertyType.STRING);
                relationTypeNameProperty.setMandatory(true);
                TypeProperty relationDirectionProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationDirection, PropertyType.STRING);
                relationDirectionProperty.setMandatory(true);
                TypeProperty mappingNotExistHandleMethodProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingNotExistHandleMethod, PropertyType.STRING);
                mappingNotExistHandleMethodProperty.setMandatory(true);
                TypeProperty targetDataTypeNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName, PropertyType.STRING);
                targetDataTypeNameProperty.setMandatory(false);
                TypeProperty targetDataTypeKindProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeKind, PropertyType.STRING);
                targetDataTypeKindProperty.setMandatory(true);
                TypeProperty targetDataPropertyNameProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName, PropertyType.STRING);
                targetDataPropertyNameProperty.setMandatory(true);
                TypeProperty targetDataPropertyTypeProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType, PropertyType.STRING);
                targetDataPropertyTypeProperty.setMandatory(true);
                TypeProperty mappingMinValueProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMinValue, PropertyType.STRING);
                mappingMinValueProperty.setMandatory(false);
                TypeProperty mappingMaxValueProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMaxValue, PropertyType.STRING);
                mappingMaxValueProperty.setMandatory(false);
                TypeProperty targetDataPropertyValueProperty=dataRelationMappingFactType.addTypeProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyValue, PropertyType.STRING);
                targetDataPropertyValueProperty.setMandatory(false);
            }

            Fact dataRelationMappingDefinitionFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType);
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_SolutionName,solutionName);
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeName,dataMappingDefinitionVO.getSourceDataTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataTypeKind,dataMappingDefinitionVO.getSourceDataTypeKind());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyName,dataMappingDefinitionVO.getSourceDataPropertyName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_SourceDataPropertyType,dataMappingDefinitionVO.getSourceDataPropertyType());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationTypeName,dataMappingDefinitionVO.getRelationTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_RelationDirection,dataMappingDefinitionVO.getRelationDirection());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingNotExistHandleMethod,dataMappingDefinitionVO.getMappingNotExistHandleMethod());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeName,dataMappingDefinitionVO.getTargetDataTypeName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataTypeKind,dataMappingDefinitionVO.getTargetDataTypeKind());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyName,dataMappingDefinitionVO.getTargetDataPropertyName());
            dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyType,dataMappingDefinitionVO.getTargetDataPropertyType());
            if(dataMappingDefinitionVO.getMinValue()!=null){
                dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMinValue,dataMappingDefinitionVO.getMinValue());
            }
            if(dataMappingDefinitionVO.getMaxValue()!=null){
                dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_MappingMaxValue,dataMappingDefinitionVO.getMaxValue());
            }
            if(dataMappingDefinitionVO.getRangeResult()!=null){
                dataRelationMappingDefinitionFact.setInitProperty(InfoDiscoverSpaceOperationUtil.MetaConfig_PropertyName_TargetDataPropertyValue,dataMappingDefinitionVO.getRangeResult());
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
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType)){
                ExploreParameters dataMaooingDefinitionRecordEP = new ExploreParameters();
                dataMaooingDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType);
                dataMaooingDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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

    public static boolean deleteCommonDataRelationMappingDefinition(String businessSolutionName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType)){
                return false;
            }
            ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
            solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType);
            solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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

    public static boolean createDataDateDimensionMappingDefinition(String solutionName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType)){
                FactType dataRelationMappingFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType);
                TypeProperty solutionNameProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);
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
            Fact dataRelationMappingDefinitionFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType);
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_SolutionName,solutionName);
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

    public static List<DataMappingDefinitionVO> getDataDateDimensionMappingDefinitionList(String businessSolutionName){
        List<DataMappingDefinitionVO> dataRelationMappingDefinitionList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType)){
                ExploreParameters dataMaooingDefinitionRecordEP = new ExploreParameters();
                dataMaooingDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType);
                dataMaooingDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                dataMaooingDefinitionRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> dataMappingDefinitionRecordFactsList = ie.discoverFacts(dataMaooingDefinitionRecordEP);

                if(dataMappingDefinitionRecordFactsList!=null){
                    for(Fact currentFact:dataMappingDefinitionRecordFactsList){
                        DataMappingDefinitionVO currentDataMappingDefinitionVO=new DataMappingDefinitionVO();
                        currentDataMappingDefinitionVO.setSolutionName(businessSolutionName);
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

    public static boolean deleteDataDateDimensionMappingDefinition(String businessSolutionName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType)){
                return false;
            }
            ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
            solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType);
            solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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

    public static boolean createDataPropertiesDuplicateMappingDefinition(String solutionName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType)){
                FactType dataRelationMappingFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType);
                TypeProperty solutionNameProperty=dataRelationMappingFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);
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

            Fact dataRelationMappingDefinitionFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType);
            dataRelationMappingDefinitionFact.setInitProperty(MetaConfig_PropertyName_SolutionName,solutionName);
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

    public static List<DataMappingDefinitionVO> getDataPropertiesDuplicateMappingDefinitionList(String businessSolutionName){
        List<DataMappingDefinitionVO> dataRelationMappingDefinitionList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType)){
                ExploreParameters dataMaooingDefinitionRecordEP = new ExploreParameters();
                dataMaooingDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType);
                dataMaooingDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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

    public static boolean deleteDataPropertiesDuplicateMappingDefinition(String businessSolutionName, DataMappingDefinitionVO dataMappingDefinitionVO){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType)){
                return false;
            }
            ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
            solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType);
            solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType)){
                return false;
            }
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();

            ExploreParameters solutionDefinitionRecordEP1 = new ExploreParameters();
            solutionDefinitionRecordEP1.setType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType);
            solutionDefinitionRecordEP1.setResultNumber(1);
            solutionDefinitionRecordEP1.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));

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
            solutionDefinitionRecordEP2.setType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType);
            solutionDefinitionRecordEP2.setResultNumber(1);
            solutionDefinitionRecordEP2.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));

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
            solutionDefinitionRecordEP3.setType(BUSINESSSOLUTION_SolutionDataRelationMappingDefinitionFactType);
            solutionDefinitionRecordEP3.setResultNumber(1);
            solutionDefinitionRecordEP3.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType)){
                return false;
            }
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            ExploreParameters solutionDefinitionRecordEP1 = new ExploreParameters();
            solutionDefinitionRecordEP1.setType(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType);
            solutionDefinitionRecordEP1.setResultNumber(1);
            solutionDefinitionRecordEP1.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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
            solutionDefinitionRecordEP2.setType(BUSINESSSOLUTION_SolutionDataDateDimensionMappingDefinitionFactType);
            solutionDefinitionRecordEP2.setResultNumber(1);
            solutionDefinitionRecordEP2.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType)){
                return false;
            }
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();

            ExploreParameters solutionDefinitionRecordEP1 = new ExploreParameters();
            solutionDefinitionRecordEP1.setType(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType);
            solutionDefinitionRecordEP1.setResultNumber(1);
            solutionDefinitionRecordEP1.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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
                solutionDefinitionRecordEP2.setType(BUSINESSSOLUTION_SolutionDataPropertiesDuplicateMappingDefinitionFactType);
                solutionDefinitionRecordEP2.setResultNumber(1);
                solutionDefinitionRecordEP2.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
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

    private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();

    public static File generateBusinessSolutionTemplateFile(String solutionName){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        TemplateExporter templateExporter=new TemplateExporter(metaConfigSpaceName,solutionName);
        try {
            String fileFullName=templateExporter.exportSolutionTemplate(tempFileDir);
            return new File(fileFullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean importBusinessSolutionTemplateFromZipFile(String solutionZipFileLocation){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        TemplateImporter templateImporter=new TemplateImporter(metaConfigSpaceName);
        try {
            templateImporter.importSolution(solutionZipFileLocation,false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            File file = new File(solutionZipFileLocation);
            if(file.exists()) {
                try {
                    FileUtils.forceDelete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
