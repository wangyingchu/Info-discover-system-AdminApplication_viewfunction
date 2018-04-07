package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 3/21/17.
 */
public abstract class BaseChartParametersInput extends VerticalLayout {
    public abstract String getParametersQueryString();
    public abstract boolean validateParametersInput();
}
