package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.FilteringItem;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationType;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.SQLBuilder;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * Created by wangychu on 10/26/16.
 */
public class QueryTypeDataInstancePanel extends VerticalLayout implements InputPropertyNamePanelInvoker{
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String discoverSpaceName;
    private String dataInstanceTypeKind;
    private String dataInstanceTypeName;
    private Label operationTitle;

    private SectionActionsBar dataTypeNoticeActionsBar;
    private MenuBar.MenuItem queryTypeDefinedPropertyMenuItem;
    private MenuBar.MenuItem queryCustomPropertyMenuItem;
    private MenuBar.MenuItem removeQueryPropertyMenuItem;
    private Button queryButton;
    private VerticalLayout queryConditionItemsContainerLayout;

    private Panel queryConditionInputContainerPanel;
    private Map<String,QueryConditionItem> queryConditionItemMap;
    private List<QueryConditionItem> queryConditionItemList;
    private Map<String,PropertyTypeVO> typePropertiesInfoMap;

    private MenuBar.Command queryTypePropertyMenuItemCommand;
    private MenuBar.Command queryCustomPropertyMenuItemCommand;
    private MenuBar.Command removeQueryPropertyMenuItemCommand;
    private String currentTempCustomPropertyDataType;

    private int queryPageSize=50;
    private int queryStartPage=1;
    private int queryEndPage=101;
    private long maxResultNumber=0;
    private boolean queryDistinctMode=true;

    private TypeDataInstanceList typeDataInstanceList;

    public QueryTypeDataInstancePanel(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        setSpacing(true);
        setMargin(true);
        this.typePropertiesInfoMap=new HashMap<String,PropertyTypeVO>();
        this.queryConditionItemMap=new HashMap<String,QueryConditionItem>();
        this.queryConditionItemList=new ArrayList<QueryConditionItem>();
        dataTypeNoticeActionsBar = new SectionActionsBar(new Label("---", ContentMode.HTML));
        addComponent(dataTypeNoticeActionsBar);
        //left side
        HorizontalSplitPanel typeDataInstanceQuerySplitPanel = new HorizontalSplitPanel();
        typeDataInstanceQuerySplitPanel.setSizeFull();
        typeDataInstanceQuerySplitPanel.setSplitPosition(420, Unit.PIXELS);
        addComponent(typeDataInstanceQuerySplitPanel);

        VerticalLayout queryConditionInputContainerLayout=new VerticalLayout();
        queryConditionInputContainerLayout.addStyleName("ui_appElementRightSideSpacing");
        typeDataInstanceQuerySplitPanel.setFirstComponent(queryConditionInputContainerLayout);

        this.operationTitle = new Label(FontAwesome.LIST_UL.getHtml() + " ---", ContentMode.HTML);
        this.operationTitle.addStyleName(ValoTheme.LABEL_SMALL);
        this.operationTitle.addStyleName("ui_appStandaloneElementPadding");
        this.operationTitle.addStyleName("ui_appSectionLightDiv");
        queryConditionInputContainerLayout.addComponent(operationTitle);

        MenuBar dimensionInstanceOperationMenuBar = new MenuBar();
        dimensionInstanceOperationMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        dimensionInstanceOperationMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);

        this.queryTypePropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedTypePropertyName=selectedItem.getText();
                addTypePropertyQueryInputUI(selectedTypePropertyName);
            }
        };

        this.queryCustomPropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedPropertyDataType=selectedItem.getText();
                addCustomPropertyQueryInputUI(selectedPropertyDataType);
            }
        };

        this.removeQueryPropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                removeQueryPropertyConditionItem(selectedItem);
            }
        };

        this.queryTypeDefinedPropertyMenuItem = dimensionInstanceOperationMenuBar.addItem("类型预定义属性", FontAwesome.FILTER, null);

        this.queryCustomPropertyMenuItem = dimensionInstanceOperationMenuBar.addItem("自定义属性", FontAwesome.FILTER, null);
        this.queryCustomPropertyMenuItem.addItem(ApplicationConstant.DataFieldType_STRING, FontAwesome.CIRCLE_O, this.queryCustomPropertyMenuItemCommand);
        this.queryCustomPropertyMenuItem.addItem(ApplicationConstant.DataFieldType_BOOLEAN, FontAwesome.CIRCLE_O, this.queryCustomPropertyMenuItemCommand);
        this.queryCustomPropertyMenuItem.addItem(ApplicationConstant.DataFieldType_DATE, FontAwesome.CIRCLE_O, this.queryCustomPropertyMenuItemCommand);
        this.queryCustomPropertyMenuItem.addItem(ApplicationConstant.DataFieldType_INT, FontAwesome.CIRCLE_O, this.queryCustomPropertyMenuItemCommand);
        this.queryCustomPropertyMenuItem.addItem(ApplicationConstant.DataFieldType_LONG, FontAwesome.CIRCLE_O,this.queryCustomPropertyMenuItemCommand);
        this.queryCustomPropertyMenuItem.addItem(ApplicationConstant.DataFieldType_DOUBLE, FontAwesome.CIRCLE_O,this.queryCustomPropertyMenuItemCommand);
        this.queryCustomPropertyMenuItem.addItem(ApplicationConstant.DataFieldType_FLOAT, FontAwesome.CIRCLE_O, this.queryCustomPropertyMenuItemCommand);
        this.queryCustomPropertyMenuItem.addItem(ApplicationConstant.DataFieldType_SHORT, FontAwesome.CIRCLE_O, this.queryCustomPropertyMenuItemCommand);

        this.removeQueryPropertyMenuItem=dimensionInstanceOperationMenuBar.addItem("删除查询条件", FontAwesome.TRASH_O, null);

        queryConditionInputContainerLayout.addComponent(dimensionInstanceOperationMenuBar);

        this.queryConditionItemsContainerLayout=new VerticalLayout();
        this.queryConditionItemsContainerLayout.setWidth(100,Unit.PERCENTAGE);

        queryConditionInputContainerPanel=new Panel();
        queryConditionInputContainerLayout.addComponent(queryConditionInputContainerPanel);
        queryConditionInputContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        //queryConditionInputContainerPanel.setHeight(450,Unit.PIXELS);
        queryConditionInputContainerPanel.setContent(this.queryConditionItemsContainerLayout);

        VerticalLayout spacingLayout0=new VerticalLayout();
        spacingLayout0.setWidth(100,Unit.PERCENTAGE);
        queryConditionInputContainerLayout.addComponent(spacingLayout0);
        spacingLayout0.addStyleName("ui_appSectionLightDiv");

        HorizontalLayout queryButtonsContainerLayout=new HorizontalLayout();
        queryConditionInputContainerLayout.addComponent(queryButtonsContainerLayout);
        queryConditionInputContainerLayout.setComponentAlignment(queryButtonsContainerLayout,Alignment.MIDDLE_CENTER);

        this.queryButton=new Button("---", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                executeDataInstanceQuery();
            }
        });
        this.queryButton.setIcon(FontAwesome.SEARCH);
        this.queryButton.addStyleName("primary");
        this.queryButton.addStyleName(ValoTheme.BUTTON_SMALL);
        queryButtonsContainerLayout.addComponent(this.queryButton);

        HorizontalLayout queryButtonsSpaceDiv=new HorizontalLayout();
        queryButtonsSpaceDiv.setWidth(10,Unit.PIXELS);
        queryButtonsContainerLayout.addComponent(queryButtonsSpaceDiv);

        Button queryConfigButton=new Button("设置查询结果集参数");
        queryConfigButton.setIcon(FontAwesome.COG);
        queryConfigButton.addStyleName(ValoTheme.BUTTON_TINY);
        queryConfigButton.addStyleName(ValoTheme.BUTTON_LINK);
        queryConfigButton.addStyleName(ValoTheme.BUTTON_QUIET);
        queryConfigButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
               showQueryExploreParametersConfigInput();
            }
        });
        queryButtonsContainerLayout.addComponent(queryConfigButton);

        //right side
        VerticalLayout queryResultContainerLayout=new VerticalLayout();
        queryResultContainerLayout.setWidth(100,Unit.PERCENTAGE);
        typeDataInstanceQuerySplitPanel.setSecondComponent(queryResultContainerLayout);

        Label operationResultTitle = new Label(FontAwesome.DATABASE.getHtml() + " 查询结果", ContentMode.HTML);
        operationResultTitle.addStyleName(ValoTheme.LABEL_SMALL);
        operationResultTitle.addStyleName("ui_appStandaloneElementPadding");
        operationResultTitle.addStyleName("ui_appSectionLightDiv");
        queryResultContainerLayout.addComponent(operationResultTitle);

        this.typeDataInstanceList=new TypeDataInstanceList(this.currentUserClientInfo);
        this.typeDataInstanceList.setTablePageSize(this.queryPageSize);
        queryResultContainerLayout.addComponent(this.typeDataInstanceList);
    }

    public int getQueryPageSize() {
        return queryPageSize;
    }

    public void setQueryPageSize(int queryPageSize) {
        this.queryPageSize = queryPageSize;
    }

    public int getQueryStartPage() {
        return queryStartPage;
    }

    public void setQueryStartPage(int queryStartPage) {
        this.queryStartPage = queryStartPage;
    }

    public int getQueryEndPage() {
        return queryEndPage;
    }

    public void setQueryEndPage(int queryEndPage) {
        this.queryEndPage = queryEndPage;
    }

    public long getMaxResultNumber() {
        return maxResultNumber;
    }

    public void setMaxResultNumber(long maxResultNumber) {
        this.maxResultNumber = maxResultNumber;
    }

    public boolean getQueryDistinctMode() {
        return queryDistinctMode;
    }

    public void setQueryDistinctMode(boolean queryDistinctMode) {
        this.queryDistinctMode = queryDistinctMode;
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    public String getDataInstanceTypeKind() {
        return dataInstanceTypeKind;
    }

    public void setDataInstanceTypeKind(String dataInstanceTypeKind) {
        this.dataInstanceTypeKind = dataInstanceTypeKind;
    }

    @Override
    public void attach() {
        super.attach();
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
            if(this.getContainerDialog()!=null){
                this.getContainerDialog().setCaptionAsHtml(true);
                this.getContainerDialog().setCaption(UICommonElementsUtil.generateMovableWindowTitleWithFormat("查询维度数据"));
            }
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 查询条件 ( 维度属性 ) :");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.TAGS.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            this.queryButton.setCaption("查询维度数据");

            List<PropertyTypeVO> dimensionTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveDimensionTypePropertiesInfo(this.getDiscoverSpaceName(), getDataInstanceTypeName());
            if(dimensionTypePropertiesList!=null){
                setQueryPerDefinedProperties(dimensionTypePropertiesList);
            }
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
            if(this.getContainerDialog()!=null){
                this.getContainerDialog().setCaptionAsHtml(true);
                this.getContainerDialog().setCaption(UICommonElementsUtil.generateMovableWindowTitleWithFormat("查询事实数据"));
            }
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 查询条件 ( 事实属性 ) :");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.CLONE.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            this.queryButton.setCaption("查询事实数据");

            List<PropertyTypeVO> factTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveFactTypePropertiesInfo(this.getDiscoverSpaceName(), getDataInstanceTypeName());
            if(factTypePropertiesList!=null){
                setQueryPerDefinedProperties(factTypePropertiesList);
            }
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(getDataInstanceTypeKind())){
            if(this.getContainerDialog()!=null){
                this.getContainerDialog().setCaptionAsHtml(true);
                this.getContainerDialog().setCaption(UICommonElementsUtil.generateMovableWindowTitleWithFormat("查询关系数据"));
            }
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 查询条件 ( 关系属性 ) :");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.SHARE_ALT.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            this.queryButton.setCaption("查询关系数据");

            List<PropertyTypeVO> relationTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveRelationTypePropertiesInfo(this.getDiscoverSpaceName(), getDataInstanceTypeName());
            if(relationTypePropertiesList!=null){
                setQueryPerDefinedProperties(relationTypePropertiesList);
            }
        }

        Window containerWindow=this.getContainerDialog();
        containerWindow.addWindowModeChangeListener(new Window.WindowModeChangeListener() {
            @Override
            public void windowModeChanged(Window.WindowModeChangeEvent windowModeChangeEvent) {
                setUIElementsSizeForWindowSizeChange();
            }
        });
        setUIElementsSizeForWindowSizeChange();
    }

    private void setQueryPerDefinedProperties( List<PropertyTypeVO> typePropertiesList){
        for(PropertyTypeVO currentPropertyTypeVO:typePropertiesList){
            this.typePropertiesInfoMap.put(currentPropertyTypeVO.getPropertyName(),currentPropertyTypeVO);
            if(dataInstanceTypeName.equals(currentPropertyTypeVO.getPropertySourceOwner())){
                this.queryTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.CIRCLE_O, this.queryTypePropertyMenuItemCommand);
            }else{
                this.queryTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.REPLY_ALL, this.queryTypePropertyMenuItemCommand);
            }
        }
    }

    private void setUIElementsSizeForWindowSizeChange(){
        Window containerDialog=this.getContainerDialog();
        int queryConditionInputContainerPanelHeight=0;
        int typeDataInstanceListHeight=0;
        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        int containerDialogInitFixHeight=(int)(containerDialog.getHeight()/100*browserWindowHeight);
        if (containerDialog.getWindowMode().equals(WindowMode.MAXIMIZED)){
            queryConditionInputContainerPanelHeight=browserWindowHeight-250;
            typeDataInstanceListHeight=browserWindowHeight-270;
        }else{
            queryConditionInputContainerPanelHeight=containerDialogInitFixHeight-250;
            typeDataInstanceListHeight=containerDialogInitFixHeight-270;
        }
        queryConditionInputContainerPanel.setHeight(queryConditionInputContainerPanelHeight,Unit.PIXELS);
        this.typeDataInstanceList.setTypeDataInstanceListHeight(typeDataInstanceListHeight);
    }

    private void addTypePropertyQueryInputUI(String propertyName){
        if(this.queryConditionItemMap.get(propertyName)!=null){
            Notification errorNotification = new Notification("数据校验错误",
                    "已添加过查询属性 "+propertyName, Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        PropertyTypeVO currentPropertyTypeInfo=this.typePropertiesInfoMap.get(propertyName);
        QueryConditionItem currentQueryConditionItem=new QueryConditionItem(this.currentUserClientInfo,currentPropertyTypeInfo);
        currentQueryConditionItem.setDataInstanceTypeName(this.getDataInstanceTypeName());
        if(this.queryConditionItemList.size()==0){
            currentQueryConditionItem.setIsFirstQueryCondition(true);
        }
        this.queryConditionItemList.add(currentQueryConditionItem);
        this.queryConditionItemMap.put(propertyName,currentQueryConditionItem);
        this.queryConditionItemsContainerLayout.addComponent(currentQueryConditionItem);
        this.removeQueryPropertyMenuItem.addItem(propertyName, FontAwesome.SEARCH_MINUS, this.removeQueryPropertyMenuItemCommand);
    }

    private void addCustomPropertyQueryInputUI(String propertyName){
        this.currentTempCustomPropertyDataType=propertyName;
        InputPropertyNamePanel inputPropertyNamePanel=new InputPropertyNamePanel(this.currentUserClientInfo);
        final Window window = new Window();
        window.setWidth(450.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(inputPropertyNamePanel);
        inputPropertyNamePanel.setContainerDialog(window);
        inputPropertyNamePanel.setInputPropertyNamePanelInvoker(this);
        UI.getCurrent().addWindow(window);
    }

    private void removeQueryPropertyConditionItem(MenuBar.MenuItem selectedItem){
        String propertyName=selectedItem.getText();
        QueryConditionItem targetItem=this.queryConditionItemMap.get(propertyName);
        if(targetItem!=null){
            this.queryConditionItemsContainerLayout.removeComponent(targetItem);
            this.queryConditionItemList.remove(targetItem);
            this.queryConditionItemMap.remove(propertyName);
            this.removeQueryPropertyMenuItem.removeChild(selectedItem);
        }
        if(this.queryConditionItemList.size()>0){
            QueryConditionItem baseQueryConditionItem=this.queryConditionItemList.get(0);
            baseQueryConditionItem.setIsFirstQueryCondition(true);
        }
    }

    @Override
    public void inputPropertyNameActionFinish(String propertyNameValue) {
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(propertyNameValue);
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入查询属性名称 "+propertyNameValue+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(propertyNameValue);
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入查询属性名称 "+propertyNameValue+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(this.queryConditionItemMap.get(propertyNameValue)!=null){
            Notification errorNotification = new Notification("数据校验错误",
                    "已添加过查询属性 "+propertyNameValue, Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(this.currentTempCustomPropertyDataType!=null){
            PropertyTypeVO customPropertyTypeInfo=new PropertyTypeVO();
            customPropertyTypeInfo.setPropertyType(this.currentTempCustomPropertyDataType);
            customPropertyTypeInfo.setPropertyName(propertyNameValue);
            customPropertyTypeInfo.setPropertySourceOwner(this.getDataInstanceTypeName());
            customPropertyTypeInfo.setReadOnly(false);
            customPropertyTypeInfo.setNullable(false);
            customPropertyTypeInfo.setMandatory(false);
            QueryConditionItem currentQueryConditionItem=new QueryConditionItem(this.currentUserClientInfo,customPropertyTypeInfo);
            currentQueryConditionItem.setDataInstanceTypeName(this.getDataInstanceTypeName());
            if(this.queryConditionItemList.size()==0){
                currentQueryConditionItem.setIsFirstQueryCondition(true);
            }
            this.queryConditionItemList.add(currentQueryConditionItem);
            this.queryConditionItemMap.put(propertyNameValue,currentQueryConditionItem);
            this.queryConditionItemsContainerLayout.addComponent(currentQueryConditionItem);
            this.removeQueryPropertyMenuItem.addItem(propertyNameValue, FontAwesome.SEARCH_MINUS, this.removeQueryPropertyMenuItemCommand);
        }
    }

    private void executeDataInstanceQuery(){
        ExploreParameters exploreParameters=new ExploreParameters();
        if(InfoDiscoverEngineConstant.FACT_ROOTCLASSNAME.equals(this.getDataInstanceTypeName())||
                InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME.equals(this.getDataInstanceTypeName())||
                InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME.equals(this.getDataInstanceTypeName())){
            exploreParameters.setType(null);
        }else{
            exploreParameters.setType(this.getDataInstanceTypeName());
        }
        if(this.queryConditionItemList.size()>0){
            QueryConditionItem firstCondition=this.queryConditionItemList.get(0);
            FilteringItem defaultFilteringItem= firstCondition.getFilteringItem();
            if(defaultFilteringItem==null){
                Notification errorNotification = new Notification("数据校验错误",
                        "未设定合法的查询约束条件", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            exploreParameters.setDefaultFilteringItem(defaultFilteringItem);
            for(int i=1;i<this.queryConditionItemList.size();i++){
                QueryConditionItem currentQueryConditionItem=this.queryConditionItemList.get(i);
                FilteringItem currentFilteringItem= currentQueryConditionItem.getFilteringItem();
                if(currentFilteringItem==null){
                    Notification errorNotification = new Notification("数据校验错误",
                            "未设定合法的查询约束条件", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                    return;
                }
                exploreParameters.addFilteringItem(currentFilteringItem,currentQueryConditionItem.getFilteringLogic());
            }
        }
        if(this.getMaxResultNumber()!=0){
            exploreParameters.setResultNumber((int) this.getMaxResultNumber());
        }else{
            exploreParameters.setPageSize(this.getQueryPageSize());
            exploreParameters.setStartPage(this.getQueryStartPage());
            exploreParameters.setEndPage(this.getQueryEndPage());
        }
        exploreParameters.setDistinctMode(this.getQueryDistinctMode());

        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
            List<MeasurableValueVO> resultDimensionValuesList= InfoDiscoverSpaceOperationUtil.queryDimensions(this.discoverSpaceName, exploreParameters);
            try {
                String sql= SQLBuilder.buildQuerySQL(InformationType.DIMENSION, exploreParameters);
                this.typeDataInstanceList.setQuerySQL(sql);
            } catch (InfoDiscoveryEngineInfoExploreException e) {
                e.printStackTrace();
            }
            renderQueryResultsGrid(this.queryConditionItemList,resultDimensionValuesList);
        }

        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
            List<MeasurableValueVO> resultFactValuesList= InfoDiscoverSpaceOperationUtil.queryFacts(this.discoverSpaceName, exploreParameters);
            try {
                String sql= SQLBuilder.buildQuerySQL(InformationType.FACT, exploreParameters);
                this.typeDataInstanceList.setQuerySQL(sql);
            } catch (InfoDiscoveryEngineInfoExploreException e) {
                e.printStackTrace();
            }
            renderQueryResultsGrid(this.queryConditionItemList,resultFactValuesList);
        }

        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(getDataInstanceTypeKind())){
            List<MeasurableValueVO> resultRelationValuesList= InfoDiscoverSpaceOperationUtil.queryRelations(this.discoverSpaceName, exploreParameters);
            try {
                String sql= SQLBuilder.buildQuerySQL(InformationType.RELATION, exploreParameters);
                this.typeDataInstanceList.setQuerySQL(sql);
            } catch (InfoDiscoveryEngineInfoExploreException e) {
                e.printStackTrace();
            }
            renderQueryResultsGrid(this.queryConditionItemList,resultRelationValuesList);
        }
    }

    private void renderQueryResultsGrid(List<QueryConditionItem> queryConditions,List<MeasurableValueVO> queryResults){
        this.typeDataInstanceList.setDiscoverSpaceName(this.getDiscoverSpaceName());
        this.typeDataInstanceList.setDataInstanceTypeName(this.getDataInstanceTypeName());
        this.typeDataInstanceList.setDataInstanceTypeKind(this.getDataInstanceTypeKind());
        this.typeDataInstanceList.setTablePageSize(this.getQueryPageSize());
        List<PropertyTypeVO> queryParameterList=new ArrayList<PropertyTypeVO>();
        for(QueryConditionItem currentQueryConditionItem:queryConditions){
            queryParameterList.add(currentQueryConditionItem.getPropertyTypeVO());
        }
        this.typeDataInstanceList.renderTypeDataInstanceList(queryParameterList,queryResults);
    }

    private void showQueryExploreParametersConfigInput(){
        QueryExploreParametersConfigInput queryExploreParametersConfigInput=new QueryExploreParametersConfigInput(this.currentUserClientInfo);
        queryExploreParametersConfigInput.setContainerQueryTypeDataInstancePanel(this);
        queryExploreParametersConfigInput.setPageSize(this.getQueryPageSize());
        queryExploreParametersConfigInput.setStartPage(this.getQueryStartPage());
        queryExploreParametersConfigInput.setEndPage(this.getQueryEndPage());
        if(this.getMaxResultNumber() !=0){
            queryExploreParametersConfigInput.setResultNumber(this.getMaxResultNumber());
        }
        queryExploreParametersConfigInput.setDistinctMode(this.getQueryDistinctMode());
        final Window window = new Window();
        window.setWidth(350.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(queryExploreParametersConfigInput);
        queryExploreParametersConfigInput.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }
}


