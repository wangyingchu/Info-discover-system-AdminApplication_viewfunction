package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 3/23/17.
 */
public class Values3DPlusColorChartParametersInput extends BaseChartParametersInput{

    private UserClientInfo currentUserClientInfo;
    private List<String> measurablePropertiesNameList;
    private ComboBox xAxisComboBox;
    private ComboBox yAxisComboBox;
    private ComboBox zAxisComboBox;
    private ComboBox cAxisComboBox;
    private ComboBox chartTypeComboBox;

    public Values3DPlusColorChartParametersInput(UserClientInfo userClientInfo, List<String> measurablePropertiesNameList) {
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

        cAxisComboBox = new ComboBox("数值密度属性");
        cAxisComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cAxisComboBox.setRequired(false);
        cAxisComboBox.setWidth("100%");
        cAxisComboBox.setTextInputAllowed(true);
        cAxisComboBox.setNullSelectionAllowed(true);
        cAxisComboBox.setInputPrompt("选择或输入属性名称");
        cAxisComboBox.addItems(this.measurablePropertiesNameList);
        cAxisComboBox.setTextInputAllowed(true);
        cAxisComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(cAxisComboBox);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(spacingLayout);
        spacingLayout.addStyleName("ui_appSectionLightDiv");

        HorizontalLayout chartRenderTypeTitleLayout=new HorizontalLayout();
        chartRenderTypeTitleLayout.setHeight(30,Unit.PIXELS);
        chartRenderTypeTitleLayout.addStyleName("ui_appStandaloneElementPadding");
        Label typeValueTitle= new Label(VaadinIcons.CHART_3D.getHtml() +" 图形渲染类型", ContentMode.HTML);
        typeValueTitle.addStyleName(ValoTheme.LABEL_TINY);
        chartRenderTypeTitleLayout.addComponent(typeValueTitle);
        chartRenderTypeTitleLayout.setComponentAlignment(typeValueTitle,Alignment.MIDDLE_LEFT);
        this.addComponent(chartRenderTypeTitleLayout);

        FormLayout renderTypeValueForm = new FormLayout();
        renderTypeValueForm.setMargin(false);
        renderTypeValueForm.setWidth(100,Unit.PERCENTAGE);
        renderTypeValueForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        this.addComponent(renderTypeValueForm);

        chartTypeComboBox = new ComboBox("渲染类型");
        chartTypeComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        chartTypeComboBox.setRequired(false);
        chartTypeComboBox.setWidth("100%");
        chartTypeComboBox.setTextInputAllowed(false);
        chartTypeComboBox.setNullSelectionAllowed(false);
        chartTypeComboBox.addItem("bar-color");
        chartTypeComboBox.addItem("bar-size");
        chartTypeComboBox.addItem("dot-color");
        chartTypeComboBox.addItem("dot-size");
        chartTypeComboBox.addItem("dot");
        chartTypeComboBox.addItem("dot-line");
        chartTypeComboBox.addItem("bar");
        chartTypeComboBox.addItem("line");
        chartTypeComboBox.select("bar-color");
        renderTypeValueForm.addComponent(chartTypeComboBox);

        VerticalLayout spacingLayout2=new VerticalLayout();
        spacingLayout2.setWidth(100,Unit.PERCENTAGE);
        addComponent(spacingLayout2);
        spacingLayout2.addStyleName("ui_appSectionLightDiv");
    }

    @Override
    public String getParametersQueryString() {
        StringBuffer parametersQuerySb=new StringBuffer();
        parametersQuerySb.append("&x="+xAxisComboBox.getValue().toString());
        parametersQuerySb.append("&y="+yAxisComboBox.getValue().toString());
        parametersQuerySb.append("&z="+zAxisComboBox.getValue().toString());
        if(cAxisComboBox.getValue()!=null) {
            parametersQuerySb.append("&c=" + cAxisComboBox.getValue().toString());
        }
        parametersQuerySb.append("&type="+chartTypeComboBox.getValue().toString());
        return parametersQuerySb.toString();
    }

    @Override
    public boolean validateParametersInput() {
        boolean validateResult=true;
        validateResult=validateResult&xAxisComboBox.isValid();
        validateResult=validateResult&yAxisComboBox.isValid();
        validateResult=validateResult& zAxisComboBox.isValid();
        validateResult=validateResult& cAxisComboBox.isValid();
        return validateResult;
    }
}
