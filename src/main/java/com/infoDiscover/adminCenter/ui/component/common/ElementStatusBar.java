package com.infoDiscover.adminCenter.ui.component.common;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class ElementStatusBar extends HorizontalLayout {

    private Label elementTypeNameProp;
    private HorizontalLayout statusElementsBarLayout;

    public ElementStatusBar(){
        setHeight("32px");
        setWidth("100%");
        setSpacing(false);
        this.setStyleName("ui_compElementStatusBar");

        HorizontalLayout statusElementContainer=new HorizontalLayout();
        this.addComponent(statusElementContainer);

        elementTypeNameProp = new Label( FontAwesome.TERMINAL.getHtml(), ContentMode.HTML);
        elementTypeNameProp.setStyleName("ui_appLightDarkMessage");
        statusElementContainer.addComponent(elementTypeNameProp);

        statusElementsBarLayout=new HorizontalLayout();
        statusElementsBarLayout.setWidth("100%");
        statusElementsBarLayout.setStyleName("ui_appLightDarkMessage");
        statusElementContainer.addComponent(statusElementsBarLayout);
    }

    public void setInfoDiscoverSpaceName(String discoverSpaceName){
        this.elementTypeNameProp.setValue(FontAwesome.TERMINAL.getHtml()  + FontAwesome.CUBE.getHtml()+ " " +discoverSpaceName);
    }

    public void clearInfoDiscoverSpaceName(){
        this.elementTypeNameProp.setValue(FontAwesome.TERMINAL.getHtml());
    }

    public void setBusinessSolutionName(String businessSolutionName){
        this.elementTypeNameProp.setValue(FontAwesome.TERMINAL.getHtml()  + VaadinIcons.CLIPBOARD_TEXT.getHtml()+ " " +businessSolutionName);
    }

    public void clearBusinessSolutionName(){
        this.elementTypeNameProp.setValue(FontAwesome.TERMINAL.getHtml());
    }

    // rule engine
    public void setRuleName(String ruleName) {
        this.elementTypeNameProp.setValue(FontAwesome.TERMINAL.getHtml()  + FontAwesome
                .CUBE.getHtml()+ " " +ruleName);
    }

    public void clearRuleName(){
        this.elementTypeNameProp.setValue(FontAwesome.TERMINAL.getHtml());
    }

    public void addStatusElement(Component barElementComponent){
        statusElementsBarLayout.addComponent(barElementComponent);
    }

    public void clearStatusElements(){
        statusElementsBarLayout.removeAllComponents();
    }
}
