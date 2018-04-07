package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.businessDataDefinitionManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.CustomPropertyAliasNameVO;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;
import java.util.List;

/**
 * Created by wangychu on 25/05/2017.
 */
public class CustomPropertyAliasNameManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Button deletePropertyAliasNameButton;
    private TreeTable CustomPropertyAliasNamesTable;
    private String PROPERTYNAME_PROPERTY="自定义属性名称";
    private String PROPERTYALIASNAME_PROPERTY="自定义属性别名";
    private String PROPERTYTYPE_PROPERTY="自定义属性类型";
    private String currentSelectedCustomPropertyName;
    private String currentSelectedCustomPropertyType;
    private File cusomtPropertyAliasNamesFile;
    private FileDownloader cusomtPropertyAliasNamesFileDownloader;
    private Button exportCustomPropertyAliasNamesButton;

    public CustomPropertyAliasNameManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        this.setMargin(new MarginInfo(true,false,false,false));

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int dataDisplayElementHeight=screenHeight-480;

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

        Button importCustomPropertyAliasNamesButton=new Button("导入属性别名");
        importCustomPropertyAliasNamesButton.setIcon(FontAwesome.DOWNLOAD);
        importCustomPropertyAliasNamesButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        importCustomPropertyAliasNamesButton.addStyleName(ValoTheme.BUTTON_TINY);
        importCustomPropertyAliasNamesButton.addStyleName("ui_appElementBottomSpacing");
        importCustomPropertyAliasNamesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeImportCustomPropertyAliasNamesOperation();
            }
        });
        actionButtonPlacementLayout.addComponent(importCustomPropertyAliasNamesButton);

        exportCustomPropertyAliasNamesButton=new Button("导出属性别名");
        exportCustomPropertyAliasNamesButton.setIcon(FontAwesome.UPLOAD);
        exportCustomPropertyAliasNamesButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        exportCustomPropertyAliasNamesButton.addStyleName(ValoTheme.BUTTON_TINY);
        exportCustomPropertyAliasNamesButton.addStyleName("ui_appElementBottomSpacing");
        actionButtonPlacementLayout.addComponent(exportCustomPropertyAliasNamesButton);

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

        this.CustomPropertyAliasNamesTable = new TreeTable();
        this.CustomPropertyAliasNamesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.CustomPropertyAliasNamesTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.CustomPropertyAliasNamesTable.setSizeFull();
        this.CustomPropertyAliasNamesTable.setSelectable(true);
        this.CustomPropertyAliasNamesTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.CustomPropertyAliasNamesTable.setNullSelectionAllowed(false);
        this.CustomPropertyAliasNamesTable.addContainerProperty(PROPERTYNAME_PROPERTY, String.class, "");
        this.CustomPropertyAliasNamesTable.addContainerProperty(PROPERTYTYPE_PROPERTY, String.class, "");
        this.CustomPropertyAliasNamesTable.addContainerProperty(PROPERTYALIASNAME_PROPERTY, String.class, "");
        this.CustomPropertyAliasNamesTable.setColumnWidth(PROPERTYTYPE_PROPERTY,120);

        this.CustomPropertyAliasNamesTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                currentSelectedCustomPropertyName=itemClickEvent.getItem().getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                currentSelectedCustomPropertyType=itemClickEvent.getItem().getItemProperty(PROPERTYTYPE_PROPERTY).getValue().toString();
                deletePropertyAliasNameButton.setEnabled(true);
            }
        });

        this.addComponent(this.CustomPropertyAliasNamesTable);
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void renderCustomPropertyAliasInfo(){
        clearPropertyAliasSelectStatus();
        List<CustomPropertyAliasNameVO> customPropertyAliasNameList=InfoDiscoverSpaceOperationUtil.queryCustomPropertyAliasNames(this.getDiscoverSpaceName());
        for(CustomPropertyAliasNameVO currentCustomPropertyAliasNameVO:customPropertyAliasNameList){

            Object[] newCustomPropertyAliasInfo=new Object[]{
                    " "+currentCustomPropertyAliasNameVO.getCustomPropertyName(),
                    currentCustomPropertyAliasNameVO.getCustomPropertyType(),
                    currentCustomPropertyAliasNameVO.getCustomPropertyAliasName()
            };
            final Object newDataItemKey = this.CustomPropertyAliasNamesTable.addItem(newCustomPropertyAliasInfo, null);
            this.CustomPropertyAliasNamesTable.setChildrenAllowed(newDataItemKey, false);
            this.CustomPropertyAliasNamesTable.setColumnCollapsible(newDataItemKey, false);
        }
        setupFileDownloader();
    }

    private void setupFileDownloader(){
        this.cusomtPropertyAliasNamesFile=InfoDiscoverSpaceOperationUtil.generateCustomPropertyAliasNamesJsonFile(this.getDiscoverSpaceName());
        Resource res = new FileResource(this.cusomtPropertyAliasNamesFile);
        if(this.cusomtPropertyAliasNamesFileDownloader==null){
            this.cusomtPropertyAliasNamesFileDownloader = new FileDownloader(res);
            this.cusomtPropertyAliasNamesFileDownloader.extend(exportCustomPropertyAliasNamesButton);
        }else{
            this.cusomtPropertyAliasNamesFileDownloader.setFileDownloadResource(res);
        }
    }

    private void clearPropertyAliasSelectStatus(){
        this.CustomPropertyAliasNamesTable.getContainerDataSource().removeAllItems();
        this.deletePropertyAliasNameButton.setEnabled(false);
        this.currentSelectedCustomPropertyName=null;
        this.currentSelectedCustomPropertyType=null;
    }

    private void executeAddCustomPropertyAliasNameOperation(){
        CreateCustomPropertyAliasNamePanel createCustomPropertyAliasNamePanel=new CreateCustomPropertyAliasNamePanel(this.currentUserClientInfo);
        createCustomPropertyAliasNamePanel.setDiscoverSpaceName(this.discoverSpaceName);
        createCustomPropertyAliasNamePanel.setRelatedCustomPropertyAliasNameManagementPanel(this);
        final Window window = new Window();
        window.setHeight(330.0f, Unit.PIXELS);
        window.setWidth(550.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(createCustomPropertyAliasNamePanel);
        createCustomPropertyAliasNamePanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    private void executeImportCustomPropertyAliasNamesOperation(){
        ImportCustomPropertyAliasNamesPanel importCustomPropertyAliasNamesPanel=new ImportCustomPropertyAliasNamesPanel(this.currentUserClientInfo);
        importCustomPropertyAliasNamesPanel.setDiscoverSpaceName(this.discoverSpaceName);
        importCustomPropertyAliasNamesPanel.setRelatedCustomPropertyAliasNameManagementPanel(this);
        final Window window = new Window();
        window.setHeight(270.0f, Unit.PIXELS);
        window.setWidth(550.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(importCustomPropertyAliasNamesPanel);
        importCustomPropertyAliasNamesPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    public void addNewCustomPropertyAliasName(String propertyName,String propertyType,String propertyAliasName){
        Object[] newCustomPropertyAliasInfo=new Object[]{
                " "+propertyName,
                propertyType,
                propertyAliasName
        };
        final Object newDataItemKey = this.CustomPropertyAliasNamesTable.addItem(newCustomPropertyAliasInfo, null);
        this.CustomPropertyAliasNamesTable.setChildrenAllowed(newDataItemKey, false);
        this.CustomPropertyAliasNamesTable.setColumnCollapsible(newDataItemKey, false);
        this.cusomtPropertyAliasNamesFile=InfoDiscoverSpaceOperationUtil.generateCustomPropertyAliasNamesJsonFile(this.getDiscoverSpaceName());
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
                            InfoDiscoverSpaceOperationUtil.deleteCustomPropertyAliasName(discoverSpaceName, currentSelectedCustomPropertyName.trim(), currentSelectedCustomPropertyType);
                    if(deleteFactTypePropertyResult){
                        renderCustomPropertyAliasInfo();
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
