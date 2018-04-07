package com.infoDiscover.adminCenter.ui.component.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class SystemMessageConfirmDialog extends Window{

    private VerticalLayout confirmMessageContentContainer;
    private Button confirmButton;

    public SystemMessageConfirmDialog(){
        this.setResizable(false);
        this.setModal(true);
        this.center();
        this.setClosable(false);

        VerticalLayout dialogContentContainer =new VerticalLayout();
        dialogContentContainer.setMargin(true);
        dialogContentContainer.setSpacing(false);
        dialogContentContainer.setWidth(500.0f, Sizeable.Unit.PIXELS);
        this.setContent(dialogContentContainer);

        Label dialogTitle=new Label("系统消息", ContentMode.HTML);
        dialogTitle.addStyleName("h3");
        dialogTitle.addStyleName("ui_appSectionDiv");
        dialogTitle.addStyleName("ui_appFadeMargin");
        dialogContentContainer.addComponent(dialogTitle);

        confirmMessageContentContainer =new VerticalLayout();
        confirmMessageContentContainer.addStyleName("ui_appActionsBar");
        confirmMessageContentContainer.addStyleName("light");
        dialogContentContainer.addComponent(confirmMessageContentContainer);

        HorizontalLayout actionButtonsContainer=new HorizontalLayout();
        actionButtonsContainer.setSpacing(true);
        actionButtonsContainer.setMargin(true);
        actionButtonsContainer.setWidth("100%");
        dialogContentContainer.addComponent(actionButtonsContainer);

        confirmButton = new Button("确认");
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName(ValoTheme.BUTTON_TINY);
        confirmButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        actionButtonsContainer.addComponent(confirmButton);
        actionButtonsContainer.setComponentAlignment(confirmButton, Alignment.MIDDLE_RIGHT);
        actionButtonsContainer.setExpandRatio(confirmButton, 1L);

        final SystemMessageConfirmDialog selfWindow=this;
        confirmButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                selfWindow.close();
            }
        });
    }

    public void setConfirmMessage(Component component){
        confirmMessageContentContainer.addComponent(component);
    }
}
