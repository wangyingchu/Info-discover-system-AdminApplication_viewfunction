package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement;

import com.infoDiscover.adminCenter.ui.component.common.ElementStatusBar;
import com.infoDiscover.adminCenter.ui.component.common.GeneralInfoView;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceComponentSelectedEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceDeletedEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 9/29/16.
 */
public class InfoDiscoverSpacesDetailPanel extends VerticalLayout implements DiscoverSpaceComponentSelectedEvent.DiscoverSpaceComponentSelectedListener,
        DiscoverSpaceDeletedEvent.DiscoverSpaceDeletedListener{

    private UserClientInfo currentUserClientInfo;
    private ElementStatusBar elementStatusBar;
    private Navigator contentNavigator;
    private static final String NAV_GENERAL="general_idm";
    private static final String NAV_DISCOVERSPACE_DETAIL="ds_detail";
    private  InfoDiscoverSpaceDetail infoDiscoverSpaceDetail;

    public InfoDiscoverSpacesDetailPanel(UserClientInfo currentUserClientInfo){
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
        this.infoDiscoverSpaceDetail=new InfoDiscoverSpaceDetail(currentUserClientInfo);
        contentNavigator.addView(NAV_DISCOVERSPACE_DETAIL, infoDiscoverSpaceDetail);

        contentNavigator.navigateTo(NAV_GENERAL);
    }

    @Override
    public void receivedDiscoverSpaceComponentSelectedEvent(DiscoverSpaceComponentSelectedEvent event) {
        String discoverSpaceName=event.getDiscoverSpaceName();
        this.elementStatusBar.setInfoDiscoverSpaceName(discoverSpaceName);
        if(discoverSpaceName!=null){
            this.infoDiscoverSpaceDetail.setDiscoverSpaceName(discoverSpaceName);
            contentNavigator.navigateTo(NAV_DISCOVERSPACE_DETAIL);
            this.infoDiscoverSpaceDetail.renderDiscoverSpaceDetail();
        }
    }

    @Override
    public void receivedDiscoverSpaceDeletedEvent(DiscoverSpaceDeletedEvent event) {
        //String discoverSpaceName=event.getDiscoverSpaceName();
        this.elementStatusBar.clearInfoDiscoverSpaceName();
        contentNavigator.navigateTo(NAV_GENERAL);
    }
}
