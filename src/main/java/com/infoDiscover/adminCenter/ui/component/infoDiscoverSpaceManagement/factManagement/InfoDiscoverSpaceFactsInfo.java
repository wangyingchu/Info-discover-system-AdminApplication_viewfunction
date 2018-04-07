package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.factManagement;

import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionActionBarTitle;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceLaunchDataAnalyzeApplicationEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceOpenProcessingDataListEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceDetail;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 10/21/16.
 */
public class InfoDiscoverSpaceFactsInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail;
    private FactsRuntimeGeneralInfoPanel factsRuntimeGeneralInfoPanel;
    private FactTypesManagementPanel factTypesManagementPanel;
    private FactInstancesManagementPanel factInstancesManagementPane;
    private SecondarySectionActionBarTitle secondarySectionActionBarTitle;

    public InfoDiscoverSpaceFactsInfo(UserClientInfo currentUserClientInfo){
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

        this.factsRuntimeGeneralInfoPanel =new FactsRuntimeGeneralInfoPanel(this.currentUserClientInfo);
        TabSheet.Tab factsRuntimeGeneralInfoLayoutTab =tabs.addTab(this.factsRuntimeGeneralInfoPanel, "事实数据运行时信息");
        factsRuntimeGeneralInfoLayoutTab.setIcon(FontAwesome.INFO_CIRCLE);

        this.factTypesManagementPanel =new FactTypesManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab factTypesManagementPanelLayoutTab =tabs.addTab(this.factTypesManagementPanel, "事实类型管理");
        factTypesManagementPanelLayoutTab.setIcon(FontAwesome.TASKS);

        this.factInstancesManagementPane=new FactInstancesManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab factInstancesManagementLayoutTab =tabs.addTab(this.factInstancesManagementPane, "事实数据管理");
        factInstancesManagementLayoutTab.setIcon(FontAwesome.SQUARE_O);

        HorizontalLayout actionButtonsPlacementLayout=new HorizontalLayout();
        addComponent(actionButtonsPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout=new HorizontalLayout();
        actionButtonsSpacingLayout.setWidth("10px");
        actionButtonsPlacementLayout.addComponent(actionButtonsSpacingLayout);

        Button refreshDiscoverSpaceFactsInfoButton=new Button("刷新事实数据信息");
        refreshDiscoverSpaceFactsInfoButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        refreshDiscoverSpaceFactsInfoButton.addStyleName(ValoTheme.BUTTON_TINY);
        refreshDiscoverSpaceFactsInfoButton.setIcon(FontAwesome.REFRESH);

        refreshDiscoverSpaceFactsInfoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(parentInfoDiscoverSpaceDetail!=null){
                    parentInfoDiscoverSpaceDetail.renderDiscoverSpaceDetail();
                }
            }
        });
        actionButtonsPlacementLayout.addComponent(refreshDiscoverSpaceFactsInfoButton);

        VerticalLayout spacingLayout=new VerticalLayout();
        addComponent(spacingLayout);
    }

    public void renderFactsInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.secondarySectionActionBarTitle.updateSectionTitle(this.discoverSpaceName);
        this.factsRuntimeGeneralInfoPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.factsRuntimeGeneralInfoPanel.renderFactsRuntimeGeneralInfo(discoverSpaceStatisticMetrics);
        this.factTypesManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.factTypesManagementPanel.renderFactTypesManagementInfo(discoverSpaceStatisticMetrics);
        this.factInstancesManagementPane.setDiscoverSpaceName(this.discoverSpaceName);
        this.factInstancesManagementPane.renderFactInstancesManagementInfo(discoverSpaceStatisticMetrics);
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void setParentInfoDiscoverSpaceDetail(InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail) {
        this.parentInfoDiscoverSpaceDetail = parentInfoDiscoverSpaceDetail;
    }
}
