package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceAddProcessingDataEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.TypeDataInstanceDetailPanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 5/1/17.
 */
public class SimilarRelationableTableActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private RelationableValueVO relationableValueVO;

    public SimilarRelationableTableActions(UserClientInfo userClientInfo,RelationableValueVO relationableValueVO) {
        this.currentUserClientInfo = userClientInfo;
        this.relationableValueVO=relationableValueVO;
        String relationableType=this.relationableValueVO.getRelationableTypeKind();
        String dataTypeName=this.relationableValueVO.getRelationableTypeName();
        String dataId=this.relationableValueVO.getId();
        String relationableInfoText=null;
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(relationableType)){
            relationableInfoText= FontAwesome.TAGS.getHtml()+" "+dataTypeName+" /"+" "+dataId;

        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(relationableType)){
            relationableInfoText= FontAwesome.CLONE.getHtml()+" "+dataTypeName+" /"+" "+dataId;

        }
        Label relationableInfoLabel=new Label(relationableInfoText, ContentMode.HTML);
        this.addComponent(relationableInfoLabel);
        this.setComponentAlignment(relationableInfoLabel, Alignment.MIDDLE_RIGHT);

        Label operationSpaceDivLabel=new Label("<span style='color:#AAAAAA;padding-left:10px;padding-right:5px;'>|</span>",ContentMode.HTML);
        this.addComponent(operationSpaceDivLabel);
        this.setComponentAlignment(operationSpaceDivLabel, Alignment.MIDDLE_RIGHT);

        Button showTypeDataDetailButton = new Button();
        showTypeDataDetailButton.setIcon(FontAwesome.EYE);
        showTypeDataDetailButton.setDescription("显示数据详情");
        showTypeDataDetailButton.addStyleName(ValoTheme.BUTTON_SMALL);
        showTypeDataDetailButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        showTypeDataDetailButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showDataDetailInfoPanel();
            }
        });
        addComponent(showTypeDataDetailButton);

        Button addToProcessingListButton = new Button();
        addToProcessingListButton.setIcon(VaadinIcons.INBOX);
        addToProcessingListButton.setDescription("加入待处理数据列表");
        addToProcessingListButton.addStyleName(ValoTheme.BUTTON_SMALL);
        addToProcessingListButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        addToProcessingListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                processAddToProcessingList();
            }
        });
        addComponent(addToProcessingListButton);
    }

    private void showDataDetailInfoPanel(){
        MeasurableValueVO targetMeasurableValue=InfoDiscoverSpaceOperationUtil.getMeasurableValueById(this.relationableValueVO.getDiscoverSpaceName(),this.relationableValueVO.getId());
        if(targetMeasurableValue==null){
            Notification errorNotification = new Notification("获取数据错误",
                    "系统中不存在ID为 "+this.relationableValueVO.getId()+" 的数据", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        String discoverSpaceName=targetMeasurableValue.getDiscoverSpaceName();
        String dataTypeName=targetMeasurableValue.getMeasurableTypeName();
        String dataId=targetMeasurableValue.getId();
        String targetWindowUID=discoverSpaceName+"_GlobalDataInstanceDetailWindow_"+dataTypeName+"_"+dataId;
        Window targetWindow=this.currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(discoverSpaceName,targetWindowUID);
        if(targetWindow!=null){
            targetWindow.bringToFront();
            //targetWindow.center();
        }else{
            String dataTypeKind= targetMeasurableValue.getMeasurableTypeKind();
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
            TypeDataInstanceDetailPanel typeDataInstanceDetailPanel=new TypeDataInstanceDetailPanel(this.currentUserClientInfo,targetMeasurableValue);
            final Window window = new Window(UICommonElementsUtil.generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
            window.setWidth(570, Unit.PIXELS);
            window.setHeight(800,Unit.PIXELS);
            window.setCaptionAsHtml(true);
            window.setResizable(true);
            window.setDraggable(true);
            window.setModal(false);
            window.center();
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

    private void processAddToProcessingList(){
        ProcessingDataVO processingDataVO=new ProcessingDataVO();
        processingDataVO.setId(this.relationableValueVO.getId());
        processingDataVO.setDiscoverSpaceName(this.relationableValueVO.getDiscoverSpaceName());
        processingDataVO.setDataTypeKind(this.relationableValueVO.getRelationableTypeKind());
        processingDataVO.setDataTypeName(this.relationableValueVO.getRelationableTypeName());

        DiscoverSpaceAddProcessingDataEvent discoverSpaceAddProcessingDataEvent=new DiscoverSpaceAddProcessingDataEvent(this.relationableValueVO.getDiscoverSpaceName());
        discoverSpaceAddProcessingDataEvent.setProcessingData(processingDataVO);
        this.currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceAddProcessingDataEvent);
    }
}
