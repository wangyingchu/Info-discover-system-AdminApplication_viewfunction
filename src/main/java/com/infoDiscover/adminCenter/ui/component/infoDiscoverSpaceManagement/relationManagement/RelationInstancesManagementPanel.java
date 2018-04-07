package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationTypeVO;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceTypeDataInstanceQueryRequiredEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 10/25/16.
 */
public class RelationInstancesManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private VerticalLayout rightSideUIElementsContainer;
    private HierarchicalContainer relationTypesInfoContainer;
    private TreeTable relationTypesTreeTable;
    private Label relationTypeNameLabel;
    private Label relationCountLabel;
    private String NAME_PROPERTY="关系类型名称";
    private DiscoverSpaceStatisticMetrics currentDiscoverSpaceStatisticMetrics;
    private String currentSelectedRelationTypeName;
    private RelationTypeDataInstancesInfoChart relationTypeDataInstancesInfoChart;
    private MenuBar.Command searchRelationInstanceMenuItemCommand;
    private MenuBar.MenuItem searchRelationInstanceMenuItem;
    private FormLayout relationTypeDataInstanceCountInfoForm;

    private VerticalLayout rightSideUIPromptBox;
    private VerticalLayout rightSideUIElementsBox;

    public RelationInstancesManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int relationTypesTreeHeight=screenHeight-520;
        int dataCountFormPanelHeight=screenHeight-660;

        //left side elements
        VerticalLayout leftSideUIElementsContainer=new VerticalLayout();
        leftSideUIElementsContainer.setWidth(400,Unit.PIXELS);
        leftSideUIElementsContainer.setHeight("100%");
        elementPlacementLayout.addComponent(leftSideUIElementsContainer);

        Label leftSideTitle= new Label(FontAwesome.EXCHANGE.getHtml() +" 关系类型选择:", ContentMode.HTML);
        leftSideTitle.addStyleName(ValoTheme.LABEL_SMALL);
        leftSideTitle.addStyleName("ui_appStandaloneElementPadding");
        leftSideTitle.addStyleName("ui_appSectionLightDiv");
        leftSideUIElementsContainer.addComponent(leftSideTitle);

        this.relationTypesInfoContainer =new HierarchicalContainer();
        this.relationTypesInfoContainer.addContainerProperty(NAME_PROPERTY, String.class, "");

        this.relationTypesTreeTable = new TreeTable();
        this.relationTypesTreeTable.setSizeFull();
        this.relationTypesTreeTable.setSelectable(true);
        this.relationTypesTreeTable.setMultiSelect(false);
        this.relationTypesTreeTable.setImmediate(true);
        this.relationTypesTreeTable.setNullSelectionAllowed(false);
        this.relationTypesTreeTable.setContainerDataSource(this.relationTypesInfoContainer);
        this.relationTypesTreeTable.setItemCaptionPropertyId(NAME_PROPERTY);
        this.relationTypesTreeTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.relationTypesTreeTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.relationTypesTreeTable.setHeight(relationTypesTreeHeight, Unit.PIXELS);
        this.relationTypesTreeTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedRelationTypeName=itemClickEvent.getItem().getItemProperty(NAME_PROPERTY).getValue().toString();
                renderRelationTypeSelectedUIElements(selectedRelationTypeName);
            }
        });
        leftSideUIElementsContainer.addComponent(this.relationTypesTreeTable);
        leftSideUIElementsContainer.addStyleName("ui_appElementRightSideSpacing");

        //right side elements
        this.rightSideUIElementsContainer=new VerticalLayout();
        elementPlacementLayout.addComponent(rightSideUIElementsContainer);
        rightSideUIElementsContainer.setHeight("100%");
        elementPlacementLayout.addComponent(rightSideUIElementsContainer);
        elementPlacementLayout.setExpandRatio(rightSideUIElementsContainer, 1.0F);

        this.rightSideUIPromptBox=new VerticalLayout();
        VerticalLayout messageHeightSpaceDiv=new VerticalLayout();
        messageHeightSpaceDiv.setHeight(30,Unit.PIXELS);
        this.rightSideUIPromptBox.addComponent(messageHeightSpaceDiv);
        Label functionMessage = new Label( FontAwesome.SHARE_ALT.getHtml()+" 关系数据操作。", ContentMode.HTML);
        functionMessage.setStyleName("ui_appLightDarkMessage");
        functionMessage.addStyleName(ValoTheme.LABEL_LARGE);
        this.rightSideUIPromptBox.addComponent(functionMessage);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);

        this.rightSideUIElementsBox=new VerticalLayout();
        //this.rightSideUIElementsContainer.addComponent(this.rightSideUIElementsBox);

        Label rightSideTitle= new Label(FontAwesome.SHARE_ALT.getHtml() +" 关系数据操作:", ContentMode.HTML);
        rightSideTitle.addStyleName(ValoTheme.LABEL_SMALL);
        rightSideTitle.addStyleName("ui_appStandaloneElementPadding");
        rightSideTitle.addStyleName("ui_appSectionLightDiv");
        this.rightSideUIElementsBox.addComponent(rightSideTitle);

        HorizontalLayout relationTypeInfoLayout=new HorizontalLayout();
        relationTypeInfoLayout.setWidth("100%");
        relationTypeInfoLayout.addStyleName("ui_appStandaloneElementPadding");
        relationTypeInfoLayout.addStyleName("ui_appSectionLightDiv");
        this.rightSideUIElementsBox.addComponent(relationTypeInfoLayout);

        HorizontalLayout relationTypeDetailContainerLayout=new HorizontalLayout();
        relationTypeInfoLayout.addComponent(relationTypeDetailContainerLayout);

        Label relationTypeLabel = new Label( FontAwesome.EXCHANGE.getHtml()+" 关系类型名称:", ContentMode.HTML);
        relationTypeLabel.addStyleName(ValoTheme.LABEL_TINY);
        relationTypeDetailContainerLayout.addComponent(relationTypeLabel);

        HorizontalLayout spacingDivLayout1=new HorizontalLayout();
        spacingDivLayout1.setWidth(10,Unit.PIXELS);
        relationTypeDetailContainerLayout.addComponent(spacingDivLayout1);

        relationTypeDetailContainerLayout.setComponentAlignment(relationTypeLabel, Alignment.MIDDLE_LEFT);

        this.relationTypeNameLabel =new Label("-");
        //this.relationTypeNameLabel.addStyleName(ValoTheme.LABEL_COLORED);
        this.relationTypeNameLabel.addStyleName("ui_appFriendlyElement");
        this.relationTypeNameLabel.addStyleName(ValoTheme.LABEL_H2);
        this.relationTypeNameLabel.addStyleName(ValoTheme.LABEL_BOLD);
        this.relationTypeNameLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        relationTypeDetailContainerLayout.addComponent(this.relationTypeNameLabel);

        HorizontalLayout spacingDivLayout2=new HorizontalLayout();
        spacingDivLayout2.setWidth(10,Unit.PIXELS);
        relationTypeDetailContainerLayout.addComponent(spacingDivLayout2);

        Label relationNumberLabel = new Label( " 类型数据总量:");
        relationNumberLabel.addStyleName(ValoTheme.LABEL_TINY);
        relationTypeDetailContainerLayout.addComponent(relationNumberLabel);
        relationTypeDetailContainerLayout.setComponentAlignment(relationNumberLabel, Alignment.MIDDLE_LEFT);

        HorizontalLayout spacingDivLayout3=new HorizontalLayout();
        spacingDivLayout3.setWidth(10,Unit.PIXELS);
        relationTypeDetailContainerLayout.addComponent(spacingDivLayout3);

        this.relationCountLabel =new Label("");
        //this.relationCountLabel.addStyleName(ValoTheme.LABEL_COLORED);
        this.relationCountLabel.addStyleName("ui_appFriendlyElement");
        this.relationCountLabel.addStyleName(ValoTheme.LABEL_H2);
        this.relationCountLabel.addStyleName(ValoTheme.LABEL_BOLD);
        this.relationCountLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        relationTypeDetailContainerLayout.addComponent(this.relationCountLabel);

        HorizontalLayout relationDataOperationContainerLayout=new HorizontalLayout();
        this.rightSideUIElementsBox.addComponent(relationDataOperationContainerLayout);

        HorizontalLayout relationDataOperationContainerSpaceDivLayout=new HorizontalLayout();
        relationDataOperationContainerSpaceDivLayout.setWidth("10px");
        relationDataOperationContainerLayout.addComponent(relationDataOperationContainerSpaceDivLayout);

        MenuBar relationInstanceOperationMenuBar = new MenuBar();
        relationInstanceOperationMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        //relationInstanceOperationMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
        relationDataOperationContainerLayout.addComponent(relationInstanceOperationMenuBar);


        // Define a common menu command for all the search relation menu items
        this.searchRelationInstanceMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedRelationTypeName=selectedItem.getText();
                executeSearchRelationTypeOperation(selectedRelationTypeName);
            }
        };

        // Put operation items in the menu
        this.searchRelationInstanceMenuItem = relationInstanceOperationMenuBar.addItem("查询关系数据", FontAwesome.SEARCH, null);

        HorizontalLayout relationTypeDataInstanceInfoContainerLayout=new HorizontalLayout();
        relationTypeDataInstanceInfoContainerLayout.setWidth(700, Unit.PIXELS);
        this.rightSideUIElementsBox.addComponent(relationTypeDataInstanceInfoContainerLayout);

        VerticalLayout relationTypeSummaryInfo=new VerticalLayout();
        relationTypeSummaryInfo.setWidth("100%");
        relationTypeDataInstanceInfoContainerLayout.addComponent(relationTypeSummaryInfo);

        Label relationDataCountLabel=new Label("关系类型数据量");
        relationDataCountLabel.setWidth("100%");
        relationDataCountLabel.addStyleName("h4");
        relationDataCountLabel.addStyleName("ui_appSectionDiv");
        relationDataCountLabel.addStyleName("ui_appFadeMargin");
        relationTypeSummaryInfo.addComponent(relationDataCountLabel);

        this.relationTypeDataInstanceCountInfoForm = new FormLayout();
        this.relationTypeDataInstanceCountInfoForm.setMargin(false);
        this.relationTypeDataInstanceCountInfoForm.setWidth("100%");
        this.relationTypeDataInstanceCountInfoForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        Panel dataCountFormContainerPanel = new Panel();
        dataCountFormContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        dataCountFormContainerPanel.setWidth("100%");
        dataCountFormContainerPanel.setHeight(dataCountFormPanelHeight,Unit.PIXELS);
        dataCountFormContainerPanel.setContent(this.relationTypeDataInstanceCountInfoForm);
        relationTypeSummaryInfo.addComponent(dataCountFormContainerPanel);

        this.relationTypeDataInstancesInfoChart =new RelationTypeDataInstancesInfoChart(this.currentUserClientInfo);
        relationTypeDataInstanceInfoContainerLayout.addComponent(this.relationTypeDataInstancesInfoChart);
        relationTypeDataInstanceInfoContainerLayout.setComponentAlignment(this.relationTypeDataInstancesInfoChart, Alignment.TOP_LEFT);
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void renderRelationInstancesManagementInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.relationTypeNameLabel.setValue("-");
        this.relationCountLabel.setValue("-");
        this.searchRelationInstanceMenuItem.removeChildren();
        this.relationTypeDataInstanceCountInfoForm.removeAllComponents();
        this.relationTypesInfoContainer.removeAllItems();
        this.relationTypeDataInstancesInfoChart.cleanChartDisplay();
        this.currentDiscoverSpaceStatisticMetrics=discoverSpaceStatisticMetrics;
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        RelationTypeVO rootRelationTypeVO= InfoDiscoverSpaceOperationUtil.retrieveRootRelationTypeRuntimeInfo(this.discoverSpaceName, discoverSpaceStatisticMetrics);
        String rootRelationTypeItemId="RELATION_TYPE_ID";
        Item rootRelationTypeItem =  this.relationTypesInfoContainer.addItem(rootRelationTypeItemId);
        rootRelationTypeItem.getItemProperty(NAME_PROPERTY).setValue(rootRelationTypeVO.getTypeName());
        List<RelationTypeVO> childRelationTypesList= rootRelationTypeVO.getChildRelationTypesVOList();
        for(int i=0;i<childRelationTypesList.size();i++){
            RelationTypeVO currentChildRelationType=childRelationTypesList.get(i);
            setRelationTypesTreeTableData(rootRelationTypeItemId, i, currentChildRelationType);
        }
        this.relationTypesTreeTable.setCollapsed(rootRelationTypeItemId, false);
        this.relationTypesTreeTable.select(null);
    }

    private void setRelationTypesTreeTableData(String parentDataKey, int currentDataIndex, RelationTypeVO currentRelationTypeVO){
        String currentDataId=parentDataKey+currentDataIndex;
        Item currentRelationTypeItem =  this.relationTypesInfoContainer.addItem(currentDataId);
        currentRelationTypeItem.getItemProperty(NAME_PROPERTY).setValue(currentRelationTypeVO.getTypeName());
        this.relationTypesInfoContainer.setParent(currentDataId, parentDataKey);
        List<RelationTypeVO> childRelationTypesList= currentRelationTypeVO.getChildRelationTypesVOList();
        if(childRelationTypesList.size()==0){
            this.relationTypesTreeTable.setChildrenAllowed(currentDataId, false);
            this.relationTypesTreeTable.setColumnCollapsible(currentDataId, false);
        }
        for(int i=0;i<childRelationTypesList.size();i++){
            RelationTypeVO currentChildRelationType=childRelationTypesList.get(i);
            setRelationTypesTreeTableData(currentDataId, i, currentChildRelationType);
        }
    }

    private void renderRelationTypeSelectedUIElements(String relationTypeName){
        this.currentSelectedRelationTypeName = relationTypeName;
        this.relationTypeNameLabel.setValue(relationTypeName);
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIPromptBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIElementsBox);
        if(relationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            this.relationCountLabel.setValue("" + this.currentDiscoverSpaceStatisticMetrics.getSpaceRelationDataCount());
        }else{
            DataTypeStatisticMetrics dataTypeStatisticMetrics= getRelationTypeStatisticMetrics(relationTypeName);
            this.relationCountLabel.setValue("" + dataTypeStatisticMetrics.getTypeDataCount());
        }
        this.setRelationInstanceOperationsUIElements();
        this.relationTypeDataInstancesInfoChart.renderRelationTypeDataInstancesInfoChart(this.discoverSpaceName, relationTypeName, this.currentDiscoverSpaceStatisticMetrics);
    }

    private DataTypeStatisticMetrics getRelationTypeStatisticMetrics(String relationTypeName){
        List<DataTypeStatisticMetrics> relationTypesStatisticMetrics=this.currentDiscoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:relationTypesStatisticMetrics){
            String currentRelationTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            if(currentRelationTypeName.equals(relationTypeName)) {
                return currentDataTypeStatisticMetrics;
            }
        }
        return null;
    }

    private void setRelationInstanceOperationsUIElements(){
        this.searchRelationInstanceMenuItem.removeChildren();
        if(this.currentSelectedRelationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            RelationTypeVO currentRelationTypeVO=InfoDiscoverSpaceOperationUtil.retrieveRootRelationTypeRuntimeInfo(this.discoverSpaceName, this.currentDiscoverSpaceStatisticMetrics);
            setRelationInstanceCountInfo(currentRelationTypeVO);
            List<RelationTypeVO> childRelationTypeVOsList=currentRelationTypeVO.getChildRelationTypesVOList();
            MenuBar.MenuItem rootSearchRelationTypeItem = searchRelationInstanceMenuItem.addItem(this.currentSelectedRelationTypeName, null, searchRelationInstanceMenuItemCommand);
            if(childRelationTypeVOsList!=null){
                for(RelationTypeVO currentRelationType:childRelationTypeVOsList){
                    MenuBar.MenuItem currentSearchDimensionTypeItem = rootSearchRelationTypeItem.addItem(currentRelationType.getTypeName(), null, searchRelationInstanceMenuItemCommand);
                    setChildMenuItem(currentSearchDimensionTypeItem,currentRelationType,searchRelationInstanceMenuItemCommand);
                }
            }
        }else{
            RelationTypeVO currentRelationTypeVO=InfoDiscoverSpaceOperationUtil.retrieveRelationTypeRuntimeInfo(this.discoverSpaceName, this.currentSelectedRelationTypeName, this.currentDiscoverSpaceStatisticMetrics);
            setRelationInstanceCountInfo(currentRelationTypeVO);
            MenuBar.MenuItem currentSearchDimensionTypeItem = searchRelationInstanceMenuItem.addItem(currentRelationTypeVO.getTypeName(), null, searchRelationInstanceMenuItemCommand);
            setChildMenuItem(currentSearchDimensionTypeItem,currentRelationTypeVO,searchRelationInstanceMenuItemCommand);
        }
    }

    private void setChildMenuItem(MenuBar.MenuItem parentMenuItem,RelationTypeVO parentRelationTypeVO,MenuBar.Command menuCommand){
        List<RelationTypeVO> childRelationTypeVOsList=parentRelationTypeVO.getChildRelationTypesVOList();
        if(childRelationTypeVOsList!=null){
            for(RelationTypeVO currentRelationType:childRelationTypeVOsList){
                MenuBar.MenuItem currentRelationTypeItem = parentMenuItem.addItem(currentRelationType.getTypeName(), null, menuCommand);
                setChildMenuItem(currentRelationTypeItem,currentRelationType,menuCommand);
            }
        }
    }

    private void setRelationInstanceCountInfo(RelationTypeVO targetRelationTypeVO){
        this.relationTypeDataInstanceCountInfoForm.removeAllComponents();
        if(targetRelationTypeVO.getTypeName().equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            List<RelationTypeVO> childTypeVOList=targetRelationTypeVO.getChildRelationTypesVOList();
            if(childTypeVOList!=null){
                for(RelationTypeVO currentRelationTypeVO:childTypeVOList){
                    TextField currentRelationTypeDataSize = new TextField(currentRelationTypeVO.getTypeName());
                    currentRelationTypeDataSize.setValue("" + currentRelationTypeVO.getTypeDataRecordCount());
                    currentRelationTypeDataSize.setRequired(false);
                    currentRelationTypeDataSize.setReadOnly(true);
                    this.relationTypeDataInstanceCountInfoForm.addComponent(currentRelationTypeDataSize);
                }
            }
        }else{
            long parentRelationTypeTotalCount=targetRelationTypeVO.getTypeDataRecordCount();
            List<RelationTypeVO> childTypeVOList=targetRelationTypeVO.getChildRelationTypesVOList();
            if(childTypeVOList!=null){
                for(RelationTypeVO currentRelationTypeVO:childTypeVOList){
                    TextField currentRelationTypeDataSize = new TextField(currentRelationTypeVO.getTypeName());
                    currentRelationTypeDataSize.setValue("" + currentRelationTypeVO.getTypeDataRecordCount());
                    currentRelationTypeDataSize.setRequired(false);
                    currentRelationTypeDataSize.setReadOnly(true);
                    this.relationTypeDataInstanceCountInfoForm.addComponent(currentRelationTypeDataSize);
                    parentRelationTypeTotalCount=parentRelationTypeTotalCount-currentRelationTypeVO.getTypeDataRecordCount();
                }
            }
            TextField currentRelationTypeDataSize = new TextField(targetRelationTypeVO.getTypeName());
            currentRelationTypeDataSize.setValue("" + parentRelationTypeTotalCount);
            currentRelationTypeDataSize.setRequired(false);
            currentRelationTypeDataSize.setReadOnly(true);
            this.relationTypeDataInstanceCountInfoForm.addComponent(currentRelationTypeDataSize);
        }
    }

    private void executeSearchRelationTypeOperation(String relationType){
        DiscoverSpaceTypeDataInstanceQueryRequiredEvent currentQueryEvent=new DiscoverSpaceTypeDataInstanceQueryRequiredEvent();
        currentQueryEvent.setDiscoverSpaceName(this.getDiscoverSpaceName());
        currentQueryEvent.setDataInstanceTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION);
        currentQueryEvent.setDataInstanceTypeName(relationType);
        this.currentUserClientInfo.getEventBlackBoard().fire(currentQueryEvent);
    }
}
