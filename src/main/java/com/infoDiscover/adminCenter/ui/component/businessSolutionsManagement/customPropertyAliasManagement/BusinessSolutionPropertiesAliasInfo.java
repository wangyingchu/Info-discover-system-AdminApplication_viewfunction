package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.customPropertyAliasManagement;

import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.BusinessSolutionDetail;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.commonUseElement.BusinessSolutionOperationsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 6/28/17.
 */
public class BusinessSolutionPropertiesAliasInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private BusinessSolutionDetail parentBusinessSolutionDetail;
    private String businessSolutionName;
    private BusinessSolutionOperationsBar businessSolutionOperationsBar;
    private CustomPropertyAliasNameDefinitionManagementPanel customPropertyAliasNameDefinitionManagementPanel;

    public BusinessSolutionPropertiesAliasInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        this.businessSolutionOperationsBar=new BusinessSolutionOperationsBar(this.currentUserClientInfo);
        addComponent(businessSolutionOperationsBar);

        customPropertyAliasNameDefinitionManagementPanel=new CustomPropertyAliasNameDefinitionManagementPanel(this.currentUserClientInfo);
        addComponent(customPropertyAliasNameDefinitionManagementPanel);
    }

    public BusinessSolutionDetail getParentBusinessSolutionDetail() {
        return parentBusinessSolutionDetail;
    }

    public void setParentBusinessSolutionDetail(BusinessSolutionDetail parentBusinessSolutionDetail) {
        this.parentBusinessSolutionDetail = parentBusinessSolutionDetail;
    }

    public void renderPropertiesAliasInfo(){
        businessSolutionOperationsBar.setupOperationsBarInfo(getBusinessSolutionName());
        customPropertyAliasNameDefinitionManagementPanel.renderCustomPropertyAliasInfo(getBusinessSolutionName());
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}
