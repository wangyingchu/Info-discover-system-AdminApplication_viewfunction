package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.TypeDataInstanceDetailPanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangychu on 4/5/17.
 */
public class ProcessingDataAnalyzePanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Map<String,List<ProcessingDataVO>> processingDataMapForAnalyzing;
    private Map<String,TabSheet.Tab> actionPanelTabKeyMap;
    private List<ProcessingDataVO> processingDimensionsForAnalyzing;
    private List<ProcessingDataVO> processingFactsForAnalyzing;
    private List<ProcessingDataVO> processingRelationsForAnalyzing;

    private final static String DataTypeName_PROPERTY="DataTypeName_PROPERTY";
    private final static String Id_PROPERTY="Id_PROPERTY";
    private final static String DataTypeKind_PROPERTY="DataTypeKind_PROPERTY";
    private final static String DiscoverSpaceName_PROPERTY="DiscoverSpaceName_PROPERTY";

    private final static String exploreRelatedInfoActionName = "探索本数据项关联的数据信息";
    private final static String exploreSimilarInfoActionName = "探索与本数据项相似的数据信息";
    private final static String findRelationInfoOfTwoItemAction_1Name = "发现本数据项与另一数据项的关联信息 (1)";
    private final static String findRelationInfoOfTwoItemAction_2Name = "发现本数据项与另一数据项的关联信息 (2)";
    private final static String compareInfoOfManyItemsActionName = "比较本数据项与其他数据项的属性信息";
    private final static String showDataDetailInfoActionName="显示数据详情";
    private final static String exploreRelationDataInfoActionName = "探索本关系关联的数据信息";
    private final static String exploreRelatedInfoTabNamePerfix="事实关联数据探索-";
    private final static String exploreRelationDataInfoTabNamePerfix="关系关联数据探索-";
    private final static String exploreSimilarInfoTabNamePerfix="事实相似数据探索-";

    private TabSheet dataAnalyzePageTabs;
    private Tree processingDataTree;

    private String factDataRootItemId="processingDataInstance_Fact";
    private String dimensionDataRootItemId="processingDataInstance_Dimension";
    private String relationDataRootItemId="processingDataInstance_Relation";

    private FindRelationInfoOfTwoAnalyzingDataPanel findRelationInfoOfTwoAnalyzingDataPanel;
    private CompareInfoOfManyAnalyzingDataPanel compareInfoOfManyAnalyzingDataPanel;

    public ProcessingDataAnalyzePanel(UserClientInfo userClientInfo,String discoverSpaceNam,Map<String,List<ProcessingDataVO>> processingDataMap){
        this.setMargin(true);
        this.currentUserClientInfo = userClientInfo;
        this.discoverSpaceName=discoverSpaceNam;
        this.processingDataMapForAnalyzing=processingDataMap;

        this.processingDimensionsForAnalyzing=this.processingDataMapForAnalyzing.get(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
        this.processingFactsForAnalyzing=this.processingDataMapForAnalyzing.get(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT);
        this.processingRelationsForAnalyzing=this.processingDataMapForAnalyzing.get(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION);

        SectionActionsBar dataTypeNoticeActionsBar = new SectionActionsBar(new Label(FontAwesome.CUBE.getHtml()+" "+this.discoverSpaceName, ContentMode.HTML));
        addComponent(dataTypeNoticeActionsBar);

        HorizontalLayout actionButtonsContainer=new HorizontalLayout();
        Button addNewDataForAnalyzeButton = new Button("添加待分析数据");
        addNewDataForAnalyzeButton.setIcon(FontAwesome.PLUS_SQUARE);

        addNewDataForAnalyzeButton.addStyleName(ValoTheme.BUTTON_SMALL);
        addNewDataForAnalyzeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        final ProcessingDataAnalyzePanel self=this;
        addNewDataForAnalyzeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                AddNewDataForAnalyzePanel addNewDataForAnalyzePanel=new AddNewDataForAnalyzePanel(currentUserClientInfo);
                addNewDataForAnalyzePanel.setDiscoverSpaceName(discoverSpaceName);
                addNewDataForAnalyzePanel.setRelatedProcessingDataAnalyzePanel(self);
                final Window window = new Window();
                window.setWidth(360.0f, Unit.PIXELS);
                window.setHeight(180.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(addNewDataForAnalyzePanel);
                addNewDataForAnalyzePanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        actionButtonsContainer.addComponent(addNewDataForAnalyzeButton);
        dataTypeNoticeActionsBar.addActionComponent(actionButtonsContainer);

        HorizontalSplitPanel visualizationAnalyzeSplitPanel = new HorizontalSplitPanel();
        visualizationAnalyzeSplitPanel.setSizeFull();
        visualizationAnalyzeSplitPanel.setSplitPosition(170, Unit.PIXELS);
        addComponent(visualizationAnalyzeSplitPanel);

        //Left side
        VerticalLayout processingDataTreesContainer=new VerticalLayout();

        int browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        processingDataTreesContainer.setHeight(browserWindowHeight-110,Unit.PIXELS);
        processingDataTreesContainer.setWidth(100,Unit.PERCENTAGE);
        processingDataTreesContainer.setMargin(false);
        processingDataTreesContainer.setSpacing(false);

        Label analyzeDataSelectorsTitle= new Label(FontAwesome.LIST_UL.getHtml() +" 待分析数据", ContentMode.HTML);
        analyzeDataSelectorsTitle.addStyleName(ValoTheme.LABEL_TINY);
        analyzeDataSelectorsTitle.addStyleName("ui_appSectionLightDiv");
        processingDataTreesContainer.addComponent(analyzeDataSelectorsTitle);

        VerticalLayout processingDataSelectorContainerLayout=new VerticalLayout();
        processingDataSelectorContainerLayout.setWidth(100,Unit.PERCENTAGE);

        Panel processingDataSelectorContainerPanel=new Panel();
        processingDataSelectorContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        processingDataSelectorContainerPanel.setContent(processingDataSelectorContainerLayout);
        processingDataSelectorContainerPanel.setHeight(browserWindowHeight-140,Unit.PIXELS);
        processingDataTreesContainer.addComponent(processingDataSelectorContainerPanel);
        processingDataTreesContainer.setExpandRatio(processingDataSelectorContainerPanel,1f);

        processingDataTree = new Tree();
        processingDataTree.addContainerProperty(DataTypeName_PROPERTY, String.class, "");
        processingDataTree.addContainerProperty(Id_PROPERTY, String.class, "");
        processingDataTree.addContainerProperty(DataTypeKind_PROPERTY, String.class, "");
        processingDataTree.addContainerProperty(DiscoverSpaceName_PROPERTY, String.class, "");

        processingDataTree.setSelectable(false);
        processingDataSelectorContainerLayout.addComponent(processingDataTree);

        AbstractSelect.ItemDescriptionGenerator itemDescriptionGenerator=new AbstractSelect.ItemDescriptionGenerator() {
            @Override
            public String generateDescription(Component component, Object itemId, Object o1) {
                return getProcessingDataDescription(itemId.toString());
            }
        };
        processingDataTree.setItemDescriptionGenerator(itemDescriptionGenerator);

        processingDataTree.addItem(factDataRootItemId);
        processingDataTree.setItemCaption(factDataRootItemId,"事实数据");
        processingDataTree.setItemIcon(factDataRootItemId,FontAwesome.CLONE);

        processingDataTree.addItem(dimensionDataRootItemId);
        processingDataTree.setItemCaption(dimensionDataRootItemId,"维度数据");
        processingDataTree.setItemIcon(dimensionDataRootItemId,FontAwesome.TAGS);

        processingDataTree.addItem(relationDataRootItemId);
        processingDataTree.setItemCaption(relationDataRootItemId,"关系数据");
        processingDataTree.setItemIcon(relationDataRootItemId,FontAwesome.SHARE_ALT);

        for(ProcessingDataVO currentFactVO:this.processingFactsForAnalyzing){
            Item newRecord=processingDataTree.addItem(currentFactVO.getId());
            newRecord.getItemProperty(DataTypeName_PROPERTY).setValue(currentFactVO.getDataTypeName());
            newRecord.getItemProperty(Id_PROPERTY).setValue(currentFactVO.getId());
            newRecord.getItemProperty(DataTypeKind_PROPERTY).setValue(currentFactVO.getDataTypeKind());
            newRecord.getItemProperty(DiscoverSpaceName_PROPERTY).setValue(currentFactVO.getDiscoverSpaceName());
            processingDataTree.setItemCaption(currentFactVO.getId(),currentFactVO.getId());
            processingDataTree.setItemIcon(currentFactVO.getId(),FontAwesome.SQUARE_O);
            processingDataTree.setChildrenAllowed(currentFactVO.getId(),false);
            processingDataTree.setParent(currentFactVO.getId(),factDataRootItemId);
        }

        for(ProcessingDataVO currentFactVO:this.processingDimensionsForAnalyzing){
            Item newRecord=processingDataTree.addItem(currentFactVO.getId());
            newRecord.getItemProperty(DataTypeName_PROPERTY).setValue(currentFactVO.getDataTypeName());
            newRecord.getItemProperty(Id_PROPERTY).setValue(currentFactVO.getId());
            newRecord.getItemProperty(DataTypeKind_PROPERTY).setValue(currentFactVO.getDataTypeKind());
            newRecord.getItemProperty(DiscoverSpaceName_PROPERTY).setValue(currentFactVO.getDiscoverSpaceName());
            processingDataTree.setItemCaption(currentFactVO.getId(),currentFactVO.getId());
            processingDataTree.setItemIcon(currentFactVO.getId(),FontAwesome.TAG);
            processingDataTree.setChildrenAllowed(currentFactVO.getId(),false);
            processingDataTree.setParent(currentFactVO.getId(),dimensionDataRootItemId);
        }

        if(this.processingRelationsForAnalyzing!=null){
            for(ProcessingDataVO currentFactVO:this.processingRelationsForAnalyzing){
                Item newRecord=processingDataTree.addItem(currentFactVO.getId());
                newRecord.getItemProperty(DataTypeName_PROPERTY).setValue(currentFactVO.getDataTypeName());
                newRecord.getItemProperty(Id_PROPERTY).setValue(currentFactVO.getId());
                newRecord.getItemProperty(DataTypeKind_PROPERTY).setValue(currentFactVO.getDataTypeKind());
                newRecord.getItemProperty(DiscoverSpaceName_PROPERTY).setValue(currentFactVO.getDiscoverSpaceName());
                processingDataTree.setItemCaption(currentFactVO.getId(),currentFactVO.getId());
                processingDataTree.setItemIcon(currentFactVO.getId(),FontAwesome.SHARE_ALT_SQUARE);
                processingDataTree.setChildrenAllowed(currentFactVO.getId(),false);
                processingDataTree.setParent(currentFactVO.getId(),relationDataRootItemId);
            }
        }

        processingDataTree.expandItem(factDataRootItemId);
        processingDataTree.expandItem(dimensionDataRootItemId);
        processingDataTree.expandItem(relationDataRootItemId);

        processingDataTree.addActionHandler(new Action.Handler() {
            @Override
            public Action[] getActions(Object itemId, Object o1) {
                if(itemId==null||itemId.equals(factDataRootItemId)||itemId.equals(dimensionDataRootItemId)||itemId.equals(relationDataRootItemId)){
                    return new Action[0];
                }
                String dataKind=getProcessingDataKind(itemId.toString());
                if(dataKind==null){
                    return new Action[0];
                }
                Action showDataDetailInfoAction = new Action(showDataDetailInfoActionName, FontAwesome.EYE);
                Action exploreRelatedInfoAction = new Action(exploreRelatedInfoActionName, VaadinIcons.CLUSTER);
                Action exploreSimilarInfoAction = new Action(exploreSimilarInfoActionName,VaadinIcons.FLIP_H);
                Action findRelationInfoOfTwoItem_1Action = new Action(findRelationInfoOfTwoItemAction_1Name, VaadinIcons.SPECIALIST);
                Action findRelationInfoOfTwoItem_2Action = new Action(findRelationInfoOfTwoItemAction_2Name, VaadinIcons.SPECIALIST);
                Action compareInfoOfManyItemsAction = new Action(compareInfoOfManyItemsActionName, VaadinIcons.SCALE_UNBALANCE);
                Action exploreRelationDataInfoAction = new Action(exploreRelationDataInfoActionName, VaadinIcons.GLASSES);

                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataKind)){
                    return new Action[]{exploreRelatedInfoAction,exploreSimilarInfoAction,findRelationInfoOfTwoItem_1Action,findRelationInfoOfTwoItem_2Action,compareInfoOfManyItemsAction,showDataDetailInfoAction};
                }
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(dataKind)){
                    return new Action[]{exploreRelatedInfoAction,findRelationInfoOfTwoItem_1Action,findRelationInfoOfTwoItem_2Action,showDataDetailInfoAction};
                }
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataKind)){
                    return new Action[]{exploreRelationDataInfoAction,showDataDetailInfoAction};
                }
                return new Action[0];
            }

            @Override
            public void handleAction(Action action, Object o, Object itemId) {
                executeDataAnalyzeLogic(action.getCaption(),itemId.toString());
            }
        });
        VerticalLayout spacingDivLayout1=new VerticalLayout();
        spacingDivLayout1.setHeight(10,Unit.PIXELS);
        processingDataSelectorContainerLayout.addComponent(spacingDivLayout1);

        visualizationAnalyzeSplitPanel.setFirstComponent(processingDataTreesContainer);
        //Right side
        this.dataAnalyzePageTabs=new TabSheet();
        visualizationAnalyzeSplitPanel.setSecondComponent(dataAnalyzePageTabs);

        findRelationInfoOfTwoAnalyzingDataPanel=new FindRelationInfoOfTwoAnalyzingDataPanel(this.currentUserClientInfo);
        findRelationInfoOfTwoAnalyzingDataPanel.setDiscoverSpaceName(this.discoverSpaceName);
        TabSheet.Tab findRelationInfoOfTwoItemActionLayoutTab =dataAnalyzePageTabs.addTab(findRelationInfoOfTwoAnalyzingDataPanel, "两项数据间关联关系发现");
        findRelationInfoOfTwoItemActionLayoutTab.setIcon(VaadinIcons.SPECIALIST);

        compareInfoOfManyAnalyzingDataPanel=new CompareInfoOfManyAnalyzingDataPanel(this.currentUserClientInfo,this.discoverSpaceName);
        TabSheet.Tab compareInfoOfManyItemsActionLayoutTab =dataAnalyzePageTabs.addTab(compareInfoOfManyAnalyzingDataPanel, "多项数据间属性值比较");
        compareInfoOfManyItemsActionLayoutTab.setIcon(VaadinIcons.SCALE_UNBALANCE);

        /*
        this.dataAnalyzePageTabs.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent selectedTabChangeEvent) {
                Component currentSelectedComponent=selectedTabChangeEvent.getTabSheet().getSelectedTab();
                if(currentSelectedComponent instanceof ExploreProcessingDataRelatedInfoPanel){}
            }
        });
        */

        this.dataAnalyzePageTabs.setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabSheet, Component component) {
                if(component instanceof ExploreProcessingDataRelatedInfoPanel){
                    ExploreProcessingDataRelatedInfoPanel currentPanel=(ExploreProcessingDataRelatedInfoPanel)component;
                    String currentTabNameKey=exploreRelatedInfoTabNamePerfix+currentPanel.getProcessingData().getId();
                    actionPanelTabKeyMap.remove(currentTabNameKey);
                    tabSheet.removeComponent(component);
                }
                if(component instanceof ExploreRelationRelatedDataInfoPanel){
                    ExploreRelationRelatedDataInfoPanel currentPanel=(ExploreRelationRelatedDataInfoPanel)component;
                    String currentTabNameKey=exploreRelationDataInfoTabNamePerfix+currentPanel.getProcessingData().getId();
                    actionPanelTabKeyMap.remove(currentTabNameKey);
                    tabSheet.removeComponent(component);
                }
                if(component instanceof ExploreProcessingDataSimilarInfoPanel){
                    ExploreProcessingDataSimilarInfoPanel currentPanel=(ExploreProcessingDataSimilarInfoPanel)component;
                    String currentTabNameKey=exploreSimilarInfoTabNamePerfix+currentPanel.getProcessingData().getId();
                    actionPanelTabKeyMap.remove(currentTabNameKey);
                    tabSheet.removeComponent(component);
                }
            }
        });

        this.actionPanelTabKeyMap =new HashMap<>();
    }

    private String getProcessingDataKind(String dataId){
        for(ProcessingDataVO currentFactVO:this.processingFactsForAnalyzing){
            if(currentFactVO.getId().equals(dataId)){
                return InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT;
            }
        }
        for(ProcessingDataVO currentDimensionVO:this.processingDimensionsForAnalyzing){
            if(currentDimensionVO.getId().equals(dataId)){
                return InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION;
            }
        }
        if(this.processingRelationsForAnalyzing!=null){
            for(ProcessingDataVO currentRelationVO:this.processingRelationsForAnalyzing){
                if(currentRelationVO.getId().equals(dataId)){
                    return InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION;
                }
            }
        }
        return null;
    }

    private String getProcessingDataDescription(String dataId){
        for(ProcessingDataVO currentFactVO:this.processingFactsForAnalyzing){
            if(currentFactVO.getId().equals(dataId)){
                return currentFactVO.getDataTypeName()+"<br/>"+currentFactVO.getId();
            }
        }
        for(ProcessingDataVO currentDimensionVO:this.processingDimensionsForAnalyzing){
            if(currentDimensionVO.getId().equals(dataId)){
                return currentDimensionVO.getDataTypeName()+"<br/>"+currentDimensionVO.getId();
            }
        }
        if(this.processingRelationsForAnalyzing!=null){
            for(ProcessingDataVO currentRelationVO:this.processingRelationsForAnalyzing){
                if(currentRelationVO.getId().equals(dataId)){
                    return currentRelationVO.getDataTypeName()+"<br/>"+currentRelationVO.getId();
                }
            }
        }
        return null;
    }

    private ProcessingDataVO getProcessingData(String dataId){
        for(ProcessingDataVO currentFactVO:this.processingFactsForAnalyzing){
            if(currentFactVO.getId().equals(dataId)){
                return currentFactVO;
            }
        }
        for(ProcessingDataVO currentDimensionVO:this.processingDimensionsForAnalyzing){
            if(currentDimensionVO.getId().equals(dataId)){
                return currentDimensionVO;
            }
        }
        if(this.processingRelationsForAnalyzing!=null){
            for(ProcessingDataVO currentRelationVO:this.processingRelationsForAnalyzing){
                if(currentRelationVO.getId().equals(dataId)){
                    return currentRelationVO;
                }
            }
        }
        return null;
    }

    private void executeDataAnalyzeLogic(String analyzeCommandName,String dataItemId){
        ProcessingDataVO targetProcessingDataVO=getProcessingData(dataItemId);
        if(targetProcessingDataVO==null){
            return;
        }
        if(exploreRelatedInfoActionName.equals(analyzeCommandName)){
            String tabCaption=exploreRelatedInfoTabNamePerfix+dataItemId;
            TabSheet.Tab alreadyExistTab=this.actionPanelTabKeyMap.get(tabCaption);
            if(alreadyExistTab!=null){
                dataAnalyzePageTabs.setSelectedTab(alreadyExistTab);
            }else{
                ExploreProcessingDataRelatedInfoPanel exploreProcessingDataRelatedInfoPanel=new ExploreProcessingDataRelatedInfoPanel(this.currentUserClientInfo,targetProcessingDataVO);
                TabSheet.Tab exploreRelatedInfoActionLayoutTab = dataAnalyzePageTabs.addTab(exploreProcessingDataRelatedInfoPanel, exploreRelatedInfoTabNamePerfix+dataItemId);
                exploreRelatedInfoActionLayoutTab.setClosable(true);
                exploreRelatedInfoActionLayoutTab.setIcon(VaadinIcons.CLUSTER);
                dataAnalyzePageTabs.setSelectedTab(exploreRelatedInfoActionLayoutTab);
                this.actionPanelTabKeyMap.put(tabCaption,exploreRelatedInfoActionLayoutTab);
            }
        }else if(exploreSimilarInfoActionName.equals(analyzeCommandName)){
            String tabCaption=exploreSimilarInfoTabNamePerfix+dataItemId;
            TabSheet.Tab alreadyExistTab=this.actionPanelTabKeyMap.get(tabCaption);
            if(alreadyExistTab!=null){
                dataAnalyzePageTabs.setSelectedTab(alreadyExistTab);
            }else{
                ExploreProcessingDataSimilarInfoPanel exploreProcessingDataSimilarInfoPanel=new ExploreProcessingDataSimilarInfoPanel(this.currentUserClientInfo,targetProcessingDataVO);
                TabSheet.Tab exploreSimilarInfoActionLayoutTab = dataAnalyzePageTabs.addTab(exploreProcessingDataSimilarInfoPanel, exploreSimilarInfoTabNamePerfix+dataItemId);
                exploreSimilarInfoActionLayoutTab.setClosable(true);
                exploreSimilarInfoActionLayoutTab.setIcon(VaadinIcons.FLIP_H);
                dataAnalyzePageTabs.setSelectedTab(exploreSimilarInfoActionLayoutTab);
                this.actionPanelTabKeyMap.put(tabCaption,exploreSimilarInfoActionLayoutTab);
            }
        }else if(showDataDetailInfoActionName.equals(analyzeCommandName)){
            showDataDetailInfoPanel(targetProcessingDataVO);
        }else if(exploreRelationDataInfoActionName.equals(analyzeCommandName)){
            String tabCaption=exploreRelationDataInfoTabNamePerfix+dataItemId;
            TabSheet.Tab alreadyExistTab=this.actionPanelTabKeyMap.get(tabCaption);
            if(alreadyExistTab!=null){
                dataAnalyzePageTabs.setSelectedTab(alreadyExistTab);
            }else{
                ExploreRelationRelatedDataInfoPanel exploreRelationRelatedDataInfoPanel=new ExploreRelationRelatedDataInfoPanel(this.currentUserClientInfo,targetProcessingDataVO);
                TabSheet.Tab exploreRelatedInfoActionLayoutTab = dataAnalyzePageTabs.addTab(exploreRelationRelatedDataInfoPanel, exploreRelationDataInfoTabNamePerfix+dataItemId);
                exploreRelatedInfoActionLayoutTab.setClosable(true);
                exploreRelatedInfoActionLayoutTab.setIcon(VaadinIcons.GLASSES);
                dataAnalyzePageTabs.setSelectedTab(exploreRelatedInfoActionLayoutTab);
                this.actionPanelTabKeyMap.put(tabCaption,exploreRelatedInfoActionLayoutTab);

            }
        }else if(findRelationInfoOfTwoItemAction_1Name.equals(analyzeCommandName)){
            findRelationInfoOfTwoAnalyzingDataPanel.addFirstAnalyzingData(targetProcessingDataVO.getId());
        }else if(findRelationInfoOfTwoItemAction_2Name.equals(analyzeCommandName)){
            findRelationInfoOfTwoAnalyzingDataPanel.addSecondAnalyzingData(targetProcessingDataVO.getId());
        }
        else if(compareInfoOfManyItemsActionName.equals(analyzeCommandName)){
            compareInfoOfManyAnalyzingDataPanel.addDataForCompare(targetProcessingDataVO);
        }
    }

    private void showDataDetailInfoPanel(ProcessingDataVO targetProcessingDataVO){
        String discoverSpaceName=targetProcessingDataVO.getDiscoverSpaceName();
        String dataTypeName=targetProcessingDataVO.getDataTypeName();
        String dataId=targetProcessingDataVO.getId();
        String targetWindowUID=discoverSpaceName+"_GlobalDataInstanceDetailWindow_"+dataTypeName+"_"+dataId;
        Window targetWindow=this.currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(discoverSpaceName,targetWindowUID);
        if(targetWindow!=null){
            targetWindow.bringToFront();
            targetWindow.center();
        }else{
            String dataTypeKind= targetProcessingDataVO.getDataTypeKind();
            String dataDetailInfoTitle;
            if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION)){
                dataDetailInfoTitle="维度数据详细信息";
            }
            else if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT)){
                dataDetailInfoTitle="事实数据详细信息";
            }
            else if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION)){
                dataDetailInfoTitle="关系数据详细信息";
            }else{
                dataDetailInfoTitle="数据详细信息";
            }
            MeasurableValueVO targetMeasurableValueVO=InfoDiscoverSpaceOperationUtil.getMeasurableValueById(targetProcessingDataVO.getDiscoverSpaceName(),targetProcessingDataVO.getId());
            TypeDataInstanceDetailPanel typeDataInstanceDetailPanel=new TypeDataInstanceDetailPanel(this.currentUserClientInfo,targetMeasurableValueVO);
            final Window window = new Window(UICommonElementsUtil.generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
            window.setWidth(570, Unit.PIXELS);
            window.setHeight(800,Unit.PIXELS);
            window.setCaptionAsHtml(true);
            window.setResizable(true);
            window.setDraggable(true);
            window.setModal(false);
            window.setContent(typeDataInstanceDetailPanel);
            typeDataInstanceDetailPanel.setContainerDialog(window);
            window.addCloseListener(new Window.CloseListener() {
                @Override
                public void windowClose(Window.CloseEvent closeEvent) {
                    currentUserClientInfo.getRuntimeWindowsRepository().removeExistingWindow(discoverSpaceName,targetWindowUID);
                }
            });
            this.currentUserClientInfo.getRuntimeWindowsRepository().addNewWindow(discoverSpaceName,targetWindowUID,window);
            UI.getCurrent().addWindow(window);
        }
    }

    public boolean addNewProcessingData(ProcessingDataVO newProcessingData){
        String processingDataId=newProcessingData.getId();
        if(processingDataTree.getItem(processingDataId)!=null){
            Notification errorNotification = new Notification("数据校验错误","ID为 "+processingDataId+" 的待分析数据已经存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{

            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(newProcessingData.getDataTypeKind())){
                this.processingFactsForAnalyzing.add(newProcessingData);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(newProcessingData.getDataTypeKind())){
                this.processingDimensionsForAnalyzing.add(newProcessingData);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(newProcessingData.getDataTypeKind())){
                if(this.processingRelationsForAnalyzing==null){
                    this.processingRelationsForAnalyzing=new ArrayList<>();
                }
                this.processingRelationsForAnalyzing.add(newProcessingData);
            }
            Item newRecord=processingDataTree.addItem(newProcessingData.getId());
            newRecord.getItemProperty(DataTypeName_PROPERTY).setValue(newProcessingData.getDataTypeName());
            newRecord.getItemProperty(Id_PROPERTY).setValue(newProcessingData.getId());
            newRecord.getItemProperty(DataTypeKind_PROPERTY).setValue(newProcessingData.getDataTypeKind());
            newRecord.getItemProperty(DiscoverSpaceName_PROPERTY).setValue(newProcessingData.getDiscoverSpaceName());
            processingDataTree.setItemCaption(newProcessingData.getId(),newProcessingData.getId());
            processingDataTree.setChildrenAllowed(newProcessingData.getId(),false);

            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(newProcessingData.getDataTypeKind())){
                processingDataTree.setItemIcon(newProcessingData.getId(),FontAwesome.SQUARE_O);
                processingDataTree.setParent(newProcessingData.getId(),factDataRootItemId);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(newProcessingData.getDataTypeKind())){
                processingDataTree.setItemIcon(newProcessingData.getId(),FontAwesome.TAG);
                processingDataTree.setParent(newProcessingData.getId(),dimensionDataRootItemId);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(newProcessingData.getDataTypeKind())){
                processingDataTree.setItemIcon(newProcessingData.getId(),FontAwesome.SHARE_ALT_SQUARE);
                processingDataTree.setParent(newProcessingData.getId(),relationDataRootItemId);
            }
            return true;
        }
    }
}
