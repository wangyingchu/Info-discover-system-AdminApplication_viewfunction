package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.CreateTypePropertyPanelInvoker;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 5/8/17.
 */
public class CreateTypePropertyDefinitionPanel extends VerticalLayout{

    private UserClientInfo currentUserClientInfo;
    private String propertyTypeKind;
    private String businessSolutionName;
    private String typeName;
    private Window containerDialog;
    private CreateTypePropertyPanelInvoker createTypePropertyPanelInvoker;
    private SectionActionsBar dataFieldActionsBar;
    private TextField typePropertyName;
    private TextField typePropertyAliasName;
    private ComboBox dataFieldType;
    private CheckBox mandatoryFieldCheck;
    private CheckBox readOnlyFieldCheck;
    private CheckBox nullableFieldCheck;

    public CreateTypePropertyDefinitionPanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle addNewPropertySectionTitle=new MainSectionTitle("添加新的类型属性");
        addComponent(addNewPropertySectionTitle);

        dataFieldActionsBar=new SectionActionsBar(new Label("---" , ContentMode.HTML));
        addComponent(dataFieldActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        this.typePropertyName=new TextField("属性名称");
        this.typePropertyName.setRequired(true);
        form.addComponent(this.typePropertyName);

        this.typePropertyAliasName=new TextField("属性别名");
        this.typePropertyAliasName.setRequired(true);
        form.addComponent(this.typePropertyAliasName);

        dataFieldType = new ComboBox("属性数据类型");
        dataFieldType.setRequired(true);
        dataFieldType.setWidth("100%");
        dataFieldType.setTextInputAllowed(false);
        dataFieldType.setNullSelectionAllowed(false);
        dataFieldType.setInputPrompt("请选择属性数据类型");
        dataFieldType.addItem(ApplicationConstant.DataFieldType_STRING);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_BOOLEAN);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_DATE);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_INT);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_LONG);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_DOUBLE);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_FLOAT);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_SHORT);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_BYTE);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_BINARY);
        form.addComponent(dataFieldType);

        HorizontalLayout checkboxRow = new HorizontalLayout();
        checkboxRow.setMargin(true);
        checkboxRow.setSpacing(true);
        form.addComponent(checkboxRow);

        this.mandatoryFieldCheck = new CheckBox("必填属性", false);
        checkboxRow.addComponent(this.mandatoryFieldCheck);

        this.readOnlyFieldCheck = new CheckBox("只读属性", false);
        checkboxRow.addComponent(this.readOnlyFieldCheck);

        this.nullableFieldCheck = new CheckBox("允许空值", false);
        this.nullableFieldCheck.setValue(true);
        checkboxRow.addComponent(this.nullableFieldCheck);
        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("添加类型属性", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new type property logic */
                addNewTypeProperty();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public void attach() {
        super.attach();
        Label sectionActionBarLabel=null;
        if(this.getPropertyTypeKind().equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION)){
            sectionActionBarLabel=new Label(VaadinIcons.CLIPBOARD_TEXT.getHtml()+" "+getBusinessSolutionName()+" /"+FontAwesome.TAGS.getHtml()+" "+this.getTypeName() , ContentMode.HTML);
        }
        if(this.getPropertyTypeKind().equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT)){
            sectionActionBarLabel=new Label(VaadinIcons.CLIPBOARD_TEXT.getHtml()+" "+getBusinessSolutionName()+" /"+FontAwesome.CLONE.getHtml()+" "+this.getTypeName() , ContentMode.HTML);
        }
        if(this.getPropertyTypeKind().equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION)){
            sectionActionBarLabel=new Label(VaadinIcons.CLIPBOARD_TEXT.getHtml()+" "+getBusinessSolutionName()+" /"+FontAwesome.SHARE_ALT.getHtml()+" "+this.getTypeName() , ContentMode.HTML);
        }
        dataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
    }

    public String getPropertyTypeKind() {
        return propertyTypeKind;
    }

    public void setPropertyTypeKind(String propertyTypeKind) {
        this.propertyTypeKind = propertyTypeKind;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void addNewTypeProperty(){
        final String typePropertyNameStr=this.typePropertyName.getValue();
        if(typePropertyNameStr==null||typePropertyNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入类型属性名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(typePropertyNameStr);
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入类型属性名称 "+typePropertyNameStr+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(typePropertyNameStr);
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入类型属性名称 "+typePropertyNameStr+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        final String typePropertyAliasNameStr=this.typePropertyAliasName.getValue();
        if(typePropertyAliasNameStr==null||typePropertyAliasNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入类型属性别名", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        Object dataFieldTypeObj=dataFieldType.getValue();
        if(dataFieldTypeObj==null){
            Notification errorNotification = new Notification("数据校验错误",
                    "请选择属性数据类型", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isExistTypePropertyName= BusinessSolutionOperationUtil.
                checkSolutionTypePropertyDefinitionExistence(getBusinessSolutionName(), getPropertyTypeKind(), getTypeName(), typePropertyNameStr);
        if(isExistTypePropertyName){
            String checkErrorMessage=null;
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getPropertyTypeKind())){
                checkErrorMessage="维度类型定义 "+getTypeName()+" 中已经存在属性 "+typePropertyNameStr;
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getPropertyTypeKind())){
                checkErrorMessage="事实类型定义 "+getTypeName()+" 中已经存在属性 "+typePropertyNameStr;
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(getPropertyTypeKind())){
                checkErrorMessage="关系类型定义 "+getTypeName()+" 中已经存在属性 "+typePropertyNameStr;
            }
            Notification errorNotification = new Notification("数据校验错误",checkErrorMessage, Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        //do add new logic
        final String propertyTypeStr=dataFieldType.getValue().toString();
        final boolean isMandatory=mandatoryFieldCheck.getValue();
        final boolean isReadOnly=readOnlyFieldCheck.getValue();
        final boolean isNullable=nullableFieldCheck.getValue();

        String confirmMessageString=null;
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getPropertyTypeKind())){
            confirmMessageString=" 请确认在维度类型定义 "+getTypeName()+" 中添加类型属性 "+typePropertyNameStr;
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getPropertyTypeKind())){
            confirmMessageString=" 请确认在事实类型定义 "+getTypeName()+" 中添加类型属性 "+typePropertyNameStr;
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(getPropertyTypeKind())){
            confirmMessageString=" 请确认在关系类型定义 "+getTypeName()+" 中添加类型属性 "+typePropertyNameStr;
        }
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog addTypePropertyConfirmDialog = new ConfirmDialog();
        addTypePropertyConfirmDialog.setConfirmMessage(confirmMessage);

        final CreateTypePropertyDefinitionPanel self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addTypePropertyConfirmDialog.close();
                boolean createTypePropertyResult=BusinessSolutionOperationUtil.createSolutionTypePropertyDefinition(
                        getBusinessSolutionName(), getPropertyTypeKind(),  getTypeName(),typePropertyNameStr,typePropertyAliasNameStr,propertyTypeStr,isMandatory,isReadOnly,isNullable,getTypeName());
                if(createTypePropertyResult){
                    self.containerDialog.close();
                    getCreateTypePropertyPanelInvoker().createTypePropertyActionFinish(createTypePropertyResult);
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建类型属性定义成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建类型属性定义错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addTypePropertyConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addTypePropertyConfirmDialog);
    }

    public CreateTypePropertyPanelInvoker getCreateTypePropertyPanelInvoker() {
        return createTypePropertyPanelInvoker;
    }

    public void setCreateTypePropertyPanelInvoker(CreateTypePropertyPanelInvoker createTypePropertyPanelInvoker) {
        this.createTypePropertyPanelInvoker = createTypePropertyPanelInvoker;
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}
