package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

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
 * Created by wangychu on 5/15/17.
 */
public class AliasNameEditPanel extends VerticalLayout {

    public static final String AliasNameType_TYPE="TYPE";
    public static final String AliasNameType_PROPERTY="PROPERTY";

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private TextField aliasName;
    private AliasNameEditPanelInvoker aliasNameEditPanelInvoker;
    private String aliasNameType;

    public AliasNameEditPanel(UserClientInfo userClientInfo,String currentAliasName) {
        this.currentUserClientInfo = userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle addNewPropertySectionTitle = new MainSectionTitle("修改别名名称");
        addComponent(addNewPropertySectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        this.aliasName=new TextField("别名名称");
        if(currentAliasName!=null&&!currentAliasName.equals("")){
            this.aliasName.setValue(currentAliasName);
        }
        this.aliasName.setRequired(true);
        form.addComponent(this.aliasName);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button editButton=new Button("确认修改", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do edit alias name logic */
                doEditAliasName();
            }
        });
        editButton.setIcon(FontAwesome.CHECK);
        editButton.addStyleName("primary");
        footer.addComponent(editButton);
    }

    private void doEditAliasName(){
        final String newAliasNameStr=this.aliasName.getValue();
        if(newAliasNameStr==null||newAliasNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入别名名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }else{
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+"请确认修改别名名称", ContentMode.HTML);

            final ConfirmDialog editAliasNameConfirmDialog = new ConfirmDialog();
            editAliasNameConfirmDialog.setConfirmMessage(confirmMessage);

            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    if(getAliasNameEditPanelInvoker()!=null){
                        if(AliasNameType_TYPE.equals(getAliasNameType())){
                            getAliasNameEditPanelInvoker().editTypeAliasNameAction(newAliasNameStr);
                        }
                        if(AliasNameType_PROPERTY.equals(getAliasNameType())){
                            getAliasNameEditPanelInvoker().editTypePropertyAliasNameAction(newAliasNameStr);
                        }
                    }
                    editAliasNameConfirmDialog.close();
                    if(getContainerDialog()!=null){
                        getContainerDialog().close();
                    }
                }
            };
            editAliasNameConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(editAliasNameConfirmDialog);
        }
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public AliasNameEditPanelInvoker getAliasNameEditPanelInvoker() {
        return aliasNameEditPanelInvoker;
    }

    public void setAliasNameEditPanelInvoker(AliasNameEditPanelInvoker aliasNameEditPanelInvoker) {
        this.aliasNameEditPanelInvoker = aliasNameEditPanelInvoker;
    }

    public String getAliasNameType() {
        return aliasNameType;
    }

    public void setAliasNameType(String aliasNameType) {
        this.aliasNameType = aliasNameType;
    }
}
