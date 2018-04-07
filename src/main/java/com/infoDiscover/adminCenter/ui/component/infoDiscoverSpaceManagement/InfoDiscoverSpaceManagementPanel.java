package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 9/28/16.
 */
public class InfoDiscoverSpaceManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;

    public InfoDiscoverSpaceManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        String windowsHeight=""+(screenHeight-230)+"px";
        this.setHeight(windowsHeight);

        boolean initMetaConfigSpaceResult=InfoDiscoverSpaceOperationUtil.initMetaConfigDiscoverSpace();
        if(initMetaConfigSpaceResult){
            HorizontalSplitPanel infoDiscoverSpaceManagementSplitPanel = new HorizontalSplitPanel();
            infoDiscoverSpaceManagementSplitPanel.setSizeFull();
            infoDiscoverSpaceManagementSplitPanel.setSplitPosition(200, Unit.PIXELS);

            this.addComponent(infoDiscoverSpaceManagementSplitPanel);
            this.setExpandRatio(infoDiscoverSpaceManagementSplitPanel, 1.0F);

            InfoDiscoverSpaceBrowser infoDiscoverSpaceBrowser=new InfoDiscoverSpaceBrowser(this.currentUserClientInfo);
            infoDiscoverSpaceManagementSplitPanel.setFirstComponent(infoDiscoverSpaceBrowser);

            InfoDiscoverSpacesDetailPanel infoDiscoverSpacesDetailPanel =new InfoDiscoverSpacesDetailPanel(this.currentUserClientInfo);
            infoDiscoverSpaceManagementSplitPanel.setSecondComponent(infoDiscoverSpacesDetailPanel);
        }else{
            Notification errorNotification = new Notification("系统初始化错误","元数据存储库初始化失败", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }
}
