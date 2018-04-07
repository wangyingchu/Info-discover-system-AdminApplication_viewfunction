package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceCreatedEvent;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 9/30/16.
 */
public class CreateInfoDiscoverSpacePanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private TextField discoverSpaceName;

    public CreateInfoDiscoverSpacePanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        // Add New Discover Space Section
        MainSectionTitle addNewDiscoverSpaceSectionTitle=new MainSectionTitle("创建新的信息发现空间");
        addComponent(addNewDiscoverSpaceSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        discoverSpaceName = new TextField("信息发现空间名称");
        discoverSpaceName.setRequired(true);
        form.addComponent(discoverSpaceName);
        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("创建信息发现空间", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new discover space logic */
                addNewDiscoverSpace();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    private void addNewDiscoverSpace(){
        final String discoverSpaceNameStr=discoverSpaceName.getValue();
        if(discoverSpaceNameStr==null||discoverSpaceNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入信息发现空间名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(discoverSpaceNameStr.equals(AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE))){
            Notification errorNotification = new Notification("数据校验错误",
                    "信息发现空间名称 "+discoverSpaceNameStr+" 是系统保留关键字", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isExistDiscoverSpaceName= InfoDiscoverSpaceOperationUtil.checkDiscoverSpaceExistence(discoverSpaceNameStr);
        if(isExistDiscoverSpaceName){
            Notification errorNotification = new Notification("数据校验错误",
                    "信息发现空间 "+discoverSpaceNameStr+" 已经存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        //do add new logic
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                " 请确认创建信息发现空间  <b>"+discoverSpaceNameStr +"</b>.", ContentMode.HTML);
        final ConfirmDialog addDiscoverSpaceConfirmDialog = new ConfirmDialog();
        addDiscoverSpaceConfirmDialog.setConfirmMessage(confirmMessage);

        final CreateInfoDiscoverSpacePanel self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addDiscoverSpaceConfirmDialog.close();
                boolean createActivitySpaceResult=InfoDiscoverSpaceOperationUtil.createDiscoverSpace(discoverSpaceNameStr);
                if(createActivitySpaceResult){
                    self.containerDialog.close();

                    DiscoverSpaceCreatedEvent discoverSpaceCreatedEvent=new DiscoverSpaceCreatedEvent(discoverSpaceNameStr);
                    self.currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceCreatedEvent);

                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建信息发现空间成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建信息发现空间错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addDiscoverSpaceConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addDiscoverSpaceConfirmDialog);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}
