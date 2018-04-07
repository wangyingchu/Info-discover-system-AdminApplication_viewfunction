package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 10/25/16.
 */
public class CreateRelationTypePanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String discoverSpaceName;
    private String parentRelationType;
    private TextField relationTypeName;
    private TextField relationTypeAliasName;
    private RelationTypesManagementPanel relationTypesManagementPanel;

    public CreateRelationTypePanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle addNewRelationTypeSectionTitle=new MainSectionTitle("创建新的关系类型");
        addComponent(addNewRelationTypeSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        relationTypeName = new TextField("关系类型名称");
        relationTypeName.setRequired(true);
        form.addComponent(relationTypeName);

        relationTypeAliasName = new TextField("类型别名");
        relationTypeAliasName.setRequired(true);
        form.addComponent(relationTypeAliasName);

        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("创建关系类型", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new logic */
                addNewRelationType();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
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

    public String getParentRelationType() {
        return parentRelationType;
    }

    public void setParentRelationType(String parentRelationType) {
        this.parentRelationType = parentRelationType;
    }

    private void addNewRelationType(){
        final String relationTypeNameStr= relationTypeName.getValue();
        if(relationTypeNameStr==null||relationTypeNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入关系类型名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(relationTypeNameStr);
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入关系类型名称 "+relationTypeNameStr+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(relationTypeNameStr);
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入关系类型名称 "+relationTypeNameStr+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isExistRelationTypeName= InfoDiscoverSpaceOperationUtil.checkRelationTypeExistence(getDiscoverSpaceName(), relationTypeNameStr);
        if(isExistRelationTypeName){
            Notification errorNotification = new Notification("数据校验错误",
                    "信息发现空间 "+getDiscoverSpaceName()+" 中已经存在关系类型 "+relationTypeNameStr, Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        final String relationTypeAliasNameStr= relationTypeAliasName.getValue();
        if(relationTypeAliasNameStr==null||relationTypeAliasNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入类型别名名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        //do add new logic
        Label confirmMessage=null;
        if(getParentRelationType().equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                    " 请确认创建根关系类型  <b>"+relationTypeNameStr +"</b>.", ContentMode.HTML);
        }else{
            confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                    " 请确认在关系类型 "+ getParentRelationType()+" 下创建子类型  <b>"+relationTypeNameStr +"</b>.", ContentMode.HTML);
        }

        final ConfirmDialog addRelationTypeConfirmDialog = new ConfirmDialog();
        addRelationTypeConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addRelationTypeConfirmDialog.close();
                boolean createRelationTypeResult=InfoDiscoverSpaceOperationUtil.createRelationType(getDiscoverSpaceName(),relationTypeNameStr,relationTypeAliasNameStr, getParentRelationType());
                if(createRelationTypeResult){
                    containerDialog.close();
                    getRelationTypesManagementPanel().updateRelationTypesInfo(getParentRelationType(), relationTypeNameStr, false);
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建关系类型成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建关系类型错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addRelationTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addRelationTypeConfirmDialog);
    }

    public RelationTypesManagementPanel getRelationTypesManagementPanel() {
        return relationTypesManagementPanel;
    }

    public void setRelationTypesManagementPanel(RelationTypesManagementPanel relationTypesManagementPanel) {
        this.relationTypesManagementPanel = relationTypesManagementPanel;
    }
}
