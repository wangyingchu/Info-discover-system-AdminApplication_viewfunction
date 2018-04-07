package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataListVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement.ProcessingDataAnalyzePanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangychu on 12/15/16.
 */
public class ProcessingDataOperationPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private SectionActionsBar discoverSpaceNameNoticeActionsBar;
    private ProcessingDataListVO processingDataList;

    private ProcessingDataList dimensionProcessingDataList;
    private ProcessingDataList factProcessingDataList;
    private ProcessingDataList relationProcessingDataList;
    private boolean showOperationActions;

    private boolean showRelationData;
    public ProcessingDataOperationPanel(UserClientInfo currentUserClientInfo,boolean showOperationActions,boolean isSelectable,boolean showRelationData){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(true);
        this.setSpacing(true);
        this.showOperationActions=showOperationActions;
        this.showRelationData=showRelationData;
        if(this.showOperationActions) {
            this.discoverSpaceNameNoticeActionsBar = new SectionActionsBar(new Label("---", ContentMode.HTML));
            this.addComponent(this.discoverSpaceNameNoticeActionsBar);
        }
        TabSheet processingDataListTabs=new TabSheet();
        VerticalLayout discoverSpaceDimensionsListLayout=new VerticalLayout();
        TabSheet.Tab discoverSpaceDimensionsInfoLayoutTab =processingDataListTabs.addTab(discoverSpaceDimensionsListLayout, "空间维度数据");
        discoverSpaceDimensionsInfoLayoutTab.setIcon(FontAwesome.TAGS);
        this.dimensionProcessingDataList=new ProcessingDataList(this.currentUserClientInfo,isSelectable);
        this.dimensionProcessingDataList.setProcessingDataType(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
        discoverSpaceDimensionsListLayout.addComponent(this.dimensionProcessingDataList);

        VerticalLayout discoverSpaceFactsListLayout=new VerticalLayout();
        TabSheet.Tab discoverSpaceFactsInfoLayoutTab =processingDataListTabs.addTab(discoverSpaceFactsListLayout, "空间事实数据");
        discoverSpaceFactsInfoLayoutTab.setIcon(FontAwesome.CLONE);
        this.factProcessingDataList=new ProcessingDataList(this.currentUserClientInfo,isSelectable);
        this.factProcessingDataList.setProcessingDataType(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT);
        discoverSpaceFactsListLayout.addComponent(this.factProcessingDataList);
        if(this.showRelationData) {
            VerticalLayout discoverSpaceRelationsListLayout = new VerticalLayout();
            TabSheet.Tab discoverSpaceRelationsInfoLayoutTab = processingDataListTabs.addTab(discoverSpaceRelationsListLayout, "空间关系数据");
            discoverSpaceRelationsInfoLayoutTab.setIcon(FontAwesome.SHARE_ALT);
            this.relationProcessingDataList = new ProcessingDataList(this.currentUserClientInfo,isSelectable);
            this.relationProcessingDataList.setProcessingDataType(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION);
            discoverSpaceRelationsListLayout.addComponent(this.relationProcessingDataList);
        }
        this.addComponent(processingDataListTabs);
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
        this.dimensionProcessingDataList.setDiscoverSpaceName(this.discoverSpaceName);
        this.factProcessingDataList.setDiscoverSpaceName(this.discoverSpaceName);
        if(this.showRelationData) {
            this.relationProcessingDataList.setDiscoverSpaceName(this.discoverSpaceName);
        }
    }

    @Override
    public void attach() {
        super.attach();
        if(this.showOperationActions) {
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName(), ContentMode.HTML);
            discoverSpaceNameNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            HorizontalLayout actionButtonsContainer=new HorizontalLayout();
            Button refreshListButton = new Button();
            refreshListButton.setIcon(FontAwesome.REFRESH);
            refreshListButton.setDescription("刷新待处理数据列表信息");
            refreshListButton.addStyleName(ValoTheme.BUTTON_SMALL);
            refreshListButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            refreshListButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    dimensionProcessingDataList.refreshDataList();
                    factProcessingDataList.refreshDataList();
                    if(showRelationData) {
                        relationProcessingDataList.refreshDataList();
                    }
                }
            });
            actionButtonsContainer.addComponent(refreshListButton);

            Button analyzeSelectedDataButton = new Button();
            analyzeSelectedDataButton.setIcon(VaadinIcons.LINE_BAR_CHART);
            analyzeSelectedDataButton.setDescription("分析选择的待处理数据");
            analyzeSelectedDataButton.addStyleName(ValoTheme.BUTTON_SMALL);
            analyzeSelectedDataButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            analyzeSelectedDataButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    analyzeSelectedDataButton();
                }
            });
            actionButtonsContainer.addComponent(analyzeSelectedDataButton);
            discoverSpaceNameNoticeActionsBar.addActionComponent(actionButtonsContainer);
        }
    }

    public ProcessingDataListVO getProcessingDataList() {
        return processingDataList;
    }

    public void setProcessingDataList(ProcessingDataListVO processingDataList) {
        this.processingDataList = processingDataList;
        this.dimensionProcessingDataList.setProcessingDataVOList(this.processingDataList.getProcessingDimensions());
        this.factProcessingDataList.setProcessingDataVOList(this.processingDataList.getProcessingFacts());
        if(this.showRelationData) {
            this.relationProcessingDataList.setProcessingDataVOList(this.processingDataList.getProcessingRelations());
        }
    }

    public void setDataTablesHeight(int tableHeight){
        this.dimensionProcessingDataList.setProcessingDataInstanceTableHeight(tableHeight);
        this.factProcessingDataList.setProcessingDataInstanceTableHeight(tableHeight);
        if(this.relationProcessingDataList!=null) {
            this.relationProcessingDataList.setProcessingDataInstanceTableHeight(tableHeight);
        }
    }

    public List<String> getSelectedDimensionsId(){
        return this.dimensionProcessingDataList.getSelectedDataId();
    }

    public List<String> getSelectedFactsId(){
        return this.factProcessingDataList.getSelectedDataId();
    }

    public List<String> getSelectedRelationsId(){
        if(this.relationProcessingDataList!=null) {
            return this.relationProcessingDataList.getSelectedDataId();
        }else{
            return null;
        }
    }

    private void analyzeSelectedDataButton(){
        Map<String,List<ProcessingDataVO>> selectedProcessingData=new HashMap<>();
        int totalSelectedDataCount=0;
        List<ProcessingDataVO> dimensionProcessingDataForAnalyzing=this.dimensionProcessingDataList.getSelectedProcessingDataInfo();
        totalSelectedDataCount=dimensionProcessingDataForAnalyzing.size();
        List<ProcessingDataVO> factProcessingDataForAnalyzing=this.factProcessingDataList.getSelectedProcessingDataInfo();
        totalSelectedDataCount=totalSelectedDataCount+factProcessingDataForAnalyzing.size();
        if(this.relationProcessingDataList!=null){
            List<ProcessingDataVO> relationProcessingDataForAnalyzing=this.relationProcessingDataList.getSelectedProcessingDataInfo();
            totalSelectedDataCount=totalSelectedDataCount+relationProcessingDataForAnalyzing.size();
            selectedProcessingData.put(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,this.relationProcessingDataList.getSelectedProcessingDataInfo());
        }
        if(totalSelectedDataCount==0){
            Notification errorNotification = new Notification("数据校验错误","请选择至少一项待处理数据", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        selectedProcessingData.put(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION,dimensionProcessingDataForAnalyzing);
        selectedProcessingData.put(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,factProcessingDataForAnalyzing);

        ProcessingDataAnalyzePanel processingDataAnalyzePanel=new ProcessingDataAnalyzePanel(this.currentUserClientInfo,this.discoverSpaceName,selectedProcessingData);
        String dataDetailInfoTitle="待处理数据信息分析发现";
        final Window window = new Window(UICommonElementsUtil.generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
        window.setSizeFull();
        window.setCaptionAsHtml(true);
        window.setResizable(false);
        window.setDraggable(false);
        window.setModal(false);
        window.setContent(processingDataAnalyzePanel);
        UI.getCurrent().addWindow(window);
    }
}
