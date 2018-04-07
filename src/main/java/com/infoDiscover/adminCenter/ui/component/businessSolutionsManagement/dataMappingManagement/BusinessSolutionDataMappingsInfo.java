package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dataMappingManagement;

import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.BusinessSolutionDetail;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.commonUseElement.BusinessSolutionOperationsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 5/5/17.
 */
public class BusinessSolutionDataMappingsInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private BusinessSolutionDetail parentBusinessSolutionDetail;
    private String businessSolutionName;
    private BusinessSolutionOperationsBar businessSolutionOperationsBar;
    private DataMappingDefinitionsManagementPanel dataMappingDefinitionsManagementPanel;

    public BusinessSolutionDataMappingsInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        this.businessSolutionOperationsBar=new BusinessSolutionOperationsBar(this.currentUserClientInfo);
        addComponent(businessSolutionOperationsBar);

        this.dataMappingDefinitionsManagementPanel=new DataMappingDefinitionsManagementPanel(this.currentUserClientInfo);
        addComponent(dataMappingDefinitionsManagementPanel);
    }

    public BusinessSolutionDetail getParentBusinessSolutionDetail() {
        return parentBusinessSolutionDetail;
    }

    public void setParentBusinessSolutionDetail(BusinessSolutionDetail parentBusinessSolutionDetail) {
        this.parentBusinessSolutionDetail = parentBusinessSolutionDetail;
    }

    public void renderDataMappingsInfo(){
        businessSolutionOperationsBar.setupOperationsBarInfo(getBusinessSolutionName());
        this.dataMappingDefinitionsManagementPanel.renderDataMappingDefinitionsInfo(getBusinessSolutionName());
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}
