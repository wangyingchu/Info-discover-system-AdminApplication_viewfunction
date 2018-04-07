package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangychu on 12/15/16.
 */
public class ProcessingDataListVO {

    private String discoverSpaceName;

    private List<ProcessingDataVO> dimensionDataList;
    private List<ProcessingDataVO> factDataList;
    private List<ProcessingDataVO> relationDataList;

    public ProcessingDataListVO(String discoverSpaceName){
        this.discoverSpaceName=discoverSpaceName;
        this.dimensionDataList=new ArrayList<ProcessingDataVO>();
        this.factDataList=new ArrayList<ProcessingDataVO>();
        this.relationDataList=new ArrayList<ProcessingDataVO>();
    }

    public boolean addProcessingData(ProcessingDataVO processingDataVO){
        if(!this.discoverSpaceName.equals(processingDataVO.getDiscoverSpaceName())){
            return false;
        }
        String dataTypeKind=processingDataVO.getDataTypeKind();
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataTypeKind)){
            if(!checkProcessingDataExistence(getProcessingRelations(),processingDataVO)){
                getProcessingRelations().add(processingDataVO);
                return true;
            }
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(dataTypeKind)){
            if(!checkProcessingDataExistence(getProcessingDimensions(),processingDataVO)){
                getProcessingDimensions().add(processingDataVO);
                return true;
            }
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataTypeKind)){
            if(!checkProcessingDataExistence(getProcessingFacts(),processingDataVO)){
                getProcessingFacts().add(processingDataVO);
                return true;
            }
        }
        return false;
    }

    public boolean removeProcessingData(ProcessingDataVO processingDataVO){
        if(!this.discoverSpaceName.equals(processingDataVO.getDiscoverSpaceName())){
            return false;
        }
        String dataTypeKind=processingDataVO.getDataTypeKind();
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataTypeKind)){
           return removeExistProcessingData(getProcessingRelations(),processingDataVO);
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(dataTypeKind)){
            return removeExistProcessingData(getProcessingDimensions(),processingDataVO);
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataTypeKind)){
            return removeExistProcessingData(getProcessingFacts(),processingDataVO);
        }
        return false;
    }

    public List<ProcessingDataVO> getProcessingDimensions(){
        return this.dimensionDataList;
    }

    public List<ProcessingDataVO> getProcessingFacts(){
        return this.factDataList;
    }

    public List<ProcessingDataVO> getProcessingRelations(){
        return this.relationDataList;
    }

    private boolean checkProcessingDataExistence(List<ProcessingDataVO> processingDataList, ProcessingDataVO processingDataVO){
        for(ProcessingDataVO currentProcessingDataVO:processingDataList){
            if(currentProcessingDataVO.getId().equals(processingDataVO.getId())){
                return true;
            }
        }
        return false;
    }

    private boolean removeExistProcessingData(List<ProcessingDataVO> processingDataList,ProcessingDataVO processingDataVO){
        for(ProcessingDataVO currentProcessingDataVO:processingDataList){
            if(currentProcessingDataVO.getId().equals(processingDataVO.getId())){
                processingDataList.remove(currentProcessingDataVO);
                return true;
            }
        }
        return false;
    }
}
