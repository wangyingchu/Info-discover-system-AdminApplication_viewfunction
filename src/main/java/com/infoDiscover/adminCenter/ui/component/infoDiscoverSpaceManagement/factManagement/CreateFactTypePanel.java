package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.factManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
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
 * Created by wangychu on 10/22/16.
 */
public class CreateFactTypePanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String discoverSpaceName;
    private TextField factTypeAliasName;
    private TextField factTypeName;
    private FactTypesManagementPanel factTypesManagementPanel;

    public CreateFactTypePanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle addNewFactTypeSectionTitle=new MainSectionTitle("创建新的事实类型");
        addComponent(addNewFactTypeSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        factTypeName = new TextField("事实类型名称");
        factTypeName.setRequired(true);
        form.addComponent(factTypeName);

        factTypeAliasName = new TextField("类型别名");
        factTypeAliasName.setRequired(true);
        form.addComponent(factTypeAliasName);

        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("创建事实类型", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new logic */
                addNewFactType();
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

    private void addNewFactType(){
        final String factTypeNameStr= factTypeName.getValue();
        if(factTypeNameStr==null||factTypeNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入事实类型名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(factTypeNameStr);
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入事实类型名称 "+factTypeNameStr+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(factTypeNameStr);
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入事实类型名称 "+factTypeNameStr+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isExistFactTypeName= InfoDiscoverSpaceOperationUtil.checkFactTypeExistence(getDiscoverSpaceName(), factTypeNameStr);
        if(isExistFactTypeName){
            Notification errorNotification = new Notification("数据校验错误",
                    "信息发现空间 "+getDiscoverSpaceName()+" 中已经存在事实类型 "+factTypeNameStr, Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        final String factTypeAliasNameStr= factTypeAliasName.getValue();
        if(factTypeAliasNameStr==null||factTypeAliasNameStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入类型别名名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        //do add new logic
        Label confirmMessage= new Label(FontAwesome.INFO.getHtml()+ " 请确认创建事实类型  <b>"+factTypeNameStr +"</b>.", ContentMode.HTML);
        final ConfirmDialog addFactTypeConfirmDialog = new ConfirmDialog();
        addFactTypeConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addFactTypeConfirmDialog.close();
                boolean createFactTypeResult=InfoDiscoverSpaceOperationUtil.createFactType(getDiscoverSpaceName(), factTypeNameStr,factTypeAliasNameStr);
                if(createFactTypeResult){
                    containerDialog.close();
                    getFactTypesManagementPanel().updateFactTypesInfo(factTypeNameStr, false);
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建事实类型成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("创建事实类型错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addFactTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addFactTypeConfirmDialog);
    }

    public FactTypesManagementPanel getFactTypesManagementPanel() {
        return factTypesManagementPanel;
    }

    public void setFactTypesManagementPanel(FactTypesManagementPanel factTypesManagementPanel) {
        this.factTypesManagementPanel = factTypesManagementPanel;
    }
}
