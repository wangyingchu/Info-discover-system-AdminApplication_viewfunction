package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.businessDataDefinitionManagement;

import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionActionBarTitle;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceDetail;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceLaunchDataAnalyzeApplicationEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceOpenProcessingDataListEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 25/05/2017.
 */
public class InfoDiscoverSpaceBusinessDataDefinitionsInfo  extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail;
    private SecondarySectionActionBarTitle secondarySectionActionBarTitle;
    private CustomPropertyAliasNameManagementPanel customPropertyAliasNameManagementPanel;
    private CommonDataRelationMappingManagementPanel commonDataRelationMappingManagementPanel;
    private DataAndDateDimensionMappingManagementPanel dataAndDateDimensionMappingManagementPanel;
    private DataPropertiesDuplicateMappingManagementPanel dataPropertiesDuplicateMappingManagementPanel;

    public InfoDiscoverSpaceBusinessDataDefinitionsInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        Button openProcessingDataListButton = new Button("待处理数据");
        openProcessingDataListButton.setIcon(VaadinIcons.MAILBOX);
        openProcessingDataListButton.setDescription("显示待处理数据列表");
        openProcessingDataListButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        openProcessingDataListButton.addStyleName(ValoTheme.BUTTON_SMALL);
        openProcessingDataListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DiscoverSpaceOpenProcessingDataListEvent discoverSpaceOpenProcessingDataListEvent =new DiscoverSpaceOpenProcessingDataListEvent(discoverSpaceName);
                currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceOpenProcessingDataListEvent);
            }
        });

        Button launchDataAnalyzeApplicationButton = new Button("信息分析发现应用");
        launchDataAnalyzeApplicationButton.setIcon(VaadinIcons.CHART_TIMELINE);
        launchDataAnalyzeApplicationButton.setDescription("启动信息分析发现应用系统");
        launchDataAnalyzeApplicationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        launchDataAnalyzeApplicationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        launchDataAnalyzeApplicationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DiscoverSpaceLaunchDataAnalyzeApplicationEvent discoverSpaceLaunchDataAnalyzeApplicationEvent =new DiscoverSpaceLaunchDataAnalyzeApplicationEvent(discoverSpaceName);
                currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceLaunchDataAnalyzeApplicationEvent);
            }
        });

        secondarySectionActionBarTitle=new SecondarySectionActionBarTitle("-------",new Button[]{openProcessingDataListButton,launchDataAnalyzeApplicationButton});
        addComponent(secondarySectionActionBarTitle);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);

        this.customPropertyAliasNameManagementPanel =new CustomPropertyAliasNameManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab customPropertyAliasManagementPanelTab =tabs.addTab(this.customPropertyAliasNameManagementPanel, "自定义属性别名管理");
        customPropertyAliasManagementPanelTab.setIcon(VaadinIcons.COINS);

        this.commonDataRelationMappingManagementPanel=new CommonDataRelationMappingManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab commonDataRelationMappingDefinitionInfoLayoutTab =tabs.addTab(this.commonDataRelationMappingManagementPanel, "常规数据属性关联映射管理");
        commonDataRelationMappingDefinitionInfoLayoutTab.setIcon(FontAwesome.COGS);

        this.dataAndDateDimensionMappingManagementPanel=new DataAndDateDimensionMappingManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab factAndDateDimensionMappingDefinitionInfoLayoutTab =tabs.addTab(this.dataAndDateDimensionMappingManagementPanel, "数据与时间维度关联定义管理");
        factAndDateDimensionMappingDefinitionInfoLayoutTab.setIcon(FontAwesome.CLOCK_O);

        this.dataPropertiesDuplicateMappingManagementPanel=new DataPropertiesDuplicateMappingManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab dataPropertiesDuplicateMappingDefinitionInfoLayoutTab =tabs.addTab(this.dataPropertiesDuplicateMappingManagementPanel, "数据属性复制规则定义管理");
        dataPropertiesDuplicateMappingDefinitionInfoLayoutTab.setIcon(FontAwesome.COPY);
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void setParentInfoDiscoverSpaceDetail(InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail) {
        this.parentInfoDiscoverSpaceDetail = parentInfoDiscoverSpaceDetail;
    }

    public void renderBusinessDataDefinitionsInfo(){
        this.secondarySectionActionBarTitle.updateSectionTitle(this.discoverSpaceName);
        this.customPropertyAliasNameManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.customPropertyAliasNameManagementPanel.renderCustomPropertyAliasInfo();
        this.commonDataRelationMappingManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.commonDataRelationMappingManagementPanel.renderCommonDataRelationMappingInfo();
        this.dataAndDateDimensionMappingManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.dataAndDateDimensionMappingManagementPanel.renderDataAndDateDimensionMappingInfo();
        this.dataPropertiesDuplicateMappingManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.dataPropertiesDuplicateMappingManagementPanel.renderDataPropertiesDuplicateMappingInfo();
    }
}
