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
 * Created by wangychu on 3/25/17.
 */
public class GeographicalCoordinatesChartParametersInput extends BaseChartParametersInput{

    private UserClientInfo currentUserClientInfo;
    private List<String> geographicalCoordinatesPropertiesNameList;
    private List<String> stringPropertiesNameList;
    private ComboBox lngPropertyComboBox;
    private ComboBox latPropertyComboBox;
    private ComboBox locationTitleComboBox;
    private ComboBox locationDescComboBox;

    public GeographicalCoordinatesChartParametersInput(UserClientInfo userClientInfo,List<String> geographicalCoordinatesPropertiesNameList,List<String> stringPropertiesNameList) {
        this.setMargin(true);
        this.setSpacing(true);
        this.currentUserClientInfo = userClientInfo;

        this.geographicalCoordinatesPropertiesNameList=geographicalCoordinatesPropertiesNameList;
        this.stringPropertiesNameList=stringPropertiesNameList;

        Label analyzeParametersInputTitle= new Label(FontAwesome.LIST_UL.getHtml() +" 可视化分析参数属性", ContentMode.HTML);
        analyzeParametersInputTitle.addStyleName(ValoTheme.LABEL_SMALL);
        analyzeParametersInputTitle.addStyleName("ui_appSectionLightDiv");
        this.addComponent(analyzeParametersInputTitle);

        FormLayout coordinateAxisPropertiesForm = new FormLayout();
        coordinateAxisPropertiesForm.setMargin(false);
        coordinateAxisPropertiesForm.setWidth(100,Unit.PERCENTAGE);
        coordinateAxisPropertiesForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        lngPropertyComboBox = new ComboBox("经度属性");
        lngPropertyComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        lngPropertyComboBox.setRequired(true);
        lngPropertyComboBox.setWidth("100%");
        lngPropertyComboBox.setTextInputAllowed(true);
        lngPropertyComboBox.setNullSelectionAllowed(false);
        lngPropertyComboBox.setInputPrompt("选择或输入属性名称");
        lngPropertyComboBox.addItems(this.geographicalCoordinatesPropertiesNameList);
        lngPropertyComboBox.setTextInputAllowed(true);
        lngPropertyComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(lngPropertyComboBox);

        latPropertyComboBox = new ComboBox("纬度属性");
        latPropertyComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        latPropertyComboBox.setRequired(true);
        latPropertyComboBox.setWidth("100%");
        latPropertyComboBox.setTextInputAllowed(true);
        latPropertyComboBox.setNullSelectionAllowed(false);
        latPropertyComboBox.setInputPrompt("选择或输入属性名称");
        latPropertyComboBox.addItems(this.geographicalCoordinatesPropertiesNameList);
        latPropertyComboBox.setTextInputAllowed(true);
        latPropertyComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(latPropertyComboBox);

        locationTitleComboBox = new ComboBox("标题属性");
        locationTitleComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        locationTitleComboBox.setRequired(true);
        locationTitleComboBox.setWidth("100%");
        locationTitleComboBox.setTextInputAllowed(true);
        locationTitleComboBox.setNullSelectionAllowed(false);
        locationTitleComboBox.setInputPrompt("选择或输入属性名称");
        locationTitleComboBox.addItems(this.stringPropertiesNameList);
        locationTitleComboBox.setTextInputAllowed(true);
        locationTitleComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(locationTitleComboBox);

        locationDescComboBox = new ComboBox("描述属性");
        locationDescComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        locationDescComboBox.setRequired(false);
        locationDescComboBox.setWidth("100%");
        locationDescComboBox.setTextInputAllowed(true);
        locationDescComboBox.setNullSelectionAllowed(false);
        locationDescComboBox.setInputPrompt("选择或输入属性名称");
        locationDescComboBox.addItems(this.stringPropertiesNameList);
        locationDescComboBox.setTextInputAllowed(true);
        locationDescComboBox.setNewItemsAllowed(true);
        coordinateAxisPropertiesForm.addComponent(locationDescComboBox);

        this.addComponent(coordinateAxisPropertiesForm);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(spacingLayout);
        spacingLayout.addStyleName("ui_appSectionLightDiv");
    }

    @Override
    public String getParametersQueryString() {
        StringBuffer parametersQuerySb=new StringBuffer();
        parametersQuerySb.append("&latProperty="+latPropertyComboBox.getValue().toString());
        parametersQuerySb.append("&lngProperty="+lngPropertyComboBox.getValue().toString());
        parametersQuerySb.append("&titleProperty="+locationTitleComboBox.getValue().toString());
        if(locationDescComboBox.getValue()!=null){
            parametersQuerySb.append("&descProperty="+locationDescComboBox.getValue().toString());
        }
        return parametersQuerySb.toString();
    }

    @Override
    public boolean validateParametersInput() {
        boolean validateResult=true;
        validateResult=validateResult&lngPropertyComboBox.isValid();
        validateResult=validateResult&latPropertyComboBox.isValid();
        validateResult=validateResult& locationTitleComboBox.isValid();
        validateResult=validateResult& locationDescComboBox.isValid();
        if(lngPropertyComboBox.getValue()!=null&&latPropertyComboBox.getValue()!=null) {
            if (lngPropertyComboBox.getValue().equals(latPropertyComboBox.getValue())) {
                return false;
            }
        }
        return validateResult;
    }
}
