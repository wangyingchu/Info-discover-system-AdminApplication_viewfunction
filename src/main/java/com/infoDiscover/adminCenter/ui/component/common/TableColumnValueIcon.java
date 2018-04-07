package com.infoDiscover.adminCenter.ui.component.common;

import com.vaadin.server.FontIcon;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Created by wangychu on 12/12/16.
 */
public class TableColumnValueIcon extends HorizontalLayout {

    public TableColumnValueIcon(FontIcon valueIcon,String valueIconDescription){
        Label valueIconLabel=new Label(valueIcon.getHtml()+"", ContentMode.HTML);
        if(valueIconDescription!=null){
            valueIconLabel.setDescription(valueIconDescription);
        }
        this.addComponent(valueIconLabel);
    }
}
