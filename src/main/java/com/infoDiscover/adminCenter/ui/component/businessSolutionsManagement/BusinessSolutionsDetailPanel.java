package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event.BusinessSolutionComponentSelectedEvent;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event.BusinessSolutionDeletedEvent;
import com.infoDiscover.adminCenter.ui.component.common.ElementStatusBar;
import com.infoDiscover.adminCenter.ui.component.common.GeneralInfoView;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 5/4/17.
 */
public class BusinessSolutionsDetailPanel extends VerticalLayout implements BusinessSolutionComponentSelectedEvent.BusinessSolutionComponentSelectedListener, BusinessSolutionDeletedEvent.BusinessSolutionDeletedListener{

    private UserClientInfo currentUserClientInfo;
    private ElementStatusBar elementStatusBar;
    private Navigator contentNavigator;
    private static final String NAV_GENERAL="general_idm";
    private static final String NAV_BUSINESSSOLUTION_DETAIL="bs_detail";
    private BusinessSolutionDetail businessSolutionDetail;

    public BusinessSolutionsDetailPanel(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        this.elementStatusBar=new ElementStatusBar();
        this.addComponent(elementStatusBar);

        HorizontalLayout contentContainer = new HorizontalLayout();
        contentContainer.setMargin(false);
        contentContainer.setSpacing(false);
        contentContainer.setSizeFull();
        this.addComponent(contentContainer);

        ComponentContainer componentContainer=(ComponentContainer)contentContainer;
        contentNavigator = new Navigator(UI.getCurrent(),componentContainer);
        /* Config Components View */
        GeneralInfoView generalInfoView=new GeneralInfoView(currentUserClientInfo);
        contentNavigator.addView(NAV_GENERAL, generalInfoView);
        this.businessSolutionDetail=new BusinessSolutionDetail(this.currentUserClientInfo);
        contentNavigator.addView(NAV_BUSINESSSOLUTION_DETAIL, this.businessSolutionDetail);

        contentNavigator.navigateTo(NAV_GENERAL);
    }

    @Override
    public void receivedBusinessSolutionComponentSelectedEvent(BusinessSolutionComponentSelectedEvent event) {
        String businessSolutionName=event.getBusinessSolutionName();
        this.elementStatusBar.setBusinessSolutionName(businessSolutionName);
        if(businessSolutionName!=null){
            this.businessSolutionDetail.setBusinessSolutionName(businessSolutionName);
            contentNavigator.navigateTo(NAV_BUSINESSSOLUTION_DETAIL);
            this.businessSolutionDetail.renderBusinessSolutionDetail();
        }
    }

    @Override
    public void receivedBusinessSolutionDeletedEvent(BusinessSolutionDeletedEvent event) {
        this.elementStatusBar.clearBusinessSolutionName();
        contentNavigator.navigateTo(NAV_GENERAL);
    }
}
