package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangychu on 3/24/17.
 */
public class TimeValuesChartParametersInput extends BaseChartParametersInput{

    private UserClientInfo currentUserClientInfo;
    private List<String> measurablePropertiesNameList;
    private List<String> datePropertiesNameList;
    private ComboBox timeAxisComboBox;
    private DateField startDateField;
    private DateField endDateField;
    private List<ComboBox> valuePropertiesSelectorList;
    private VerticalLayout valuePropertiesSelectorContainer;

    private List<ComboBox> barPropertiesSelectorList;
    private VerticalLayout barPropertiesSelectorContainer;

    public TimeValuesChartParametersInput(UserClientInfo userClientInfo,List<String> measurablePropertiesNameList,List<String> datePropertiesNameList) {
        this.setMargin(true);
        this.setSpacing(true);
        this.currentUserClientInfo = userClientInfo;
        this.measurablePropertiesNameList = measurablePropertiesNameList;
        this.datePropertiesNameList=datePropertiesNameList;

        this.valuePropertiesSelectorList =new ArrayList<>();
        this.barPropertiesSelectorList =new ArrayList<>();

        Label analyzeParametersInputTitle= new Label(FontAwesome.LIST_UL.getHtml() +" 可视化分析参数属性", ContentMode.HTML);
        analyzeParametersInputTitle.addStyleName(ValoTheme.LABEL_SMALL);
        analyzeParametersInputTitle.addStyleName("ui_appSectionLightDiv");
        this.addComponent(analyzeParametersInputTitle);

        FormLayout coordinateAxisPropertiesForm = new FormLayout();
        coordinateAxisPropertiesForm.setMargin(false);
        coordinateAxisPropertiesForm.setWidth(100,Unit.PERCENTAGE);
        coordinateAxisPropertiesForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        this.addComponent(coordinateAxisPropertiesForm);
        timeAxisComboBox = new ComboBox("时间轴属性");
        timeAxisComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        timeAxisComboBox.setRequired(true);
        timeAxisComboBox.setWidth("100%");
        timeAxisComboBox.setTextInputAllowed(true);
        timeAxisComboBox.setNullSelectionAllowed(false);
        timeAxisComboBox.setInputPrompt("选择或输入属性名称");
        timeAxisComboBox.addItems(this.datePropertiesNameList);
        timeAxisComboBox.setTextInputAllowed(true);
        timeAxisComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(timeAxisComboBox);

        startDateField=new DateField("开始时间");
        startDateField.setDateFormat("yyyy-MM-dd hh:mm");
        startDateField.setResolution(Resolution.MINUTE);
        coordinateAxisPropertiesForm.addComponent(startDateField);

        endDateField=new DateField("结束时间");
        endDateField.setDateFormat("yyyy-MM-dd hh:mm");
        endDateField.setResolution(Resolution.MINUTE);
        coordinateAxisPropertiesForm.addComponent(endDateField);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(spacingLayout);
        spacingLayout.addStyleName("ui_appSectionLightDiv");

        HorizontalLayout valuePropertiesTitleLayout=new HorizontalLayout();
        valuePropertiesTitleLayout.setHeight(30,Unit.PIXELS);
        valuePropertiesTitleLayout.addStyleName("ui_appStandaloneElementPadding");

        Label valuePropertiesTypeTitle= new Label(VaadinIcons.SPARK_LINE.getHtml() +" 同一时刻显示的数据属性", ContentMode.HTML);
        valuePropertiesTypeTitle.addStyleName(ValoTheme.LABEL_TINY);

        Button addNewValuePropertyButton=new Button("添加数据属性");
        addNewValuePropertyButton.setIcon(FontAwesome.PLUS);
        addNewValuePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        addNewValuePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        addNewValuePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addNewValuePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addChartValuePropertySelector(true);
            }
        });

        valuePropertiesTitleLayout.addComponent(valuePropertiesTypeTitle);
        valuePropertiesTitleLayout.setComponentAlignment(valuePropertiesTypeTitle,Alignment.MIDDLE_LEFT);
        valuePropertiesTitleLayout.addComponent(addNewValuePropertyButton);
        valuePropertiesTitleLayout.setComponentAlignment(addNewValuePropertyButton,Alignment.MIDDLE_LEFT);

        this.addComponent(valuePropertiesTitleLayout);

        this.valuePropertiesSelectorContainer =new VerticalLayout();
        this.valuePropertiesSelectorContainer.setWidth(100,Unit.PERCENTAGE);
        this.addComponent(this.valuePropertiesSelectorContainer);

        addChartValuePropertySelector(false);

        HorizontalLayout barPropertiesTitleLayout=new HorizontalLayout();
        barPropertiesTitleLayout.setHeight(30,Unit.PIXELS);
        barPropertiesTitleLayout.addStyleName("ui_appStandaloneElementPadding");

        Label barPropertiesTypeTitle= new Label(VaadinIcons.MARGIN_BOTTOM.getHtml() +" 柱状图数据属性", ContentMode.HTML);
        barPropertiesTypeTitle.addStyleName(ValoTheme.LABEL_TINY);

        Button addNewBarPropertyButton=new Button("添加柱状图数据属性");
        addNewBarPropertyButton.setIcon(FontAwesome.PLUS);
        addNewBarPropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        addNewBarPropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        addNewBarPropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addNewBarPropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addChartBarPropertySelector();
            }
        });

        barPropertiesTitleLayout.addComponent(barPropertiesTypeTitle);
        barPropertiesTitleLayout.setComponentAlignment(barPropertiesTypeTitle,Alignment.MIDDLE_LEFT);
        barPropertiesTitleLayout.addComponent(addNewBarPropertyButton);
        barPropertiesTitleLayout.setComponentAlignment(addNewBarPropertyButton,Alignment.MIDDLE_LEFT);

        this.addComponent(barPropertiesTitleLayout);

        this.barPropertiesSelectorContainer =new VerticalLayout();
        this.barPropertiesSelectorContainer.setWidth(100,Unit.PERCENTAGE);
        this.addComponent(this.barPropertiesSelectorContainer);
    }

    private void addChartValuePropertySelector(boolean enableRemoveButton){
        VerticalLayout containerLayout=new VerticalLayout();
        containerLayout.setWidth(100,Unit.PERCENTAGE);

        HorizontalLayout chartPropertySelectLayout=new HorizontalLayout();
        chartPropertySelectLayout.setWidth(100,Unit.PERCENTAGE);

        FormLayout chartPropertyForm=new FormLayout();
        chartPropertyForm.setMargin(false);
        chartPropertyForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        chartPropertySelectLayout.addComponent(chartPropertyForm);

        ComboBox propertySelector = new ComboBox();
        propertySelector.addStyleName(ValoTheme.COMBOBOX_SMALL);
        propertySelector.setRequired(true);
        propertySelector.setWidth("100%");
        propertySelector.setTextInputAllowed(true);
        propertySelector.setNullSelectionAllowed(false);
        propertySelector.setInputPrompt("选择或输入属性名称");
        propertySelector.addItems(this.measurablePropertiesNameList);
        propertySelector.setTextInputAllowed(true);
        propertySelector.setNewItemsAllowed(true);
        chartPropertyForm.addComponent(propertySelector);
        this.valuePropertiesSelectorList.add(propertySelector);

        Button removePropertyButton=new Button();
        removePropertyButton.setIcon(FontAwesome.TRASH);
        removePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        removePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        removePropertyButton.setEnabled(enableRemoveButton);
        removePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                valuePropertiesSelectorList.remove(propertySelector);
                valuePropertiesSelectorContainer.removeComponent(containerLayout);
            }
        });

        chartPropertySelectLayout.addComponent(removePropertyButton);
        chartPropertySelectLayout.setExpandRatio(chartPropertyForm,1);
        containerLayout.addComponent(chartPropertySelectLayout);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        spacingLayout.addStyleName("ui_appSectionLightDiv");
        containerLayout.addComponent(spacingLayout);
        this.valuePropertiesSelectorContainer.addComponent(containerLayout);
    }

    private void addChartBarPropertySelector(){
        VerticalLayout containerLayout=new VerticalLayout();
        containerLayout.setWidth(100,Unit.PERCENTAGE);

        HorizontalLayout chartPropertySelectLayout=new HorizontalLayout();
        chartPropertySelectLayout.setWidth(100,Unit.PERCENTAGE);

        FormLayout chartPropertyForm=new FormLayout();
        chartPropertyForm.setMargin(false);
        chartPropertyForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        chartPropertySelectLayout.addComponent(chartPropertyForm);

        ComboBox propertySelector = new ComboBox();
        propertySelector.addStyleName(ValoTheme.COMBOBOX_SMALL);
        propertySelector.setRequired(true);
        propertySelector.setWidth("100%");
        propertySelector.setTextInputAllowed(true);
        propertySelector.setNullSelectionAllowed(false);
        propertySelector.setInputPrompt("选择或输入属性名称");
        propertySelector.addItems(this.measurablePropertiesNameList);
        chartPropertyForm.addComponent(propertySelector);
        this.barPropertiesSelectorList.add(propertySelector);

        Button removePropertyButton=new Button();
        removePropertyButton.setIcon(FontAwesome.TRASH);
        removePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        removePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        removePropertyButton.setEnabled(true);
        removePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                barPropertiesSelectorList.remove(propertySelector);
                barPropertiesSelectorContainer.removeComponent(containerLayout);
            }
        });

        chartPropertySelectLayout.addComponent(removePropertyButton);
        chartPropertySelectLayout.setExpandRatio(chartPropertyForm,1);
        containerLayout.addComponent(chartPropertySelectLayout);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        spacingLayout.addStyleName("ui_appSectionLightDiv");
        containerLayout.addComponent(spacingLayout);
        this.barPropertiesSelectorContainer.addComponent(containerLayout);
    }

    @Override
    public String getParametersQueryString() {
        StringBuffer parametersQuerySb=new StringBuffer();
        parametersQuerySb.append("&dateProperty="+timeAxisComboBox.getValue().toString());
        parametersQuerySb.append("&valueProperties=");
        for(int i=0;i<valuePropertiesSelectorList.size();i++){
            ComboBox currentSelector=valuePropertiesSelectorList.get(i);
            if(i!=valuePropertiesSelectorList.size()-1){
                parametersQuerySb.append(currentSelector.getValue().toString()+",");
            }else{
                parametersQuerySb.append(currentSelector.getValue().toString());
            }
        }
        if(barPropertiesSelectorList.size()>0){
            parametersQuerySb.append("&barProperties=");
            for(int i=0;i<barPropertiesSelectorList.size();i++){
                ComboBox currentSelector=barPropertiesSelectorList.get(i);
                if(i!=barPropertiesSelectorList.size()-1){
                    parametersQuerySb.append(currentSelector.getValue().toString()+",");
                }else{
                    parametersQuerySb.append(currentSelector.getValue().toString());
                }
            }
        }
        if(startDateField.getValue()!=null){
            parametersQuerySb.append("&start=");
            parametersQuerySb.append((new SimpleDateFormat("yyyy-MM-dd hh:mm")).format(startDateField.getValue()));
        }
        if(endDateField.getValue()!=null){
            parametersQuerySb.append("&end=");
            parametersQuerySb.append((new SimpleDateFormat("yyyy-MM-dd hh:mm")).format(endDateField.getValue()));
        }
        return parametersQuerySb.toString();
    }

    @Override
    public boolean validateParametersInput() {
        boolean validateResult=true;
        validateResult=validateResult&timeAxisComboBox.isValid();
        validateResult=validateResult&startDateField.isValid();
        validateResult=validateResult& endDateField.isValid();
        for(ComboBox bubblePropertySelector:valuePropertiesSelectorList){
            validateResult=validateResult&bubblePropertySelector.isValid();
        }
        for(ComboBox bubblePropertySelector:barPropertiesSelectorList){
            validateResult=validateResult&bubblePropertySelector.isValid();
        }
        if(startDateField.getValue()!=null&&endDateField.getValue()!=null){
            if(endDateField.getValue().getTime()<=startDateField.getValue().getTime()){
                return false;
            }
        }
        return validateResult;
    }
}
