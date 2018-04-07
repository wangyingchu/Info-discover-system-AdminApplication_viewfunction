package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 10/25/16.
 */
public class RelationsRuntimeGeneralInfoPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Grid relationTypesDataGrid;
    private InfoDiscoverSpaceRelationsInfoChart infoDiscoverSpaceRelationsInfoChart;
    private String discoverSpaceName;

    public RelationsRuntimeGeneralInfoPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        SecondarySectionTitle secondarySectionTitle=new SecondarySectionTitle("关系数据基本信息");
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);
        relationTypesDataGrid = new Grid();
        relationTypesDataGrid.setSelectionMode(Grid.SelectionMode.NONE);
        relationTypesDataGrid.setWidth("100%");
        // Define columns
        relationTypesDataGrid.addColumn("关系类型名称", String.class);
        relationTypesDataGrid.addColumn("关系类型数据总量", Long.class);

        VerticalLayout generalInfoContainer=new VerticalLayout();
        generalInfoContainer.addComponent(secondarySectionTitle);
        generalInfoContainer.addComponent(relationTypesDataGrid);
        elementPlacementLayout.addComponent(generalInfoContainer);

        HorizontalLayout chartsPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");

        elementPlacementLayout.addComponent(chartsPlacementLayout);
        elementPlacementLayout.setComponentAlignment(chartsPlacementLayout, Alignment.MIDDLE_LEFT);

        this.infoDiscoverSpaceRelationsInfoChart=new InfoDiscoverSpaceRelationsInfoChart(this.currentUserClientInfo);
        chartsPlacementLayout.addComponent(this.infoDiscoverSpaceRelationsInfoChart);
        chartsPlacementLayout.setComponentAlignment(this.infoDiscoverSpaceRelationsInfoChart, Alignment.MIDDLE_LEFT);

        VerticalLayout showDetailChartButtonBarContainerLayout=new VerticalLayout();
        Button editDataFieldsButton = new Button();
        editDataFieldsButton.setIcon(VaadinIcons.BAR_CHART_H);
        editDataFieldsButton.setDescription("信息发现空间关系类型数据详细分布");
        editDataFieldsButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editDataFieldsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        editDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String targetWindowUID=discoverSpaceName+"_GlobalRelationTypesTreeDataCountDetailWindow";
                Window targetWindow=currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(discoverSpaceName,targetWindowUID);
                if(targetWindow!=null){
                    targetWindow.bringToFront();
                    targetWindow.center();
                }else{
                    String chartDataQueryAddress= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+
                            "infoAnalysePages/infoDiscoverSpaceAnalyse/spaceRelationTypesTreeDataCountTreeMap.html?graphHeight=740&discoverSpace="+discoverSpaceName;
                    String windowTitle="信息发现空间 <b>"+discoverSpaceName+"</b> 关系类型数据详细分布";
                    BrowserFrame dataRelationGraphBrowserFrame=new BrowserFrame("",new ExternalResource(chartDataQueryAddress));
                    dataRelationGraphBrowserFrame.setSizeFull();
                    final Window window = new Window(UICommonElementsUtil.generateMovableWindowTitleWithFormat(windowTitle));
                    window.setWidth(1000, Unit.PIXELS);
                    window.setHeight(800,Unit.PIXELS);
                    window.setCaptionAsHtml(true);
                    window.setResizable(false);
                    window.setDraggable(true);
                    window.setModal(false);
                    window.center();
                    window.setContent(dataRelationGraphBrowserFrame);
                    window.addCloseListener(new Window.CloseListener() {
                        @Override
                        public void windowClose(Window.CloseEvent closeEvent) {
                            currentUserClientInfo.getRuntimeWindowsRepository().removeExistingWindow(discoverSpaceName,targetWindowUID);
                        }
                    });
                    currentUserClientInfo.getRuntimeWindowsRepository().addNewWindow(discoverSpaceName,targetWindowUID,window);
                    UI.getCurrent().addWindow(window);
                }
            }
        });

        showDetailChartButtonBarContainerLayout.addComponent(editDataFieldsButton);
        chartsPlacementLayout.addComponent(showDetailChartButtonBarContainerLayout);
        chartsPlacementLayout.setComponentAlignment(showDetailChartButtonBarContainerLayout, Alignment.TOP_RIGHT);
    }

    public void renderRelationsRuntimeGeneralInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.infoDiscoverSpaceRelationsInfoChart.renderRelationsInfoChart(discoverSpaceStatisticMetrics);
        this.relationTypesDataGrid.getContainerDataSource().removeAllItems();
        List<DataTypeStatisticMetrics> relationsStatisticMetricsList=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
        for(DataTypeStatisticMetrics currentMetrics:relationsStatisticMetricsList){
            String relationTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            this.relationTypesDataGrid.addRow(relationTypeName, new Long(currentMetrics.getTypeDataCount()));
        }
        relationTypesDataGrid.recalculateColumnWidths();
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }
}
