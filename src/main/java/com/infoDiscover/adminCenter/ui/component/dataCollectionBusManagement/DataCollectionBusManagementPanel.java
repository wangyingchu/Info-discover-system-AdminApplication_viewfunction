package com.infoDiscover.adminCenter.ui.component.dataCollectionBusManagement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 9/28/16.
 */
public class DataCollectionBusManagementPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    public DataCollectionBusManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        String windowsHeight=""+(screenHeight-230)+"px";
        this.setHeight(windowsHeight);

        HorizontalSplitPanel activityManagementSplitPanel = new HorizontalSplitPanel();
        activityManagementSplitPanel.setSizeFull();
        activityManagementSplitPanel.setSplitPosition(250, Unit.PIXELS);
    }
}
