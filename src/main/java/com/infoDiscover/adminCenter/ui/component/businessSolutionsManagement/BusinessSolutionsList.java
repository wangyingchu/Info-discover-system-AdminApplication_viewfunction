package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event.BusinessSolutionComponentSelectedEvent;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event.BusinessSolutionCreatedEvent;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event.BusinessSolutionDeletedEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangychu on 5/4/17.
 */
public class BusinessSolutionsList extends VerticalLayout implements BusinessSolutionCreatedEvent.BusinessSolutionCreatedListener,
        BusinessSolutionDeletedEvent.BusinessSolutionDeletedListener{

    private UserClientInfo currentUserClientInfo;
    private List<Button> businessSolutionsList;

    public BusinessSolutionsList(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        this.businessSolutionsList=new ArrayList<Button>();
        this.renderBusinessSolutionsList();
    }

    private void renderBusinessSolutionsList(){
        this.removeAllComponents();
        this.businessSolutionsList.clear();
        List<String> solutionsList= BusinessSolutionOperationUtil.getExistBusinessSolutions();
        if(solutionsList!=null){
            for(final String currentSolution:solutionsList){
                Button solutionButton = new Button(currentSolution);
                solutionButton.setIcon(VaadinIcons.CLIPBOARD_TEXT);
                solutionButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
                solutionButton.addStyleName(ValoTheme.BUTTON_SMALL);
                solutionButton.addStyleName("ui_appHighLightElement");
                solutionButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        clearButtonSelectedStyle();
                        event.getButton().addStyleName("ui_appFriendlyElement");
                        sendBusinessSolutionSelectedEvent(currentSolution);
                    }
                });
                this.businessSolutionsList.add(solutionButton);
                addComponent(solutionButton);
            }
        }
    }

    private void clearButtonSelectedStyle(){
        for(Button currentButton:this.businessSolutionsList){
            currentButton.removeStyleName("ui_appFriendlyElement");
        }
    }

    private void sendBusinessSolutionSelectedEvent(String solutionName){
        BusinessSolutionComponentSelectedEvent businessSolutionComponentSelectedEvent=new BusinessSolutionComponentSelectedEvent(solutionName);
        this.currentUserClientInfo.getEventBlackBoard().fire(businessSolutionComponentSelectedEvent);
    }

    @Override
    public void receivedBusinessSolutionDeletedEvent(BusinessSolutionDeletedEvent event) {
        this.renderBusinessSolutionsList();
    }

    @Override
    public void receivedBusinessSolutionCreatedEvent(BusinessSolutionCreatedEvent event) {
        this.renderBusinessSolutionsList();
    }
}
