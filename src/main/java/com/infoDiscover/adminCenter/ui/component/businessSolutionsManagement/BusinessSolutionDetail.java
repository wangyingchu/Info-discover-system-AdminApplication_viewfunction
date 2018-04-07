package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.customPropertyAliasManagement.BusinessSolutionPropertiesAliasInfo;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dataMappingManagement.BusinessSolutionDataMappingsInfo;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dimensionTypeManagement.BusinessSolutionDimensionTypesInfo;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.factTypeManagement.BusinessSolutionFactTypesInfo;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.relationTypeManagement.BusinessSolutionRelationTypesInfo;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 5/5/17.
 */
public class BusinessSolutionDetail extends VerticalLayout implements View {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private BusinessSolutionDimensionTypesInfo businessSolutionDimensionTypesInfo;
    private BusinessSolutionFactTypesInfo businessSolutionFactTypesInfo;
    private BusinessSolutionRelationTypesInfo businessSolutionRelationTypesInfo;
    private BusinessSolutionDataMappingsInfo businessSolutionDataMappingsInfo;
    private BusinessSolutionPropertiesAliasInfo businessSolutionPropertiesAliasInfo;

    public BusinessSolutionDetail(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        VerticalLayout viewContentContainer = new VerticalLayout();
        viewContentContainer.setMargin(false);
        viewContentContainer.setSpacing(false);
        viewContentContainer.addStyleName("ui_appSubViewContainer");
        this.addComponent(viewContentContainer);

        TabSheet tabs=new TabSheet();
        viewContentContainer.addComponent(tabs);

        VerticalLayout businessSolutionDimensionTypesInfoLayout=new VerticalLayout();
        TabSheet.Tab businessSolutionDimensionTypesInfoLayoutTab =tabs.addTab(businessSolutionDimensionTypesInfoLayout, "方案维度类型管理");
        businessSolutionDimensionTypesInfoLayoutTab.setIcon(FontAwesome.TAGS);
        businessSolutionDimensionTypesInfo=new BusinessSolutionDimensionTypesInfo(this.currentUserClientInfo);
        businessSolutionDimensionTypesInfo.setParentBusinessSolutionDetail(this);
        businessSolutionDimensionTypesInfoLayout.addComponent(businessSolutionDimensionTypesInfo);

        VerticalLayout businessSolutionFactTypesInfoLayout=new VerticalLayout();
        TabSheet.Tab businessSolutionFactTypesInfoLayoutTab =tabs.addTab(businessSolutionFactTypesInfoLayout, "方案事实类型管理");
        businessSolutionFactTypesInfoLayoutTab.setIcon(FontAwesome.CLONE);
        businessSolutionFactTypesInfo=new BusinessSolutionFactTypesInfo(this.currentUserClientInfo);
        businessSolutionFactTypesInfo.setParentBusinessSolutionDetail(this);
        businessSolutionFactTypesInfoLayout.addComponent(businessSolutionFactTypesInfo);

        VerticalLayout businessSolutionRelationTypesInfoLayout=new VerticalLayout();
        TabSheet.Tab businessSolutionRelationTypesInfoLayoutTab =tabs.addTab(businessSolutionRelationTypesInfoLayout, "方案关系类型管理");
        businessSolutionRelationTypesInfoLayoutTab.setIcon(FontAwesome.SHARE_ALT);
        businessSolutionRelationTypesInfo=new BusinessSolutionRelationTypesInfo(this.currentUserClientInfo);
        businessSolutionRelationTypesInfo.setParentBusinessSolutionDetail(this);
        businessSolutionRelationTypesInfoLayout.addComponent(businessSolutionRelationTypesInfo);

        VerticalLayout businessSolutionDataMappingInfoLayout=new VerticalLayout();
        TabSheet.Tab businessSolutionDataMappingInfoLayoutTab =tabs.addTab(businessSolutionDataMappingInfoLayout, "数据关联映射管理");
        businessSolutionDataMappingInfoLayoutTab.setIcon(VaadinIcons.CONTROLLER);
        businessSolutionDataMappingsInfo=new BusinessSolutionDataMappingsInfo(this.currentUserClientInfo);
        businessSolutionDataMappingsInfo.setParentBusinessSolutionDetail(this);
        businessSolutionDataMappingInfoLayout.addComponent(businessSolutionDataMappingsInfo);

        VerticalLayout customPropertyAliasInfoLayout=new VerticalLayout();
        TabSheet.Tab customPropertyAliasInfoLayoutTab =tabs.addTab(customPropertyAliasInfoLayout, "自定义属性别名管理");
        customPropertyAliasInfoLayoutTab.setIcon(VaadinIcons.COINS);
        businessSolutionPropertiesAliasInfo=new BusinessSolutionPropertiesAliasInfo(this.currentUserClientInfo);
        businessSolutionPropertiesAliasInfo.setParentBusinessSolutionDetail(this);
        customPropertyAliasInfoLayout.addComponent(businessSolutionPropertiesAliasInfo);
    }

    public void renderBusinessSolutionDetail(){
        businessSolutionDimensionTypesInfo.setBusinessSolutionName(getBusinessSolutionName());
        businessSolutionDimensionTypesInfo.renderDimensionTypesInfo();

        businessSolutionFactTypesInfo.setBusinessSolutionName(getBusinessSolutionName());
        businessSolutionFactTypesInfo.renderFactTypesInfo();

        businessSolutionRelationTypesInfo.setBusinessSolutionName(getBusinessSolutionName());
        businessSolutionRelationTypesInfo.renderRelationTypesInfo();

        businessSolutionDataMappingsInfo.setBusinessSolutionName(getBusinessSolutionName());
        businessSolutionDataMappingsInfo.renderDataMappingsInfo();

        businessSolutionPropertiesAliasInfo.setBusinessSolutionName(getBusinessSolutionName());
        businessSolutionPropertiesAliasInfo.renderPropertiesAliasInfo();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {}

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}
