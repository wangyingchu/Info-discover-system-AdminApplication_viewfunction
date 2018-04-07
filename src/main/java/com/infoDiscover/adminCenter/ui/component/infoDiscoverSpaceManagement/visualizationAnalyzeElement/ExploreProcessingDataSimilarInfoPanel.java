package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * Created by wangychu on 4/17/17.
 */
public class ExploreProcessingDataSimilarInfoPanel extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private ProcessingDataVO processingData;
    private int browserWindowHeight;
    private VerticalLayout dimensionCheckboxFormLayout;
    private SimilarRelationablesList similarRelationablesList;
    private BrowserFrame similarDataInfoBrowserFrame;
    private Map<CheckBox,String> analyzingDimensionCheckBoxMap;
    private Label similarDataTableTitle;
    private final static String similarInfoGraphBaseAddress= AdminCenterPropertyHandler.
            getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+"infoAnalysePages/typeInstanceRelationAnalyse/similarTypeInstancesExploreGraph.html";

    public ExploreProcessingDataSimilarInfoPanel(UserClientInfo userClientInfo,ProcessingDataVO processingData){
        this.setMargin(true);
        this.setSpacing(true);
        this.currentUserClientInfo = userClientInfo;
        this.processingData=processingData;
        this.setWidth(100,Unit.PERCENTAGE);
        browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        this.setHeight(browserWindowHeight-150,Unit.PIXELS);

        VerticalLayout dataRelatedDimensionInfoLayout=new VerticalLayout();
        dataRelatedDimensionInfoLayout.setHeight(100,Unit.PERCENTAGE);
        dataRelatedDimensionInfoLayout.setWidth(170,Unit.PIXELS);
        this.addComponent(dataRelatedDimensionInfoLayout);

        Label analyzeDataDimensionSelectorsTitle= new Label(FontAwesome.TAG.getHtml() +" 选择相似数据关联的维度", ContentMode.HTML);
        analyzeDataDimensionSelectorsTitle.addStyleName(ValoTheme.LABEL_COLORED);
        analyzeDataDimensionSelectorsTitle.addStyleName(ValoTheme.LABEL_SMALL);
        analyzeDataDimensionSelectorsTitle.setWidth(100,Unit.PERCENTAGE);
        dataRelatedDimensionInfoLayout.addComponent(analyzeDataDimensionSelectorsTitle);

        Panel analyzeDataDimensionSelectorsPanel=new Panel();
        analyzeDataDimensionSelectorsPanel.setWidth(170,Unit.PERCENTAGE);
        analyzeDataDimensionSelectorsPanel.setHeight(browserWindowHeight-250,Unit.PIXELS);
        dataRelatedDimensionInfoLayout.addComponent(analyzeDataDimensionSelectorsPanel);

        dimensionCheckboxFormLayout=new VerticalLayout();
        dimensionCheckboxFormLayout.setMargin(true);
        analyzeDataDimensionSelectorsPanel.setContent(dimensionCheckboxFormLayout);

        dataRelatedDimensionInfoLayout.setExpandRatio(analyzeDataDimensionSelectorsPanel,1);

        HorizontalLayout actionButtonsContainerLayout=new HorizontalLayout();
        dataRelatedDimensionInfoLayout.addComponent(actionButtonsContainerLayout);

        MenuBar exploreSimilarDataMenuBar = new MenuBar();
        exploreSimilarDataMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);

        MenuBar.Command visualizationAnalyzeMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedCommandName=selectedItem.getText();
                if("探索与所有已选维度关联的数据".equals(selectedCommandName)){
                    similarDataTableTitle.setValue(VaadinIcons.TABLE.getHtml() +" 相似的数据 (与所有已选维度关联)");
                    doExploreAllMatchedSimilarData();
                }
                if("探索与任一已选维度关联的数据".equals(selectedCommandName)){
                    similarDataTableTitle.setValue(VaadinIcons.TABLE.getHtml() +" 相似的数据 (与任一已选维度关联)");
                    doExploreAnyMatchedSimilarData();
                }

            }
        };
        MenuBar.MenuItem exploreSimilarDataRootItem=exploreSimilarDataMenuBar.addItem("探索相似数据", VaadinIcons.SEARCH, null);
        exploreSimilarDataRootItem.addItem("探索与所有已选维度关联的数据", FontAwesome.PLAY, visualizationAnalyzeMenuItemCommand);
        exploreSimilarDataRootItem.addItem("探索与任一已选维度关联的数据", FontAwesome.PLAY, visualizationAnalyzeMenuItemCommand);
        actionButtonsContainerLayout.addComponent(exploreSimilarDataMenuBar);

        VerticalLayout spacingDivLayout0=new VerticalLayout();
        spacingDivLayout0.setHeight(100,Unit.PERCENTAGE);
        spacingDivLayout0.setWidth(100,Unit.PIXELS);
        this.addComponent(spacingDivLayout0);

        VerticalLayout similarDataTableLayout=new VerticalLayout();
        similarDataTableLayout.setHeight(100,Unit.PERCENTAGE);
        similarDataTableLayout.setWidth(400,Unit.PIXELS);
        this.addComponent(similarDataTableLayout);

        similarDataTableTitle= new Label(VaadinIcons.TABLE.getHtml() +" 相似的数据", ContentMode.HTML);
        similarDataTableTitle.addStyleName(ValoTheme.LABEL_SMALL);
        similarDataTableTitle.setWidth(100,Unit.PERCENTAGE);
        similarDataTableLayout.addComponent(similarDataTableTitle);

        similarRelationablesList=new SimilarRelationablesList(this.currentUserClientInfo,browserWindowHeight);
        similarDataTableLayout.addComponent(similarRelationablesList);
        similarDataTableLayout.setExpandRatio(similarRelationablesList,1);

        VerticalLayout dataDetailGraphLayout=new VerticalLayout();
        dataDetailGraphLayout.setHeight(100,Unit.PERCENTAGE);
        dataDetailGraphLayout.setWidth(100,Unit.PERCENTAGE);
        this.addComponent(dataDetailGraphLayout);
        this.setExpandRatio(dataDetailGraphLayout,1f);

        similarDataInfoBrowserFrame = new BrowserFrame();
        similarDataInfoBrowserFrame.setSizeFull();
        similarDataInfoBrowserFrame.setHeight(browserWindowHeight-180,Unit.PIXELS);
        dataDetailGraphLayout.addComponent(similarDataInfoBrowserFrame);

        analyzingDimensionCheckBoxMap=new HashMap<>();
        loadRelatedDimensionsInfo();
    }

    private void loadRelatedDimensionsInfo(){
        List<RelationValueVO> relationValuesList = InfoDiscoverSpaceOperationUtil.getRelationableRelationsById(processingData.getDiscoverSpaceName(), processingData.getDataTypeKind(), processingData.getId());
        if(relationValuesList!=null){
            for(RelationValueVO currentRelationValue:relationValuesList){
                currentRelationValue.getId();
                currentRelationValue.getRelationTypeName();
                RelationableValueVO fromData=currentRelationValue.getFromRelationable();
                if(!fromData.getId().equals(processingData.getId())){
                    if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(fromData.getRelationableTypeKind())){
                        CheckBox dataSelectCheckBox = new CheckBox(VaadinIcons.ANGLE_DOUBLE_LEFT.getHtml()+fromData.getRelationableTypeName()+"["+fromData.getId()+"]");
                        dataSelectCheckBox.setCaptionAsHtml(true);
                        dataSelectCheckBox.setDescription( "关联的关系: "+currentRelationValue.getRelationTypeName()+"["+currentRelationValue.getId()+"]");
                        dataSelectCheckBox.addStyleName(ValoTheme.CHECKBOX_SMALL);
                        dataSelectCheckBox.setValue(true);
                        dimensionCheckboxFormLayout.addComponent(dataSelectCheckBox);
                        analyzingDimensionCheckBoxMap.put(dataSelectCheckBox,fromData.getId());
                    }
                }
                RelationableValueVO toData=currentRelationValue.getToRelationable();
                if(!toData.getId().equals(processingData.getId())){
                    if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(toData.getRelationableTypeKind())){
                        CheckBox dataSelectCheckBox = new CheckBox(VaadinIcons.ANGLE_DOUBLE_RIGHT.getHtml()+toData.getRelationableTypeName()+"["+toData.getId()+"]");
                        dataSelectCheckBox.setCaptionAsHtml(true);
                        dataSelectCheckBox.setDescription( "关联的关系: "+currentRelationValue.getRelationTypeName()+"["+currentRelationValue.getId()+"]");
                        dataSelectCheckBox.addStyleName(ValoTheme.CHECKBOX_SMALL);
                        dataSelectCheckBox.setValue(true);
                        dimensionCheckboxFormLayout.addComponent(dataSelectCheckBox);
                        analyzingDimensionCheckBoxMap.put(dataSelectCheckBox,toData.getId());
                    }
                }
            }
        }
    }

    public ProcessingDataVO getProcessingData(){
        return this.processingData;
    }

    private void doExploreAllMatchedSimilarData(){
        List<String> dimensionList=new ArrayList<>();
        Set<CheckBox> checkboxSet=analyzingDimensionCheckBoxMap.keySet();
        Iterator<CheckBox> checkboxIterator=checkboxSet.iterator();
        while(checkboxIterator.hasNext()){
            CheckBox currentCheckBox=checkboxIterator.next();
            if(currentCheckBox.getValue()){
                dimensionList.add(analyzingDimensionCheckBoxMap.get(currentCheckBox));
            }
        }
        if(dimensionList.size()==0){
            Notification errorNotification = new Notification("数据校验错误","请选择至少一项关联的维度", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        List<RelationableValueVO> similarRelationableValues=
                InfoDiscoverSpaceOperationUtil.getSimilarRelationableConnectedSameDimensions(processingData.getDiscoverSpaceName(),processingData.getId(),dimensionList,"ALL");
        this.similarRelationablesList.renderSimilarRelationablesList(similarRelationableValues);
        this.similarDataInfoBrowserFrame.setSource(new ExternalResource(getGraphLocationFullAddress(dimensionList,"ALL")));
    }

    private void doExploreAnyMatchedSimilarData(){
        List<String> dimensionList=new ArrayList<>();
        Set<CheckBox> checkboxSet=analyzingDimensionCheckBoxMap.keySet();
        Iterator<CheckBox> checkboxIterator=checkboxSet.iterator();
        while(checkboxIterator.hasNext()){
            CheckBox currentCheckBox=checkboxIterator.next();
            if(currentCheckBox.getValue()){
                dimensionList.add(analyzingDimensionCheckBoxMap.get(currentCheckBox));
            }
        }
        if(dimensionList.size()==0){
            Notification errorNotification = new Notification("数据校验错误","请选择至少一项关联的维度", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        List<RelationableValueVO> similarRelationableValues=
                InfoDiscoverSpaceOperationUtil.getSimilarRelationableConnectedSameDimensions(processingData.getDiscoverSpaceName(),processingData.getId(),dimensionList,"ANY");
        this.similarRelationablesList.renderSimilarRelationablesList(similarRelationableValues);
        this.similarDataInfoBrowserFrame.setSource(new ExternalResource(getGraphLocationFullAddress(dimensionList,"ANY")));
    }

    private String getGraphLocationFullAddress(List<String> dimensionsList,String filteringPattern){
        long timeStampPostValue=new Date().getTime();
        String dataId=this.processingData.getId();
        String sourceDataInstanceId=dataId.replaceAll("#","%23");
        sourceDataInstanceId=sourceDataInstanceId.replaceAll(":","%3a");

        StringBuffer dimensionsListStr=new StringBuffer();
        for(int i=0;i<dimensionsList.size();i++){
            String currentDataId=dimensionsList.get(i);
            String enCodedID=currentDataId.replaceFirst("#","%23").replaceFirst(":","%3a");
            if(i!=dimensionsList.size()-1){
                dimensionsListStr.append(enCodedID+",");
            }else{
                dimensionsListStr.append(enCodedID);
            }
        }
        String graphLocationFullAddress=
                this.similarInfoGraphBaseAddress+"?discoverSpace="+this.processingData.getDiscoverSpaceName()+
                        "&sourceDataInstanceId="+sourceDataInstanceId+"&dimensionsIdList="+dimensionsListStr+
                        "&filteringPattern="+filteringPattern+"&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-200);
        return graphLocationFullAddress;
    }
}
