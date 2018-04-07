package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 10/19/16.
 */
public class InputPropertyNamePanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private TextField propertyName;
    private InputPropertyNamePanelInvoker inputPropertyNamePanelInvoker;

    public InputPropertyNamePanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle inputPropertyNameSectionTitle=new MainSectionTitle("添加新的属性");
        addComponent(inputPropertyNameSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        propertyName = new TextField("属性名称");
        propertyName.setRequired(true);
        form.addComponent(propertyName);
        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("确定", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                inputPropertyName();
            }
        });
        addButton.setIcon(FontAwesome.CHECK_CIRCLE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void inputPropertyName(){
        final String dimensionTypeNameStr= propertyName.getValue();
        if(dimensionTypeNameStr==null||dimensionTypeNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入属性名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        containerDialog.close();
        if(this.getInputPropertyNamePanelInvoker()!=null){
            this.getInputPropertyNamePanelInvoker().inputPropertyNameActionFinish(dimensionTypeNameStr);
        }
    }

    public InputPropertyNamePanelInvoker getInputPropertyNamePanelInvoker() {
        return inputPropertyNamePanelInvoker;
    }

    public void setInputPropertyNamePanelInvoker(InputPropertyNamePanelInvoker inputPropertyNamePanelInvoker) {
        this.inputPropertyNamePanelInvoker = inputPropertyNamePanelInvoker;
    }
}
