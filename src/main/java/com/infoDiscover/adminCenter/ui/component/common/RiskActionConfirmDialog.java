package com.infoDiscover.adminCenter.ui.component.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 10/3/16.
 */
public class RiskActionConfirmDialog extends Window {

    private VerticalLayout confirmMessageContentContainer;
    private Button confirmButton;

    public RiskActionConfirmDialog(){
        this.setResizable(false);
        this.setModal(true);
        this.center();
        this.setClosable(false);

        VerticalLayout dialogContentContainer =new VerticalLayout();
        dialogContentContainer.setMargin(true);
        dialogContentContainer.setSpacing(false);
        dialogContentContainer.setWidth(500.0f, Unit.PIXELS);
        this.setContent(dialogContentContainer);

        Label dialogTitle=new Label("<span style='color:#CE0000;'>确认执行危险操作</span>", ContentMode.HTML);
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

        confirmButton = new Button("确认执行操作");
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName(ValoTheme.BUTTON_TINY);
        confirmButton.addStyleName(ValoTheme.BUTTON_DANGER);
        actionButtonsContainer.addComponent(confirmButton);
        actionButtonsContainer.setComponentAlignment(confirmButton, Alignment.MIDDLE_RIGHT);
        actionButtonsContainer.setExpandRatio(confirmButton, 1L);

        final RiskActionConfirmDialog selfWindow=this;
        Button cancelButton = new Button("取消");
        cancelButton.setIcon(FontAwesome.TIMES);
        cancelButton.addStyleName(ValoTheme.BUTTON_TINY);
        actionButtonsContainer.addComponent(cancelButton);
        actionButtonsContainer.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                selfWindow.close();
            }
        });
    }

    public void setConfirmMessage(Component component){
        confirmMessageContentContainer.addComponent(component);
    }

    public void setConfirmButtonClickListener(Button.ClickListener confirmButtonClickListener){
        confirmButton.addClickListener(confirmButtonClickListener);
    }
}
