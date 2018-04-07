package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationTypeVO;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.AliasNameEditPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.AliasNameEditPanelInvoker;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.CreateTypePropertyPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.CreateTypePropertyPanelInvoker;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * Created by wangychu on 10/25/16.
 */
public class RelationTypesManagementPanel extends VerticalLayout implements CreateTypePropertyPanelInvoker,AliasNameEditPanelInvoker {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private TreeTable relationTypesTreeTable;
    private TreeTable relationTypePropertiesTable;

    private String NAME_PROPERTY="关系类型名称";
    private String ALIASNAME_PROPERTY="类型别名";
    private String CHILDTYPECOUNT_PROPERTY="子类型数量";
    private String DESCENDANTTYPECOUNT_PROPERTY="后代类型数量";
    private String RELATIONDATAFULLCOUNT_PROPERTY ="类型数据总量";

    private String PROPERTYNAME_PROPERTY="类型属性名";
    private String PROPERTYALIASNAME_PROPERTY="属性别名";
    private String PROPERTYTYPE_PROPERTY="属性数据类型";
    private String MUSTINPUT_PROPERTY="必填属性";
    private String READONLY_PROPERTY="只读属性";
    private String ALLOWNULL_PROPERTY="允许空值";

    private VerticalLayout rightSideUIElementsContainer;
    private VerticalLayout rightSideUIElementsBox;
    private VerticalLayout rightSideUIPromptBox;

    private Button createChildRelationTypeButton;
    private Button removeRelationTypeButton;
    private Button deleteRelationTypePropertyButton;
    private Button editTypeAliasNameButton;
    private Button editRelationTypePropertyAliasNameButton;
    private String currentSelectedRelationTypeName;
    private String currentSelectedRelationTypePropertyName;
    private DiscoverSpaceStatisticMetrics currentDiscoverSpaceStatisticMetrics;
    private Map<String,PropertyTypeVO> currentRelationTypePropertiesMap;

    public RelationTypesManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int dataDisplayElementHeight=screenHeight-550;

        //left side elements
        VerticalLayout leftSideUIElementsContainer=new VerticalLayout();
        elementPlacementLayout.addComponent(leftSideUIElementsContainer);

        VerticalLayout leftSideActionButtonsSpaceDiv=new VerticalLayout();
        leftSideUIElementsContainer.addComponent(leftSideActionButtonsSpaceDiv);

        HorizontalLayout leftSideActionButtonPlacementLayout=new HorizontalLayout();
        leftSideUIElementsContainer.addComponent(leftSideActionButtonPlacementLayout);

        HorizontalLayout leftSideActionButtonsSpacingLayout1=new HorizontalLayout();
        leftSideActionButtonsSpacingLayout1.setWidth("10px");
        leftSideActionButtonPlacementLayout.addComponent(leftSideActionButtonsSpacingLayout1);

        Label leftSideTitle= new Label(FontAwesome.EXCHANGE.getHtml() +" 关系类型", ContentMode.HTML);
        leftSideActionButtonPlacementLayout.addComponent(leftSideTitle);

        HorizontalLayout leftSideActionButtonsSpacingLayout2=new HorizontalLayout();
        leftSideActionButtonsSpacingLayout2.setWidth("20px");
        leftSideActionButtonPlacementLayout.addComponent(leftSideActionButtonsSpacingLayout2);

        this.createChildRelationTypeButton =new Button("创建子关系类型");
        this.createChildRelationTypeButton.setEnabled(false);
        this.createChildRelationTypeButton.setIcon(FontAwesome.PLUS_CIRCLE);
        this.createChildRelationTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.createChildRelationTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.createChildRelationTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.createChildRelationTypeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeCreateRelationTypeOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(createChildRelationTypeButton);

        Label spaceDivLabel1=new Label("|");
        leftSideActionButtonPlacementLayout. addComponent(spaceDivLabel1);

        this.editTypeAliasNameButton =new Button("修改类型别名");
        this.editTypeAliasNameButton.setEnabled(false);
        this.editTypeAliasNameButton.setIcon(FontAwesome.EDIT);
        this.editTypeAliasNameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.editTypeAliasNameButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.editTypeAliasNameButton.addStyleName("ui_appElementBottomSpacing");
        this.editTypeAliasNameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeEditRelationTypeAliasNameOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(editTypeAliasNameButton);

        this.removeRelationTypeButton =new Button("删除关系类型");
        this.removeRelationTypeButton.setEnabled(false);
        this.removeRelationTypeButton.setIcon(FontAwesome.TRASH_O);
        this.removeRelationTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.removeRelationTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.removeRelationTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.removeRelationTypeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeDeleteRelationTypeOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(removeRelationTypeButton);

        this.relationTypesTreeTable = new TreeTable();
        this.relationTypesTreeTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.relationTypesTreeTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.relationTypesTreeTable.setSizeFull();
        this.relationTypesTreeTable.setSelectable(true);
        this.relationTypesTreeTable.setHeight(dataDisplayElementHeight,Unit.PIXELS);
        this.relationTypesTreeTable.setNullSelectionAllowed(false);

        this.relationTypesTreeTable.addContainerProperty(NAME_PROPERTY, String.class, "");
        this.relationTypesTreeTable.addContainerProperty(ALIASNAME_PROPERTY, String.class, "");
        this.relationTypesTreeTable.addContainerProperty(CHILDTYPECOUNT_PROPERTY, String.class, "");
        this.relationTypesTreeTable.addContainerProperty(DESCENDANTTYPECOUNT_PROPERTY, String.class, "");
        this.relationTypesTreeTable.addContainerProperty(RELATIONDATAFULLCOUNT_PROPERTY, String.class, "");

        this.relationTypesTreeTable.setColumnWidth(CHILDTYPECOUNT_PROPERTY,90);
        this.relationTypesTreeTable.setColumnWidth(DESCENDANTTYPECOUNT_PROPERTY,90);
        this.relationTypesTreeTable.setColumnWidth(RELATIONDATAFULLCOUNT_PROPERTY,90);

        this.relationTypesTreeTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedRelationTypeName=itemClickEvent.getItem().getItemProperty(NAME_PROPERTY).getValue().toString();
                renderRelationTypeSelectedUIElements(selectedRelationTypeName);
            }
        });
        leftSideUIElementsContainer.addComponent(relationTypesTreeTable);
        leftSideUIElementsContainer.addStyleName("ui_appElementRightSideSpacing");

        //right side elements
        this.rightSideUIElementsContainer=new VerticalLayout();
        elementPlacementLayout.addComponent(rightSideUIElementsContainer);

        this.rightSideUIPromptBox=new VerticalLayout();
        VerticalLayout messageHeightSpaceDiv=new VerticalLayout();
        messageHeightSpaceDiv.setHeight(30,Unit.PIXELS);
        this.rightSideUIPromptBox.addComponent(messageHeightSpaceDiv);
        Label functionMessage = new Label( FontAwesome.LIST_UL.getHtml()+" 选定关系类型包含的属性信息。", ContentMode.HTML);
        functionMessage.setStyleName("ui_appLightDarkMessage");
        functionMessage.addStyleName(ValoTheme.LABEL_LARGE);
        this.rightSideUIPromptBox.addComponent(functionMessage);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);

        this.rightSideUIElementsBox=new VerticalLayout();

        VerticalLayout rightSideActionButtonsSpaceDiv=new VerticalLayout();
        this.rightSideUIElementsBox.addComponent(rightSideActionButtonsSpaceDiv);

        HorizontalLayout rightSideActionButtonPlacementLayout=new HorizontalLayout();
        this.rightSideUIElementsBox.addComponent(rightSideActionButtonPlacementLayout);

        HorizontalLayout rightSideActionButtonsSpacingLayout1=new HorizontalLayout();
        rightSideActionButtonsSpacingLayout1.setWidth("10px");
        rightSideActionButtonPlacementLayout.addComponent(rightSideActionButtonsSpacingLayout1);

        Label rightSideTitle= new Label(FontAwesome.LIST_UL.getHtml() +" 类型属性", ContentMode.HTML);
        rightSideActionButtonPlacementLayout.addComponent(rightSideTitle);

        HorizontalLayout rightSideActionButtonsSpacingLayout2=new HorizontalLayout();
        rightSideActionButtonsSpacingLayout2.setWidth("20px");
        rightSideActionButtonPlacementLayout.addComponent(rightSideActionButtonsSpacingLayout2);

        Button addRelationTypePropertyButton=new Button("添加类型属性");
        addRelationTypePropertyButton.setIcon(FontAwesome.PLUS_CIRCLE);
        addRelationTypePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addRelationTypePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        addRelationTypePropertyButton.addStyleName("ui_appElementBottomSpacing");
        addRelationTypePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeAddRelationTypePropertyOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(addRelationTypePropertyButton);

        Label spaceDivLabel2=new Label("|");
        rightSideActionButtonPlacementLayout. addComponent(spaceDivLabel2);

        this.editRelationTypePropertyAliasNameButton =new Button("修改属性别名");
        this.editRelationTypePropertyAliasNameButton.setIcon(FontAwesome.EDIT);
        this.editRelationTypePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.editRelationTypePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.editRelationTypePropertyAliasNameButton.addStyleName("ui_appElementBottomSpacing");
        this.editRelationTypePropertyAliasNameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeEditRelationTypePropertyAliasNameOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(this.editRelationTypePropertyAliasNameButton);

        this.deleteRelationTypePropertyButton =new Button("删除类型属性");
        this.deleteRelationTypePropertyButton.setIcon(FontAwesome.TRASH_O);
        this.deleteRelationTypePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.deleteRelationTypePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.deleteRelationTypePropertyButton.addStyleName("ui_appElementBottomSpacing");
        this.deleteRelationTypePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeDeleteRelationTypePropertyOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(this.deleteRelationTypePropertyButton);

        this.relationTypePropertiesTable = new TreeTable();
        this.relationTypePropertiesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.relationTypePropertiesTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.relationTypePropertiesTable.setSizeFull();
        this.relationTypePropertiesTable.setSelectable(true);
        this.relationTypePropertiesTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.relationTypePropertiesTable.setNullSelectionAllowed(false);

        this.relationTypePropertiesTable.addContainerProperty(PROPERTYNAME_PROPERTY, String.class, "");
        this.relationTypePropertiesTable.addContainerProperty(PROPERTYALIASNAME_PROPERTY, String.class, "");
        this.relationTypePropertiesTable.addContainerProperty(PROPERTYTYPE_PROPERTY, String.class, "");
        this.relationTypePropertiesTable.addContainerProperty(MUSTINPUT_PROPERTY, String.class, "");
        this.relationTypePropertiesTable.addContainerProperty(READONLY_PROPERTY, String.class, "");
        this.relationTypePropertiesTable.addContainerProperty(ALLOWNULL_PROPERTY, String.class, "");

        this.relationTypePropertiesTable.setColumnWidth(PROPERTYTYPE_PROPERTY,100);
        this.relationTypePropertiesTable.setColumnWidth(MUSTINPUT_PROPERTY,70);
        this.relationTypePropertiesTable.setColumnWidth(READONLY_PROPERTY,70);
        this.relationTypePropertiesTable.setColumnWidth(ALLOWNULL_PROPERTY,70);

        this.relationTypePropertiesTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedRelationTypeName=itemClickEvent.getItem().getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                renderRelationTypePropertySelectedUIElements(selectedRelationTypeName);
            }
        });
        this.rightSideUIElementsBox.addComponent(relationTypePropertiesTable);
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void renderRelationTypesManagementInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.currentDiscoverSpaceStatisticMetrics=discoverSpaceStatisticMetrics;
        this.relationTypesTreeTable.getContainerDataSource().removeAllItems();
        clearRelationTypeSelectStatus();
        RelationTypeVO rootRelationTypeVO= InfoDiscoverSpaceOperationUtil.retrieveRootRelationTypeRuntimeInfo(this.discoverSpaceName, discoverSpaceStatisticMetrics);
        List<RelationTypeVO> childRelationTypesList= rootRelationTypeVO.getChildRelationTypesVOList();
        Object[] rootRelationTypeInfo=new Object[]{
                rootRelationTypeVO.getTypeName(),
                "",
                ""+ childRelationTypesList.size(),
                ""+ rootRelationTypeVO.getDescendantRelationTypesNumber(),
                ""+ rootRelationTypeVO.getTypeDataRecordCount()
        };
        final Object rootRelationTypeInfoKey = this.relationTypesTreeTable.addItem(rootRelationTypeInfo, null);
        for(RelationTypeVO currentChildRelationType:childRelationTypesList){
            setRelationTypesTreeTableData(rootRelationTypeInfoKey, currentChildRelationType);
        }
        this.relationTypesTreeTable.setCollapsed(rootRelationTypeInfoKey, false);
        this.relationTypePropertiesTable.setWidth("100%");
    }

    private void setRelationTypesTreeTableData(Object parentTreeDataKey, RelationTypeVO currentRelationTypeVO){
        List<RelationTypeVO> childRelationTypesList= currentRelationTypeVO.getChildRelationTypesVOList();
        Object[] currentRelationTypeInfo=new Object[]{
                currentRelationTypeVO.getTypeName(),
                currentRelationTypeVO.getTypeAliasName(),
                ""+ childRelationTypesList.size(),
                ""+ currentRelationTypeVO.getDescendantRelationTypesNumber(),
                ""+ currentRelationTypeVO.getTypeDataRecordCount()
        };
        final Object currentRelationTypeInfoKey = this.relationTypesTreeTable.addItem(currentRelationTypeInfo, null);
        this.relationTypesTreeTable.setParent(currentRelationTypeInfoKey, parentTreeDataKey);
        if(childRelationTypesList.size()==0){
            this.relationTypesTreeTable.setChildrenAllowed(currentRelationTypeInfoKey, false);
            this.relationTypesTreeTable.setColumnCollapsible(currentRelationTypeInfoKey, false);
        }
        for(RelationTypeVO currentChildRelationType:childRelationTypesList){
            setRelationTypesTreeTableData(currentRelationTypeInfoKey, currentChildRelationType);
        }
    }

    private void clearRelationTypeSelectStatus(){
        this.currentRelationTypePropertiesMap =null;
        this.relationTypePropertiesTable.getContainerDataSource().removeAllItems();
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        this.createChildRelationTypeButton.setEnabled(false);
        this.removeRelationTypeButton.setEnabled(false);
        this.editTypeAliasNameButton.setEnabled(false);
        this.currentSelectedRelationTypeName =null;
    }

    private void renderRelationTypeSelectedUIElements(String relationTypeName){
        if(relationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
            this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        }else{
            this.rightSideUIElementsContainer.removeComponent(this.rightSideUIPromptBox);
            this.rightSideUIElementsContainer.addComponent(this.rightSideUIElementsBox);
            this.currentRelationTypePropertiesMap =null;
            this.relationTypePropertiesTable.getContainerDataSource().removeAllItems();
            List<PropertyTypeVO> relationTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveRelationTypePropertiesInfo(this.discoverSpaceName, relationTypeName);
            this.currentRelationTypePropertiesMap =new HashMap<String,PropertyTypeVO>();
            for(PropertyTypeVO currentPropertyTypeVO:relationTypePropertiesList){
                this.currentRelationTypePropertiesMap.put(currentPropertyTypeVO.getPropertyName(), currentPropertyTypeVO);
                Object[] currentRelationTypePropertiesInfo=new Object[]{
                        " "+currentPropertyTypeVO.getPropertyName(),
                        currentPropertyTypeVO.getPropertyAliasName(),
                        currentPropertyTypeVO.getPropertyType(),
                        ""+ currentPropertyTypeVO.isMandatory(),
                        ""+ currentPropertyTypeVO.isReadOnly(),
                        ""+ currentPropertyTypeVO.isNullable()
                };
                final Object currentRelationTypeInfoKey = this.relationTypePropertiesTable.addItem(currentRelationTypePropertiesInfo, null);

                if(relationTypeName.equals(currentPropertyTypeVO.getPropertySourceOwner())){
                    this.relationTypePropertiesTable.setItemIcon(currentRelationTypeInfoKey, FontAwesome.CIRCLE_O);
                }else{
                    this.relationTypePropertiesTable.setItemIcon(currentRelationTypeInfoKey, FontAwesome.REPLY_ALL);
                }
                this.relationTypePropertiesTable.setChildrenAllowed(currentRelationTypeInfoKey, false);
                this.relationTypePropertiesTable.setColumnCollapsible(currentRelationTypeInfoKey, false);
            }
        }
        this.currentSelectedRelationTypeName =relationTypeName;
        this.createChildRelationTypeButton.setEnabled(true);
        if(relationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            this.removeRelationTypeButton.setEnabled(false);
            this.editTypeAliasNameButton.setEnabled(false);
        }else{
            DataTypeStatisticMetrics relationTypeStatisticMetrics= getRelationTypeStatisticMetrics(relationTypeName);
            if(relationTypeStatisticMetrics==null){
                this.removeRelationTypeButton.setEnabled(false);
                this.editTypeAliasNameButton.setEnabled(false);
            }else{
                long typeDataCount=relationTypeStatisticMetrics.getTypeDataCount();
                int childTypeCount= getChildRelationTypeCount(relationTypeName);
                if(childTypeCount>0){
                    this.removeRelationTypeButton.setEnabled(false);
                }else{
                    this.removeRelationTypeButton.setEnabled(true);
                }
                this.editTypeAliasNameButton.setEnabled(true);
            }
        }
        this.deleteRelationTypePropertyButton.setEnabled(false);
        this.editRelationTypePropertyAliasNameButton.setEnabled(false);
    }

    private void renderRelationTypePropertySelectedUIElements(String propertyName){
        this.currentSelectedRelationTypePropertyName =propertyName.trim();
        this.deleteRelationTypePropertyButton.setEnabled(false);
        this.editRelationTypePropertyAliasNameButton.setEnabled(false);
        if(this.currentRelationTypePropertiesMap !=null){
            PropertyTypeVO currentSelectedPropertyTypeVO=this.currentRelationTypePropertiesMap.get(propertyName.trim());
            if(currentSelectedPropertyTypeVO!=null){
                if(this.currentSelectedRelationTypeName.equals(currentSelectedPropertyTypeVO.getPropertySourceOwner())){
                    this.deleteRelationTypePropertyButton.setEnabled(true);
                    this.editRelationTypePropertyAliasNameButton.setEnabled(true);
                }else{
                    this.deleteRelationTypePropertyButton.setEnabled(false);
                    this.editRelationTypePropertyAliasNameButton.setEnabled(false);
                }
            }
        }
    }

    private void executeCreateRelationTypeOperation(){
        if(this.currentSelectedRelationTypeName !=null){
            CreateRelationTypePanel createRelationTypePanel=new CreateRelationTypePanel(this.currentUserClientInfo);
            createRelationTypePanel.setDiscoverSpaceName(this.discoverSpaceName);
            createRelationTypePanel.setParentRelationType(this.currentSelectedRelationTypeName);
            createRelationTypePanel.setRelationTypesManagementPanel(this);
            final Window window = new Window();
            window.setWidth(450.0f, Unit.PIXELS);
            window.setHeight(240.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(createRelationTypePanel);
            createRelationTypePanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }

    private void executeDeleteRelationTypeOperation(){
        if(this.currentSelectedRelationTypeName !=null){
            boolean isUsedInCommonDataRelationDefinition=
                    InfoDiscoverSpaceOperationUtil.checkInfoUsedInCommonDataRelationMappingDefinition(discoverSpaceName,"RELATION",this.currentSelectedRelationTypeName,null,null);
            if(isUsedInCommonDataRelationDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据属性关联映射规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataDateDimensionDefinition=
                    InfoDiscoverSpaceOperationUtil.checkInfoUsedInDataDateDimensionMappingDefinition(discoverSpaceName,"RELATION",this.currentSelectedRelationTypeName,null);
            if(isUsedInDataDateDimensionDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据与时间维度关联定义规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            //do delete logic
            String deleteConfirmMessage=FontAwesome.INFO.getHtml()+
                    " 请确认是否删除关系类型  <b>"+this.currentSelectedRelationTypeName +"</b>。";
            DataTypeStatisticMetrics relationTypeStatisticMetrics= getRelationTypeStatisticMetrics(this.currentSelectedRelationTypeName);
            long dataTypeCount=relationTypeStatisticMetrics.getTypeDataCount();
            if(dataTypeCount>0){
                deleteConfirmMessage=deleteConfirmMessage+"<span style='color:#CE0000;'>执行删除操作将会永久性的删除所有该类型的关系数据。</span><br/>注意：此项操作不可撤销执行结果。</span>";
            }
            Label confirmMessage= new Label(deleteConfirmMessage, ContentMode.HTML);
            final ConfirmDialog deleteRelationTypeConfirmDialog = new ConfirmDialog();
            deleteRelationTypeConfirmDialog.setConfirmMessage(confirmMessage);

            final String parentRelationTypeName= getParentRelationTypeName(this.currentSelectedRelationTypeName);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteRelationTypeConfirmDialog.close();

                    boolean deleteRelationTypeResult=InfoDiscoverSpaceOperationUtil.deleteRelationType(discoverSpaceName, currentSelectedRelationTypeName);
                    if(deleteRelationTypeResult){
                        updateRelationTypesInfo(parentRelationTypeName, currentSelectedRelationTypeName, true);
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除关系类型成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除关系类型错误",
                                "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            deleteRelationTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(deleteRelationTypeConfirmDialog);
        }
    }

    public void updateRelationTypesInfo(String parentRelationType, String sourceRelationType, boolean isDeleteAction){
        DiscoverSpaceStatisticMetrics newDiscoverSpaceStatisticMetrics=InfoDiscoverSpaceOperationUtil.getDiscoverSpaceStatisticMetrics(this.discoverSpaceName);
        renderRelationTypesManagementInfo(newDiscoverSpaceStatisticMetrics);
        Collection objectIdCollection=this.relationTypesTreeTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.relationTypesTreeTable.getItem(itemId);
            String currentRelationTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            if(currentRelationTypeName.equals(parentRelationType)){
                expandParentsRelationTypes(itemId);
                if(isDeleteAction){
                    //refresh data for delete action
                }else{
                    //refresh data for create action
                    this.renderRelationTypeSelectedUIElements(parentRelationType);
                    this.relationTypesTreeTable.select(itemId);
                    this.relationTypesTreeTable.setCollapsed(itemId, false);
                }
            }
        }
    }

    private void expandParentsRelationTypes(Object targetItemId){
        Object parentItemId=this.relationTypesTreeTable.getParent(targetItemId);
        if(parentItemId!=null){
            this.relationTypesTreeTable.setCollapsed(parentItemId, false);
            expandParentsRelationTypes(parentItemId);
        }
    }

    private String getParentRelationTypeName(String childRelatonTypeName){
        Collection objectIdCollection=this.relationTypesTreeTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.relationTypesTreeTable.getItem(itemId);
            String currentRelationTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            if(currentRelationTypeName.equals(childRelatonTypeName)){
                Object parentItemId=this.relationTypesTreeTable.getParent(itemId);
                Item parentItem=this.relationTypesTreeTable.getItem(parentItemId);
                return parentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            }
        }
        return null;
    }

    private DataTypeStatisticMetrics getRelationTypeStatisticMetrics(String targetRelationTypeName){
        List<DataTypeStatisticMetrics> relationsStatisticMetricsList=this.currentDiscoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:relationsStatisticMetricsList){
            String currentRelationTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            if(currentRelationTypeName.equals(targetRelationTypeName)){
                return currentDataTypeStatisticMetrics;
            }
        }
        return null;
    }

    private int getChildRelationTypeCount(String childRelationTypeName){
        Collection objectIdCollection=this.relationTypesTreeTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.relationTypesTreeTable.getItem(itemId);
            String currentRelationTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            if(currentRelationTypeName.equals(childRelationTypeName)){
                String childTypeCountStr=currentItem.getItemProperty(CHILDTYPECOUNT_PROPERTY).getValue().toString();
                return Integer.parseInt(childTypeCountStr);
            }
        }
        return 0;
    }

    private void executeAddRelationTypePropertyOperation(){
        if(this.currentSelectedRelationTypeName !=null){
            CreateTypePropertyPanel createTypePropertyPanel=new CreateTypePropertyPanel(this.currentUserClientInfo);
            createTypePropertyPanel.setDiscoverSpaceName(this.discoverSpaceName);
            createTypePropertyPanel.setPropertyTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION);
            createTypePropertyPanel.setTypeName(this.currentSelectedRelationTypeName);
            createTypePropertyPanel.setCreateTypePropertyPanelInvoker(this);

            final Window window = new Window();
            window.setHeight(380.0f, Unit.PIXELS);
            window.setWidth(550.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(createTypePropertyPanel);
            createTypePropertyPanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }

    private void executeDeleteRelationTypePropertyOperation(){
        if(this.currentSelectedRelationTypeName !=null&&this.currentSelectedRelationTypePropertyName !=null){
            //do delete logic
            Label confirmMessage= new Label(FontAwesome.INFO.getHtml()+
                    " 请确认是否删除关系类型 "+this.currentSelectedRelationTypeName +" 中的属性 <b>"+this.currentSelectedRelationTypePropertyName +"</b>.", ContentMode.HTML);
            final ConfirmDialog deleteRelationTypeConfirmDialog = new ConfirmDialog();
            deleteRelationTypeConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteRelationTypeConfirmDialog.close();
                    boolean deleteRelationTypePropertyResult=
                            InfoDiscoverSpaceOperationUtil.deleteRelationTypeProperty(discoverSpaceName, currentSelectedRelationTypeName, currentSelectedRelationTypePropertyName);
                    if(deleteRelationTypePropertyResult){
                        renderRelationTypeSelectedUIElements(currentSelectedRelationTypeName);
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除关系类型属性成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除关系类型属性错误",
                                "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            deleteRelationTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(deleteRelationTypeConfirmDialog);
        }
    }

    @Override
    public void createTypePropertyActionFinish(boolean actionResult) {
        if(this.currentSelectedRelationTypeName !=null){
            renderRelationTypeSelectedUIElements(this.currentSelectedRelationTypeName);
        }
    }

    private void executeEditRelationTypeAliasNameOperation(){
        if(this.currentSelectedRelationTypeName!=null){
            String currentAliasName=InfoDiscoverSpaceOperationUtil.getTypeKindAliasName(this.discoverSpaceName,InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,this.currentSelectedRelationTypeName);
            AliasNameEditPanel aliasNameEditPanel=new AliasNameEditPanel(this.currentUserClientInfo,currentAliasName);
            aliasNameEditPanel.setAliasNameType(AliasNameEditPanel.AliasNameType_TYPE);
            aliasNameEditPanel.setAliasNameEditPanelInvoker(this);
            final Window window = new Window();
            window.setHeight(200.0f, Unit.PIXELS);
            window.setWidth(450.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(aliasNameEditPanel);
            aliasNameEditPanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }

    @Override
    public void editTypeAliasNameAction(String aliasName) {
        boolean updateTypeAliasNameResult=InfoDiscoverSpaceOperationUtil.updateTypeKindAliasName(this.discoverSpaceName,InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,this.currentSelectedRelationTypeName,aliasName);
        if(updateTypeAliasNameResult){
            Notification resultNotification = new Notification("更新数据操作成功",
                    "修改类型别名成功", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());

            Collection objectIdCollection=this.relationTypesTreeTable.getContainerDataSource().getItemIds();
            Iterator idIterator=objectIdCollection.iterator();
            while(idIterator.hasNext()){
                Object itemId=idIterator.next();
                Item currentItem=this.relationTypesTreeTable.getItem(itemId);
                String currentDimensionTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
                if(currentDimensionTypeName.equals(this.currentSelectedRelationTypeName)){
                    currentItem.getItemProperty(ALIASNAME_PROPERTY).setValue(aliasName);
                }
            }
        }else{
            Notification errorNotification = new Notification("修改类型别名错误",
                    "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    private void executeEditRelationTypePropertyAliasNameOperation(){
        if(this.currentSelectedRelationTypeName!=null&&this.currentSelectedRelationTypePropertyName!=null){
            String currentAliasName=InfoDiscoverSpaceOperationUtil.
                    getTypePropertyAliasName(this.discoverSpaceName,InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,this.currentSelectedRelationTypeName,this.currentSelectedRelationTypePropertyName);
            AliasNameEditPanel aliasNameEditPanel=new AliasNameEditPanel(this.currentUserClientInfo,currentAliasName);
            aliasNameEditPanel.setAliasNameType(AliasNameEditPanel.AliasNameType_PROPERTY);
            aliasNameEditPanel.setAliasNameEditPanelInvoker(this);
            final Window window = new Window();
            window.setHeight(200.0f, Unit.PIXELS);
            window.setWidth(450.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(aliasNameEditPanel);
            aliasNameEditPanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }

    @Override
    public void editTypePropertyAliasNameAction(String aliasName) {
        boolean updateTypeAliasNameResult=InfoDiscoverSpaceOperationUtil.
                updateTypePropertyAliasName(this.discoverSpaceName,InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,this.currentSelectedRelationTypeName,this.currentSelectedRelationTypePropertyName,aliasName);
        if(updateTypeAliasNameResult){
            Notification resultNotification = new Notification("更新数据操作成功",
                    "修改属性别名成功", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());

            Collection objectIdCollection=this.relationTypePropertiesTable.getContainerDataSource().getItemIds();
            Iterator idIterator=objectIdCollection.iterator();
            while(idIterator.hasNext()){
                Object itemId=idIterator.next();
                Item currentItem=this.relationTypePropertiesTable.getItem(itemId);
                String currentDimensionTypePropertyName=currentItem.getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                if(currentDimensionTypePropertyName.trim().equals(this.currentSelectedRelationTypePropertyName)){
                    currentItem.getItemProperty(PROPERTYALIASNAME_PROPERTY).setValue(""+aliasName);
                }
            }
        }else{
            Notification errorNotification = new Notification("修改属性别名错误",
                    "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }
}
