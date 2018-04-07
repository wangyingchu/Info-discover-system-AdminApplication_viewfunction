package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.relationTypeManagement;

import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.BusinessSolutionDetail;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.commonUseElement.BusinessSolutionOperationsBar;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dimensionTypeManagement.DimensionTypeDefinitionsManagementPanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 5/5/17.
 */
public class BusinessSolutionRelationTypesInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private BusinessSolutionDetail parentBusinessSolutionDetail;
    private String businessSolutionName;
    private BusinessSolutionOperationsBar businessSolutionOperationsBar;
    private RelationTypeDefinitionsManagementPanel relationTypeDefinitionsManagementPanel;

    public BusinessSolutionRelationTypesInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        this.businessSolutionOperationsBar=new BusinessSolutionOperationsBar(this.currentUserClientInfo);
        addComponent(businessSolutionOperationsBar);

        this.relationTypeDefinitionsManagementPanel=new RelationTypeDefinitionsManagementPanel(this.currentUserClientInfo);
        addComponent(relationTypeDefinitionsManagementPanel);
    }

    public BusinessSolutionDetail getParentBusinessSolutionDetail() {
        return parentBusinessSolutionDetail;
    }

    public void setParentBusinessSolutionDetail(BusinessSolutionDetail parentBusinessSolutionDetail) {
        this.parentBusinessSolutionDetail = parentBusinessSolutionDetail;
    }

    public void renderRelationTypesInfo(){
        businessSolutionOperationsBar.setupOperationsBarInfo(getBusinessSolutionName());
        relationTypeDefinitionsManagementPanel.renderRelationTypeDefinitionsManagementInfo(getBusinessSolutionName());
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}
