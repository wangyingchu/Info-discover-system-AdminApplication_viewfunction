package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.customPropertyAliasManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.CustomPropertyAliasDefinitionVO;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 6/28/17.
 */
public class CustomPropertyAliasNameDefinitionManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private Button deletePropertyAliasNameButton;
    private TreeTable customPropertyAliasNamesTable;
    private String PROPERTYNAME_PROPERTY="自定义属性名称";
    private String PROPERTYALIASNAME_PROPERTY="自定义属性别名";
    private String PROPERTYTYPE_PROPERTY="自定义属性类型";
    private String currentSelectedCustomPropertyName;
    private String currentSelectedCustomPropertyType;

    public CustomPropertyAliasNameDefinitionManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        this.setMargin(new MarginInfo(true,false,false,false));

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int dataDisplayElementHeight=screenHeight-440;

        HorizontalLayout actionButtonPlacementLayout=new HorizontalLayout();
        this.addComponent(actionButtonPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout1=new HorizontalLayout();
        actionButtonsSpacingLayout1.setWidth("10px");
        actionButtonPlacementLayout.addComponent(actionButtonsSpacingLayout1);

        Label actionSideTitle= new Label(FontAwesome.LIST_UL.getHtml() +" 数据自定义属性别名", ContentMode.HTML);
        actionButtonPlacementLayout.addComponent(actionSideTitle);

        HorizontalLayout actionButtonsSpacingLayout=new HorizontalLayout();
        actionButtonsSpacingLayout.setWidth("20px");
        actionButtonPlacementLayout.addComponent(actionButtonsSpacingLayout);

        Button addCustomPropertyAliasNameButton=new Button("添加属性别名");
        addCustomPropertyAliasNameButton.setIcon(FontAwesome.PLUS_CIRCLE);
        addCustomPropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addCustomPropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_TINY);
        addCustomPropertyAliasNameButton.addStyleName("ui_appElementBottomSpacing");
        addCustomPropertyAliasNameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeAddCustomPropertyAliasNameOperation();
            }
        });
        actionButtonPlacementLayout.addComponent(addCustomPropertyAliasNameButton);

        Label spaceDivLabel2=new Label("|");
        actionButtonPlacementLayout. addComponent(spaceDivLabel2);

        this.deletePropertyAliasNameButton =new Button("删除属性别名");
        this.deletePropertyAliasNameButton.setIcon(FontAwesome.TRASH_O);
        this.deletePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.deletePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.deletePropertyAliasNameButton.addStyleName("ui_appElementBottomSpacing");
        this.deletePropertyAliasNameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeDeleteCustomPropertyAliasNameOperation();
            }
        });
        actionButtonPlacementLayout.addComponent(this.deletePropertyAliasNameButton);

        this.customPropertyAliasNamesTable = new TreeTable();
        this.customPropertyAliasNamesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.customPropertyAliasNamesTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.customPropertyAliasNamesTable.setSizeFull();
        this.customPropertyAliasNamesTable.setSelectable(true);
        this.customPropertyAliasNamesTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.customPropertyAliasNamesTable.setNullSelectionAllowed(false);
        this.customPropertyAliasNamesTable.addContainerProperty(PROPERTYNAME_PROPERTY, String.class, "");
        this.customPropertyAliasNamesTable.addContainerProperty(PROPERTYTYPE_PROPERTY, String.class, "");
        this.customPropertyAliasNamesTable.addContainerProperty(PROPERTYALIASNAME_PROPERTY, String.class, "");
        this.customPropertyAliasNamesTable.setColumnWidth(PROPERTYTYPE_PROPERTY,120);

        this.customPropertyAliasNamesTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                currentSelectedCustomPropertyName=itemClickEvent.getItem().getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                currentSelectedCustomPropertyType=itemClickEvent.getItem().getItemProperty(PROPERTYTYPE_PROPERTY).getValue().toString();
                deletePropertyAliasNameButton.setEnabled(true);
            }
        });
        this.addComponent(this.customPropertyAliasNamesTable);
    }

    public void renderCustomPropertyAliasInfo(String businessSolutionName){
        setBusinessSolutionName(businessSolutionName);
        clearPropertyAliasSelectStatus();
        List<CustomPropertyAliasDefinitionVO> customPropertyAliasNameList= BusinessSolutionOperationUtil.getSolutionCustomPropertyAliasNames(getBusinessSolutionName());
        for(CustomPropertyAliasDefinitionVO currentCustomPropertyAliasNameVO:customPropertyAliasNameList){
            Object[] newCustomPropertyAliasInfo=new Object[]{
                    " "+currentCustomPropertyAliasNameVO.getCustomPropertyName(),
                    currentCustomPropertyAliasNameVO.getCustomPropertyType(),
                    currentCustomPropertyAliasNameVO.getCustomPropertyAliasName()
            };
            final Object newDataItemKey = this.customPropertyAliasNamesTable.addItem(newCustomPropertyAliasInfo, null);
            this.customPropertyAliasNamesTable.setChildrenAllowed(newDataItemKey, false);
            this.customPropertyAliasNamesTable.setColumnCollapsible(newDataItemKey, false);
        }
    }

    private void clearPropertyAliasSelectStatus(){
        this.customPropertyAliasNamesTable.getContainerDataSource().removeAllItems();
        this.deletePropertyAliasNameButton.setEnabled(false);
        this.currentSelectedCustomPropertyName=null;
        this.currentSelectedCustomPropertyType=null;
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    private void executeAddCustomPropertyAliasNameOperation(){
        CreateCustomPropertyAliasDefinitionPanel createCustomPropertyAliasDefinitionPanel=new CreateCustomPropertyAliasDefinitionPanel(this.currentUserClientInfo);
        createCustomPropertyAliasDefinitionPanel.setBusinessSolutionName(getBusinessSolutionName());
        createCustomPropertyAliasDefinitionPanel.setRelatedCustomPropertyAliasNameDefinitionManagementPanel(this);
        final Window window = new Window();
        window.setHeight(330.0f, Unit.PIXELS);
        window.setWidth(550.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(createCustomPropertyAliasDefinitionPanel);
        createCustomPropertyAliasDefinitionPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    public void addNewCustomPropertyAliasName(String propertyName,String propertyType,String propertyAliasName){
        Object[] newCustomPropertyAliasInfo=new Object[]{
                " "+propertyName,
                propertyType,
                propertyAliasName
        };
        final Object newDataItemKey = this.customPropertyAliasNamesTable.addItem(newCustomPropertyAliasInfo, null);
        this.customPropertyAliasNamesTable.setChildrenAllowed(newDataItemKey, false);
        this.customPropertyAliasNamesTable.setColumnCollapsible(newDataItemKey, false);
    }

    private void executeDeleteCustomPropertyAliasNameOperation(){
        if(this.currentSelectedCustomPropertyName!=null&& this.currentSelectedCustomPropertyType!=null){
            //do delete logic
            Label confirmMessage= new Label(FontAwesome.INFO.getHtml()+
                    " 请确认是否删除自定义属性 "+this.currentSelectedCustomPropertyName +" ("+currentSelectedCustomPropertyType+") 的别名.", ContentMode.HTML);
            final ConfirmDialog deleteFactTypeConfirmDialog = new ConfirmDialog();
            deleteFactTypeConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteFactTypeConfirmDialog.close();

                    boolean deleteFactTypePropertyResult=
                            BusinessSolutionOperationUtil.deleteCustomPropertyAliasDefinition(getBusinessSolutionName(), currentSelectedCustomPropertyName.trim(), currentSelectedCustomPropertyType);
                    if(deleteFactTypePropertyResult){
                        renderCustomPropertyAliasInfo(getBusinessSolutionName());
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除自定义属性别名成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除自定义属性别名错误",
                                "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            deleteFactTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(deleteFactTypeConfirmDialog);
        }
    }
}
