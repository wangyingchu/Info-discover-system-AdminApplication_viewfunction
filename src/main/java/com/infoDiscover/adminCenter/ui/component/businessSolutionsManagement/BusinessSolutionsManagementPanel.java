package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 5/4/17.
 */
public class BusinessSolutionsManagementPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    public BusinessSolutionsManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        String windowsHeight=""+(screenHeight-230)+"px";
        this.setHeight(windowsHeight);

        HorizontalSplitPanel businessSolutionsManagementSplitPanel = new HorizontalSplitPanel();
        businessSolutionsManagementSplitPanel.setSizeFull();
        businessSolutionsManagementSplitPanel.setSplitPosition(200, Unit.PIXELS);

        this.addComponent(businessSolutionsManagementSplitPanel);
        this.setExpandRatio(businessSolutionsManagementSplitPanel, 1.0F);

        BusinessSolutionBrowser businessSolutionBrowser=new BusinessSolutionBrowser(this.currentUserClientInfo);
        businessSolutionsManagementSplitPanel.setFirstComponent(businessSolutionBrowser);

        BusinessSolutionsDetailPanel businessSolutionsDetailPanel =new BusinessSolutionsDetailPanel(this.currentUserClientInfo);
        businessSolutionsManagementSplitPanel.setSecondComponent(businessSolutionsDetailPanel);
    }
}
