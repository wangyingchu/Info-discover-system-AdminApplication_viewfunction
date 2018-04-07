package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyValueVO;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;

import com.vaadin.data.validator.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * Created by wangychu on 10/19/16.
 */
public class CreateTypeDataInstancePanel extends VerticalLayout implements InputPropertyNamePanelInvoker {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String discoverSpaceName;
    private String dataInstanceTypeKind;
    private String dataInstanceTypeName;
    private SectionActionsBar dataFieldActionsBar;
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
    private MainSectionTitle addNewTypeDataInstanceSectionTitle;
    private Label operationTitle;
    private Button addButton;

    public CreateTypeDataInstancePanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        this.typePropertiesInfoMap=new HashMap<String,PropertyTypeVO>();
        this.dataPropertiesInfoMap=new HashMap<String,PropertyTypeVO>();
        this.dataPropertiesEditorMap =new HashMap<String,Field>();
        setSpacing(true);
        setMargin(true);
        this.addNewTypeDataInstanceSectionTitle =new MainSectionTitle("---");
        addComponent(this.addNewTypeDataInstanceSectionTitle);
        dataFieldActionsBar=new SectionActionsBar(new Label("---" , ContentMode.HTML));
        addComponent(dataFieldActionsBar);

        this.operationTitle= new Label(FontAwesome.LIST_UL.getHtml() +" ---", ContentMode.HTML);
        this.operationTitle.addStyleName(ValoTheme.LABEL_SMALL);
        this.operationTitle.addStyleName("ui_appStandaloneElementPadding");
        this.operationTitle.addStyleName("ui_appSectionLightDiv");
        addComponent(operationTitle);

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

        Panel dataCountFormContainerPanel = new Panel();
        dataCountFormContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        dataCountFormContainerPanel.setWidth("100%");
        dataCountFormContainerPanel.setHeight(400,Unit.PIXELS);
        dataCountFormContainerPanel.setContent(this.propertiesEditForm);
        addComponent(dataCountFormContainerPanel);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        footer.setWidth("100%");
        this.addButton=new Button("---", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new data logic */
                addNewTypeData();
            }
        });
        this.addButton.setIcon(FontAwesome.PLUS_SQUARE);
        this.addButton.addStyleName("primary");
        footer.addComponent(this.addButton);
        addComponent(footer);

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
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
            this.addNewTypeDataInstanceSectionTitle.setValue("创建维度数据");
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 添加维度属性:");
            this.addButton.setCaption("创建维度数据");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.TAGS.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);

            List<PropertyTypeVO> dimensionTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveDimensionTypePropertiesInfo(this.getDiscoverSpaceName(), getDataInstanceTypeName());
            if(dimensionTypePropertiesList!=null){
                generateTypePropertiesEditUIElements(dimensionTypePropertiesList);
            }
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
            this.addNewTypeDataInstanceSectionTitle.setValue("创建事实数据");
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 添加事实属性:");
            this.addButton.setCaption("创建事实数据");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.CLONE.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);

            List<PropertyTypeVO> factTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveFactTypePropertiesInfo(this.getDiscoverSpaceName(), getDataInstanceTypeName());
            if(factTypePropertiesList!=null){
                generateTypePropertiesEditUIElements(factTypePropertiesList);
            }
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
        String dataTypeStr="---";
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
            dataTypeStr="维度";
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
            dataTypeStr="事实";
        }
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
                        "属性 "+propertyNameValue+" 是所属"+dataTypeStr+"类型预定义属性", Notification.Type.ERROR_MESSAGE);
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

    private void addNewTypeData(){
        String dataTypeStr="---";
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
            dataTypeStr="维度";
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
            dataTypeStr="事实";
        }
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
                return;
            }
        }

        String confirmMessageString=" 请确认创建类型为 "+this.getDataInstanceTypeName()+" 的"+dataTypeStr+"数据";
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog addDataConfirmDialog = new ConfirmDialog();
        addDataConfirmDialog.setConfirmMessage(confirmMessage);

        final String dataTypeMessageStr=dataTypeStr;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addDataConfirmDialog.close();
                List<PropertyValueVO> dataProperties=retrievePropertyValueObjects();
                boolean createTypePropertyResult=false;
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
                    createTypePropertyResult=InfoDiscoverSpaceOperationUtil.createDimension(getDiscoverSpaceName(), getDataInstanceTypeName(),dataProperties);
                }
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
                    createTypePropertyResult=InfoDiscoverSpaceOperationUtil.createFact(getDiscoverSpaceName(), getDataInstanceTypeName(),dataProperties);
                }
                if(createTypePropertyResult){
                    getContainerDialog().close();
                    //getCreateTypePropertyPanelInvoker().createTypePropertyActionFinish(createTypePropertyResult);
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建"+dataTypeMessageStr+"成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建"+dataTypeMessageStr+"错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addDataConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addDataConfirmDialog);
    }

    public String getDataInstanceTypeKind() {
        return dataInstanceTypeKind;
    }

    public void setDataInstanceTypeKind(String dataInstanceTypeKind) {
        this.dataInstanceTypeKind = dataInstanceTypeKind;
    }

    private List<PropertyValueVO> retrievePropertyValueObjects(){
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
}
