package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.businessDataDefinitionManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 25/05/2017.
 */
public class CreateCustomPropertyAliasNamePanel  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Window containerDialog;
    private CustomPropertyAliasNameManagementPanel relatedCustomPropertyAliasNameManagementPanel;
    private SectionActionsBar dataFieldActionsBar;
    private TextField customPropertyName;
    private TextField customPropertyAliasName;
    private ComboBox dataFieldType;

    public CreateCustomPropertyAliasNamePanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle addNewPropertySectionTitle=new MainSectionTitle("添加新的自定义属性别名");
        addComponent(addNewPropertySectionTitle);

        dataFieldActionsBar=new SectionActionsBar(new Label("---" , ContentMode.HTML));
        addComponent(dataFieldActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        this.customPropertyName=new TextField("属性名称");
        this.customPropertyName.setRequired(true);
        form.addComponent(this.customPropertyName);

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

        this.customPropertyAliasName=new TextField("属性别名");
        this.customPropertyAliasName.setRequired(true);
        form.addComponent(this.customPropertyAliasName);

        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("添加属性别名", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new property alias name logic */
                addNewCustomPropertyAliasName();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    @Override
    public void attach() {
        super.attach();
        Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName(), ContentMode.HTML);
        dataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
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

    public void addNewCustomPropertyAliasName(){
        final String customPropertyNameStr=this.customPropertyName.getValue();
        if(customPropertyNameStr==null||customPropertyNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入自定义属性名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(customPropertyNameStr);
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入自定义属性名称 "+customPropertyNameStr+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(customPropertyNameStr);
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入自定义属性名称 "+customPropertyNameStr+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        Object dataFieldTypeObj=dataFieldType.getValue();
        if(dataFieldTypeObj==null){
            Notification errorNotification = new Notification("数据校验错误",
                    "请选择自定义属性数据类型", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        final String customPropertyAliasNameStr=this.customPropertyAliasName.getValue();
        if(customPropertyAliasNameStr==null||customPropertyAliasNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入自定义属性别名", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        boolean isExistTypePropertyName= InfoDiscoverSpaceOperationUtil.
                checkCustomPropertyAliasNameExistence(getDiscoverSpaceName(), customPropertyNameStr, dataFieldTypeObj.toString());
        if(isExistTypePropertyName){
            String checkErrorMessage="信息发现空间 "+getDiscoverSpaceName()+" 中已经存在自定义属性 "+customPropertyNameStr+" 的别名";
            Notification errorNotification = new Notification("数据校验错误",checkErrorMessage, Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        //do add new logic
        final String customPropertyTypeStr=dataFieldType.getValue().toString();

        String confirmMessageString=" 请确认在信息发现空间 "+getDiscoverSpaceName()+" 中添加自定义属性 "+customPropertyNameStr+" ("+customPropertyTypeStr+")"+" 的别名 "+customPropertyAliasNameStr;
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog addTypePropertyConfirmDialog = new ConfirmDialog();
        addTypePropertyConfirmDialog.setConfirmMessage(confirmMessage);

        final CreateCustomPropertyAliasNamePanel self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addTypePropertyConfirmDialog.close();
                boolean createTypePropertyResult=InfoDiscoverSpaceOperationUtil.recordCustomPropertyAliasName(
                        getDiscoverSpaceName(),customPropertyNameStr,customPropertyTypeStr,customPropertyAliasNameStr);
                if(createTypePropertyResult){
                    self.containerDialog.close();
                    getRelatedCustomPropertyAliasNameManagementPanel().addNewCustomPropertyAliasName(customPropertyNameStr,customPropertyTypeStr,customPropertyAliasNameStr);
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建自定义属性别名成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建自定义属性别名错误",
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

    public CustomPropertyAliasNameManagementPanel getRelatedCustomPropertyAliasNameManagementPanel() {
        return relatedCustomPropertyAliasNameManagementPanel;
    }

    public void setRelatedCustomPropertyAliasNameManagementPanel(CustomPropertyAliasNameManagementPanel relatedCustomPropertyAliasNameManagementPanel) {
        this.relatedCustomPropertyAliasNameManagementPanel = relatedCustomPropertyAliasNameManagementPanel;
    }
}
