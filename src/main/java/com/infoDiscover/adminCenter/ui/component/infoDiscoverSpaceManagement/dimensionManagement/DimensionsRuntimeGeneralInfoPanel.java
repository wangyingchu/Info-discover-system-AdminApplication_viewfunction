package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.dimensionManagement;

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
 * Created by wangychu on 10/6/16.
 */
public class DimensionsRuntimeGeneralInfoPanel extends VerticalLayout{

    private UserClientInfo currentUserClientInfo;
    private Grid dimensionTypesDataGrid;
    private InfoDiscoverSpaceDimensionsInfoChart infoDiscoverSpaceDimensionsInfoChart;
    private String discoverSpaceName;

    public DimensionsRuntimeGeneralInfoPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        SecondarySectionTitle secondarySectionTitle=new SecondarySectionTitle("维度数据基本信息");
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);
        dimensionTypesDataGrid = new Grid();
        dimensionTypesDataGrid.setSelectionMode(Grid.SelectionMode.NONE);
        dimensionTypesDataGrid.setWidth("100%");
        // Define columns
        dimensionTypesDataGrid.addColumn("维度类型名称", String.class);
        dimensionTypesDataGrid.addColumn("维度类型数据总量", Long.class);

        VerticalLayout generalInfoContainer=new VerticalLayout();
        generalInfoContainer.addComponent(secondarySectionTitle);
        generalInfoContainer.addComponent(dimensionTypesDataGrid);
        elementPlacementLayout.addComponent(generalInfoContainer);

        HorizontalLayout chartsPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");

        elementPlacementLayout.addComponent(chartsPlacementLayout);
        elementPlacementLayout.setComponentAlignment(chartsPlacementLayout, Alignment.MIDDLE_LEFT);

        this.infoDiscoverSpaceDimensionsInfoChart=new InfoDiscoverSpaceDimensionsInfoChart(this.currentUserClientInfo);
        chartsPlacementLayout.addComponent(infoDiscoverSpaceDimensionsInfoChart);
        chartsPlacementLayout.setComponentAlignment(infoDiscoverSpaceDimensionsInfoChart, Alignment.MIDDLE_LEFT);

        VerticalLayout showDetailChartButtonBarContainerLayout=new VerticalLayout();
        Button editDataFieldsButton = new Button();
        editDataFieldsButton.setIcon(VaadinIcons.BAR_CHART_H);
        editDataFieldsButton.setDescription("信息发现空间维度类型数据详细分布");
        editDataFieldsButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editDataFieldsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        editDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String targetWindowUID=discoverSpaceName+"_GlobalDimensionTypesTreeDataCountDetailWindow";
                Window targetWindow=currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(discoverSpaceName,targetWindowUID);
                if(targetWindow!=null){
                    targetWindow.bringToFront();
                    targetWindow.center();
                }else{
                    String chartDataQueryAddress= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+
                            "infoAnalysePages/infoDiscoverSpaceAnalyse/spaceDimensionTypesTreeDataCountTreeMap.html?graphHeight=740&discoverSpace="+discoverSpaceName;
                    String windowTitle="信息发现空间 <b>"+discoverSpaceName+"</b> 维度类型数据详细分布";
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

    public void renderDimensionsRuntimeGeneralInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.infoDiscoverSpaceDimensionsInfoChart.renderDimensionsInfoChart(discoverSpaceStatisticMetrics);
        this.dimensionTypesDataGrid.getContainerDataSource().removeAllItems();
        List<DataTypeStatisticMetrics> dimensionsStatisticMetricsList=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
        for(DataTypeStatisticMetrics currentMetrics:dimensionsStatisticMetricsList){
            String dimensionTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION,"");
            this.dimensionTypesDataGrid.addRow(dimensionTypeName, new Long(currentMetrics.getTypeDataCount()));
        }
        dimensionTypesDataGrid.recalculateColumnWidths();
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }
}
