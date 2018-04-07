package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.businessDataDefinitionManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.*;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.TypeSummaryVO;
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
public class DataAndDateDimensionMappingEditor extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Window containerDialog;
    private DataAndDateDimensionMappingManagementPanel relatedDataAndDateDimensionMappingManagementPanel;
    private SectionActionsBar dataFieldActionsBar;
    private TextField dateDimensionPerfix;
    private OptionGroup sourceDataType;
    private ComboBox sourceDataTypeField;
    private ComboBox sourceDataPropertyField;
    private ComboBox relationTypeField;
    private OptionGroup relationDirection;
    private Map<String,PropertyTypeVO> sourceDataTypePropertiesMap;
    private Map<String,TypeSummaryVO> factTypeMap;
    private Map<String,TypeSummaryVO> dimensionTypeMap;
    private Map<String,TypeSummaryVO> relationTypeMap;

    public DataAndDateDimensionMappingEditor(UserClientInfo currentUserClientInfo){
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
        Label sectionActionBarLabel=new Label(VaadinIcons.CLIPBOARD_TEXT.getHtml()+" "+getDiscoverSpaceName(), ContentMode.HTML);
        dataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        initRelationSelectData(InfoDiscoverSpaceOperationUtil.getTypeDataSummary(getDiscoverSpaceName(),InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION));
        initFactSelectData(InfoDiscoverSpaceOperationUtil.getTypeDataSummary(getDiscoverSpaceName(),InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT));
        initDimensionSelectData(InfoDiscoverSpaceOperationUtil.getTypeDataSummary(getDiscoverSpaceName(),InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION));
        setSourceDataTypeChooseList("事实");
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void initFactSelectData(List<TypeSummaryVO> factTypeList){
        if(this.factTypeMap==null){
            this.factTypeMap=new HashMap<>();
        }else{
            this.factTypeMap.clear();
        }
        for(TypeSummaryVO currentFactTypeVO:factTypeList){
            String typeName=currentFactTypeVO.getTypeName();
            String typeAlias=currentFactTypeVO.getTypeAliasName();
            String selectorItem=typeName+" ("+typeAlias+")";
            this.factTypeMap.put(selectorItem,currentFactTypeVO);
        }
    }

    private void initDimensionSelectData(List<TypeSummaryVO> dimensionTypeList){
        if(this.dimensionTypeMap==null){
            this.dimensionTypeMap=new HashMap<>();
        }else{
            this.dimensionTypeMap.clear();
        }
        for(TypeSummaryVO currentDimensionTypeVO:dimensionTypeList){
            String typeName=currentDimensionTypeVO.getTypeName();
            String typeAlias=currentDimensionTypeVO.getTypeAliasName();
            String selectorItem=typeName+" ("+typeAlias+")";
            this.dimensionTypeMap.put(selectorItem,currentDimensionTypeVO);
        }
    }

    private void initRelationSelectData(List<TypeSummaryVO> relationTypeSummaryList){
        if(this.relationTypeField!=null){
            this.relationTypeField.removeAllItems();
        }
        if(this.relationTypeMap==null){
            this.relationTypeMap=new HashMap<>();
        }else{
            this.relationTypeMap.clear();
        }
        for(TypeSummaryVO currentRelationTypeVO:relationTypeSummaryList){
            String typeName=currentRelationTypeVO.getTypeName();
            String typeAlias=currentRelationTypeVO.getTypeAliasName();
            String selectorItem=typeName+" ("+typeAlias+")";
            this.relationTypeField.addItem(selectorItem);
            this.relationTypeMap.put(selectorItem,currentRelationTypeVO);
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
            if(this.factTypeMap!=null){
                Set<String> labelSet=this.factTypeMap.keySet();
                iterator=labelSet.iterator();
            }
        }
        if("维度".equals(dataType)){
            this.sourceDataTypeField.setCaption("源维度类型名称");
            this.sourceDataTypeField.setInputPrompt("请选择源维度类型名称");
            this.sourceDataPropertyField.setCaption("源维度属性名称");
            this.sourceDataPropertyField.setInputPrompt("请选择源维度属性名称");
            if(this.dimensionTypeMap!=null) {
                Set<String> labelSet = this.dimensionTypeMap.keySet();
                iterator = labelSet.iterator();
            }
        }
        while(iterator.hasNext()){
            this.sourceDataTypeField.addItem(iterator.next());
        }
    }

    private void setSourceDataTypePropertySelectChooseList(String propertyTypeLabel){
        this.sourceDataPropertyField.removeAllItems();
        String propertyTypeName;
        List<PropertyTypeVO> propertyTypeVOList=null;
        if("事实".equals(this.sourceDataType.getValue().toString())){
            propertyTypeName=this.factTypeMap.get(propertyTypeLabel).getTypeName();
            propertyTypeVOList=InfoDiscoverSpaceOperationUtil.retrieveFactTypePropertiesInfo(discoverSpaceName,propertyTypeName);
        }
        if("维度".equals(this.sourceDataType.getValue().toString())){
            propertyTypeName=this.dimensionTypeMap.get(propertyTypeLabel).getTypeName();
            propertyTypeVOList=InfoDiscoverSpaceOperationUtil.retrieveDimensionTypePropertiesInfo(discoverSpaceName,propertyTypeName);
        }
        if(this.sourceDataTypePropertiesMap==null){
            this.sourceDataTypePropertiesMap=new HashMap<>();
        }else{
            this.sourceDataTypePropertiesMap.clear();
        }
        if(propertyTypeVOList!=null){
            for(PropertyTypeVO currentPropertyTypeVO:propertyTypeVOList){
                if(ApplicationConstant.DataFieldType_DATE.equals(currentPropertyTypeVO.getPropertyType())) {
                    String propertyName = currentPropertyTypeVO.getPropertyName();
                    String propertyAlias = currentPropertyTypeVO.getPropertyAliasName();
                    String propertyType = currentPropertyTypeVO.getPropertyType();
                    String selectorItem = propertyName + " (" + propertyAlias + ")" + " - " + propertyType;
                    this.sourceDataPropertyField.addItem(selectorItem);
                    this.sourceDataTypePropertiesMap.put(selectorItem, currentPropertyTypeVO);
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
            sourceDataTypeName=factTypeMap.get(sourceDataTypeLabel).getTypeName();
        }
        if("维度".equals(sourceDataTypeOption)){
            sourceDataType="DIMENSION";
            sourceDataTypeName=dimensionTypeMap.get(sourceDataTypeLabel).getTypeName();
        }
        String sourceDataTypePropertyLabel=sourceDataPropertyField.getValue().toString();
        String sourceDataPropertyName=sourceDataTypePropertiesMap.get(sourceDataTypePropertyLabel).getPropertyName();
        String sourceDataPropertyType=sourceDataTypePropertiesMap.get(sourceDataTypePropertyLabel).getPropertyType();

        dataMappingDefinitionVO.setSourceDataPropertyName(sourceDataPropertyName);
        dataMappingDefinitionVO.setSourceDataPropertyType(sourceDataPropertyType);
        dataMappingDefinitionVO.setSourceDataTypeKind(sourceDataType);
        dataMappingDefinitionVO.setSourceDataTypeName(sourceDataTypeName);

        String relationTypeLabel=relationTypeField.getValue().toString();
        String relationTypeName=relationTypeMap.get(relationTypeLabel).getTypeName();
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

        String confirmMessageString=" 请确认在信息发现空间 "+getDiscoverSpaceName()+" 中添加数据与时间维度关联定义规则";
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog addDefinitionConfirmDialog = new ConfirmDialog();
        addDefinitionConfirmDialog.setConfirmMessage(confirmMessage);

        final DataAndDateDimensionMappingEditor self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addDefinitionConfirmDialog.close();
                boolean createTypePropertyResult= InfoDiscoverSpaceOperationUtil.createDataDateDimensionMappingDefinition(getDiscoverSpaceName(),dataMappingDefinitionVO);
                if(createTypePropertyResult){
                    self.containerDialog.close();
                    getRelatedDataAndDateDimensionMappingManagementPanel().renderDataAndDateDimensionMappingInfo();
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

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public DataAndDateDimensionMappingManagementPanel getRelatedDataAndDateDimensionMappingManagementPanel() {
        return relatedDataAndDateDimensionMappingManagementPanel;
    }

    public void setRelatedDataAndDateDimensionMappingManagementPanel(DataAndDateDimensionMappingManagementPanel relatedDataAndDateDimensionMappingManagementPanel) {
        this.relatedDataAndDateDimensionMappingManagementPanel = relatedDataAndDateDimensionMappingManagementPanel;
    }
}