package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dimensionTypeManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 6/26/17.
 */
public class CreateDimensionTypeDefinitionPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String businessSolutionName;
    private TextField dimensionTypeAliasName;
    private TextField dimensionTypeName;
    private String parentDimensionType;
    private DimensionTypeDefinitionsManagementPanel dimensionTypeDefinitionsManagementPanel;

    public CreateDimensionTypeDefinitionPanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle addNewFactTypeSectionTitle=new MainSectionTitle("创建新的维度类型定义");
        addComponent(addNewFactTypeSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        dimensionTypeName = new TextField("维度类型名称");
        dimensionTypeName.setRequired(true);
        form.addComponent(dimensionTypeName);

        dimensionTypeAliasName = new TextField("类型别名");
        dimensionTypeAliasName.setRequired(true);
        form.addComponent(dimensionTypeAliasName);

        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("创建维度类型", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new logic */
                addNewDimensionType();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void addNewDimensionType(){
        final String dimensionTypeNameStr= dimensionTypeName.getValue();
        if(dimensionTypeNameStr==null||dimensionTypeNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入维度类型名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(dimensionTypeNameStr);
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入维度类型名称 "+dimensionTypeNameStr+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(dimensionTypeNameStr);
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入维度类型名称 "+dimensionTypeNameStr+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isExistDimensionTypeName= BusinessSolutionOperationUtil.checkSolutionDimensionTypeExistence(getBusinessSolutionName(), dimensionTypeNameStr);
        if(isExistDimensionTypeName){
            Notification errorNotification = new Notification("数据校验错误",
                    "业务解决方案 "+getBusinessSolutionName()+" 中已经存在维度类型 "+dimensionTypeNameStr, Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        final String dimensionTypeAliasNameStr= dimensionTypeAliasName.getValue();
        if(dimensionTypeAliasNameStr==null||dimensionTypeAliasNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入类型别名名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        //do add new logic
        Label confirmMessage= new Label(FontAwesome.INFO.getHtml()+ " 请确认创建维度类型  <b>"+dimensionTypeNameStr +"</b>.", ContentMode.HTML);
        final ConfirmDialog addDimensionTypeConfirmDialog = new ConfirmDialog();
        addDimensionTypeConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addDimensionTypeConfirmDialog.close();
                boolean createFactTypeResult=
                BusinessSolutionOperationUtil.createBusinessSolutionDimensionType(getBusinessSolutionName(),getParentDimensionType(), dimensionTypeNameStr,dimensionTypeAliasNameStr);
                if(createFactTypeResult){
                    containerDialog.close();
                    getDimensionTypeDefinitionsManagementPanel().updateDimensionTypesInfo(getParentDimensionType(),dimensionTypeNameStr,false);
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建维度类型成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建维度类型错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addDimensionTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addDimensionTypeConfirmDialog);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    public DimensionTypeDefinitionsManagementPanel getDimensionTypeDefinitionsManagementPanel() {
        return dimensionTypeDefinitionsManagementPanel;
    }

    public void setDimensionTypeDefinitionsManagementPanel(DimensionTypeDefinitionsManagementPanel dimensionTypeDefinitionsManagementPanel) {
        this.dimensionTypeDefinitionsManagementPanel = dimensionTypeDefinitionsManagementPanel;
    }

    public String getParentDimensionType() {
        return parentDimensionType;
    }

    public void setParentDimensionType(String parentDimensionType) {
        this.parentDimensionType = parentDimensionType;
    }
}
