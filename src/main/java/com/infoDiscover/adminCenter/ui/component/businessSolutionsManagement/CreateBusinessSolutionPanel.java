package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event.BusinessSolutionCreatedEvent;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 5/4/17.
 */
public class CreateBusinessSolutionPanel extends VerticalLayout{

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private TextField businessSolutionName;

    public CreateBusinessSolutionPanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        // Add New Business Solution Section
        MainSectionTitle addNewBusinessSolutionSectionTitle=new MainSectionTitle("创建新的业务解决方案模板");
        addComponent(addNewBusinessSolutionSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        businessSolutionName = new TextField("业务解决方案名称");
        businessSolutionName.setRequired(true);
        form.addComponent(businessSolutionName);
        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("创建业务解决方案模板", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new business solution logic */
                addNewBusinessSolution();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    private void addNewBusinessSolution(){
        final String businessSolutionNameStr=businessSolutionName.getValue();
        if(businessSolutionNameStr==null||businessSolutionNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入业务解决方案名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        boolean isExistBusinessSolutionName= BusinessSolutionOperationUtil.checkBusinessSolutionExistence(businessSolutionNameStr);
        if(isExistBusinessSolutionName){
            Notification errorNotification = new Notification("数据校验错误",
                    "业务解决方案 "+businessSolutionNameStr+" 已经存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        //do add new logic
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                " 请确认创建业务解决方案模板  <b>"+businessSolutionNameStr +"</b>.", ContentMode.HTML);
        final ConfirmDialog addBusinessSolutionConfirmDialog = new ConfirmDialog();
        addBusinessSolutionConfirmDialog.setConfirmMessage(confirmMessage);

        final CreateBusinessSolutionPanel self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addBusinessSolutionConfirmDialog.close();
                boolean createBusinessSolutionResult=BusinessSolutionOperationUtil.createBusinessSolution(businessSolutionNameStr);
                if(createBusinessSolutionResult){
                    self.containerDialog.close();

                    BusinessSolutionCreatedEvent businessSolutionCreatedEvent=new BusinessSolutionCreatedEvent(businessSolutionNameStr);
                    self.currentUserClientInfo.getEventBlackBoard().fire(businessSolutionCreatedEvent);

                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建业务解决方案模板成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建业务解决方案模板错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addBusinessSolutionConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addBusinessSolutionConfirmDialog);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}
