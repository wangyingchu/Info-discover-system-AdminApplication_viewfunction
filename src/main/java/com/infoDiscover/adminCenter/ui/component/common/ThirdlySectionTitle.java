package com.infoDiscover.adminCenter.ui.component.common;

import com.vaadin.ui.Label;

/**
 * Created by wangychu on 10/7/16.
 */
public class ThirdlySectionTitle extends Label {

    public ThirdlySectionTitle(String sectionName){
        super(sectionName);
        addStyleName("h4");
        addStyleName("colored");
        addStyleName("ui_appSectionDiv");
        addStyleName("ui_appFadeMargin");
    }
}
