package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.ExportBusinessSolutionPanel;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event.BusinessSolutionDeletedEvent;
import com.infoDiscover.adminCenter.ui.component.common.RiskActionConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionActionBarTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 5/5/17.
 */
public class BusinessSolutionOperationsBar extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private SecondarySectionActionBarTitle secondarySectionActionBarTitle;
    private String businessSolutionName;

    public BusinessSolutionOperationsBar(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setWidth(100, Sizeable.Unit.PERCENTAGE);

        Button exportBusinessSolutionButton = new Button("导出业务解决方案模板");
        exportBusinessSolutionButton.setIcon(VaadinIcons.UPLOAD);
        exportBusinessSolutionButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        exportBusinessSolutionButton.addStyleName(ValoTheme.BUTTON_SMALL);
        exportBusinessSolutionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                doExportBusinessSolutionData();
            }
        });

        Button deleteBusinessSolutionButton = new Button("删除业务解决方案模板");
        deleteBusinessSolutionButton.setIcon(FontAwesome.TRASH);
        deleteBusinessSolutionButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        deleteBusinessSolutionButton.addStyleName(ValoTheme.BUTTON_SMALL);
        deleteBusinessSolutionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                doDeleteBusinessSolution();
            }
        });

        secondarySectionActionBarTitle=new SecondarySectionActionBarTitle("-------",new Button[]{exportBusinessSolutionButton,deleteBusinessSolutionButton});
        addComponent(secondarySectionActionBarTitle);
    }

    public void setupOperationsBarInfo(String businessSolutionName){
        this.businessSolutionName=businessSolutionName;
        this.secondarySectionActionBarTitle.updateSectionTitle(businessSolutionName);
    }

    private void doDeleteBusinessSolution(){
        //do delete solution logic
        Label confirmMessage=new Label("<span style='color:#CE0000;'><span style='font-weight:bold;'>"+FontAwesome.EXCLAMATION_TRIANGLE.getHtml()+
                " 请确认是否删除业务解决方案模板  <b style='color:#333333;'>"+this.businessSolutionName +"</b>,执行删除操作将会永久性的删除该模板中的所有数据。</span><br/>注意：此项操作不可撤销执行结果。</span>", ContentMode.HTML);
        final RiskActionConfirmDialog deleteSolutionConfirmDialog = new RiskActionConfirmDialog();
        deleteSolutionConfirmDialog.setConfirmMessage(confirmMessage);

        final BusinessSolutionOperationsBar self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                deleteSolutionConfirmDialog.close();
                boolean deleteSolutionResult= BusinessSolutionOperationUtil.deleteBusinessSolutionDefinition(businessSolutionName);
                if(deleteSolutionResult){
                    BusinessSolutionDeletedEvent businessSolutionDeletedEvent =new BusinessSolutionDeletedEvent(businessSolutionName);

                    self.currentUserClientInfo.getEventBlackBoard().fire(businessSolutionDeletedEvent);
                    Notification resultNotification = new Notification("删除数据操作成功",
                            "删除业务解决方案模板成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("删除业务解决方案模板错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        deleteSolutionConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(deleteSolutionConfirmDialog);
    }

    private void doExportBusinessSolutionData(){
        ExportBusinessSolutionPanel exportBusinessSolutionPanel=new ExportBusinessSolutionPanel(this.currentUserClientInfo,this.businessSolutionName);
        final Window window = new Window();
        window.setWidth(450.0f, Unit.PIXELS);
        window.setHeight(260.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(exportBusinessSolutionPanel);
        exportBusinessSolutionPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }
}
