package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.*;
import com.vaadin.data.Property;
import com.vaadin.data.validator.*;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;


/**
 * Created by wangychu on 10/27/16.
 */
public class QueryConditionItem extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Label propertyNameLabel;
    private PropertyTypeVO propertyTypeVO;
    private String dataInstanceTypeName;
    private boolean reverseCondition=false;
    private boolean isFirstQueryCondition=false;
    private String filteringLogic_AND="AND";
    private String filteringLogic_OR="OR";
    private String filteringLogic;
    private Button filteringLogicNotButton;
    private Button filteringLogicOrButton;
    private Button filteringLogicAndButton;
    private ComboBox filteringItemTypeSelection;
    private  HorizontalLayout conditionValueInputElementsLayout;

    private final String FilteringItemType_Equal="Equal";
    private final String FilteringItemType_NotEqual="Not Equal";
    private final String FilteringItemType_SimilarTo="Similar To";
    private final String FilteringItemType_RegularMatch="Regular Match";
    private final String FilteringItemType_Between= "Between";
    private final String FilteringItemType_GreatThan="Great Than";
    private final String FilteringItemType_GreatThanEqual="Great Than Equal";
    private final String FilteringItemType_LessThan="Less Than";
    private final String FilteringItemType_LessThanEqual="Less Than Equal";
    private final String FilteringItemType_InValue="In Value";
    private final String FilteringItemType_NullValue="Null Value";

    private final String SimilarToMatchingType_BeginWith="Begin With";
    private final String SimilarToMatchingType_EndWith="End With";
    private final String SimilarToMatchingType_Contain="Contain";

    private String currentSelectedFilteringItemType;
    private Field singleQueryValueTextField;
    private MultiValuePropertyInput multiValuePropertyInput;
    private Field betweenQueryFromValueTextField;
    private Field betweenQueryToValueTextField;
    private ComboBox similarToMatchingTypeSelector;
    private TextField similarToConditionValueTextField;

    public QueryConditionItem(UserClientInfo userClientInfo,PropertyTypeVO propertyTypeVO) {
        this.currentUserClientInfo = userClientInfo;
        this.propertyTypeVO=propertyTypeVO;
        this.reverseCondition=false;
        this.filteringLogic=filteringLogic_OR;
        setSpacing(true);
        setMargin(true);
        this.addStyleName("ui_appSection_Top_LightDiv");

        Panel propertyInfoElementContainerPanel=new Panel();
        propertyInfoElementContainerPanel.setWidth(380,Unit.PIXELS);
        propertyInfoElementContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        this.addComponent(propertyInfoElementContainerPanel);

        HorizontalLayout queryPropertyAndGroupIngInfoContainerLayout=new HorizontalLayout();
        propertyInfoElementContainerPanel.setContent(queryPropertyAndGroupIngInfoContainerLayout);

        this.propertyNameLabel=new Label(FontAwesome.CIRCLE_O.getHtml()+" "+"-", ContentMode.HTML);
        propertyNameLabel.addStyleName(ValoTheme.LABEL_BOLD);
        queryPropertyAndGroupIngInfoContainerLayout.addComponent(propertyNameLabel);

        HorizontalLayout propertyConditionControllerContainerLayout=new HorizontalLayout();
        queryPropertyAndGroupIngInfoContainerLayout.addComponent(propertyConditionControllerContainerLayout);
        queryPropertyAndGroupIngInfoContainerLayout.setComponentAlignment(propertyConditionControllerContainerLayout, Alignment.MIDDLE_RIGHT);
        queryPropertyAndGroupIngInfoContainerLayout.setExpandRatio(propertyNameLabel,1.0f);

        this.filteringLogicOrButton=new Button("OR");
        this.filteringLogicOrButton.setIcon(VaadinIcons.PLUS);
        this.filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        this.filteringLogicOrButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setFilteringLogic(filteringLogic_OR);
            }
        });
        propertyConditionControllerContainerLayout.addComponent(this.filteringLogicOrButton);

        this.filteringLogicAndButton=new Button("AND");
        this.filteringLogicAndButton.setIcon(VaadinIcons.CLOSE);
        this.filteringLogicAndButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.filteringLogicAndButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.filteringLogicAndButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setFilteringLogic(filteringLogic_AND);
            }
        });
        propertyConditionControllerContainerLayout.addComponent(this.filteringLogicAndButton);

        HorizontalLayout spacingDivLayout0=new HorizontalLayout();
        spacingDivLayout0.setWidth(10,Unit.PIXELS);
        propertyConditionControllerContainerLayout.addComponent(spacingDivLayout0);

        this.filteringLogicNotButton=new Button("NOT");
        this.filteringLogicNotButton.setIcon(FontAwesome.BAN);
        this.filteringLogicNotButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.filteringLogicNotButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.filteringLogicNotButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setReverseConditionLogic();
            }
        });
        propertyConditionControllerContainerLayout.addComponent(this.filteringLogicNotButton);

        HorizontalLayout conditionValueInfoContainerLayout = new HorizontalLayout();
        Label inputIconLabel=new Label(VaadinIcons.INPUT.getHtml(), ContentMode.HTML);
        conditionValueInfoContainerLayout.addComponent(inputIconLabel);

        this.filteringItemTypeSelection = new ComboBox();
        this.filteringItemTypeSelection.setTextInputAllowed(false);
        this.filteringItemTypeSelection.setPageLength(11);
        this.filteringItemTypeSelection.addStyleName(ValoTheme.COMBOBOX_BORDERLESS);
        this.filteringItemTypeSelection.addStyleName(ValoTheme.COMBOBOX_SMALL);
        this.filteringItemTypeSelection.setRequired(true);
        this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
        this.filteringItemTypeSelection.setTextInputAllowed(false);
        this.filteringItemTypeSelection.setNullSelectionAllowed(false);
        this.filteringItemTypeSelection.setInputPrompt("约束条件");
        this.filteringItemTypeSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                String filteringItemType=valueChangeEvent.getProperty().getValue().toString();
                renderFilteringItemInputElements(filteringItemType);
            }
        });

        conditionValueInfoContainerLayout.addComponent(this.filteringItemTypeSelection);
        this.addComponent(conditionValueInfoContainerLayout);

        this.conditionValueInputElementsLayout = new HorizontalLayout();
        conditionValueInfoContainerLayout.addComponent(this.conditionValueInputElementsLayout);

        setIsFirstQueryConditionControl(this.isFirstQueryCondition);
    }
    @Override
    public void attach() {
        super.attach();
        //String propertyName="propertyNameprpropertyNameprpropertyNameprpropertyNamepr1234567890";
        //String propertyName="propertyNameprp2345tdfghj56789";
        String propertyName="-";
        if(this.getPropertyTypeVO() !=null){
            String propertyDataType= this.getPropertyTypeVO().getPropertyType();

            switch(propertyDataType) {
                case ApplicationConstant.DataFieldType_STRING:
                    propertyName="["+ApplicationConstant.DataFieldType_STRING+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    propertyName="["+ApplicationConstant.DataFieldType_BOOLEAN+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    propertyName= "["+ApplicationConstant.DataFieldType_DATE+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    propertyName="["+ApplicationConstant.DataFieldType_INT+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    propertyName="["+ ApplicationConstant.DataFieldType_LONG+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    propertyName="["+ApplicationConstant.DataFieldType_DOUBLE+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    propertyName="["+ApplicationConstant.DataFieldType_FLOAT+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    propertyName="["+ApplicationConstant.DataFieldType_SHORT+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    propertyName="["+ApplicationConstant.DataFieldType_BYTE+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    propertyName="["+ApplicationConstant.DataFieldType_BINARY+"] "+ this.getPropertyTypeVO().getPropertyName();
                    break;
            }
            if(this.getDataInstanceTypeName()!=null){
                if(this.getDataInstanceTypeName().equals(this.getPropertyTypeVO().getPropertySourceOwner())){
                    this.propertyNameLabel.setValue(FontAwesome.CIRCLE_O.getHtml()+" "+propertyName);
                }else{
                    this.propertyNameLabel.setValue(FontAwesome.REPLY_ALL.getHtml()+" "+propertyName);
                }
            }else{
                this.propertyNameLabel.setValue(" "+propertyName);
            }
        }else{
            this.propertyNameLabel.setValue(" "+propertyName);
        }
        if(propertyName.length()<=30){
            propertyNameLabel.setWidth(280, Unit.PIXELS);
        }
        setQueryConditionSelectionByDataType();
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    private void setQueryConditionSelectionByDataType(){
        if(this.getPropertyTypeVO() !=null) {
            String propertyDataType = this.getPropertyTypeVO().getPropertyType();
            switch (propertyDataType) {
                case ApplicationConstant.DataFieldType_STRING:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_SimilarTo);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_RegularMatch);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    break;
            }
        }
    }

    private void setReverseConditionLogic(){
        this.reverseCondition=!this.reverseCondition;
        if(this.reverseCondition){
            this.filteringLogicNotButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        }else{
            this.filteringLogicNotButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        }
    }

    private void setFilteringLogic(String filteringLogicValue){
        this.filteringLogic=filteringLogicValue;
        this.filteringLogicOrButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        this.filteringLogicAndButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        if(this.filteringLogic.equals(filteringLogic_OR)){
            this.filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        }
        if(this.filteringLogic.equals(filteringLogic_AND)){
            this.filteringLogicAndButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        }
    }

    private void setIsFirstQueryConditionControl(boolean isFirstQueryCondition){
        this.filteringLogicOrButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        this.filteringLogicAndButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        if(isFirstQueryCondition){
            this.filteringLogicOrButton.setEnabled(false);
            this.filteringLogicAndButton.setEnabled(false);
        }else{
            this.filteringLogicOrButton.setEnabled(true);
            this.filteringLogicAndButton.setEnabled(true);
            setFilteringLogic(this.filteringLogic);
        }
    }

    public void setIsFirstQueryCondition(boolean isFirstQueryCondition) {
        this.isFirstQueryCondition = isFirstQueryCondition;
        setIsFirstQueryConditionControl(this.isFirstQueryCondition);
    }

    private void renderFilteringItemInputElements(String filteringItemType){
        this.currentSelectedFilteringItemType=filteringItemType;
        this.conditionValueInputElementsLayout.removeAllComponents();
        if(this.singleQueryValueTextField!=null){
            this.singleQueryValueTextField.discard();
            this.singleQueryValueTextField=null;
        }
        this.multiValuePropertyInput=null;
        if(this.betweenQueryFromValueTextField!=null){
            this.betweenQueryFromValueTextField.discard();
            this.betweenQueryFromValueTextField=null;
        }
        if(this.similarToConditionValueTextField !=null){
            this.similarToConditionValueTextField.discard();
            this.similarToConditionValueTextField =null;
        }
        if(this.similarToMatchingTypeSelector !=null){
            this.similarToMatchingTypeSelector.discard();
            this.similarToMatchingTypeSelector =null;
        }
        switch(filteringItemType){
            case FilteringItemType_Equal:
                this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField= generateSingleQueryValueTextField(190);
                this.conditionValueInputElementsLayout.addComponent(this.singleQueryValueTextField);
                break;
            case FilteringItemType_NotEqual:
                this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField= generateSingleQueryValueTextField(190);
                this.conditionValueInputElementsLayout.addComponent(this.singleQueryValueTextField);
                break;
            case FilteringItemType_RegularMatch:
                this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField= generateSingleQueryValueTextField(190);
                this.conditionValueInputElementsLayout.addComponent(this.singleQueryValueTextField);
                break;
            case FilteringItemType_GreatThan:
                this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField= generateSingleQueryValueTextField(190);
                this.conditionValueInputElementsLayout.addComponent(this.singleQueryValueTextField);
                break;
            case FilteringItemType_GreatThanEqual:
                this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField= generateSingleQueryValueTextField(190);
                this.conditionValueInputElementsLayout.addComponent(this.singleQueryValueTextField);
                break;
            case FilteringItemType_LessThan:
                this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField= generateSingleQueryValueTextField(190);
                this.conditionValueInputElementsLayout.addComponent(this.singleQueryValueTextField);
                break;
            case FilteringItemType_LessThanEqual:
                this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField= generateSingleQueryValueTextField(190);
                this.conditionValueInputElementsLayout.addComponent(this.singleQueryValueTextField);
                break;
            case FilteringItemType_SimilarTo:
                this.filteringItemTypeSelection.setWidth(105,Unit.PIXELS);
                HorizontalLayout fieldLayout=generateSimilarToQueryValueInputElements();
                this.conditionValueInputElementsLayout.addComponent(fieldLayout);
                break;
            case FilteringItemType_Between:
                this.filteringItemTypeSelection.setWidth(95,Unit.PIXELS);
                HorizontalLayout betweenFieldLayout=generateBetweenQueryValueInputElements();
                this.conditionValueInputElementsLayout.addComponent(betweenFieldLayout);
                break;
            case FilteringItemType_InValue:
                this.filteringItemTypeSelection.setWidth(95,Unit.PIXELS);
                this.multiValuePropertyInput=generateInValueQueryValueInputElements();
                this.conditionValueInputElementsLayout.addComponent(this.multiValuePropertyInput);
                break;
            case FilteringItemType_NullValue:
                this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                break;
        }
    }

    private HorizontalLayout generateSimilarToQueryValueInputElements(){
        HorizontalLayout containerHorizontalLayout=new HorizontalLayout();
        this.similarToMatchingTypeSelector = new ComboBox();
        this.similarToMatchingTypeSelector.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        this.similarToMatchingTypeSelector.setWidth(48,Unit.PIXELS);
        this.similarToMatchingTypeSelector.setTextInputAllowed(false);
        this.similarToMatchingTypeSelector.setNullSelectionAllowed(false);
        this.similarToMatchingTypeSelector.addItem(SimilarToMatchingType_BeginWith);
        this.similarToMatchingTypeSelector.addItem(SimilarToMatchingType_EndWith);
        this.similarToMatchingTypeSelector.addItem(SimilarToMatchingType_Contain);
        this.similarToMatchingTypeSelector.setValue(SimilarToMatchingType_BeginWith);
        containerHorizontalLayout.addComponent(this.similarToMatchingTypeSelector);
        this.similarToConditionValueTextField = new TextField();
        this.similarToConditionValueTextField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        this.similarToConditionValueTextField.setWidth(192,Unit.PIXELS);
        containerHorizontalLayout.addComponent(this.similarToConditionValueTextField);
        return containerHorizontalLayout;
    }

    private HorizontalLayout generateBetweenQueryValueInputElements(){
        HorizontalLayout containerHorizontalLayout=new HorizontalLayout();
        this.betweenQueryFromValueTextField=generateSingleQueryValueTextField(122);
        containerHorizontalLayout.addComponent(this.betweenQueryFromValueTextField);
        Label divLabel=new Label("-");
        containerHorizontalLayout.addComponent(divLabel);
        this.betweenQueryToValueTextField=generateSingleQueryValueTextField(123);
        containerHorizontalLayout.addComponent(this.betweenQueryToValueTextField);
        return containerHorizontalLayout;
    }

    private MultiValuePropertyInput generateInValueQueryValueInputElements(){
        MultiValuePropertyInput multiValuePropertyInput =new MultiValuePropertyInput(this.currentUserClientInfo,215);
        multiValuePropertyInput.setQueryConditionItem(this);
        return multiValuePropertyInput;
    }

    public Field generateSingleQueryValueTextField(int textFieldWidth) {
        if (this.getPropertyTypeVO() != null) {
            String propertyDataType = this.getPropertyTypeVO().getPropertyType();
            Field currentConditionValueEditor = null;
            switch (propertyDataType) {
                case ApplicationConstant.DataFieldType_STRING:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    currentConditionValueEditor = new ComboBox();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    ((ComboBox) currentConditionValueEditor).setTextInputAllowed(false);
                    ((ComboBox) currentConditionValueEditor).setNullSelectionAllowed(false);
                    ((ComboBox) currentConditionValueEditor).addItem("true");
                    ((ComboBox) currentConditionValueEditor).addItem("false");
                    ((ComboBox) currentConditionValueEditor).setValue("true");
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    currentConditionValueEditor = new PopupDateField();
                    currentConditionValueEditor.addStyleName(ValoTheme.DATEFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    ((DateField) currentConditionValueEditor).setDateFormat("yyyy-MM-dd hh:mm:ss");
                    ((DateField) currentConditionValueEditor).setResolution(Resolution.SECOND);
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Integer.class);
                    currentConditionValueEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0");
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Long.class);
                    currentConditionValueEditor.addValidator(new LongRangeValidator("该项属性值必须为LONG类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0");
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Double.class);
                    currentConditionValueEditor.addValidator(new DoubleRangeValidator("该项属性值必须为DOUBLE类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0.0");
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Float.class);
                    currentConditionValueEditor.addValidator(new FloatRangeValidator("该项属性值必须为FLOAT类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0.0");
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Short.class);
                    currentConditionValueEditor.addValidator(new ShortRangeValidator("该项属性值必须为SHORT类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0");
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    break;
            }
            return currentConditionValueEditor;
        }
        return null;
    }

    public PropertyTypeVO getPropertyTypeVO() {
        return propertyTypeVO;
    }

    public FilteringItem getFilteringItem(){
        FilteringItem targetFilteringItem=null;
        if(this.currentSelectedFilteringItemType==null){
            /*
            Notification errorNotification = new Notification("数据校验错误",
                    "查询属性 "+this.propertyTypeVO.getPropertyName()+" 未设定约束条件", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            */
            return null;
        }
        switch(this.currentSelectedFilteringItemType){
            case FilteringItemType_Equal:
                if(this.singleQueryValueTextField.isValid()&&this.singleQueryValueTextField.getValue()!=null){
                    targetFilteringItem=new EqualFilteringItem(this.getPropertyTypeVO().getPropertyName(),getConditionValue(this.singleQueryValueTextField));
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_NotEqual:
                if(this.singleQueryValueTextField.isValid()&&this.singleQueryValueTextField.getValue()!=null){
                    targetFilteringItem=new NotEqualFilteringItem(this.getPropertyTypeVO().getPropertyName(),getConditionValue(this.singleQueryValueTextField));
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_RegularMatch:
                if(this.singleQueryValueTextField.isValid()&&this.singleQueryValueTextField.getValue()!=null){
                    targetFilteringItem=new RegularMatchFilteringItem(this.getPropertyTypeVO().getPropertyName(),getConditionValue(this.singleQueryValueTextField).toString());
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_GreatThan:
                if(this.singleQueryValueTextField.isValid()&&this.singleQueryValueTextField.getValue()!=null){
                    targetFilteringItem=new GreaterThanFilteringItem(this.getPropertyTypeVO().getPropertyName(),getConditionValue(this.singleQueryValueTextField));
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_GreatThanEqual:
                if(this.singleQueryValueTextField.isValid()&&this.singleQueryValueTextField.getValue()!=null){
                    targetFilteringItem=new GreaterThanEqualFilteringItem(this.getPropertyTypeVO().getPropertyName(),getConditionValue(this.singleQueryValueTextField));
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_LessThan:
                if(this.singleQueryValueTextField.isValid()&&this.singleQueryValueTextField.getValue()!=null){
                    targetFilteringItem=new LessThanFilteringItem(this.getPropertyTypeVO().getPropertyName(),getConditionValue(this.singleQueryValueTextField));
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_LessThanEqual:
                if(this.singleQueryValueTextField.isValid()&&this.singleQueryValueTextField.getValue()!=null){
                    targetFilteringItem=new LessThanEqualFilteringItem(this.getPropertyTypeVO().getPropertyName(),getConditionValue(this.singleQueryValueTextField));
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_SimilarTo:
                SimilarFilteringItem.MatchingType matchingType=null;
                String matchingTypeValue=this.similarToMatchingTypeSelector.getValue().toString();
                if(matchingTypeValue.equals(SimilarToMatchingType_BeginWith)){
                    matchingType= SimilarFilteringItem.MatchingType.BeginWith;
                }
                if(matchingTypeValue.equals(SimilarToMatchingType_EndWith)){
                    matchingType= SimilarFilteringItem.MatchingType.EndWith;
                }
                if(matchingTypeValue.equals(SimilarToMatchingType_Contain)){
                    matchingType= SimilarFilteringItem.MatchingType.Contain;
                }
                if(this.similarToConditionValueTextField.isValid()&&this.similarToConditionValueTextField.getValue()!=null){
                    targetFilteringItem=new SimilarFilteringItem(this.getPropertyTypeVO().getPropertyName(), this.similarToConditionValueTextField.getValue().toString(),matchingType);
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_Between:
                if(this.betweenQueryFromValueTextField.isValid()&&this.betweenQueryFromValueTextField.getValue()!=null&&this.betweenQueryToValueTextField.isValid()&&this.betweenQueryToValueTextField.getValue()!=null){
                    targetFilteringItem=new BetweenFilteringItem(this.getPropertyTypeVO().getPropertyName(),getConditionValue(this.betweenQueryFromValueTextField),getConditionValue(this.betweenQueryToValueTextField));
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_InValue:
                if(this.multiValuePropertyInput.getMultiValueList()!=null&&this.multiValuePropertyInput.getMultiValueList().size()>0){
                    targetFilteringItem=new InValueFilteringItem(this.getPropertyTypeVO().getPropertyName(),this.multiValuePropertyInput.getMultiValueList());
                    break;
                }else{
                    return null;
                }
            case FilteringItemType_NullValue:
                targetFilteringItem=new NullValueFilteringItem(this.getPropertyTypeVO().getPropertyName());
                break;
        }
        if(this.reverseCondition){
            targetFilteringItem.reverseCondition();
        }
        return targetFilteringItem;
    }

    public ExploreParameters.FilteringLogic getFilteringLogic(){
        if(this.filteringLogic.equals(filteringLogic_OR)){
           return ExploreParameters.FilteringLogic.OR;
        }
        if(this.filteringLogic.equals(filteringLogic_AND)){
            return ExploreParameters.FilteringLogic.AND;
        }
        return null;
    }

    private Object getConditionValue(Field currentValueInputField){
        String propertyDataType = this.getPropertyTypeVO().getPropertyType();
        Object propertyValueObj=null;
        switch(propertyDataType){
            case ApplicationConstant.DataFieldType_STRING:
                propertyValueObj=((TextField)currentValueInputField).getValue();
                break;
            case ApplicationConstant.DataFieldType_BOOLEAN:
                propertyValueObj=((ComboBox)currentValueInputField).getValue();
                break;
            case ApplicationConstant.DataFieldType_DATE:
                propertyValueObj=((PopupDateField)currentValueInputField).getValue();
                break;
            case ApplicationConstant.DataFieldType_INT:
                propertyValueObj=((TextField)currentValueInputField).getConvertedValue();
                break;
            case ApplicationConstant.DataFieldType_LONG:
                propertyValueObj=((TextField)currentValueInputField).getConvertedValue();
                break;
            case ApplicationConstant.DataFieldType_DOUBLE:
                propertyValueObj=((TextField)currentValueInputField).getConvertedValue();
                break;
            case ApplicationConstant.DataFieldType_FLOAT:
                propertyValueObj=((TextField)currentValueInputField).getConvertedValue();
                break;
            case ApplicationConstant.DataFieldType_SHORT:
                propertyValueObj=((TextField)currentValueInputField).getConvertedValue();
                break;
                /*
                case ApplicationConstant.DataFieldType_BYTE:
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    break;
                */
        }
        return propertyValueObj;
    }
}
