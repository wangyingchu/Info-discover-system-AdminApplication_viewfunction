package com.infoDiscover.adminCenter.logic.common;

import com.infoDiscover.adminCenter.ui.component.common.SystemMessageConfirmDialog;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class SystemConfigUtil {

    public static boolean verifyUserLoginInfo(String userName,String userPWD){
        String adminAccountName=AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.BUILDIN_ADMINISTRATOR_ACCOUNTNAME);
        String adminAccountPWD=AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.BUILDIN_ADMINISTRATOR_ACCOUNTPWD);
        if(adminAccountName==null||adminAccountPWD==null){
            return true;
        }else{
            if(adminAccountName.equals(userName)&&adminAccountPWD.equals(userPWD)){
                return true;
            }else{
                return false;
            }
        }
    }

    public static void showServerPushMessage(String messageTitle,String messageContent){
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                Notification resultNotification = new Notification(messageTitle,
                        messageContent, Notification.Type.WARNING_MESSAGE);
                resultNotification.setPosition(Position.TOP_RIGHT);
                resultNotification.setIcon(FontAwesome.INFO);
                resultNotification.show(Page.getCurrent());
            }
        });
    }

    public static void showServerPushConfirmDialog(Label confirmMessage){
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                final SystemMessageConfirmDialog systemMessageConfirmDialog = new SystemMessageConfirmDialog();
                systemMessageConfirmDialog.setConfirmMessage(confirmMessage);
                systemMessageConfirmDialog.setModal(false);
                UI.getCurrent().addWindow(systemMessageConfirmDialog);
            }
        });
    }
}
