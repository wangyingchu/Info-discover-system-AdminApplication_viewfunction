package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangychu on 3/27/17.
 */
public class Scatter2DPlusSizeChartParametersInput extends BaseChartParametersInput{


    private UserClientInfo currentUserClientInfo;
    private VerticalLayout scatterPropertyValuesInputContainer;
    private List<TextField> scatterPropertyValueInputFieldList;

    private List<String> measurablePropertiesNameList;
    private List<String> stringPropertiesNameList;

    private ComboBox xAxisComboBox;
    private ComboBox yAxisComboBox;
    private ComboBox sizeAxisComboBox;
    private ComboBox scatterPropertyComboBox;

    public Scatter2DPlusSizeChartParametersInput(UserClientInfo userClientInfo,List<String> measurablePropertiesNameList,List<String> stringPropertiesNameList) {
        this.setMargin(true);
        this.setSpacing(true);
        this.currentUserClientInfo = userClientInfo;

        this.measurablePropertiesNameList=measurablePropertiesNameList;
        this.stringPropertiesNameList=stringPropertiesNameList;
        this.scatterPropertyValueInputFieldList =new ArrayList<>();

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
        xAxisComboBox.setInputPrompt("选择或输入属性名");
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
        yAxisComboBox.setInputPrompt("选择或输入属性名");
        yAxisComboBox.addItems(this.measurablePropertiesNameList);
        yAxisComboBox.setTextInputAllowed(true);
        yAxisComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(yAxisComboBox);

        sizeAxisComboBox = new ComboBox("数值密度属性");
        sizeAxisComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        sizeAxisComboBox.setRequired(true);
        sizeAxisComboBox.setWidth("100%");
        sizeAxisComboBox.setTextInputAllowed(true);
        sizeAxisComboBox.setNullSelectionAllowed(false);
        sizeAxisComboBox.setInputPrompt("选择或输入属性名");
        sizeAxisComboBox.addItems(this.measurablePropertiesNameList);
        sizeAxisComboBox.setTextInputAllowed(true);
        sizeAxisComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(sizeAxisComboBox);

        scatterPropertyComboBox = new ComboBox("散点属性");
        scatterPropertyComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        scatterPropertyComboBox.setRequired(true);
        scatterPropertyComboBox.setWidth("100%");
        scatterPropertyComboBox.setTextInputAllowed(true);
        scatterPropertyComboBox.setNullSelectionAllowed(false);
        scatterPropertyComboBox.setInputPrompt("选择或输入属性名");
        scatterPropertyComboBox.addItems(this.stringPropertiesNameList);
        scatterPropertyComboBox.setTextInputAllowed(true);
        scatterPropertyComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(scatterPropertyComboBox);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(spacingLayout);
        spacingLayout.addStyleName("ui_appSectionLightDiv");

        HorizontalLayout scatterPropertiesTitleLayout=new HorizontalLayout();
        scatterPropertiesTitleLayout.setHeight(30,Unit.PIXELS);
        scatterPropertiesTitleLayout.addStyleName("ui_appStandaloneElementPadding");

        Label propertiesTypeTitle= new Label(VaadinIcons.SCATTER_CHART.getHtml() +" 散点属性数据范围", ContentMode.HTML);
        propertiesTypeTitle.addStyleName(ValoTheme.LABEL_TINY);

        Button addNewValueButton=new Button("添加散点属性数据值");
        addNewValueButton.setIcon(FontAwesome.PLUS);
        addNewValueButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        addNewValueButton.addStyleName(ValoTheme.BUTTON_TINY);
        addNewValueButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addNewValueButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addScatterValueTextField(true);
            }
        });

        scatterPropertiesTitleLayout.addComponent(propertiesTypeTitle);
        scatterPropertiesTitleLayout.setComponentAlignment(propertiesTypeTitle,Alignment.MIDDLE_LEFT);
        scatterPropertiesTitleLayout.addComponent(addNewValueButton);
        scatterPropertiesTitleLayout.setComponentAlignment(addNewValueButton,Alignment.MIDDLE_LEFT);
        this.addComponent(scatterPropertiesTitleLayout);

        this.scatterPropertyValuesInputContainer =new VerticalLayout();
        this.scatterPropertyValuesInputContainer.setWidth(100,Unit.PERCENTAGE);
        this.addComponent(this.scatterPropertyValuesInputContainer);
        addScatterValueTextField(false);
    }

    private void addScatterValueTextField(boolean enableRemoveButton){
        VerticalLayout containerLayout=new VerticalLayout();
        containerLayout.setWidth(100,Unit.PERCENTAGE);

        HorizontalLayout chartPropertySelectLayout=new HorizontalLayout();
        chartPropertySelectLayout.setWidth(100,Unit.PERCENTAGE);

        FormLayout chartPropertyForm=new FormLayout();
        chartPropertyForm.setMargin(false);
        chartPropertyForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        chartPropertySelectLayout.addComponent(chartPropertyForm);

        TextField propertySelector = new TextField();
        propertySelector.addStyleName(ValoTheme.COMBOBOX_SMALL);
        propertySelector.setRequired(true);
        propertySelector.setWidth("100%");
        propertySelector.setInputPrompt("请输入散点属性数据值");

        chartPropertyForm.addComponent(propertySelector);
        this.scatterPropertyValueInputFieldList.add(propertySelector);

        Button removePropertyButton=new Button();
        removePropertyButton.setIcon(FontAwesome.TRASH);
        removePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        removePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        removePropertyButton.setEnabled(enableRemoveButton);
        removePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                scatterPropertyValueInputFieldList.remove(propertySelector);
                scatterPropertyValuesInputContainer.removeComponent(containerLayout);
            }
        });

        chartPropertySelectLayout.addComponent(removePropertyButton);
        chartPropertySelectLayout.setExpandRatio(chartPropertyForm,1);
        containerLayout.addComponent(chartPropertySelectLayout);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        spacingLayout.addStyleName("ui_appSectionLightDiv");
        containerLayout.addComponent(spacingLayout);
        this.scatterPropertyValuesInputContainer.addComponent(containerLayout);
    }

    @Override
    public String getParametersQueryString() {
        StringBuffer parametersQuerySb=new StringBuffer();
        parametersQuerySb.append("&x="+xAxisComboBox.getValue().toString());
        parametersQuerySb.append("&y="+yAxisComboBox.getValue().toString());
        parametersQuerySb.append("&size="+sizeAxisComboBox.getValue().toString());
        parametersQuerySb.append("&scatter="+scatterPropertyComboBox.getValue().toString());
        parametersQuerySb.append("&scatterValues=");
        for(int i=0;i<scatterPropertyValueInputFieldList.size();i++){
            TextField currentField=scatterPropertyValueInputFieldList.get(i);
            if(i!=scatterPropertyValueInputFieldList.size()-1){
                parametersQuerySb.append(currentField.getValue().toString()+",");
            }else{
                parametersQuerySb.append(currentField.getValue().toString());
            }
        }
        return parametersQuerySb.toString();
    }

    @Override
    public boolean validateParametersInput() {
        boolean validateResult=true;
        validateResult=validateResult&xAxisComboBox.isValid();
        validateResult=validateResult&yAxisComboBox.isValid();
        validateResult=validateResult&sizeAxisComboBox.isValid();
        validateResult=validateResult& scatterPropertyComboBox.isValid();

        for(TextField scatterValueInputField:scatterPropertyValueInputFieldList){
            validateResult=validateResult&scatterValueInputField.isValid();
        }
        return validateResult;
    }
}
