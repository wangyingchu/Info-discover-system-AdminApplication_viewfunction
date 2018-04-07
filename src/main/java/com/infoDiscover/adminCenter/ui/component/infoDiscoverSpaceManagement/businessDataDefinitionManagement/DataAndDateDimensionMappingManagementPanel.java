package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.businessDataDefinitionManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.DataMappingDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.Item;
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
 * Created by wangychu on 7/6/17.
 */
public class DataAndDateDimensionMappingManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private TreeTable dataRelationMappingDefinitionsTable;
    private Button removeRelationMappingRuleButton;
    private Button executeRelationMappingRuleButton;
    private Item currentSelectedDefinitionItem;

    public DataAndDateDimensionMappingManagementPanel(UserClientInfo currentUserClientInfo){
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

        Label actionTitle= new Label(FontAwesome.LIST.getHtml() +" 数据与时间维度关联定义规则", ContentMode.HTML);
        actionButtonPlacementLayout.addComponent(actionTitle);

        HorizontalLayout actionButtonsSpacingLayout2=new HorizontalLayout();
        actionButtonsSpacingLayout2.setWidth("20px");
        actionButtonPlacementLayout.addComponent(actionButtonsSpacingLayout2);

        Button createRelationMappingRuleButton=new Button("创建数据与时间维度关联定义规则");
        createRelationMappingRuleButton.setIcon(FontAwesome.PLUS_CIRCLE);
        createRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        createRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_TINY);
        createRelationMappingRuleButton.addStyleName("ui_appElementBottomSpacing");
        actionButtonPlacementLayout.addComponent(createRelationMappingRuleButton);
        createRelationMappingRuleButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeCreateDataMappingRuleOperation();
            }
        });

        Label spaceDivLabel0=new Label("|");
        actionButtonPlacementLayout. addComponent(spaceDivLabel0);

        this.executeRelationMappingRuleButton=new Button("执行关联映射规则");
        this.executeRelationMappingRuleButton.setEnabled(false);
        this.executeRelationMappingRuleButton.setIcon(FontAwesome.PLAY);
        this.executeRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.executeRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.executeRelationMappingRuleButton.addStyleName("ui_appElementBottomSpacing");
        actionButtonPlacementLayout.addComponent(this.executeRelationMappingRuleButton);
        this.executeRelationMappingRuleButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeRunRelationMappingDefinitionOperation();
            }
        });

        Label spaceDivLabel1=new Label("|");
        actionButtonPlacementLayout. addComponent(spaceDivLabel1);

        this.removeRelationMappingRuleButton=new Button("删除关联映射规则");
        this.removeRelationMappingRuleButton.setEnabled(false);
        this.removeRelationMappingRuleButton.setIcon(FontAwesome.TRASH_O);
        this.removeRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.removeRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.removeRelationMappingRuleButton.addStyleName("ui_appElementBottomSpacing");
        actionButtonPlacementLayout.addComponent(this.removeRelationMappingRuleButton);
        this.removeRelationMappingRuleButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeDeleteRelationMappingDefinitionOperation();
            }
        });

        this.dataRelationMappingDefinitionsTable = new TreeTable();
        this.dataRelationMappingDefinitionsTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.dataRelationMappingDefinitionsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.dataRelationMappingDefinitionsTable.setSizeFull();
        this.dataRelationMappingDefinitionsTable.setSelectable(true);
        this.dataRelationMappingDefinitionsTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.dataRelationMappingDefinitionsTable.setNullSelectionAllowed(false);
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源数据类型", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源类型名称", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源属性名称", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("关联关系类型", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("数据关联方向", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("时间维度类型前缀", String.class, "");
        this.dataRelationMappingDefinitionsTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                currentSelectedDefinitionItem=itemClickEvent.getItem();
                removeRelationMappingRuleButton.setEnabled(true);
                executeRelationMappingRuleButton.setEnabled(true);
            }
        });
        this.addComponent(this.dataRelationMappingDefinitionsTable);

    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void renderDataAndDateDimensionMappingInfo(){
        this.removeRelationMappingRuleButton.setEnabled(false);
        this.executeRelationMappingRuleButton.setEnabled(false);
        this.dataRelationMappingDefinitionsTable.removeAllItems();
        this.currentSelectedDefinitionItem=null;
        List<DataMappingDefinitionVO> dataMappingDefinitionsList= InfoDiscoverSpaceOperationUtil.getDataDateDimensionMappingDefinitionList(discoverSpaceName);
        for(DataMappingDefinitionVO currentDataMappingDefinitionVO:dataMappingDefinitionsList){
            Object[] newDefinitionInfo=new Object[]{
                    currentDataMappingDefinitionVO.getSourceDataTypeKind(),
                    currentDataMappingDefinitionVO.getSourceDataTypeName(),
                    currentDataMappingDefinitionVO.getSourceDataPropertyName(),
                    currentDataMappingDefinitionVO.getRelationTypeName(),
                    currentDataMappingDefinitionVO.getRelationDirection(),
                    currentDataMappingDefinitionVO.getDateDimensionTypePrefix()
            };
            final Object newDataItemKey =this.dataRelationMappingDefinitionsTable.addItem(newDefinitionInfo,null);
            this.dataRelationMappingDefinitionsTable.setChildrenAllowed(newDataItemKey, false);
        }
    }

    private void executeCreateDataMappingRuleOperation(){
        DataAndDateDimensionMappingEditor dataAndDateDimensionMappingEditor=new DataAndDateDimensionMappingEditor(this.currentUserClientInfo);
        dataAndDateDimensionMappingEditor.setDiscoverSpaceName(this.discoverSpaceName);
        dataAndDateDimensionMappingEditor.setRelatedDataAndDateDimensionMappingManagementPanel(this);
        final Window window = new Window();
        window.setWidth(750.0f, Unit.PIXELS);
        window.setHeight(450.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(dataAndDateDimensionMappingEditor);
        dataAndDateDimensionMappingEditor.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    private void executeDeleteRelationMappingDefinitionOperation(){
        if(this.currentSelectedDefinitionItem!=null){
            Label confirmMessage= new Label(FontAwesome.INFO.getHtml()+
                    " 请确认是否删除选定的数据与时间维度关联定义规则.", ContentMode.HTML);
            final ConfirmDialog deleteDefinitionConfirmDialog = new ConfirmDialog();
            deleteDefinitionConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteDefinitionConfirmDialog.close();

                    DataMappingDefinitionVO definitionToDelete=new DataMappingDefinitionVO();
                    definitionToDelete.setSolutionName(getDiscoverSpaceName());
                    definitionToDelete.setSourceDataPropertyName(currentSelectedDefinitionItem.getItemProperty("源属性名称").getValue().toString());
                    definitionToDelete.setSourceDataTypeKind(currentSelectedDefinitionItem.getItemProperty("源数据类型").getValue().toString());
                    definitionToDelete.setSourceDataTypeName(currentSelectedDefinitionItem.getItemProperty("源类型名称").getValue().toString());
                    definitionToDelete.setRelationDirection(currentSelectedDefinitionItem.getItemProperty("数据关联方向").getValue().toString());
                    definitionToDelete.setRelationTypeName(currentSelectedDefinitionItem.getItemProperty("关联关系类型").getValue().toString());
                    definitionToDelete.setDateDimensionTypePrefix(currentSelectedDefinitionItem.getItemProperty("时间维度类型前缀").getValue().toString());
                    boolean deleteDefinitionResult=InfoDiscoverSpaceOperationUtil.deleteDataDateDimensionMappingDefinition(getDiscoverSpaceName(),definitionToDelete);
                    if(deleteDefinitionResult){
                        renderDataAndDateDimensionMappingInfo();
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除数据与时间维度关联定义规则成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除数据与时间维度关联定义规则错误",
                                "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            deleteDefinitionConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(deleteDefinitionConfirmDialog);
        }
    }

    private void executeRunRelationMappingDefinitionOperation(){
        if(this.currentSelectedDefinitionItem!=null){
            Label confirmMessage= new Label(FontAwesome.INFO.getHtml()+
                    " 请确认是否运行选定的数据与时间维度关联定义规则.", ContentMode.HTML);
            final ConfirmDialog runDefinitionConfirmDialog = new ConfirmDialog();
            runDefinitionConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    runDefinitionConfirmDialog.close();

                    DataMappingDefinitionVO definitionToRun=new DataMappingDefinitionVO();
                    definitionToRun.setSolutionName(getDiscoverSpaceName());
                    definitionToRun.setSourceDataPropertyName(currentSelectedDefinitionItem.getItemProperty("源属性名称").getValue().toString());
                    definitionToRun.setSourceDataTypeKind(currentSelectedDefinitionItem.getItemProperty("源数据类型").getValue().toString());
                    definitionToRun.setSourceDataTypeName(currentSelectedDefinitionItem.getItemProperty("源类型名称").getValue().toString());
                    definitionToRun.setRelationDirection(currentSelectedDefinitionItem.getItemProperty("数据关联方向").getValue().toString());
                    definitionToRun.setRelationTypeName(currentSelectedDefinitionItem.getItemProperty("关联关系类型").getValue().toString());
                    definitionToRun.setDateDimensionTypePrefix(currentSelectedDefinitionItem.getItemProperty("时间维度类型前缀").getValue().toString());
                    boolean runDefinitionResult=InfoDiscoverSpaceOperationUtil.runDataDateDimensionMappingDefinition(getDiscoverSpaceName(),definitionToRun);
                    if(runDefinitionResult){
                        Notification resultNotification = new Notification("启动运行规则成功",
                                "启动运行选定的数据与时间维度关联定义规则成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("启动运行数据与时间维度关联定义规则错误",
                                "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            runDefinitionConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(runDefinitionConfirmDialog);
        }
    }
}
