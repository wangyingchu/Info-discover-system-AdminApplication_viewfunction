package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.*;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceRemoveProcessingDataEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement.VisualizationAnalyzePanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationChangeListener;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wangychu on 11/7/16.
 */
public class TypeDataInstanceList extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Label queryResultCountLabel;
    private Button querySQLButton;
    private Table typeDataInstanceTable;
    private String dataInstanceTypeKind;
    private String dataInstanceTypeName;
    private String discoverSpaceName;
    private String querySQL="";
    private SimpleDateFormat dateTypePropertyFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private HorizontalLayout paginationContainerLayout;
    private int tablePageSize=20;
    private int subWindowsXPositionOffset;
    private int subWindowsYPositionOffset;
    private int currentDataInstancesCount;
    private Pagination typeDataInstancePagination;
    private  MenuBar graphDisplaySelectorMenuBar;

    public TypeDataInstanceList(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        this.setWidth(100,Unit.PERCENTAGE);
        this.setSubWindowsYPositionOffset(20);
        this.setSubWindowsYPositionOffset(20);
        setSpacing(true);
        setMargin(true);

        HorizontalLayout queryResultSummaryInfoContainerLayout=new HorizontalLayout();
        addComponent(queryResultSummaryInfoContainerLayout);
        Label queryResultCountInfo=new Label(FontAwesome.LIST_OL.getHtml()+" 类型数据总量: ", ContentMode.HTML);
        queryResultCountInfo.addStyleName(ValoTheme.LABEL_TINY);
        queryResultSummaryInfoContainerLayout.addComponent(queryResultCountInfo);
        queryResultSummaryInfoContainerLayout.setComponentAlignment(queryResultCountInfo,Alignment.MIDDLE_LEFT);
        this.queryResultCountLabel=new Label("--");
        this.queryResultCountLabel.addStyleName("ui_appFriendlyElement");
        queryResultSummaryInfoContainerLayout.addComponent(this.queryResultCountLabel);
        queryResultSummaryInfoContainerLayout.setComponentAlignment(this.queryResultCountLabel,Alignment.MIDDLE_LEFT);

        MenuBar.Command visualizationAnalyzeMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedCommandName=selectedItem.getText();
                launchVisualizationAnalyzeUI(selectedCommandName);
            }
        };

        this.graphDisplaySelectorMenuBar = new MenuBar();
        this.graphDisplaySelectorMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        this.graphDisplaySelectorMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
        MenuBar.MenuItem visualizationAnalyzeRootItem=this.graphDisplaySelectorMenuBar.addItem("查询结果可视化分析", VaadinIcons.CHART_GRID, null);
        visualizationAnalyzeRootItem.addItem(VisualizationAnalyzePanel.BubbleAnalyzeChart, FontAwesome.CIRCLE_O, visualizationAnalyzeMenuItemCommand);
        visualizationAnalyzeRootItem.addItem(VisualizationAnalyzePanel.Values3DAnalyzeChart, VaadinIcons.GLOBE_WIRE, visualizationAnalyzeMenuItemCommand);
        visualizationAnalyzeRootItem.addItem(VisualizationAnalyzePanel.Values3DPlusColorAnalyzeChart, VaadinIcons.CHART_3D, visualizationAnalyzeMenuItemCommand);
        visualizationAnalyzeRootItem.addItem(VisualizationAnalyzePanel.TimeValuesAnalyzeChart, VaadinIcons.SPLINE_CHART, visualizationAnalyzeMenuItemCommand);
        visualizationAnalyzeRootItem.addItem(VisualizationAnalyzePanel.GeographicalCoordinatesAnalyzeChart, VaadinIcons.MAP_MARKER, visualizationAnalyzeMenuItemCommand);
        MenuBar.MenuItem scatterChartAnalyzeRootItem= visualizationAnalyzeRootItem.addItem(VisualizationAnalyzePanel.ScatterAnalyzeChart, VaadinIcons.SCATTER_CHART, null);
        scatterChartAnalyzeRootItem.addItem(VisualizationAnalyzePanel.Scatter2DAnalyzeChart, null, visualizationAnalyzeMenuItemCommand);
        scatterChartAnalyzeRootItem.addItem(VisualizationAnalyzePanel.Scatter3DAnalyzeChart, null, visualizationAnalyzeMenuItemCommand);
        scatterChartAnalyzeRootItem.addItem(VisualizationAnalyzePanel.Scatter2DWithMathAnalyzeChart, null, visualizationAnalyzeMenuItemCommand);
        scatterChartAnalyzeRootItem.addItem(VisualizationAnalyzePanel.Scatter2DPlusSizeAnalyzeChart, null, visualizationAnalyzeMenuItemCommand);

        this.graphDisplaySelectorMenuBar.setEnabled(false);
        queryResultSummaryInfoContainerLayout.addComponent(graphDisplaySelectorMenuBar);

        this.querySQLButton=new Button("查询条件 SQL");
        this.querySQLButton.setIcon(FontAwesome.FILE_TEXT_O);
        this.querySQLButton.addStyleName(ValoTheme.BUTTON_SMALL);
        this.querySQLButton.addStyleName(ValoTheme.BUTTON_LINK);
        this.querySQLButton.setEnabled(false);
        this.querySQLButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showQuerySQL();
            }
        });
        queryResultSummaryInfoContainerLayout.addComponent(this.querySQLButton);

        this.typeDataInstanceTable=new Table();
        this.typeDataInstanceTable.setWidth(100, Unit.PERCENTAGE);
        this.typeDataInstanceTable.setSelectable(true);
        this.typeDataInstanceTable.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        addComponent(this.typeDataInstanceTable);

        this.paginationContainerLayout=new HorizontalLayout();
        this.paginationContainerLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(this.paginationContainerLayout);
    }

    public void setQuerySQL(String querySQL){
        this.querySQL=querySQL;
    }

    public void setTypeDataInstanceListHeight(int tableHeight){
        this.typeDataInstanceTable.setHeight(tableHeight,Unit.PIXELS);
    }

    public void renderTypeDataInstanceList(List<PropertyTypeVO> queryParameters,List<MeasurableValueVO> queryResults){
        this.querySQLButton.setEnabled(true);
        this.graphDisplaySelectorMenuBar.setEnabled(true);
        this.currentDataInstancesCount=queryResults.size();
        this.queryResultCountLabel.setValue(""+this.currentDataInstancesCount);

        List<PropertyTypeVO> typePropertiesInfoList=null;
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
            typePropertiesInfoList=InfoDiscoverSpaceOperationUtil.retrieveDimensionTypePropertiesInfo(getDiscoverSpaceName(),getDataInstanceTypeName());
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
            typePropertiesInfoList=InfoDiscoverSpaceOperationUtil.retrieveFactTypePropertiesInfo(getDiscoverSpaceName(),getDataInstanceTypeName());
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(getDataInstanceTypeKind())){
            typePropertiesInfoList=InfoDiscoverSpaceOperationUtil.retrieveRelationTypePropertiesInfo(getDiscoverSpaceName(),getDataInstanceTypeName());
        }
        if(queryParameters!=null) {
            setAdditionalQueryParams(typePropertiesInfoList, queryParameters);
        }

        Container dataContainer=this.typeDataInstanceTable.getContainerDataSource();
        dataContainer.removeAllItems();
        Container queryResultDataContainer = new IndexedContainer();
        this.typeDataInstanceTable.setContainerDataSource(queryResultDataContainer);
        this.typeDataInstanceTable.addContainerProperty(" 操作",TypeDataInstanceTableRowActions.class,null);
        this.typeDataInstanceTable.setColumnIcon(" 操作",FontAwesome.WRENCH);
        this.typeDataInstanceTable.setColumnWidth(" 操作", 130);
        this.typeDataInstanceTable.addContainerProperty(" ID",String.class,"");
        this.typeDataInstanceTable.setColumnIcon(" ID",FontAwesome.KEY);
        if(typePropertiesInfoList!=null){
            for(PropertyTypeVO currentPropertyTypeVO:typePropertiesInfoList){
                this.typeDataInstanceTable.addContainerProperty(currentPropertyTypeVO.getPropertyName(), String.class, "");
            }
        }
        typeDataInstanceTable.setPageLength(this.getTablePageSize());

        this.paginationContainerLayout.removeAllComponents();
        int startPage=queryResults.size()>0?1:0;
        typeDataInstancePagination=createPagination(queryResults.size(), startPage);
        typeDataInstancePagination.setItemsPerPageVisible(false);
        typeDataInstancePagination.addPageChangeListener(new PaginationChangeListener() {
            @Override
            public void changed(PaginationResource event) {
                //typeDataInstanceTable.setPageLength(event.limit());
                typeDataInstanceTable.setCurrentPageFirstItemIndex(event.offset());
            }
        });
        this.paginationContainerLayout.addComponent(typeDataInstancePagination);

        if (queryResults!=null){
            for(int i=0;i<queryResults.size();i++){
                MeasurableValueVO currentMeasurableValueVO=queryResults.get(i);
                Item newRecord=this.typeDataInstanceTable.addItem("typeInstance_index_"+i);
                TypeDataInstanceTableRowActions typeDataInstanceTableRowActions=new TypeDataInstanceTableRowActions(this.currentUserClientInfo);
                typeDataInstanceTableRowActions.setMeasurableValue(currentMeasurableValueVO);
                typeDataInstanceTableRowActions.setContainerTypeDataInstanceList(this);
                typeDataInstanceTableRowActions.setDataItemIdInTypeDataInstanceList("typeInstance_index_"+i);
                newRecord.getItemProperty(" 操作").setValue(typeDataInstanceTableRowActions);
                newRecord.getItemProperty(" ID").setValue(currentMeasurableValueVO.getId());
                for(PropertyTypeVO currentPropertyTypeVO:typePropertiesInfoList){
                    PropertyValueVO currentPropertyValueVO=currentMeasurableValueVO.getPropertyValue(currentPropertyTypeVO.getPropertyName());
                    if(currentPropertyValueVO!=null&&currentPropertyValueVO.getPropertyValue()!=null) {
                        Object propertyValue=currentPropertyValueVO.getPropertyValue();
                        if(propertyValue instanceof Date){
                            String dateValue=dateTypePropertyFormat.format(propertyValue);
                            newRecord.getItemProperty(currentPropertyTypeVO.getPropertyName()).setValue(dateValue);
                        }else{
                            newRecord.getItemProperty(currentPropertyTypeVO.getPropertyName()).setValue(""+propertyValue);
                        }
                    }
                }
            }
        }
    }

    private void setAdditionalQueryParams(List<PropertyTypeVO> typePropertiesInfoList,List<PropertyTypeVO> additionalQueryParameters){
        for(PropertyTypeVO currentAdditionalQueryParam:additionalQueryParameters){
            boolean isExistParam=false;
            for(PropertyTypeVO currentTypeProperty:typePropertiesInfoList){
                if(currentTypeProperty.getPropertyName().equals(currentAdditionalQueryParam.getPropertyName())){
                    isExistParam=true;
                }
            }
            if(!isExistParam){
                typePropertiesInfoList.add(currentAdditionalQueryParam);
            }
        }
    }

    public String getDataInstanceTypeKind() {
        return dataInstanceTypeKind;
    }

    public void setDataInstanceTypeKind(String dataInstanceTypeKind) {
        this.dataInstanceTypeKind = dataInstanceTypeKind;
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    private void showQuerySQL(){
        final Window window = new Window();
        window.setCaption(" 查询条件 SQL");
        window.setIcon(FontAwesome.FILE_TEXT_O);
        VerticalLayout containerLayout=new VerticalLayout();
        containerLayout.setSpacing(true);
        containerLayout.setMargin(true);
        Panel sqlTextPanel=new Panel();
        sqlTextPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        sqlTextPanel.setWidth(380,Unit.PIXELS);
        sqlTextPanel.setHeight(200,Unit.PIXELS);
        Label querySqlLabel=new Label();
        querySqlLabel.addStyleName(ValoTheme.LABEL_COLORED);
        querySqlLabel.setValue(this.querySQL);
        sqlTextPanel.setContent(querySqlLabel);
        containerLayout.addComponent(sqlTextPanel);
        window.setWidth(400.0f, Unit.PIXELS);
        window.setResizable(false);
        window.setModal(true);
        window.setContent(containerLayout);
        UI.getCurrent().addWindow(window);
    }

    private Pagination createPagination(long totalData, int initPageNumber) {
        final PaginationResource paginationResource = PaginationResource.newBuilder().setTotal(totalData).setPage(initPageNumber).setLimit(this.getTablePageSize()).build();
        final Pagination pagination = new Pagination(paginationResource);
        return pagination;
    }

    public int getTablePageSize() {
        return tablePageSize;
    }

    public void setTablePageSize(int tablePageSize) {
        this.tablePageSize = tablePageSize;
    }

    public int getSubWindowsYPositionOffset() {
        return subWindowsYPositionOffset;
    }

    public void setSubWindowsYPositionOffset(int subWindowsYPositionOffset) {
        this.subWindowsYPositionOffset = subWindowsYPositionOffset;
    }

    public int getSubWindowsXPositionOffset() {
        return subWindowsXPositionOffset;
    }

    public void setSubWindowsXPositionOffset(int subWindowsXPositionOffset) {
        this.subWindowsXPositionOffset = subWindowsXPositionOffset;
    }

    public void removeTypeDataInstanceById(String dataId,ProcessingDataVO processingDataVO){
        Item targetItem=this.typeDataInstanceTable.getItem(dataId);
        if(targetItem!=null){
            boolean removeTypeInstanceResult=this.typeDataInstanceTable.removeItem(dataId);
            if(removeTypeInstanceResult) {
                this.currentDataInstancesCount--;
                this.queryResultCountLabel.setValue(""+this.currentDataInstancesCount);
                int currentPaginationPage=this.typeDataInstancePagination.getCurrentPage();
                if(this.typeDataInstancePagination!=null){
                    this.typeDataInstancePagination.removeAllPageChangeListener();
                }
                this.paginationContainerLayout.removeAllComponents();

                if(currentPaginationPage*tablePageSize>this.currentDataInstancesCount){
                    currentPaginationPage=currentPaginationPage-1;
                }
                this.typeDataInstancePagination = createPagination(this.currentDataInstancesCount, currentPaginationPage);
                this.typeDataInstancePagination.setItemsPerPageVisible(false);
                this.typeDataInstancePagination.addPageChangeListener(new PaginationChangeListener() {
                    @Override
                    public void changed(PaginationResource event) {
                        typeDataInstanceTable.setCurrentPageFirstItemIndex(event.offset());
                    }
                });
                this.paginationContainerLayout.addComponent(this.typeDataInstancePagination);

                DiscoverSpaceRemoveProcessingDataEvent discoverSpaceRemoveProcessingDataEvent=new DiscoverSpaceRemoveProcessingDataEvent(this.getDiscoverSpaceName());
                discoverSpaceRemoveProcessingDataEvent.setProcessingData(processingDataVO);
                currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceRemoveProcessingDataEvent);
            }
        }
    }

    private void launchVisualizationAnalyzeUI(String commandText){
        MeasurableTypeVO measurableTypeVO=new MeasurableTypeVO();
        measurableTypeVO.setDiscoverSpaceName(this.getDiscoverSpaceName());
        measurableTypeVO.setMeasurableTypeKind(this.getDataInstanceTypeKind());
        measurableTypeVO.setMeasurableTypeName(this.getDataInstanceTypeName());
        VisualizationAnalyzePanel visualizationAnalyzePanel=new VisualizationAnalyzePanel(this.currentUserClientInfo,commandText,measurableTypeVO);
        visualizationAnalyzePanel.setQuerySQL(this.querySQL);
        String dataDetailInfoTitle="数据可视化分析 > "+ commandText;
        final Window window = new Window(UICommonElementsUtil.generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
        window.setWidth(570, Unit.PIXELS);
        window.setHeight(800,Unit.PIXELS);
        window.setSizeFull();
        window.setCaptionAsHtml(true);
        window.setResizable(false);
        window.setDraggable(false);
        window.setModal(true);
        window.setContent(visualizationAnalyzePanel);
        UI.getCurrent().addWindow(window);
    }
}
