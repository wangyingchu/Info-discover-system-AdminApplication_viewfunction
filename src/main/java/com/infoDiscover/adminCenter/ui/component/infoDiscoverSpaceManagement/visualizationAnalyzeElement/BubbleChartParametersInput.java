package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangychu on 3/20/17.
 */
public class BubbleChartParametersInput extends BaseChartParametersInput {

    private UserClientInfo currentUserClientInfo;
    private VerticalLayout additionalBubblePropertiesSelectorContainer;
    private List<ComboBox> additionalChartPropertiesSelectorList;
    private List<String> measurablePropertiesNameList;
    private ComboBox xAxisComboBox;
    private ComboBox yAxisComboBox;
    private TextField xAxisRulerValueEditor;
    private TextField yAxisRulerValueEditor;

    public BubbleChartParametersInput(UserClientInfo userClientInfo,List<String> measurablePropertiesNameList) {
        this.setMargin(true);
        this.setSpacing(true);
        this.currentUserClientInfo = userClientInfo;
        this.additionalChartPropertiesSelectorList=new ArrayList<>();
        this.measurablePropertiesNameList=measurablePropertiesNameList;

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

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(spacingLayout);
        spacingLayout.addStyleName("ui_appSectionLightDiv");

        HorizontalLayout chartRulesValueTitleLayout=new HorizontalLayout();
        chartRulesValueTitleLayout.setHeight(30,Unit.PIXELS);
        chartRulesValueTitleLayout.addStyleName("ui_appStandaloneElementPadding");
        Label rulesValueTitle= new Label(VaadinIcons.PASSWORD.getHtml() +" 轴数据标尺值", ContentMode.HTML);
        rulesValueTitle.addStyleName(ValoTheme.LABEL_TINY);
        chartRulesValueTitleLayout.addComponent(rulesValueTitle);
        chartRulesValueTitleLayout.setComponentAlignment(rulesValueTitle,Alignment.MIDDLE_LEFT);
        this.addComponent(chartRulesValueTitleLayout);

        FormLayout axisPropertiesRuleValueForm = new FormLayout();
        axisPropertiesRuleValueForm.setMargin(false);
        axisPropertiesRuleValueForm.setWidth(100,Unit.PERCENTAGE);
        axisPropertiesRuleValueForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        this.addComponent(axisPropertiesRuleValueForm);

        xAxisRulerValueEditor = new TextField("X 轴标尺值");
        xAxisRulerValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        xAxisRulerValueEditor.setWidth(100,Unit.PERCENTAGE);
        xAxisRulerValueEditor.setConverter(Double.class);
        xAxisRulerValueEditor.addValidator(new DoubleRangeValidator("该项属性值必须为数值类型", null, null));
        xAxisRulerValueEditor.setValue("0");
        axisPropertiesRuleValueForm.addComponent(xAxisRulerValueEditor);

        yAxisRulerValueEditor = new TextField("Y 轴标尺值");
        yAxisRulerValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        yAxisRulerValueEditor.setWidth(100,Unit.PERCENTAGE);
        yAxisRulerValueEditor.setConverter(Double.class);
        yAxisRulerValueEditor.addValidator(new DoubleRangeValidator("该项属性值必须为数值类型", null, null));
        yAxisRulerValueEditor.setValue("0");
        axisPropertiesRuleValueForm.addComponent(yAxisRulerValueEditor);

        VerticalLayout spacingLayout１=new VerticalLayout();
        spacingLayout１.setWidth(100,Unit.PERCENTAGE);
        addComponent(spacingLayout１);
        spacingLayout１.addStyleName("ui_appSectionLightDiv");

        HorizontalLayout bubblePropertiesTitleLayout=new HorizontalLayout();
        bubblePropertiesTitleLayout.setHeight(30,Unit.PIXELS);
        bubblePropertiesTitleLayout.addStyleName("ui_appStandaloneElementPadding");

        Label propertiesTypeTitle= new Label(FontAwesome.CIRCLE_O.getHtml() +" 气泡值数据属性", ContentMode.HTML);
        propertiesTypeTitle.addStyleName(ValoTheme.LABEL_TINY);

        Button addNewPropertyButton=new Button("添加气泡值数据属性");
        addNewPropertyButton.setIcon(FontAwesome.PLUS);
        addNewPropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        addNewPropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        addNewPropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addNewPropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addChartPropertySelector(true);
            }
        });

        bubblePropertiesTitleLayout.addComponent(propertiesTypeTitle);
        bubblePropertiesTitleLayout.setComponentAlignment(propertiesTypeTitle,Alignment.MIDDLE_LEFT);
        bubblePropertiesTitleLayout.addComponent(addNewPropertyButton);
        bubblePropertiesTitleLayout.setComponentAlignment(addNewPropertyButton,Alignment.MIDDLE_LEFT);

        this.addComponent(bubblePropertiesTitleLayout);

        this.additionalBubblePropertiesSelectorContainer=new VerticalLayout();
        this.additionalBubblePropertiesSelectorContainer.setWidth(100,Unit.PERCENTAGE);
        this.addComponent(this.additionalBubblePropertiesSelectorContainer);

        addChartPropertySelector(false);
    }

    private void addChartPropertySelector(boolean enableRemoveButton){
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
        this.additionalChartPropertiesSelectorList.add(propertySelector);

        Button removePropertyButton=new Button();
        removePropertyButton.setIcon(FontAwesome.TRASH);
        removePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        removePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        removePropertyButton.setEnabled(enableRemoveButton);
        removePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                additionalChartPropertiesSelectorList.remove(propertySelector);
                additionalBubblePropertiesSelectorContainer.removeComponent(containerLayout);
            }
        });

        chartPropertySelectLayout.addComponent(removePropertyButton);
        chartPropertySelectLayout.setExpandRatio(chartPropertyForm,1);
        containerLayout.addComponent(chartPropertySelectLayout);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        spacingLayout.addStyleName("ui_appSectionLightDiv");
        containerLayout.addComponent(spacingLayout);
        this.additionalBubblePropertiesSelectorContainer.addComponent(containerLayout);
    }

    @Override
    public String getParametersQueryString() {
        StringBuffer parametersQuerySb=new StringBuffer();
        parametersQuerySb.append("&x="+xAxisComboBox.getValue().toString());
        parametersQuerySb.append("&y="+yAxisComboBox.getValue().toString());
        parametersQuerySb.append("&z=");
        for(int i=0;i<additionalChartPropertiesSelectorList.size();i++){
            ComboBox currentSelector=additionalChartPropertiesSelectorList.get(i);

            if(i!=additionalChartPropertiesSelectorList.size()-1){
                parametersQuerySb.append(currentSelector.getValue().toString()+",");
            }else{
                parametersQuerySb.append(currentSelector.getValue().toString());
            }
        }
        if(!xAxisRulerValueEditor.getConvertedValue().toString().equals("0.0")){
            parametersQuerySb.append("&xRuler="+ xAxisRulerValueEditor.getConvertedValue().toString());
        }
        if(!yAxisRulerValueEditor.getConvertedValue().toString().equals("0.0")){
            parametersQuerySb.append("&yRuler="+ yAxisRulerValueEditor.getConvertedValue().toString());
        }
        return parametersQuerySb.toString();
    }

    @Override
    public boolean validateParametersInput() {
        boolean validateResult=true;
        validateResult=validateResult&xAxisComboBox.isValid();
        validateResult=validateResult&yAxisComboBox.isValid();
        validateResult=validateResult& xAxisRulerValueEditor.isValid();
        validateResult=validateResult& yAxisRulerValueEditor.isValid();
        for(ComboBox bubblePropertySelector:additionalChartPropertiesSelectorList){
            validateResult=validateResult&bubblePropertySelector.isValid();
        }
        return validateResult;
    }
}
