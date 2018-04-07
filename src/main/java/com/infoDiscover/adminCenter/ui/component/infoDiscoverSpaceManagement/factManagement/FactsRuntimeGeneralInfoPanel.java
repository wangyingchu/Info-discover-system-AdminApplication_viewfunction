package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.factManagement;

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
 * Created by wangychu on 10/21/16.
 */
public class FactsRuntimeGeneralInfoPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Grid factTypesDataGrid;
    private InfoDiscoverSpaceFactsInfoChart infoDiscoverSpaceFactsInfoChart;
    private String discoverSpaceName;

    public FactsRuntimeGeneralInfoPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        SecondarySectionTitle secondarySectionTitle=new SecondarySectionTitle("事实数据基本信息");
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);
        factTypesDataGrid = new Grid();
        factTypesDataGrid.setSelectionMode(Grid.SelectionMode.NONE);
        factTypesDataGrid.setWidth("100%");
        // Define columns
        factTypesDataGrid.addColumn("事实类型名称", String.class);
        factTypesDataGrid.addColumn("事实类型数据总量", Long.class);

        VerticalLayout generalInfoContainer=new VerticalLayout();
        generalInfoContainer.addComponent(secondarySectionTitle);
        generalInfoContainer.addComponent(factTypesDataGrid);
        elementPlacementLayout.addComponent(generalInfoContainer);

        HorizontalLayout chartsPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");

        elementPlacementLayout.addComponent(chartsPlacementLayout);
        elementPlacementLayout.setComponentAlignment(chartsPlacementLayout, Alignment.MIDDLE_LEFT);

        infoDiscoverSpaceFactsInfoChart=new InfoDiscoverSpaceFactsInfoChart(this.currentUserClientInfo);
        chartsPlacementLayout.addComponent(infoDiscoverSpaceFactsInfoChart);
        chartsPlacementLayout.setComponentAlignment(infoDiscoverSpaceFactsInfoChart, Alignment.MIDDLE_LEFT);

        VerticalLayout showDetailChartButtonBarContainerLayout=new VerticalLayout();
        Button editDataFieldsButton = new Button();
        editDataFieldsButton.setIcon(VaadinIcons.PIE_CHART);
        editDataFieldsButton.setDescription("信息发现空间事实类型数据详细占比");
        editDataFieldsButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editDataFieldsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        editDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String targetWindowUID=discoverSpaceName+"_GlobalFactTypesTreeDataCountDetailWindow";
                Window targetWindow=currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(discoverSpaceName,targetWindowUID);
                if(targetWindow!=null){
                    targetWindow.bringToFront();
                    targetWindow.center();
                }else{
                    String chartDataQueryAddress= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+
                            "infoAnalysePages/infoDiscoverSpaceAnalyse/spaceFactTypesDataOccupiesComparedPieMap.html?graphHeight=600&discoverSpace="+discoverSpaceName;
                    String windowTitle="信息发现空间 <b>"+discoverSpaceName+"</b> 事实类型数据详细占比";
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

    public void renderFactsRuntimeGeneralInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.infoDiscoverSpaceFactsInfoChart.renderFactsInfoChart(discoverSpaceStatisticMetrics);
        this.factTypesDataGrid.getContainerDataSource().removeAllItems();
        List<DataTypeStatisticMetrics> factsStatisticMetricsList=discoverSpaceStatisticMetrics.getFactsStatisticMetrics();
        for(DataTypeStatisticMetrics currentMetrics:factsStatisticMetricsList){
            String factTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_FACT,"");
            this.factTypesDataGrid.addRow(factTypeName, new Long(currentMetrics.getTypeDataCount()));
        }
        factTypesDataGrid.recalculateColumnWidths();
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }
}
