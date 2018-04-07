package com.infoDiscover.adminCenter.ui.component.common;


import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

public class ConfirmDialog extends Window {

    private VerticalLayout confirmMessageContentContainer;
    private Button confirmButton;

    public ConfirmDialog(){
        this.setResizable(false);
        this.setModal(true);
        this.center();
        this.setClosable(false);

        VerticalLayout dialogContentContainer =new VerticalLayout();
        dialogContentContainer.setMargin(true);
        dialogContentContainer.setSpacing(false);
        dialogContentContainer.setWidth(500.0f, Unit.PIXELS);
        this.setContent(dialogContentContainer);

        SecondarySectionTitle workingTasksSectionTitle=new SecondarySectionTitle("确认");
        dialogContentContainer.addComponent(workingTasksSectionTitle);

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
        confirmButton.addStyleName("small");
        confirmButton.addStyleName("primary");
        actionButtonsContainer.addComponent(confirmButton);
        actionButtonsContainer.setComponentAlignment(confirmButton, Alignment.MIDDLE_RIGHT);
        actionButtonsContainer.setExpandRatio(confirmButton, 1L);

        final ConfirmDialog selfWindow=this;
        Button cancelButton = new Button("取消");
        cancelButton.setIcon(FontAwesome.TIMES);
        cancelButton.addStyleName("small");
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
