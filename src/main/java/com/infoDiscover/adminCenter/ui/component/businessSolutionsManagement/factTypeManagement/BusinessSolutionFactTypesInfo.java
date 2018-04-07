package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.factTypeManagement;

import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.BusinessSolutionDetail;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.commonUseElement.BusinessSolutionOperationsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;

import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 5/5/17.
 */
public class BusinessSolutionFactTypesInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private BusinessSolutionDetail parentBusinessSolutionDetail;
    private String businessSolutionName;
    private BusinessSolutionOperationsBar businessSolutionOperationsBar;
    private FactTypeDefinitionsManagementPanel factTypeDefinitionsManagementPanel;

    public BusinessSolutionFactTypesInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        this.businessSolutionOperationsBar=new BusinessSolutionOperationsBar(this.currentUserClientInfo);
        addComponent(businessSolutionOperationsBar);

        factTypeDefinitionsManagementPanel=new FactTypeDefinitionsManagementPanel(this.currentUserClientInfo);
        addComponent(factTypeDefinitionsManagementPanel);
    }

    public BusinessSolutionDetail getParentBusinessSolutionDetail() {
        return parentBusinessSolutionDetail;
    }

    public void setParentBusinessSolutionDetail(BusinessSolutionDetail parentBusinessSolutionDetail) {
        this.parentBusinessSolutionDetail = parentBusinessSolutionDetail;
    }

    public void renderFactTypesInfo(){
        businessSolutionOperationsBar.setupOperationsBarInfo(getBusinessSolutionName());
        factTypeDefinitionsManagementPanel.renderFactTypeDefinitionsManagementInfo(getBusinessSolutionName());
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}
