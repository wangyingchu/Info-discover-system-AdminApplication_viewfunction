package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement;

import com.info.discover.ruleengine.manager.database.RuleEngineDatabaseConstants;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceComponentSelectedEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceCreatedEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceDeletedEvent;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangychu on 9/30/16.
 */
public class InfoDiscoverSpacesList extends VerticalLayout implements DiscoverSpaceCreatedEvent.DiscoverSpaceCreatedListener,
        DiscoverSpaceDeletedEvent.DiscoverSpaceDeletedListener{

    private UserClientInfo currentUserClientInfo;
    private List<Button> discoverSpaceButtonsList;

    public InfoDiscoverSpacesList(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        this.discoverSpaceButtonsList=new ArrayList<Button>();
        renderDiscoverSpacesList();
    }

    private void renderDiscoverSpacesList(){
        this.removeAllComponents();
        this.discoverSpaceButtonsList.clear();
        List<String> spacesList= InfoDiscoverSpaceOperationUtil.getExistDiscoverSpace
                (new String[]{RuleEngineDatabaseConstants.RuleEngineSpace, AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE)});
        if(spacesList!=null){
            for(final String currentSpace:spacesList){
                Button spaceButton = new Button(currentSpace);
                spaceButton.setIcon(FontAwesome.CUBE);
                spaceButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
                spaceButton.addStyleName(ValoTheme.BUTTON_SMALL);
                spaceButton.addStyleName("ui_appHighLightElement");
                spaceButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        clearButtonSelectedStyle();
                        event.getButton().addStyleName("ui_appFriendlyElement");
                        sendDiscoverSpaceSelectedEvent(currentSpace);
                    }
                });
                this.discoverSpaceButtonsList.add(spaceButton);
                addComponent(spaceButton);
            }
        }
    }

    private void clearButtonSelectedStyle(){
        for(Button currentButton:this.discoverSpaceButtonsList){
            currentButton.removeStyleName("ui_appFriendlyElement");
        }
    }

    private void sendDiscoverSpaceSelectedEvent(String spaceName){
        DiscoverSpaceComponentSelectedEvent discoverSpaceComponentSelectedEvent=new DiscoverSpaceComponentSelectedEvent(spaceName);
        this.currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceComponentSelectedEvent);
    }

    @Override
    public void receivedDiscoverSpaceCreatedEvent(DiscoverSpaceCreatedEvent event) {
        this.renderDiscoverSpacesList();
    }

    @Override
    public void receivedDiscoverSpaceDeletedEvent(DiscoverSpaceDeletedEvent event) {
        this.renderDiscoverSpacesList();
    }
}
