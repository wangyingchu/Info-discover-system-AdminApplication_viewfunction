package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 3/23/17.
 */
public class Values3DChartParametersInput extends BaseChartParametersInput{

    private UserClientInfo currentUserClientInfo;
    private List<String> measurablePropertiesNameList;
    private ComboBox xAxisComboBox;
    private ComboBox yAxisComboBox;
    private ComboBox zAxisComboBox;

    public Values3DChartParametersInput(UserClientInfo userClientInfo, List<String> measurablePropertiesNameList) {
        this.setMargin(true);
        this.setSpacing(true);
        this.currentUserClientInfo = userClientInfo;
        this.measurablePropertiesNameList = measurablePropertiesNameList;
        Label analyzeParametersInputTitle= new Label(FontAwesome.LIST_UL.getHtml() +" 可视化分析参数属性", ContentMode.HTML);
        analyzeParametersInputTitle.addStyleName(ValoTheme.LABEL_SMALL);
        analyzeParametersInputTitle.addStyleName("ui_appSectionLightDiv");
        this.addComponent(analyzeParametersInputTitle);

        FormLayout coordinateAxisPropertiesForm = new FormLayout();
        coordinateAxisPropertiesForm.setMargin(false);
        coordinateAxisPropertiesForm.setWidth(100,Unit.PERCENTAGE);
        coordinateAxisPropertiesForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        this.addComponent(coordinateAxisPropertiesForm);
        xAxisComboBox = new ComboBox("X 轴属性");
        xAxisComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        xAxisComboBox.setRequired(true);
        xAxisComboBox.setWidth("100%");
        xAxisComboBox.setTextInputAllowed(true);
        xAxisComboBox.setNullSelectionAllowed(false);
        xAxisComboBox.setInputPrompt("选择或输入属性名称");
        xAxisComboBox.addItems(this.measurablePropertiesNameList);
        xAxisComboBox.setTextInputAllowed(true);
        xAxisComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(xAxisComboBox);

        yAxisComboBox = new ComboBox("Y 轴属性");
        yAxisComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        yAxisComboBox.setRequired(true);
        yAxisComboBox.setWidth("100%");
        yAxisComboBox.setTextInputAllowed(true);
        yAxisComboBox.setNullSelectionAllowed(false);
        yAxisComboBox.setInputPrompt("选择或输入属性名称");
        yAxisComboBox.addItems(this.measurablePropertiesNameList);
        yAxisComboBox.setTextInputAllowed(true);
        yAxisComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(yAxisComboBox);

        zAxisComboBox = new ComboBox("Z 轴属性");
        zAxisComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        zAxisComboBox.setRequired(true);
        zAxisComboBox.setWidth("100%");
        zAxisComboBox.setTextInputAllowed(true);
        zAxisComboBox.setNullSelectionAllowed(false);
        zAxisComboBox.setInputPrompt("选择或输入属性名称");
        zAxisComboBox.addItems(this.measurablePropertiesNameList);
        zAxisComboBox.setTextInputAllowed(true);
        zAxisComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(zAxisComboBox);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(spacingLayout);
        spacingLayout.addStyleName("ui_appSectionLightDiv");
    }

    @Override
    public String getParametersQueryString() {
        StringBuffer parametersQuerySb=new StringBuffer();
        parametersQuerySb.append("&x="+xAxisComboBox.getValue().toString());
        parametersQuerySb.append("&y="+yAxisComboBox.getValue().toString());
        parametersQuerySb.append("&z="+zAxisComboBox.getValue().toString());
        return parametersQuerySb.toString();
    }

    @Override
    public boolean validateParametersInput() {
        boolean validateResult=true;
        validateResult=validateResult&xAxisComboBox.isValid();
        validateResult=validateResult&yAxisComboBox.isValid();
        validateResult=validateResult& zAxisComboBox.isValid();
        return validateResult;
    }
}
