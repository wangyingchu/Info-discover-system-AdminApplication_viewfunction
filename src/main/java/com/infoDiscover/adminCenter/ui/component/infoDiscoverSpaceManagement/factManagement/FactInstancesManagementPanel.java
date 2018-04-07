package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.factManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceTypeDataInstanceQueryRequiredEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.CreateTypeDataInstancePanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 10/24/16.
 */
public class FactInstancesManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;

    private MenuBar.Command createFactInstanceMenuItemCommand;
    private MenuBar.Command searchFactInstanceMenuItemCommand;
    private MenuBar.MenuItem createFactInstanceMenuItem;
    private MenuBar.MenuItem searchFactInstanceMenuItem;
    private FormLayout factTypeDataInstanceCountInfoForm;
    private DiscoverSpaceStatisticMetrics currentDiscoverSpaceStatisticMetrics;
    private FactTypeDataInstancesInfoChart factTypeDataInstancesInfoChart;

    public FactInstancesManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int dataCountFormPanelHeight=screenHeight-633;
        Label functionTitle= new Label(FontAwesome.CLONE.getHtml() +" 事实数据操作:", ContentMode.HTML);
        functionTitle.addStyleName(ValoTheme.LABEL_SMALL);
        functionTitle.addStyleName("ui_appStandaloneElementPadding");
        functionTitle.addStyleName("ui_appSectionLightDiv");
        this.addComponent(functionTitle);

        HorizontalLayout factDataOperationContainerLayout=new HorizontalLayout();
        this.addComponent(factDataOperationContainerLayout);

        HorizontalLayout factDataOperationContainerSpaceDivLayout=new HorizontalLayout();
        factDataOperationContainerSpaceDivLayout.setWidth("10px");
        factDataOperationContainerLayout.addComponent(factDataOperationContainerSpaceDivLayout);

        MenuBar factInstanceOperationMenuBar = new MenuBar();
        factInstanceOperationMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        factDataOperationContainerLayout.addComponent(factInstanceOperationMenuBar);

        // Define a common menu command for all the create dimension menu items
        this.createFactInstanceMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedDimensionTypeName=selectedItem.getText();
                executeCreateFactOperation(selectedDimensionTypeName);
            }
        };
        // Define a common menu command for all the search dimension menu items
        this.searchFactInstanceMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedFactTypeName=selectedItem.getText();
                executeSearchFactTypeOperation(selectedFactTypeName);
            }
        };

        // Put operation items in the menu
        this.createFactInstanceMenuItem = factInstanceOperationMenuBar.addItem("创建事实数据", FontAwesome.PLUS_CIRCLE, null);
        this.searchFactInstanceMenuItem = factInstanceOperationMenuBar.addItem("查询事实数据", FontAwesome.SEARCH, null);

        HorizontalLayout factTypeDataInstanceInfoContainerLayout=new HorizontalLayout();
        factTypeDataInstanceInfoContainerLayout.setWidth("100%");
        this.addComponent(factTypeDataInstanceInfoContainerLayout);

        VerticalLayout factTypeSummaryInfo=new VerticalLayout();
        factTypeSummaryInfo.setWidth(350, Unit.PIXELS);
        factTypeDataInstanceInfoContainerLayout.addComponent(factTypeSummaryInfo);

        Label factDataCountLabel=new Label("事实类型数据量");
        factDataCountLabel.setWidth("100%");
        factDataCountLabel.addStyleName("h4");
        factDataCountLabel.addStyleName("ui_appSectionDiv");
        factDataCountLabel.addStyleName("ui_appFadeMargin");
        factTypeSummaryInfo.addComponent(factDataCountLabel);

        this.factTypeDataInstanceCountInfoForm = new FormLayout();
        this.factTypeDataInstanceCountInfoForm.setMargin(false);
        this.factTypeDataInstanceCountInfoForm.setWidth("100%");
        this.factTypeDataInstanceCountInfoForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        Panel dataCountFormContainerPanel = new Panel();
        dataCountFormContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        dataCountFormContainerPanel.setWidth("100%");
        dataCountFormContainerPanel.setHeight(dataCountFormPanelHeight,Unit.PIXELS);
        dataCountFormContainerPanel.setContent(this.factTypeDataInstanceCountInfoForm);
        factTypeSummaryInfo.addComponent(dataCountFormContainerPanel);

        this.factTypeDataInstancesInfoChart=new FactTypeDataInstancesInfoChart(this.currentUserClientInfo);
        factTypeDataInstanceInfoContainerLayout.addComponent(this.factTypeDataInstancesInfoChart);
        factTypeDataInstanceInfoContainerLayout.setComponentAlignment(this.factTypeDataInstancesInfoChart, Alignment.TOP_LEFT);
        factTypeDataInstanceInfoContainerLayout.setExpandRatio(this.factTypeDataInstancesInfoChart, 1.0F);
        VerticalLayout spacingLayout=new VerticalLayout();
        addComponent(spacingLayout);
    }

    public void renderFactInstancesManagementInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.factTypeDataInstancesInfoChart.cleanChartDisplay();
        this.createFactInstanceMenuItem.removeChildren();
        this.searchFactInstanceMenuItem.removeChildren();
        this.factTypeDataInstanceCountInfoForm.removeAllComponents();
        this.currentDiscoverSpaceStatisticMetrics=discoverSpaceStatisticMetrics;
        this.setFactInstanceOperationsUIElements();
        this.factTypeDataInstancesInfoChart.renderFactTypeDataInstancesInfoChart(discoverSpaceStatisticMetrics);
    }

    private void setFactInstanceOperationsUIElements(){
        this.createFactInstanceMenuItem.removeChildren();
        this.searchFactInstanceMenuItem.removeChildren();
        List<DataTypeStatisticMetrics> factTypeStatisticMetrics=this.currentDiscoverSpaceStatisticMetrics.getFactsStatisticMetrics();
        if(factTypeStatisticMetrics!=null){
            searchFactInstanceMenuItem.addItem(InfoDiscoverEngineConstant.FACT_ROOTCLASSNAME, null, searchFactInstanceMenuItemCommand);
            for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:factTypeStatisticMetrics){
                String factTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_FACT,"");
                long factTypeDataCount=currentDataTypeStatisticMetrics.getTypeDataCount();
                createFactInstanceMenuItem.addItem(factTypeName, null, createFactInstanceMenuItemCommand);
                searchFactInstanceMenuItem.addItem(factTypeName, null, searchFactInstanceMenuItemCommand);
                TextField currentDimensionTypeDataSize = new TextField(factTypeName);
                currentDimensionTypeDataSize.setValue(""+factTypeDataCount);
                currentDimensionTypeDataSize.setRequired(false);
                currentDimensionTypeDataSize.setReadOnly(true);
                this.factTypeDataInstanceCountInfoForm.addComponent(currentDimensionTypeDataSize);
            }
        }
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    private void executeCreateFactOperation(String dimensionType){
        CreateTypeDataInstancePanel createTypeDataInstancePanel=new CreateTypeDataInstancePanel(this.currentUserClientInfo);
        createTypeDataInstancePanel.setDiscoverSpaceName(this.getDiscoverSpaceName());
        createTypeDataInstancePanel.setDataInstanceTypeName(dimensionType);
        createTypeDataInstancePanel.setDataInstanceTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT);
        final Window window = new Window();
        window.setWidth(550.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(createTypeDataInstancePanel);
        createTypeDataInstancePanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    private void executeSearchFactTypeOperation(String factType){
        DiscoverSpaceTypeDataInstanceQueryRequiredEvent currentQueryEvent=new DiscoverSpaceTypeDataInstanceQueryRequiredEvent();
        currentQueryEvent.setDiscoverSpaceName(this.getDiscoverSpaceName());
        currentQueryEvent.setDataInstanceTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT);
        currentQueryEvent.setDataInstanceTypeName(factType);
        this.currentUserClientInfo.getEventBlackBoard().fire(currentQueryEvent);
    }
}
