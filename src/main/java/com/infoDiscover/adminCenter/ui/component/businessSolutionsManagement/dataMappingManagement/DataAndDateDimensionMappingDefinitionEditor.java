package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dataMappingManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.*;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
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
public class DataAndDateDimensionMappingDefinitionEditor extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private Window containerDialog;
    private DataAndDateDimensionMappingDefinitionEditPanel relatedDataAndDateDimensionMappingDefinitionEditPanel;
    private SectionActionsBar dataFieldActionsBar;
    private TextField dateDimensionPerfix;
    private OptionGroup sourceDataType;
    private ComboBox sourceDataTypeField;
    private ComboBox sourceDataPropertyField;
    private ComboBox relationTypeField;
    private OptionGroup relationDirection;
    private Map<String,FactTypeDefinitionVO> factTypeDefinitionMap;
    private Map<String,DimensionTypeDefinitionVO> dimensionTypeDefinitionMap;
    private Map<String,RelationTypeDefinitionVO> relationTypeDefinitionMap;
    private Map<String,SolutionTypePropertyTypeDefinitionVO> sourceDataTypePropertiesMap;

    public DataAndDateDimensionMappingDefinitionEditor(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle editorSectionTitle=new MainSectionTitle("创建数据与时间维度关联定义规则");
        addComponent(editorSectionTitle);

        dataFieldActionsBar=new SectionActionsBar(new Label("---" , ContentMode.HTML));
        addComponent(dataFieldActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        this.sourceDataType = new OptionGroup("关联源数据类型");
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

        this.relationTypeField=new ComboBox("关联关系类型");
        this.relationTypeField.setRequired(true);
        this.relationTypeField.setWidth("100%");
        this.relationTypeField.setTextInputAllowed(true);
        this.relationTypeField.setNullSelectionAllowed(false);
        this.relationTypeField.setInputPrompt("请选择数据属性关联关系类型");
        form.addComponent(this.relationTypeField);

        this.relationDirection = new OptionGroup("数据关联方向");
        this.relationDirection.setRequired(true);
        this.relationDirection.addItems("指向源数据", "指向目标数据");
        this.relationDirection.setValue("指向目标数据");
        this.relationDirection.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        form.addComponent(this.relationDirection);

        this.dateDimensionPerfix=new TextField("时间维度类型前缀");
        this.dateDimensionPerfix.setRequired(true);
        form.addComponent(this.dateDimensionPerfix);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("创建数据关联规则", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addNewMappingDefinition();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    @Override
    public void attach() {
        super.attach();
        Label sectionActionBarLabel=new Label(VaadinIcons.CLIPBOARD_TEXT.getHtml()+" "+getBusinessSolutionName(), ContentMode.HTML);
        dataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        initRelationSelectData(BusinessSolutionOperationUtil.getRelationTypeDefinitionList(getBusinessSolutionName()));
        initFactSelectData(BusinessSolutionOperationUtil.getFactTypeDefinitionList(getBusinessSolutionName()));
        initDimensionSelectData(BusinessSolutionOperationUtil.getDimensionTypeDefinitionList(getBusinessSolutionName()));
        setSourceDataTypeChooseList("事实");
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

    public DataAndDateDimensionMappingDefinitionEditPanel getRelatedDataAndDateDimensionMappingDefinitionEditPanel() {
        return relatedDataAndDateDimensionMappingDefinitionEditPanel;
    }

    public void setRelatedDataAndDateDimensionMappingDefinitionEditPanel(DataAndDateDimensionMappingDefinitionEditPanel relatedDataAndDateDimensionMappingDefinitionEditPanel) {
        this.relatedDataAndDateDimensionMappingDefinitionEditPanel = relatedDataAndDateDimensionMappingDefinitionEditPanel;
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

    private void initRelationSelectData(List<RelationTypeDefinitionVO> relationTypeDefinitionList){
        if(this.relationTypeField!=null){
            this.relationTypeField.removeAllItems();
        }
        if(this.relationTypeDefinitionMap==null){
            this.relationTypeDefinitionMap=new HashMap<>();
        }else{
            this.relationTypeDefinitionMap.clear();
        }
        for(RelationTypeDefinitionVO currentRelationTypeDefinitionVO:relationTypeDefinitionList){
            String typeName=currentRelationTypeDefinitionVO.getTypeName();
            String typeAlias=currentRelationTypeDefinitionVO.getTypeAliasName();
            String selectorItem=typeName+" ("+typeAlias+")";
            this.relationTypeField.addItem(selectorItem);
            this.relationTypeDefinitionMap.put(selectorItem,currentRelationTypeDefinitionVO);
        }
    }

    private void setSourceDataTypeChooseList(String dataType){
        this.sourceDataTypeField.removeAllItems();
        this.sourceDataPropertyField.removeAllItems();
        Iterator<String> iterator=null;
        if("事实".equals(dataType)){
            this.sourceDataTypeField.setCaption("源事实类型名称");
            this.sourceDataTypeField.setInputPrompt("请选择源事实类型名称");
            this.sourceDataPropertyField.setCaption("源事实属性名称");
            this.sourceDataPropertyField.setInputPrompt("请选择源事实属性名称");
            if(this.factTypeDefinitionMap!=null){
                Set<String> labelSet=this.factTypeDefinitionMap.keySet();
                iterator=labelSet.iterator();
            }
        }
        if("维度".equals(dataType)){
            this.sourceDataTypeField.setCaption("源维度类型名称");
            this.sourceDataTypeField.setInputPrompt("请选择源维度类型名称");
            this.sourceDataPropertyField.setCaption("源维度属性名称");
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
                if(ApplicationConstant.DataFieldType_DATE.equals(currentSolutionTypePropertyTypeDefinitionVO.getPropertyType())){
                    String propertyName=currentSolutionTypePropertyTypeDefinitionVO.getPropertyName();
                    String propertyAlias=currentSolutionTypePropertyTypeDefinitionVO.getPropertyAliasName();
                    String propertyType=currentSolutionTypePropertyTypeDefinitionVO.getPropertyType();
                    String selectorItem=propertyName+" ("+propertyAlias+")"+" - "+propertyType;
                    this.sourceDataPropertyField.addItem(selectorItem);
                    this.sourceDataTypePropertiesMap.put(selectorItem,currentSolutionTypePropertyTypeDefinitionVO);
                }
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
                    "请选择源数据属性名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(relationTypeField.getValue()==null){
            Notification errorNotification = new Notification("数据校验错误",
                    "请选择关联关系类型", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        if(dateDimensionPerfix.getValue()==null||dateDimensionPerfix.getValue().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入时间维度类型前缀", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(dateDimensionPerfix.getValue());
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入时间维度类型前缀 "+dateDimensionPerfix.getValue()+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(dateDimensionPerfix.getValue());
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入时间维度类型前缀 "+dateDimensionPerfix.getValue()+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
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

        String relationTypeLabel=relationTypeField.getValue().toString();
        String relationTypeName=relationTypeDefinitionMap.get(relationTypeLabel).getTypeName();
        String relationDirectionOption=relationDirection.getValue().toString();
        String relationDirection="ToTarget";
        if("指向源数据".equals(relationDirectionOption)){
            relationDirection="ToSource";
        }
        if("指向目标数据".equals(relationDirectionOption)){
            relationDirection="ToTarget";
        }

        dataMappingDefinitionVO.setRelationDirection(relationDirection);
        dataMappingDefinitionVO.setRelationTypeName(relationTypeName);
        dataMappingDefinitionVO.setDateDimensionTypePrefix(dateDimensionPerfix.getValue());

        String confirmMessageString=" 请确认在业务解决方案 "+getBusinessSolutionName()+" 中添加数据与时间维度关联定义规则";
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog addDefinitionConfirmDialog = new ConfirmDialog();
        addDefinitionConfirmDialog.setConfirmMessage(confirmMessage);

        final DataAndDateDimensionMappingDefinitionEditor self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addDefinitionConfirmDialog.close();
                boolean createTypePropertyResult= BusinessSolutionOperationUtil.createDataDateDimensionMappingDefinition(getBusinessSolutionName(),dataMappingDefinitionVO);;
                if(createTypePropertyResult){
                    self.containerDialog.close();
                    getRelatedDataAndDateDimensionMappingDefinitionEditPanel().renderDataAndDateDimensionMappingDefinitionInfo(getBusinessSolutionName());
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建数据与时间维度关联定义规则成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建数据与时间维度关联定义规则错误",
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
}