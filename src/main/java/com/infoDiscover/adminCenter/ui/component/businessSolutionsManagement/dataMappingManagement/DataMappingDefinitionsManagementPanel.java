package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dataMappingManagement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 6/30/17.
 */
public class DataMappingDefinitionsManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private CommonDataRelationMappingDefinitionEditPanel commonDataRelationMappingDefinitionEditPanel;
    private DataAndDateDimensionMappingDefinitionEditPanel dataAndDateDimensionMappingDefinitionEditPanel;
    private DataPropertiesDuplicateMappingDefinitionEditorPanel dataPropertiesDuplicateMappingDefinitionEditorPanel;

    public DataMappingDefinitionsManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");

        TabSheet tabs=new TabSheet();
        this.addComponent(tabs);

        VerticalLayout commonDataRelationMappingDefinitionInfoLayout=new VerticalLayout();
        TabSheet.Tab commonDataRelationMappingDefinitionInfoLayoutTab =tabs.addTab(commonDataRelationMappingDefinitionInfoLayout, "常规数据属性关联映射管理");
        commonDataRelationMappingDefinitionInfoLayoutTab.setIcon(FontAwesome.COGS);
        commonDataRelationMappingDefinitionEditPanel=new CommonDataRelationMappingDefinitionEditPanel(this.currentUserClientInfo);
        commonDataRelationMappingDefinitionInfoLayout.addComponent(commonDataRelationMappingDefinitionEditPanel);

        VerticalLayout factAndDateDimensionMappingDefinitionInfoLayout=new VerticalLayout();
        TabSheet.Tab factAndDateDimensionMappingDefinitionInfoLayoutTab =tabs.addTab(factAndDateDimensionMappingDefinitionInfoLayout, "数据与时间维度关联定义管理");
        factAndDateDimensionMappingDefinitionInfoLayoutTab.setIcon(FontAwesome.CLOCK_O);
        dataAndDateDimensionMappingDefinitionEditPanel =new DataAndDateDimensionMappingDefinitionEditPanel(this.currentUserClientInfo);
        factAndDateDimensionMappingDefinitionInfoLayout.addComponent(dataAndDateDimensionMappingDefinitionEditPanel);

        VerticalLayout dataPropertiesDuplicateMappingDefinitionInfoLayout=new VerticalLayout();
        TabSheet.Tab dataPropertiesDuplicateMappingDefinitionInfoLayoutTab =tabs.addTab(dataPropertiesDuplicateMappingDefinitionInfoLayout, "数据属性复制规则定义管理");
        dataPropertiesDuplicateMappingDefinitionInfoLayoutTab.setIcon(FontAwesome.COPY);
        dataPropertiesDuplicateMappingDefinitionEditorPanel=new DataPropertiesDuplicateMappingDefinitionEditorPanel(this.currentUserClientInfo);
        dataPropertiesDuplicateMappingDefinitionInfoLayout.addComponent(dataPropertiesDuplicateMappingDefinitionEditorPanel);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void renderDataMappingDefinitionsInfo(String businessSolutionName){
        setBusinessSolutionName(businessSolutionName);
        commonDataRelationMappingDefinitionEditPanel.setBusinessSolutionName(getBusinessSolutionName());
        commonDataRelationMappingDefinitionEditPanel.renderCommonDataRelationMappingDefinitionInfo(getBusinessSolutionName());
        dataAndDateDimensionMappingDefinitionEditPanel.setBusinessSolutionName(getBusinessSolutionName());
        dataAndDateDimensionMappingDefinitionEditPanel.renderDataAndDateDimensionMappingDefinitionInfo(businessSolutionName);
        dataPropertiesDuplicateMappingDefinitionEditorPanel.setBusinessSolutionName(getBusinessSolutionName());
        dataPropertiesDuplicateMappingDefinitionEditorPanel.renderDataPropertiesDuplicateMappingDefinitionInfo(getBusinessSolutionName());
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}
