package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyValueVO;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.InputPropertyNamePanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.InputPropertyNamePanelInvoker;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.validator.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * Created by wangychu on 12/19/16.
 */
public class RelationPropertiesEditorPanel extends VerticalLayout implements InputPropertyNamePanelInvoker {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String discoverSpaceName;
    private String dataInstanceTypeName;
    private MenuBar.MenuItem createTypeDefinedPropertyMenuItem;
    private MenuBar.MenuItem createDataCustomMenuItem;
    private MenuBar.MenuItem removeDataPropertyMenuItem;
    private FormLayout propertiesEditForm;
    private MenuBar.Command createTypePropertyMenuItemCommand;
    private MenuBar.Command createDataCustomPropertyMenuItemCommand;
    private MenuBar.Command removeDataPropertyMenuItemCommand;
    private Map<String,PropertyTypeVO> typePropertiesInfoMap;
    private Map<String,PropertyTypeVO> dataPropertiesInfoMap;
    private Map<String,Field> dataPropertiesEditorMap;
    private String currentTempCustomPropertyDataType;
    private Panel dataCountFormContainerPanel;

    public RelationPropertiesEditorPanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        this.typePropertiesInfoMap=new HashMap<String,PropertyTypeVO>();
        this.dataPropertiesInfoMap=new HashMap<String,PropertyTypeVO>();
        this.dataPropertiesEditorMap =new HashMap<String,Field>();
        setSpacing(true);
        setMargin(true);

        MenuBar addRecordOperationMenuBar = new MenuBar();
        addRecordOperationMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        addRecordOperationMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
        this.createTypeDefinedPropertyMenuItem = addRecordOperationMenuBar.addItem("添加类型预定义属性", FontAwesome.CODE_FORK, null);
        this.createDataCustomMenuItem = addRecordOperationMenuBar.addItem("添加自定义属性", FontAwesome.TAG, null);
        this.removeDataPropertyMenuItem = addRecordOperationMenuBar.addItem("删除属性", FontAwesome.TRASH_O, null);
        addComponent(addRecordOperationMenuBar);

        this.propertiesEditForm = new FormLayout();
        this.propertiesEditForm.setMargin(false);
        this.propertiesEditForm.setWidth("100%");
        this.propertiesEditForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        dataCountFormContainerPanel = new Panel();
        dataCountFormContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        dataCountFormContainerPanel.setWidth("100%");
        dataCountFormContainerPanel.setContent(this.propertiesEditForm);
        addComponent(dataCountFormContainerPanel);

        this.createTypePropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedTypePropertyName=selectedItem.getText();
                addTypePropertyEditUI(typePropertiesInfoMap.get(selectedTypePropertyName));
            }
        };

        this.createDataCustomPropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedPropertyDataType=selectedItem.getText();
                addDataCustomPropertyEditUI(selectedPropertyDataType);
            }
        };

        this.removeDataPropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedPropertyName=selectedItem.getText();
                removeDataProperty(selectedPropertyName,selectedItem);
            }
        };

        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_STRING, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_BOOLEAN, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_DATE, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_INT, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_LONG, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_DOUBLE, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_FLOAT, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_SHORT, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    @Override
    public void attach() {
        super.attach();
        List<PropertyTypeVO> relationTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveRelationTypePropertiesInfo(this.getDiscoverSpaceName(), getDataInstanceTypeName());
        if(relationTypePropertiesList!=null){
            generateTypePropertiesEditUIElements(relationTypePropertiesList);
        }
    }

    private void generateTypePropertiesEditUIElements(List<PropertyTypeVO> typePropertiesList){
        for(PropertyTypeVO currentPropertyTypeVO:typePropertiesList){
            this.typePropertiesInfoMap.put(currentPropertyTypeVO.getPropertyName(),currentPropertyTypeVO);
            if(currentPropertyTypeVO.isMandatory()){
                addTypePropertyEditUI(currentPropertyTypeVO);
            }else{
                if(dataInstanceTypeName.equals(currentPropertyTypeVO.getPropertySourceOwner())){
                    this.createTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.CIRCLE_O, this.createTypePropertyMenuItemCommand);
                }else{
                    this.createTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.REPLY_ALL, this.createTypePropertyMenuItemCommand);
                }
            }
        }
    }

    private void addTypePropertyEditUI(PropertyTypeVO dataCustomPropertyType){
        String properTyName=dataCustomPropertyType.getPropertyName();
        Field propertyEditor=this.dataPropertiesEditorMap.get(properTyName);
        if(propertyEditor!=null){
            Notification errorNotification = new Notification("数据校验错误",
                    "属性 "+properTyName+" 已存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(dataCustomPropertyType!=null){
            String propertyName=dataCustomPropertyType.getPropertyName();
            String propertyDataType=dataCustomPropertyType.getPropertyType();
            Field currentPropertyEditor=null;
            switch(propertyDataType){
                case ApplicationConstant.DataFieldType_STRING:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_STRING+"] "+propertyName);
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    currentPropertyEditor=new ComboBox("["+ApplicationConstant.DataFieldType_BOOLEAN+"] "+propertyName);
                    ((ComboBox)currentPropertyEditor).addItem("true");
                    ((ComboBox)currentPropertyEditor).addItem("false");
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    currentPropertyEditor= new PopupDateField("["+ApplicationConstant.DataFieldType_DATE+"] "+propertyName);
                    ((DateField)currentPropertyEditor).setDateFormat("yyyy-MM-dd hh:mm:ss");
                    ((DateField)currentPropertyEditor).setResolution(Resolution.SECOND);
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_INT+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Integer.class);
                    currentPropertyEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null,null));
                    ((TextField)currentPropertyEditor).setValue("0");
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    currentPropertyEditor=new TextField("["+ ApplicationConstant.DataFieldType_LONG+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Long.class);
                    currentPropertyEditor.addValidator(new LongRangeValidator("该项属性值必须为LONG类型", null,null));
                    ((TextField)currentPropertyEditor).setValue("0");
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_DOUBLE+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Double.class);
                    currentPropertyEditor.addValidator(new DoubleRangeValidator("该项属性值必须为DOUBLE类型", null,null));
                    ((TextField)currentPropertyEditor).setValue("0.0");
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_FLOAT+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Float.class);
                    currentPropertyEditor.addValidator(new FloatRangeValidator("该项属性值必须为FLOAT类型", null,null));
                    ((TextField)currentPropertyEditor).setValue("0.0");
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_SHORT+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Short.class);
                    currentPropertyEditor.addValidator(new ShortRangeValidator("该项属性值必须为SHORT类型", null,null));
                    ((TextField)currentPropertyEditor).setValue("0");
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_BYTE+"] "+propertyName);
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_BINARY+"] "+propertyName);
                    break;
            }
            if(currentPropertyEditor!=null){
                if(dataCustomPropertyType.isMandatory()){
                    currentPropertyEditor.setRequired(true);
                }
                if(dataCustomPropertyType.isReadOnly()){
                    currentPropertyEditor.setReadOnly(true);
                }
                if(!dataCustomPropertyType.isNullable()){
                    currentPropertyEditor.addValidator(new NullValidator("该项属性值不允许为空", false));
                }
            }

            this.dataPropertiesInfoMap.put(properTyName,dataCustomPropertyType);
            this.dataPropertiesEditorMap.put(properTyName, currentPropertyEditor);
            this.propertiesEditForm.addComponent(currentPropertyEditor);
            if(!dataCustomPropertyType.isMandatory()) {
                if (dataInstanceTypeName.equals(dataCustomPropertyType.getPropertySourceOwner())) {
                    this.removeDataPropertyMenuItem.addItem(dataCustomPropertyType.getPropertyName(), FontAwesome.CIRCLE_O, this.removeDataPropertyMenuItemCommand);
                } else {
                    this.removeDataPropertyMenuItem.addItem(dataCustomPropertyType.getPropertyName(), FontAwesome.REPLY_ALL, this.removeDataPropertyMenuItemCommand);
                }
            }
        }
    }

    private void removeDataProperty(String properTyName,MenuBar.MenuItem propertyMenuItem){
        Field propertyEditor=this.dataPropertiesEditorMap.get(properTyName);
        this.propertiesEditForm.removeComponent(propertyEditor);
        this.dataPropertiesEditorMap.remove(properTyName);
        this.dataPropertiesInfoMap.remove(properTyName);
        propertyEditor.discard();
        this.removeDataPropertyMenuItem.removeChild(propertyMenuItem);
    }

    private void addDataCustomPropertyEditUI(String propertyDataType){
        this.currentTempCustomPropertyDataType=propertyDataType;
        InputPropertyNamePanel inputPropertyNamePanel=new InputPropertyNamePanel(this.currentUserClientInfo);
        final Window window = new Window();
        window.setWidth(450.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(inputPropertyNamePanel);
        inputPropertyNamePanel.setContainerDialog(window);
        inputPropertyNamePanel.setInputPropertyNamePanelInvoker(this);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public void inputPropertyNameActionFinish(String propertyNameValue) {
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(propertyNameValue);
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入属性名称 "+propertyNameValue+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(propertyNameValue);
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入属性名称 "+propertyNameValue+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(this.currentTempCustomPropertyDataType!=null){
            Field propertyEditor=this.dataPropertiesEditorMap.get(propertyNameValue);
            if(propertyEditor!=null){
                Notification errorNotification = new Notification("数据校验错误",
                        "属性 "+propertyNameValue+" 已存在", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            PropertyTypeVO propertyType=this.typePropertiesInfoMap.get(propertyNameValue);
            if(propertyType!=null){
                Notification errorNotification = new Notification("数据校验错误",
                        "属性 "+propertyNameValue+" 是所属关系类型预定义属性", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            PropertyTypeVO dataCustomPropertyTypeVO=new PropertyTypeVO();
            dataCustomPropertyTypeVO.setPropertySourceOwner(this.getDataInstanceTypeName());
            dataCustomPropertyTypeVO.setReadOnly(false);
            dataCustomPropertyTypeVO.setNullable(false);
            dataCustomPropertyTypeVO.setMandatory(false);
            dataCustomPropertyTypeVO.setPropertyName(propertyNameValue);
            dataCustomPropertyTypeVO.setPropertyType(this.currentTempCustomPropertyDataType);

            this.dataPropertiesInfoMap.put(propertyNameValue,dataCustomPropertyTypeVO);
            this.addTypePropertyEditUI(dataCustomPropertyTypeVO);
        }
    }

    public boolean validatePropertiesValue(){
        Set<String> editorMapKeySet=this.dataPropertiesEditorMap.keySet();
        Iterator<String> keyIterator=editorMapKeySet.iterator();
        while(keyIterator.hasNext()){
            String currentKey=keyIterator.next();
            boolean currentEditorValidateResult=this.dataPropertiesEditorMap.get(currentKey).isValid();
            if(!currentEditorValidateResult){
                Notification errorNotification = new Notification("数据校验错误",
                        "属性 "+currentKey+" 当前输入值非法", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return false;
            }
        }
        return true;
    }

    public List<PropertyValueVO> retrievePropertyValueObjects(){
        List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();

        Set<String> editorMapKeySet=this.dataPropertiesEditorMap.keySet();
        Iterator<String> keyIterator=editorMapKeySet.iterator();
        while(keyIterator.hasNext()){
            String currentKey=keyIterator.next();
            Field currentField=this.dataPropertiesEditorMap.get(currentKey);
            PropertyTypeVO currentPropertyTypeVO=this.dataPropertiesInfoMap.get(currentKey);
            String currentPropertyDataType=currentPropertyTypeVO.getPropertyType();

            PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
            currentPropertyValueVO.setPropertyType(currentPropertyDataType);
            currentPropertyValueVO.setPropertyName(currentKey);
            propertyValueVOList.add(currentPropertyValueVO);
            Object propertyValueObj=null;
            switch(currentPropertyDataType){
                case ApplicationConstant.DataFieldType_STRING:
                    propertyValueObj=((TextField)currentField).getValue();
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    propertyValueObj=((ComboBox)currentField).getValue();
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    propertyValueObj=((PopupDateField)currentField).getValue();
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    propertyValueObj=((TextField)currentField).getConvertedValue();
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    propertyValueObj=((TextField)currentField).getConvertedValue();
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    propertyValueObj=((TextField)currentField).getConvertedValue();
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    propertyValueObj=((TextField)currentField).getConvertedValue();
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    propertyValueObj=((TextField)currentField).getConvertedValue();
                    break;
                /*
                case ApplicationConstant.DataFieldType_BYTE:
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    break;
                */
            }
            currentPropertyValueVO.setPropertyValue(propertyValueObj);
        }
        return propertyValueVOList;
    }

    public void setEditFormHeight(int formHeight){
        dataCountFormContainerPanel.setHeight(formHeight,Unit.PIXELS);
    }
}
