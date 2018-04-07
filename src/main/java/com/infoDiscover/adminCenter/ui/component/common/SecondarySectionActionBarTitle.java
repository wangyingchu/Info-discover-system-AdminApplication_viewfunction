package com.infoDiscover.adminCenter.ui.component.common;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 12/15/16.
 */
public class SecondarySectionActionBarTitle extends HorizontalLayout{

    private Label sectionTitleLabel;

    public SecondarySectionActionBarTitle(String sectionName,Button[] actionButtons){
        setWidth(100, Sizeable.Unit.PERCENTAGE);
        HorizontalLayout sectionTitleAndActionButtonsContainerLayout=new HorizontalLayout();
        sectionTitleAndActionButtonsContainerLayout.setWidth(100,Unit.PERCENTAGE);
        sectionTitleAndActionButtonsContainerLayout.addStyleName("ui_appSectionFadeDiv");
        addComponent(sectionTitleAndActionButtonsContainerLayout);

        sectionTitleLabel=new Label(sectionName);
        sectionTitleLabel.addStyleName(ValoTheme.LABEL_H3);
        sectionTitleLabel.addStyleName(ValoTheme.LABEL_COLORED);
        sectionTitleAndActionButtonsContainerLayout.addComponent(sectionTitleLabel);

        HorizontalLayout actionButtonsContainerLayout=new HorizontalLayout();
        sectionTitleAndActionButtonsContainerLayout.addComponent(actionButtonsContainerLayout);
        if(actionButtons!=null&&actionButtons.length>0){
            for(Button currentButton:actionButtons){
                actionButtonsContainerLayout.addComponent(currentButton);
            }
        }

        HorizontalLayout spacingDivLayout=new HorizontalLayout();
        spacingDivLayout.setWidth(5,Unit.PIXELS);
        sectionTitleAndActionButtonsContainerLayout.addComponent(spacingDivLayout);

        sectionTitleAndActionButtonsContainerLayout.setComponentAlignment(actionButtonsContainerLayout,Alignment.MIDDLE_RIGHT);
        sectionTitleAndActionButtonsContainerLayout.setExpandRatio(sectionTitleLabel,1);
    }

    public void updateSectionTitle(String sectionName){
        this.sectionTitleLabel.setValue(sectionName);
    }
}
