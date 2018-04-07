package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dataMappingManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.*;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.Property;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * Created by wangychu on 7/4/17.
 */
public class DataPropertiesDuplicateMappingDefinitionEditor extends VerticalLayout{

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private Window containerDialog;
    private DataPropertiesDuplicateMappingDefinitionEditorPanel relatedDataPropertiesDuplicateMappingDefinitionEditorPanel;
    private SectionActionsBar dataFieldActionsBar;
    private OptionGroup sourceDataType;
    private ComboBox sourceDataTypeField;
    private ComboBox sourceDataPropertyField;
    private OptionGroup duplicatePropertyHandleMethod;
    private ComboBox targetDataTypeField;
    private ComboBox targetDataPropertyField;
    private Map<String,FactTypeDefinitionVO> factTypeDefinitionMap;
    private Map<String,DimensionTypeDefinitionVO> dimensionTypeDefinitionMap;
    private Map<String,SolutionTypePropertyTypeDefinitionVO> sourceDataTypePropertiesMap;
    private Map<String,SolutionTypePropertyTypeDefinitionVO> targetDataTypePropertiesMap;

    public DataPropertiesDuplicateMappingDefinitionEditor(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle editorSectionTitle=new MainSectionTitle("创建数据属性复制规则");
        addComponent(editorSectionTitle);

        dataFieldActionsBar=new SectionActionsBar(new Label("---" , ContentMode.HTML));
        addComponent(dataFieldActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        this.sourceDataType = new OptionGroup("源数据类型");
        this.sourceDataType.setRequired(true);
        this.sourceDataType.addItems("事实", "维度");
        this.sourceDataType.setValue("事实");
        this.sourceDataType.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        form.addComponent(this.sourceDataType);
        this.sourceDataType.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if(valueChangeEvent.getProperty().getValue()!=null){
                    setSourceDataTypeChooseList(valueChangeEvent.getProperty().getValue().toString());
                }
            }
        });

        this.sourceDataTypeField=new ComboBox("-");
        this.sourceDataTypeField.setRequired(true);
        this.sourceDataTypeField.setWidth("100%");
        this.sourceDataTypeField.setTextInputAllowed(true);
        this.sourceDataTypeField.setNullSelectionAllowed(false);
        this.sourceDataTypeField.setInputPrompt("-");
        form.addComponent(this.sourceDataTypeField);
        this.sourceDataTypeField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if(valueChangeEvent.getProperty().getValue()!=null){
                    setSourceDataTypePropertySelectChooseList(valueChangeEvent.getProperty().getValue().toString());
                }
            }
        });

        this.sourceDataPropertyField=new ComboBox("-");
        this.sourceDataPropertyField.setRequired(true);
        this.sourceDataPropertyField.setWidth("100%");
        this.sourceDataPropertyField.setTextInputAllowed(true);
        this.sourceDataPropertyField.setNullSelectionAllowed(false);
        this.sourceDataPropertyField.setInputPrompt("-");
        form.addComponent(this.sourceDataPropertyField);

        this.duplicatePropertyHandleMethod = new OptionGroup("待复制属性已存在处理策略");
        this.duplicatePropertyHandleMethod.setRequired(true);
        this.duplicatePropertyHandleMethod.addItems("忽略复制操作", "更新已存在属性");
        this.duplicatePropertyHandleMethod.setValue("更新已存在属性");
        this.duplicatePropertyHandleMethod.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        form.addComponent(this.duplicatePropertyHandleMethod);

        this.targetDataTypeField=new ComboBox("-");
        this.targetDataTypeField.setRequired(true);
        this.targetDataTypeField.setWidth("100%");
        this.targetDataTypeField.setTextInputAllowed(true);
        this.targetDataTypeField.setNullSelectionAllowed(false);
        this.targetDataTypeField.setInputPrompt("-");
        form.addComponent(this.targetDataTypeField);
        this.targetDataTypeField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if(valueChangeEvent.getProperty().getValue()!=null){
                    setTargetDataTypePropertySelectChooseList(valueChangeEvent.getProperty().getValue().toString());
                }
            }
        });

        this.targetDataPropertyField=new ComboBox("-");
        this.targetDataPropertyField.setRequired(true);
        this.targetDataPropertyField.setWidth("100%");
        this.targetDataPropertyField.setTextInputAllowed(true);
        this.targetDataPropertyField.setNullSelectionAllowed(false);
        this.targetDataPropertyField.setInputPrompt("-");
        form.addComponent(this.targetDataPropertyField);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("创建数据属性复制规则", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addNewMappingDefinition();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public void attach() {
        super.attach();
        Label sectionActionBarLabel=new Label(VaadinIcons.CLIPBOARD_TEXT.getHtml()+" "+getBusinessSolutionName(), ContentMode.HTML);
        dataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);

        initFactSelectData(BusinessSolutionOperationUtil.getFactTypeDefinitionList(getBusinessSolutionName()));
        initDimensionSelectData(BusinessSolutionOperationUtil.getDimensionTypeDefinitionList(getBusinessSolutionName()));
        setSourceDataTypeChooseList("事实");
        setTargetDataTypeChooseList("事实");
    }

    private void initFactSelectData(List<FactTypeDefinitionVO> factTypeDefinitionList){
        if(this.factTypeDefinitionMap==null){
            this.factTypeDefinitionMap=new HashMap<>();
        }else{
            this.factTypeDefinitionMap.clear();
        }
        for(FactTypeDefinitionVO currentFactTypeDefinitionVO:factTypeDefinitionList){
            String typeName=currentFactTypeDefinitionVO.getTypeName();
            String typeAlias=currentFactTypeDefinitionVO.getTypeAliasName();
            String selectorItem=typeName+" ("+typeAlias+")";
            this.factTypeDefinitionMap.put(selectorItem,currentFactTypeDefinitionVO);
        }
    }

    private void initDimensionSelectData(List<DimensionTypeDefinitionVO> dimensionTypeDefinitionList){
        if(this.dimensionTypeDefinitionMap==null){
            this.dimensionTypeDefinitionMap=new HashMap<>();
        }else{
            this.dimensionTypeDefinitionMap.clear();
        }
        for(DimensionTypeDefinitionVO currentDimensionTypeDefinitionVO:dimensionTypeDefinitionList){
            String typeName=currentDimensionTypeDefinitionVO.getTypeName();
            String typeAlias=currentDimensionTypeDefinitionVO.getTypeAliasName();
            String selectorItem=typeName+" ("+typeAlias+")";
            this.dimensionTypeDefinitionMap.put(selectorItem,currentDimensionTypeDefinitionVO);
        }
    }

    private void setSourceDataTypeChooseList(String dataType){
        this.sourceDataTypeField.removeAllItems();
        this.sourceDataPropertyField.removeAllItems();
        Iterator<String> iterator=null;
        if("事实".equals(dataType)){
            this.sourceDataTypeField.setCaption("源事实类型名称");
            this.sourceDataTypeField.setInputPrompt("请选择源事实类型名称");
            this.sourceDataPropertyField.setCaption("数据匹配外键属性");
            this.sourceDataPropertyField.setInputPrompt("请选择源事实属性名称");
            if(this.factTypeDefinitionMap!=null){
                Set<String> labelSet=this.factTypeDefinitionMap.keySet();
                iterator=labelSet.iterator();
            }
        }
        if("维度".equals(dataType)){
            this.sourceDataTypeField.setCaption("源维度类型名称");
            this.sourceDataTypeField.setInputPrompt("请选择源维度类型名称");
            this.sourceDataPropertyField.setCaption("数据匹配外键属性");
            this.sourceDataPropertyField.setInputPrompt("请选择源维度属性名称");
            if(this.dimensionTypeDefinitionMap!=null) {
                Set<String> labelSet = this.dimensionTypeDefinitionMap.keySet();
                iterator = labelSet.iterator();
            }
        }
        while(iterator.hasNext()){
            this.sourceDataTypeField.addItem(iterator.next());
        }
    }

    private void setSourceDataTypePropertySelectChooseList(String propertyTypeLabel){
        this.sourceDataPropertyField.removeAllItems();
        String solutionName=this.getBusinessSolutionName();
        String propertyTypeKind=null;
        String propertyTypeName=null;
        if("事实".equals(this.sourceDataType.getValue().toString())){
            propertyTypeKind= InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT;
            propertyTypeName=this.factTypeDefinitionMap.get(propertyTypeLabel).getTypeName();
        }
        if("维度".equals(this.sourceDataType.getValue().toString())){
            propertyTypeKind=InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION;
            propertyTypeName=this.dimensionTypeDefinitionMap.get(propertyTypeLabel).getTypeName();
        }
        if(this.sourceDataTypePropertiesMap==null){
            this.sourceDataTypePropertiesMap=new HashMap<>();
        }else{
            this.sourceDataTypePropertiesMap.clear();
        }
        List<SolutionTypePropertyTypeDefinitionVO> currentTypePropertyList=BusinessSolutionOperationUtil.getSolutionTypePropertiesInfo(solutionName,propertyTypeKind,propertyTypeName);
        if(currentTypePropertyList!=null){
            for(SolutionTypePropertyTypeDefinitionVO currentSolutionTypePropertyTypeDefinitionVO:currentTypePropertyList){
                String propertyName=currentSolutionTypePropertyTypeDefinitionVO.getPropertyName();
                String propertyAlias=currentSolutionTypePropertyTypeDefinitionVO.getPropertyAliasName();
                String propertyType=currentSolutionTypePropertyTypeDefinitionVO.getPropertyType();
                String selectorItem=propertyName+" ("+propertyAlias+")"+" - "+propertyType;
                this.sourceDataPropertyField.addItem(selectorItem);
                this.sourceDataTypePropertiesMap.put(selectorItem,currentSolutionTypePropertyTypeDefinitionVO);
            }
        }
    }

    private void setTargetDataTypeChooseList(String dataType){
        this.targetDataTypeField.removeAllItems();
        this.targetDataPropertyField.removeAllItems();
        Iterator<String> iterator=null;
        if("事实".equals(dataType)){
            this.targetDataTypeField.setCaption("目标事实类型名称");
            this.targetDataTypeField.setInputPrompt("请选择目标事实类型名称");
            this.targetDataPropertyField.setCaption("数据匹配主键属性");
            this.targetDataPropertyField.setInputPrompt("请选择目标事实属性名称");
            if(this.factTypeDefinitionMap!=null){
                Set<String> labelSet=this.factTypeDefinitionMap.keySet();
                iterator=labelSet.iterator();
            }
        }
        if("维度".equals(dataType)){
            this.targetDataTypeField.setCaption("目标维度类型名称");
            this.targetDataTypeField.setInputPrompt("请选择目标维度类型名称");
            this.targetDataPropertyField.setCaption("数据匹配主键属性");
            this.targetDataPropertyField.setInputPrompt("请选择目标维度属性名称");
            if(this.dimensionTypeDefinitionMap!=null) {
                Set<String> labelSet = this.dimensionTypeDefinitionMap.keySet();
                iterator = labelSet.iterator();
            }
        }
        while(iterator.hasNext()){
            this.targetDataTypeField.addItem(iterator.next());
        }
    }

    private void setTargetDataTypePropertySelectChooseList(String propertyTypeLabel){
        this.targetDataPropertyField.removeAllItems();
        String solutionName=this.getBusinessSolutionName();
        String propertyTypeKind=InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT;
        String propertyTypeName=this.factTypeDefinitionMap.get(propertyTypeLabel).getTypeName();
        if(this.targetDataTypePropertiesMap==null){
            this.targetDataTypePropertiesMap=new HashMap<>();
        }else{
            this.targetDataTypePropertiesMap.clear();
        }
        List<SolutionTypePropertyTypeDefinitionVO> currentTypePropertyList=BusinessSolutionOperationUtil.getSolutionTypePropertiesInfo(solutionName,propertyTypeKind,propertyTypeName);
        if(currentTypePropertyList!=null){
            for(SolutionTypePropertyTypeDefinitionVO currentSolutionTypePropertyTypeDefinitionVO:currentTypePropertyList){
                String propertyName=currentSolutionTypePropertyTypeDefinitionVO.getPropertyName();
                String propertyAlias=currentSolutionTypePropertyTypeDefinitionVO.getPropertyAliasName();
                String propertyType=currentSolutionTypePropertyTypeDefinitionVO.getPropertyType();
                String selectorItem=propertyName+" ("+propertyAlias+")"+" - "+propertyType;
                this.targetDataPropertyField.addItem(selectorItem);
                this.targetDataTypePropertiesMap.put(selectorItem,currentSolutionTypePropertyTypeDefinitionVO);
            }
        }
    }

    private void addNewMappingDefinition(){
        if(sourceDataTypeField.getValue()==null){
            Notification errorNotification = new Notification("数据校验错误",
                    "请选择源数据类型名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(sourceDataPropertyField.getValue()==null){
            Notification errorNotification = new Notification("数据校验错误",
                    "请选择源数据复制匹配属性", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(targetDataTypeField.getValue()==null){
            Notification errorNotification = new Notification("数据校验错误",
                    "请选择目标数据类型名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(targetDataPropertyField.getValue()==null){
            Notification errorNotification = new Notification("数据校验错误",
                    "请选择目标数据复制匹配属性", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        DataMappingDefinitionVO dataMappingDefinitionVO=new DataMappingDefinitionVO();

        String sourceDataTypeOption=sourceDataType.getValue().toString();
        String sourceDataTypeLabel=sourceDataTypeField.getValue().toString();
        String sourceDataTypeName=null;
        String sourceDataType="FACT";
        if("事实".equals(sourceDataTypeOption)){
            sourceDataType="FACT";
            sourceDataTypeName=factTypeDefinitionMap.get(sourceDataTypeLabel).getTypeName();

        }
        if("维度".equals(sourceDataTypeOption)){
            sourceDataType="DIMENSION";
            sourceDataTypeName=dimensionTypeDefinitionMap.get(sourceDataTypeLabel).getTypeName();
        }
        String sourceDataTypePropertyLabel=sourceDataPropertyField.getValue().toString();
        String sourceDataPropertyName=sourceDataTypePropertiesMap.get(sourceDataTypePropertyLabel).getPropertyName();
        String sourceDataPropertyType=sourceDataTypePropertiesMap.get(sourceDataTypePropertyLabel).getPropertyType();

        dataMappingDefinitionVO.setSourceDataPropertyName(sourceDataPropertyName);
        dataMappingDefinitionVO.setSourceDataPropertyType(sourceDataPropertyType);
        dataMappingDefinitionVO.setSourceDataTypeKind(sourceDataType);
        dataMappingDefinitionVO.setSourceDataTypeName(sourceDataTypeName);

        String duplicatePropertyHandleMethodOption=duplicatePropertyHandleMethod.getValue().toString();
        String mappingNotExistHandleMethod="Ignore";
        if("忽略复制操作".equals(duplicatePropertyHandleMethodOption)){
            mappingNotExistHandleMethod="Ignore";
        }
        if("更新已存在属性".equals(duplicatePropertyHandleMethodOption)){
            mappingNotExistHandleMethod="Replace";
        }
        dataMappingDefinitionVO.setExistingPropertyHandleMethod(mappingNotExistHandleMethod);

        String targetDataTypeLabel=targetDataTypeField.getValue().toString();
        String targetDataTypeName=factTypeDefinitionMap.get(targetDataTypeLabel).getTypeName();
        String targetDataTypePropertyLabel=targetDataPropertyField.getValue().toString();
        String targetDataPropertyName=targetDataTypePropertiesMap.get(targetDataTypePropertyLabel).getPropertyName();
        String targetDataPropertyType=targetDataTypePropertiesMap.get(targetDataTypePropertyLabel).getPropertyType();

        dataMappingDefinitionVO.setTargetDataTypeName(targetDataTypeName);
        dataMappingDefinitionVO.setTargetDataPropertyName(targetDataPropertyName);
        dataMappingDefinitionVO.setTargetDataPropertyType(targetDataPropertyType);

        String confirmMessageString=" 请确认在业务解决方案 "+getBusinessSolutionName()+" 中添加数据属性复制规则";
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog addDefinitionConfirmDialog = new ConfirmDialog();
        addDefinitionConfirmDialog.setConfirmMessage(confirmMessage);

        final DataPropertiesDuplicateMappingDefinitionEditor self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addDefinitionConfirmDialog.close();
                boolean createTypePropertyResult= BusinessSolutionOperationUtil.createDataPropertiesDuplicateMappingDefinition(getBusinessSolutionName(),dataMappingDefinitionVO);;
                if(createTypePropertyResult){
                    self.containerDialog.close();
                    getRelatedDataPropertiesDuplicateMappingDefinitionEditorPanel().renderDataPropertiesDuplicateMappingDefinitionInfo(getBusinessSolutionName());
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建数据属性复制规则成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建数据属性复制规则错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addDefinitionConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addDefinitionConfirmDialog);
    }

    public DataPropertiesDuplicateMappingDefinitionEditorPanel getRelatedDataPropertiesDuplicateMappingDefinitionEditorPanel() {
        return relatedDataPropertiesDuplicateMappingDefinitionEditorPanel;
    }

    public void setRelatedDataPropertiesDuplicateMappingDefinitionEditorPanel(DataPropertiesDuplicateMappingDefinitionEditorPanel relatedDataPropertiesDuplicateMappingDefinitionEditorPanel) {
        this.relatedDataPropertiesDuplicateMappingDefinitionEditorPanel = relatedDataPropertiesDuplicateMappingDefinitionEditorPanel;
    }
}

