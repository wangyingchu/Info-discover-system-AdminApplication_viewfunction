package com.infoDiscover.adminCenter.ui.component.common;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 9/29/16.
 */
public class GeneralInfoView extends VerticalLayout implements View {

    private UserClientInfo currentUserClientInfo;

    public GeneralInfoView(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(true);
        this.setSpacing(true);

        VerticalLayout viewContentContainer = new VerticalLayout();
        viewContentContainer.setMargin(false);
        viewContentContainer.setSpacing(false);
        viewContentContainer.addStyleName("ui_appSubViewContainer");
        this.addComponent(viewContentContainer);
        // View Title
        MainSectionTitle mainSectionTitle=new MainSectionTitle("欢迎 !");
        viewContentContainer.addComponent(mainSectionTitle);
        Label welcomeMessage = new Label( FontAwesome.SMILE_O.getHtml()+" 欢迎使用 Info Discover 高价值密度信息发现平台系统管理终端工具。", ContentMode.HTML);
        welcomeMessage.setStyleName("ui_appLightDarkMessage");
        viewContentContainer.addComponent(welcomeMessage);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {}
}
