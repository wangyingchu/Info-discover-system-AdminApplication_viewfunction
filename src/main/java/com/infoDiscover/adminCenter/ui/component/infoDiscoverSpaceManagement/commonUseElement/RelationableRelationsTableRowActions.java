package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationValueVO;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 12/12/16.
 */
public class RelationableRelationsTableRowActions extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private RelationValueVO relationValueVO;
    private RelationableRelationsList containerRelationableRelationsList;

    public RelationableRelationsTableRowActions(UserClientInfo userClientInfo,RelationValueVO relationValueVO) {
        this.currentUserClientInfo = userClientInfo;
        this.relationValueVO=relationValueVO;

        Button deleteRelationButton = new Button();
        deleteRelationButton.setIcon(FontAwesome.CHAIN_BROKEN);
        deleteRelationButton.setDescription("删除关联关系");
        deleteRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        deleteRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        deleteRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                doDeleteRelation();
            }
        });
        addComponent(deleteRelationButton);
    }

    public void doDeleteRelation(){
        String confirmMessageString=" 请确认删除关系数据 "+this.relationValueVO.getRelationTypeName()+"/ "+this.relationValueVO.getId();
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog deleteRelationConfirmDialog = new ConfirmDialog();
        deleteRelationConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                deleteRelationConfirmDialog.close();
                boolean removeRelationResult=InfoDiscoverSpaceOperationUtil.removeMeasurableById(relationValueVO.getDiscoverSpaceName(),InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,relationValueVO.getId());

                if(removeRelationResult){
                    Notification resultNotification = new Notification("删除数据操作成功",
                            "删除关系数据　"+relationValueVO.getRelationTypeName()+"/ "+relationValueVO.getId()+"　成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    if(getContainerRelationableRelationsList()!=null){
                        getContainerRelationableRelationsList().removeRelationByRelationId(relationValueVO.getId());
                    }
                }else{
                    Notification errorNotification = new Notification("删除关系数据　"+relationValueVO.getRelationTypeName()+"/ "+relationValueVO.getId()+"　错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }

            }
        };
        deleteRelationConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(deleteRelationConfirmDialog);
    }

    public RelationableRelationsList getContainerRelationableRelationsList() {
        return containerRelationableRelationsList;
    }

    public void setContainerRelationableRelationsList(RelationableRelationsList containerRelationableRelationsList) {
        this.containerRelationableRelationsList = containerRelationableRelationsList;
    }
}
