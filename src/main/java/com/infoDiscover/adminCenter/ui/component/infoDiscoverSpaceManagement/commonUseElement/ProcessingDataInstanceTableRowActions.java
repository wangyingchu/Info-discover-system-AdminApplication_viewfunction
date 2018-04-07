package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceRemoveProcessingDataEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 12/16/16.
 */
public class ProcessingDataInstanceTableRowActions extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private ProcessingDataVO processingDataVO;
    private ProcessingDataList containerProcessingDataList;

    public ProcessingDataInstanceTableRowActions(UserClientInfo userClientInfo) {
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

        Button moveOutFromProcessingListButton = new Button();
        moveOutFromProcessingListButton.setIcon(VaadinIcons.OUTBOX);
        moveOutFromProcessingListButton.setDescription("从待处理数据列表中移除");
        moveOutFromProcessingListButton.addStyleName(ValoTheme.BUTTON_SMALL);
        moveOutFromProcessingListButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        moveOutFromProcessingListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                processMoveOutFromProcessingList();
            }
        });
        addComponent(moveOutFromProcessingListButton);

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


    public ProcessingDataVO getProcessingDataVO() {
        return processingDataVO;
    }

    public void setProcessingDataVO(ProcessingDataVO processingDataVO) {
        this.processingDataVO = processingDataVO;
    }

    public ProcessingDataList getContainerProcessingDataList() {
        return containerProcessingDataList;
    }

    public void setContainerProcessingDataList(ProcessingDataList containerProcessingDataList) {
        this.containerProcessingDataList = containerProcessingDataList;
    }

    private void showDataDetailInfoPanel(){
        MeasurableValueVO targetMeasurableValue=InfoDiscoverSpaceOperationUtil.getMeasurableValueById(getProcessingDataVO().getDiscoverSpaceName(),getProcessingDataVO().getId());
        if(targetMeasurableValue==null){
            Notification errorNotification = new Notification("获取数据错误",
                    "系统中不存在ID为 "+getProcessingDataVO().getId()+" 的数据", Notification.Type.ERROR_MESSAGE);
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
            String dataTypeKind= getProcessingDataVO().getDataTypeKind();
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

    private void processMoveOutFromProcessingList(){
        String confirmMessageString=" 请确认是否从待处理数据列表中移除数据 "+getProcessingDataVO().getDataTypeName()+"/ "+getProcessingDataVO().getId()+". " +
                "执行移除操作后该数据将不再出现在待处理数据列表中供用户快速访问.但在后续操作中用户可以重新检索并将该数据加入待处理数据列表.";
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog removeFromProcessingListConfirmDialog = new ConfirmDialog();
        removeFromProcessingListConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                removeFromProcessingListConfirmDialog.close();

                ProcessingDataVO processingDataVO=new ProcessingDataVO();
                processingDataVO.setId(getProcessingDataVO().getId());
                processingDataVO.setDiscoverSpaceName(getProcessingDataVO().getDiscoverSpaceName());
                processingDataVO.setDataTypeKind(getProcessingDataVO().getDataTypeKind());
                processingDataVO.setDataTypeName(getProcessingDataVO().getDataTypeName());

                DiscoverSpaceRemoveProcessingDataEvent discoverSpaceRemoveProcessingDataEvent=new DiscoverSpaceRemoveProcessingDataEvent(getProcessingDataVO().getDiscoverSpaceName());
                discoverSpaceRemoveProcessingDataEvent.setProcessingData(processingDataVO);
                currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceRemoveProcessingDataEvent);

                getContainerProcessingDataList().removeProcessingDataByDataId(getProcessingDataVO().getId());
            }
        };
        removeFromProcessingListConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(removeFromProcessingListConfirmDialog);
    }

    private void processDeleteData(){
        MeasurableValueVO targetMeasurableValue=InfoDiscoverSpaceOperationUtil.getMeasurableValueById(getProcessingDataVO().getDiscoverSpaceName(),getProcessingDataVO().getId());
        if(targetMeasurableValue==null){
            Notification errorNotification = new Notification("获取数据错误",
                    "系统中不存在ID为 "+getProcessingDataVO().getId()+" 的数据", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        String dataTypeKind= targetMeasurableValue.getMeasurableTypeKind();
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
        String confirmMessageString=" 请确认删除"+dataTypeInfoTitle+" "+targetMeasurableValue.getMeasurableTypeName()+"/ "+targetMeasurableValue.getId();
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog deleteDataInstanceConfirmDialog = new ConfirmDialog();
        deleteDataInstanceConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                deleteDataInstanceConfirmDialog.close();

                boolean removeDataInstanceResult=InfoDiscoverSpaceOperationUtil.removeMeasurableById(targetMeasurableValue.getDiscoverSpaceName(),targetMeasurableValue.getMeasurableTypeKind(),targetMeasurableValue.getId());
                if(removeDataInstanceResult){
                    ProcessingDataVO processingDataVO=new ProcessingDataVO();
                    processingDataVO.setId(targetMeasurableValue.getId());
                    processingDataVO.setDiscoverSpaceName(targetMeasurableValue.getDiscoverSpaceName());
                    processingDataVO.setDataTypeKind(targetMeasurableValue.getMeasurableTypeKind());
                    processingDataVO.setDataTypeName(targetMeasurableValue.getMeasurableTypeName());

                    DiscoverSpaceRemoveProcessingDataEvent discoverSpaceRemoveProcessingDataEvent=new DiscoverSpaceRemoveProcessingDataEvent(targetMeasurableValue.getDiscoverSpaceName());
                    discoverSpaceRemoveProcessingDataEvent.setProcessingData(processingDataVO);
                    currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceRemoveProcessingDataEvent);

                    getContainerProcessingDataList().removeProcessingDataByDataId(getProcessingDataVO().getId());

                    Notification resultNotification = new Notification("删除数据操作成功",
                            "删除"+dataTypeInfoTitle+" "+targetMeasurableValue.getMeasurableTypeName()+"/ "+targetMeasurableValue.getId()+"　成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());

                    String targetWindowUID=targetMeasurableValue.getDiscoverSpaceName()+"_GlobalDataInstanceDetailWindow_"+targetMeasurableValue.getMeasurableTypeName()+"_"+targetMeasurableValue.getId();
                    Window detailDataWindow=currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(targetMeasurableValue.getDiscoverSpaceName(),targetWindowUID);
                    if(detailDataWindow!=null){
                        detailDataWindow.close();
                    }
                    currentUserClientInfo.getRuntimeWindowsRepository().removeExistingWindow(targetMeasurableValue.getDiscoverSpaceName(),targetWindowUID);
                }else{
                    Notification errorNotification = new Notification("删除"+dataTypeInfoTitle+" "+targetMeasurableValue.getMeasurableTypeName()+"/ "+targetMeasurableValue.getId()+"　错误",
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
}
