package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangychu on 11/4/16.
 */
public class MultiValuePropertyInput extends HorizontalLayout{

    private UserClientInfo currentUserClientInfo;
    private HorizontalLayout valuesDisplayContainer;
    private QueryConditionItem queryConditionItem;
    private Field currentValueInputField;
    private List<Object> valuesList;

    private Map<Object,HorizontalLayout> valueDisplaySectionMap;

    public MultiValuePropertyInput(UserClientInfo currentUserClientInfo,int valueDisplayFieldWidth){
        this.currentUserClientInfo=currentUserClientInfo;
        this.valuesList=new ArrayList<Object>();
        this.valueDisplaySectionMap=new HashMap<Object,HorizontalLayout>();

        Button addNewValueButton=new Button();
        addNewValueButton.setIcon(FontAwesome.PLUS_CIRCLE);
        addNewValueButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        addNewValueButton.addStyleName(ValoTheme.BUTTON_TINY);
        addNewValueButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                displayNewValueInput();
            }
        });
        addComponent(addNewValueButton);

        HorizontalLayout spacingDiv=new HorizontalLayout();
        spacingDiv.setWidth(5,Unit.PIXELS);
        addComponent(spacingDiv);

        Panel valuesDisplayPanel=new Panel();
        valuesDisplayPanel.setWidth(valueDisplayFieldWidth,Unit.PIXELS);
        valuesDisplayPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        addComponent(valuesDisplayPanel);

        this.valuesDisplayContainer=new HorizontalLayout();
        valuesDisplayPanel.setContent(this.valuesDisplayContainer);
    }

    private void displayNewValueInput(){
        final Window window = new Window();
        window.setWidth(310.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);

        VerticalLayout containerLayout=new VerticalLayout();
        containerLayout.setSpacing(true);
        containerLayout.setMargin(true);

        Label inputValueSectionTitle=new Label("添加新的值");
        inputValueSectionTitle.addStyleName(ValoTheme.LABEL_COLORED);
        inputValueSectionTitle.addStyleName(ValoTheme.LABEL_BOLD);
        containerLayout.addComponent(inputValueSectionTitle);

        HorizontalLayout valueInputFieldLayout=new HorizontalLayout();
        containerLayout.addComponent(valueInputFieldLayout);
        if(this.currentValueInputField!=null){
            this.currentValueInputField.discard();
        }
        this.currentValueInputField=this.getQueryConditionItem().generateSingleQueryValueTextField(170);
        valueInputFieldLayout.addComponent(this.currentValueInputField);

        HorizontalLayout spacingDiv=new HorizontalLayout();
        spacingDiv.setWidth(20,Unit.PIXELS);
        valueInputFieldLayout.addComponent(spacingDiv);

        Button addNewButton=new Button("确定");
        addNewButton.addStyleName(ValoTheme.BUTTON_SMALL);
        addNewButton.setIcon(FontAwesome.CHECK_CIRCLE);
        addNewButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                boolean currentEditorValidateResult=currentValueInputField.isValid();
                if(!currentEditorValidateResult){
                    Notification errorNotification = new Notification("数据校验错误",
                            "当前输入值非法", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                    return;
                }
                addNewValue();
                window.close();
            }
        });
        valueInputFieldLayout.addComponent(addNewButton);
        window.setContent(containerLayout);
        UI.getCurrent().addWindow(window);
    }

    public QueryConditionItem getQueryConditionItem() {
        return queryConditionItem;
    }

    public void setQueryConditionItem(QueryConditionItem queryConditionItem) {
        this.queryConditionItem = queryConditionItem;
    }

    private void addNewValue(){
        PropertyTypeVO currentPropertyTypeVO=this.getQueryConditionItem().getPropertyTypeVO();
        String currentPropertyDataType=currentPropertyTypeVO.getPropertyType();
        Object propertyValueObj=null;
        switch(currentPropertyDataType){
            case ApplicationConstant.DataFieldType_STRING:
                propertyValueObj=((TextField)this.currentValueInputField).getValue();
                break;
            case ApplicationConstant.DataFieldType_BOOLEAN:
                propertyValueObj=((ComboBox)this.currentValueInputField).getValue();
                break;
            case ApplicationConstant.DataFieldType_DATE:
                propertyValueObj=((PopupDateField)this.currentValueInputField).getValue();
                break;
            case ApplicationConstant.DataFieldType_INT:
                propertyValueObj=((TextField)this.currentValueInputField).getConvertedValue();
                break;
            case ApplicationConstant.DataFieldType_LONG:
                propertyValueObj=((TextField)this.currentValueInputField).getConvertedValue();
                break;
            case ApplicationConstant.DataFieldType_DOUBLE:
                propertyValueObj=((TextField)this.currentValueInputField).getConvertedValue();
                break;
            case ApplicationConstant.DataFieldType_FLOAT:
                propertyValueObj=((TextField)this.currentValueInputField).getConvertedValue();
                break;
            case ApplicationConstant.DataFieldType_SHORT:
                propertyValueObj=((TextField)this.currentValueInputField).getConvertedValue();
                break;
                /*
                case ApplicationConstant.DataFieldType_BYTE:
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    break;
                */
        }
        if(!this.valuesList.contains(propertyValueObj)){
            this.valuesList.add(propertyValueObj);
            this.valuesDisplayContainer.addComponent(generateValueDisplaySection(propertyValueObj));
        }
    }

    private HorizontalLayout generateValueDisplaySection(final Object valueObject){
        HorizontalLayout displayContainer=new HorizontalLayout();
        Label newValueLabel=new Label(valueObject.toString());
        newValueLabel.addStyleName(ValoTheme.LABEL_BOLD);
        Button removeValueButton=new Button();
        removeValueButton.setIcon(FontAwesome.REMOVE);
        removeValueButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        removeValueButton.addStyleName(ValoTheme.BUTTON_LINK);
        removeValueButton.addStyleName(ValoTheme.BUTTON_TINY);
        removeValueButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                removeValue(valueObject);
            }
        });
        displayContainer.addComponent(newValueLabel);
        displayContainer.addComponent(removeValueButton);
        this.valueDisplaySectionMap.put(valueObject,displayContainer);
        return displayContainer;
    }

    private void removeValue(Object valueToDelete){
        HorizontalLayout layoutToDelete=this.valueDisplaySectionMap.get(valueToDelete);
        if(layoutToDelete!=null){
            this.valuesDisplayContainer.removeComponent(layoutToDelete);
            this.valueDisplaySectionMap.remove(valueToDelete);
        }
        if(this.valuesList.contains(valueToDelete)){
            this.valuesList.remove(valueToDelete);
        }
    }

    public List<Object> getMultiValueList(){
        return this.valuesList;
    }
}
