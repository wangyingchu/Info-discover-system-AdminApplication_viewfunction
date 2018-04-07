package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dimensionTypeManagement;

import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.BusinessSolutionDetail;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.commonUseElement.BusinessSolutionOperationsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 5/5/17.
 */
public class BusinessSolutionDimensionTypesInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private BusinessSolutionDetail parentBusinessSolutionDetail;
    private String businessSolutionName;
    private BusinessSolutionOperationsBar businessSolutionOperationsBar;
    private DimensionTypeDefinitionsManagementPanel dimensionTypeDefinitionsManagementPanel;

    public BusinessSolutionDimensionTypesInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        this.businessSolutionOperationsBar=new BusinessSolutionOperationsBar(this.currentUserClientInfo);
        addComponent(businessSolutionOperationsBar);

        this.dimensionTypeDefinitionsManagementPanel=new DimensionTypeDefinitionsManagementPanel(this.currentUserClientInfo);
        addComponent(dimensionTypeDefinitionsManagementPanel);
    }

    public BusinessSolutionDetail getParentBusinessSolutionDetail() {
        return parentBusinessSolutionDetail;
    }

    public void setParentBusinessSolutionDetail(BusinessSolutionDetail parentBusinessSolutionDetail) {
        this.parentBusinessSolutionDetail = parentBusinessSolutionDetail;
    }

    public void renderDimensionTypesInfo(){
        businessSolutionOperationsBar.setupOperationsBarInfo(getBusinessSolutionName());
        dimensionTypeDefinitionsManagementPanel.renderDimensionTypeDefinitionsManagementInfo(getBusinessSolutionName());
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}
