package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceAddProcessingDataEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 11/8/16.
 */
public class TypeDataInstanceTableRowActions extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private MeasurableValueVO measurableValue;
    private TypeDataInstanceList containerTypeDataInstanceList;
    private String dataItemIdInTypeDataInstanceList;

    public TypeDataInstanceTableRowActions(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
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

        Button deleteButton = new Button();
        deleteButton.setIcon(FontAwesome.TRASH_O);
        deleteButton.setDescription("删除数据");
        deleteButton.addStyleName(ValoTheme.BUTTON_SMALL);
        deleteButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                processDeleteData();
            }
        });
        addComponent(deleteButton);
    }

    public MeasurableValueVO getMeasurableValue() {
        return measurableValue;
    }

    public void setMeasurableValue(MeasurableValueVO measurableValue) {
        this.measurableValue = measurableValue;
    }

    private void showDataDetailInfoPanel(){
        String discoverSpaceName=getMeasurableValue().getDiscoverSpaceName();
        String dataTypeName=getMeasurableValue().getMeasurableTypeName();
        String dataId=getMeasurableValue().getId();
        String targetWindowUID=discoverSpaceName+"_GlobalDataInstanceDetailWindow_"+dataTypeName+"_"+dataId;
        Window targetWindow=this.currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(discoverSpaceName,targetWindowUID);
        if(targetWindow!=null){
            targetWindow.bringToFront();
            //targetWindow.center();
        }else{
            String dataTypeKind= getMeasurableValue().getMeasurableTypeKind();
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
            TypeDataInstanceDetailPanel typeDataInstanceDetailPanel=new TypeDataInstanceDetailPanel(this.currentUserClientInfo,getMeasurableValue());
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

            int currentSubWindowXPositionOffset=getContainerTypeDataInstanceList().getSubWindowsXPositionOffset();
            int currentSubWindowYPositionOffset=getContainerTypeDataInstanceList().getSubWindowsYPositionOffset();
            window.setPosition(currentSubWindowXPositionOffset,currentSubWindowYPositionOffset);
            getContainerTypeDataInstanceList().setSubWindowsXPositionOffset(currentSubWindowXPositionOffset + 50);
            if(currentSubWindowYPositionOffset<=450){
                getContainerTypeDataInstanceList().setSubWindowsYPositionOffset(currentSubWindowYPositionOffset + 50);
            }else{
                getContainerTypeDataInstanceList().setSubWindowsYPositionOffset(20);
            }
            UI.getCurrent().addWindow(window);
        }
    }

    private void processAddToProcessingList(){
        ProcessingDataVO processingDataVO=new ProcessingDataVO();
        processingDataVO.setId(getMeasurableValue().getId());
        processingDataVO.setDiscoverSpaceName(getMeasurableValue().getDiscoverSpaceName());
        processingDataVO.setDataTypeKind(getMeasurableValue().getMeasurableTypeKind());
        processingDataVO.setDataTypeName(getMeasurableValue().getMeasurableTypeName());

        DiscoverSpaceAddProcessingDataEvent discoverSpaceAddProcessingDataEvent=new DiscoverSpaceAddProcessingDataEvent(getMeasurableValue().getDiscoverSpaceName());
        discoverSpaceAddProcessingDataEvent.setProcessingData(processingDataVO);
        this.currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceAddProcessingDataEvent);
    }

    private void processDeleteData(){
        String dataTypeKind= getMeasurableValue().getMeasurableTypeKind();
        String dataTypeInfoTitle;
        if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION)){
            dataTypeInfoTitle="维度数据";
        }
        else if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT)){
            dataTypeInfoTitle="事实数据";
        }
        else if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION)){
            dataTypeInfoTitle="关系数据";
        }else{
            dataTypeInfoTitle="数据详细信息";
        }
        String confirmMessageString=" 请确认删除"+dataTypeInfoTitle+" "+getMeasurableValue().getMeasurableTypeName()+"/ "+getMeasurableValue().getId();
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog deleteDataInstanceConfirmDialog = new ConfirmDialog();
        deleteDataInstanceConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                deleteDataInstanceConfirmDialog.close();

                boolean removeDataInstanceResult=InfoDiscoverSpaceOperationUtil.removeMeasurableById(getMeasurableValue().getDiscoverSpaceName(),getMeasurableValue().getMeasurableTypeKind(),getMeasurableValue().getId());
                if(removeDataInstanceResult){
                    Notification resultNotification = new Notification("删除数据操作成功",
                            "删除"+dataTypeInfoTitle+" "+getMeasurableValue().getMeasurableTypeName()+"/ "+getMeasurableValue().getId()+"　成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());

                    String discoverSpaceName=getMeasurableValue().getDiscoverSpaceName();
                    String dataTypeName=getMeasurableValue().getMeasurableTypeName();
                    String dataId=getMeasurableValue().getId();

                    String targetWindowUID=discoverSpaceName+"_GlobalDataInstanceDetailWindow_"+dataTypeName+"_"+dataId;
                    Window detailDataWindow=currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(discoverSpaceName,targetWindowUID);
                    if(detailDataWindow!=null){
                        detailDataWindow.close();
                    }
                    currentUserClientInfo.getRuntimeWindowsRepository().removeExistingWindow(discoverSpaceName,targetWindowUID);

                    if(getContainerTypeDataInstanceList()!=null){
                        ProcessingDataVO processingDataVO=new ProcessingDataVO();
                        processingDataVO.setId(getMeasurableValue().getId());
                        processingDataVO.setDiscoverSpaceName(discoverSpaceName);
                        processingDataVO.setDataTypeKind(getMeasurableValue().getMeasurableTypeKind());
                        processingDataVO.setDataTypeName(dataTypeName);
                        getContainerTypeDataInstanceList().removeTypeDataInstanceById(getDataItemIdInTypeDataInstanceList(),processingDataVO);
                    }
                }else{
                    Notification errorNotification = new Notification("删除"+dataTypeInfoTitle+" "+getMeasurableValue().getMeasurableTypeName()+"/ "+getMeasurableValue().getId()+"　错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        deleteDataInstanceConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(deleteDataInstanceConfirmDialog);
    }

    public TypeDataInstanceList getContainerTypeDataInstanceList() {
        return this.containerTypeDataInstanceList;
    }

    public void setContainerTypeDataInstanceList(TypeDataInstanceList containerTypeDataInstanceList) {
        this.containerTypeDataInstanceList = containerTypeDataInstanceList;
    }

    public String getDataItemIdInTypeDataInstanceList() {
        return dataItemIdInTypeDataInstanceList;
    }

    public void setDataItemIdInTypeDataInstanceList(String dataItemIdInTypeDataInstanceList) {
        this.dataItemIdInTypeDataInstanceList = dataItemIdInTypeDataInstanceList;
    }
}
