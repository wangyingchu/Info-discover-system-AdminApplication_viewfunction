package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.dimensionManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.DimensionTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;

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
 * Created by wangychu on 10/6/16.
 */
public class DimensionTypesManagementPanel extends VerticalLayout implements CreateTypePropertyPanelInvoker,AliasNameEditPanelInvoker {
    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private TreeTable dimensionTypesTreeTable;
    private TreeTable dimensionTypePropertiesTable;

    private String NAME_PROPERTY="维度类型名称";
    private String ALIASNAME_PROPERTY="类型别名";
    private String CHILDTYPECOUNT_PROPERTY="子类型数量";
    private String DESCENDANTTYPECOUNT_PROPERTY="后代类型数量";
    private String DIMENSIONDATAFULLCOUNT_PROPERTY="类型数据总量";

    private String PROPERTYNAME_PROPERTY="类型属性名";
    private String PROPERTYALIASNAME_PROPERTY="属性别名";
    private String PROPERTYTYPE_PROPERTY="属性数据类型";
    private String MUSTINPUT_PROPERTY="必填属性";
    private String READONLY_PROPERTY="只读属性";
    private String ALLOWNULL_PROPERTY="允许空值";

    private VerticalLayout rightSideUIElementsContainer;
    private VerticalLayout rightSideUIElementsBox;
    private VerticalLayout rightSideUIPromptBox;

    private Button createChildDimensionTypeButton;
    private Button removeDimensionTypeButton;
    private Button editTypeAliasNameButton;
    private Button deleteDimensionTypePropertyButton;
    private Button editDimensionTypePropertyAliasNameButton;
    private String currentSelectedDimensionTypeName;
    private String currentSelectedDimensionTypePropertyName;
    private DiscoverSpaceStatisticMetrics currentDiscoverSpaceStatisticMetrics;
    private Map<String,PropertyTypeVO> currentDimensionTypePropertiesMap;

    public DimensionTypesManagementPanel(UserClientInfo currentUserClientInfo){
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

        Label leftSideTitle= new Label(FontAwesome.CODE_FORK.getHtml() +" 维度类型", ContentMode.HTML);
        leftSideActionButtonPlacementLayout.addComponent(leftSideTitle);

        HorizontalLayout leftSideActionButtonsSpacingLayout2=new HorizontalLayout();
        leftSideActionButtonsSpacingLayout2.setWidth("20px");
        leftSideActionButtonPlacementLayout.addComponent(leftSideActionButtonsSpacingLayout2);

        this.createChildDimensionTypeButton=new Button("创建子维度类型");
        this.createChildDimensionTypeButton.setEnabled(false);
        this.createChildDimensionTypeButton.setIcon(FontAwesome.PLUS_CIRCLE);
        this.createChildDimensionTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.createChildDimensionTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.createChildDimensionTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.createChildDimensionTypeButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeCreateDimensionTypeOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(createChildDimensionTypeButton);

        Label spaceDivLabel1=new Label("|");
        leftSideActionButtonPlacementLayout. addComponent(spaceDivLabel1);

        this.editTypeAliasNameButton=new Button("修改类型别名");
        this.editTypeAliasNameButton.setEnabled(false);
        this.editTypeAliasNameButton.setIcon(FontAwesome.EDIT);
        this.editTypeAliasNameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.editTypeAliasNameButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.editTypeAliasNameButton.addStyleName("ui_appElementBottomSpacing");
        this.editTypeAliasNameButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeEditDimensionTypeAliasNameOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(editTypeAliasNameButton);

        this.removeDimensionTypeButton=new Button("删除维度类型");
        this.removeDimensionTypeButton.setEnabled(false);
        this.removeDimensionTypeButton.setIcon(FontAwesome.TRASH_O);
        this.removeDimensionTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.removeDimensionTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.removeDimensionTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.removeDimensionTypeButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeDeleteDimensionTypeOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(removeDimensionTypeButton);

        this.dimensionTypesTreeTable = new TreeTable();
        this.dimensionTypesTreeTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.dimensionTypesTreeTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.dimensionTypesTreeTable.setSizeFull();
        this.dimensionTypesTreeTable.setSelectable(true);
        this.dimensionTypesTreeTable.setHeight(dataDisplayElementHeight,Unit.PIXELS);
        this.dimensionTypesTreeTable.setNullSelectionAllowed(false);

        this.dimensionTypesTreeTable.addContainerProperty(NAME_PROPERTY, String.class, "");
        this.dimensionTypesTreeTable.addContainerProperty(ALIASNAME_PROPERTY, String.class, "");
        this.dimensionTypesTreeTable.addContainerProperty(CHILDTYPECOUNT_PROPERTY, String.class, "");
        this.dimensionTypesTreeTable.addContainerProperty(DESCENDANTTYPECOUNT_PROPERTY, String.class, "");
        this.dimensionTypesTreeTable.addContainerProperty(DIMENSIONDATAFULLCOUNT_PROPERTY, String.class, "");

        this.dimensionTypesTreeTable.setColumnWidth(CHILDTYPECOUNT_PROPERTY,90);
        this.dimensionTypesTreeTable.setColumnWidth(DESCENDANTTYPECOUNT_PROPERTY,90);
        this.dimensionTypesTreeTable.setColumnWidth(DIMENSIONDATAFULLCOUNT_PROPERTY,90);

        this.dimensionTypesTreeTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedDimensionTypeName=itemClickEvent.getItem().getItemProperty(NAME_PROPERTY).getValue().toString();
                renderDimensionTypeSelectedUIElements(selectedDimensionTypeName);
            }
        });
        leftSideUIElementsContainer.addComponent(dimensionTypesTreeTable);
        leftSideUIElementsContainer.addStyleName("ui_appElementRightSideSpacing");

        //right side elements
        this.rightSideUIElementsContainer=new VerticalLayout();
        elementPlacementLayout.addComponent(rightSideUIElementsContainer);

        this.rightSideUIPromptBox=new VerticalLayout();
        VerticalLayout messageHeightSpaceDiv=new VerticalLayout();
        messageHeightSpaceDiv.setHeight(30,Unit.PIXELS);
        this.rightSideUIPromptBox.addComponent(messageHeightSpaceDiv);
        Label functionMessage = new Label( FontAwesome.LIST_UL.getHtml()+" 选定维度类型包含的属性信息。", ContentMode.HTML);
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

        Button addDimensionTypePropertyButton=new Button("添加类型属性");
        addDimensionTypePropertyButton.setIcon(FontAwesome.PLUS_CIRCLE);
        addDimensionTypePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addDimensionTypePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        addDimensionTypePropertyButton.addStyleName("ui_appElementBottomSpacing");
        addDimensionTypePropertyButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeAddDimensionTypePropertyOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(addDimensionTypePropertyButton);

        Label spaceDivLabel2=new Label("|");
        rightSideActionButtonPlacementLayout. addComponent(spaceDivLabel2);

        this.editDimensionTypePropertyAliasNameButton=new Button("修改属性别名");
        this.editDimensionTypePropertyAliasNameButton.setIcon(FontAwesome.EDIT);
        this.editDimensionTypePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.editDimensionTypePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.editDimensionTypePropertyAliasNameButton.addStyleName("ui_appElementBottomSpacing");
        this.editDimensionTypePropertyAliasNameButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeEditDimensionTypePropertyAliasNameOperation();
            }
        });
        rightSideActionButtonPlacementLayout.addComponent(this.editDimensionTypePropertyAliasNameButton);

        this.deleteDimensionTypePropertyButton=new Button("删除类型属性");
        this.deleteDimensionTypePropertyButton.setIcon(FontAwesome.TRASH_O);
        this.deleteDimensionTypePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.deleteDimensionTypePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.deleteDimensionTypePropertyButton.addStyleName("ui_appElementBottomSpacing");
        this.deleteDimensionTypePropertyButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeDeleteDimensionTypePropertyOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(this.deleteDimensionTypePropertyButton);

        this.dimensionTypePropertiesTable = new TreeTable();
        this.dimensionTypePropertiesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.dimensionTypePropertiesTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.dimensionTypePropertiesTable.setSizeFull();
        this.dimensionTypePropertiesTable.setSelectable(true);
        this.dimensionTypePropertiesTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.dimensionTypePropertiesTable.setNullSelectionAllowed(false);
        this.dimensionTypePropertiesTable.addContainerProperty(PROPERTYNAME_PROPERTY, String.class, "");
        this.dimensionTypePropertiesTable.addContainerProperty(PROPERTYALIASNAME_PROPERTY, String.class, "");
        this.dimensionTypePropertiesTable.addContainerProperty(PROPERTYTYPE_PROPERTY, String.class, "");
        this.dimensionTypePropertiesTable.addContainerProperty(MUSTINPUT_PROPERTY, String.class, "");
        this.dimensionTypePropertiesTable.addContainerProperty(READONLY_PROPERTY, String.class, "");
        this.dimensionTypePropertiesTable.addContainerProperty(ALLOWNULL_PROPERTY, String.class, "");

        this.dimensionTypePropertiesTable.setColumnWidth(PROPERTYTYPE_PROPERTY,100);
        this.dimensionTypePropertiesTable.setColumnWidth(MUSTINPUT_PROPERTY,70);
        this.dimensionTypePropertiesTable.setColumnWidth(READONLY_PROPERTY,70);
        this.dimensionTypePropertiesTable.setColumnWidth(ALLOWNULL_PROPERTY,70);

        this.dimensionTypePropertiesTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedDimensionTypeName=itemClickEvent.getItem().getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                renderDimensionTypePropertySelectedUIElements(selectedDimensionTypeName);
            }
        });
        this.rightSideUIElementsBox.addComponent(dimensionTypePropertiesTable);
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void renderDimensionTypesManagementInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.currentDiscoverSpaceStatisticMetrics=discoverSpaceStatisticMetrics;
        this.dimensionTypesTreeTable.getContainerDataSource().removeAllItems();
        clearDimensionTypeSelectStatus();
        DimensionTypeVO rootDimensionTypeVO=InfoDiscoverSpaceOperationUtil.retrieveRootDimensionTypeRuntimeInfo(this.discoverSpaceName, discoverSpaceStatisticMetrics);
        List<DimensionTypeVO> childDimensionTypesList= rootDimensionTypeVO.getChildDimensionTypesVOList();
        Object[] rootDimensionTypeInfo=new Object[]{
                rootDimensionTypeVO.getTypeName(),
                "",
                ""+ childDimensionTypesList.size(),
                ""+ rootDimensionTypeVO.getDescendantDimensionTypesNumber(),
                ""+ rootDimensionTypeVO.getTypeDataRecordCount()
        };
        final Object rootDimensionTypeInfoKey = this.dimensionTypesTreeTable.addItem(rootDimensionTypeInfo, null);
        for(DimensionTypeVO currentChildDimensionType:childDimensionTypesList){
            setDimensionTypesTreeTableData(rootDimensionTypeInfoKey,currentChildDimensionType);
        }
        this.dimensionTypesTreeTable.setCollapsed(rootDimensionTypeInfoKey, false);
        this.dimensionTypePropertiesTable.setWidth("100%");
    }

    private void setDimensionTypesTreeTableData(Object parentTreeDataKey,DimensionTypeVO currentDimensionTypeVO){
        List<DimensionTypeVO> childDimensionTypesList= currentDimensionTypeVO.getChildDimensionTypesVOList();
        Object[] currentDimensionTypeInfo=new Object[]{
                currentDimensionTypeVO.getTypeName(),
                currentDimensionTypeVO.getTypeAliasName(),
                ""+ childDimensionTypesList.size(),
                ""+ currentDimensionTypeVO.getDescendantDimensionTypesNumber(),
                ""+ currentDimensionTypeVO.getTypeDataRecordCount()
        };
        final Object currentDimensionTypeInfoKey = this.dimensionTypesTreeTable.addItem(currentDimensionTypeInfo, null);
        this.dimensionTypesTreeTable.setParent(currentDimensionTypeInfoKey, parentTreeDataKey);
        if(childDimensionTypesList.size()==0){
            this.dimensionTypesTreeTable.setChildrenAllowed(currentDimensionTypeInfoKey,false);
            this.dimensionTypesTreeTable.setColumnCollapsible(currentDimensionTypeInfoKey,false);
        }
        for(DimensionTypeVO currentChildDimensionType:childDimensionTypesList){
            setDimensionTypesTreeTableData(currentDimensionTypeInfoKey,currentChildDimensionType);
        }
    }

    private void clearDimensionTypeSelectStatus(){
        this.currentDimensionTypePropertiesMap=null;
        this.dimensionTypePropertiesTable.getContainerDataSource().removeAllItems();
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        this.createChildDimensionTypeButton.setEnabled(false);
        this.removeDimensionTypeButton.setEnabled(false);
        this.editTypeAliasNameButton.setEnabled(false);
        this.currentSelectedDimensionTypeName=null;
    }

    private void renderDimensionTypeSelectedUIElements(String dimensionTypeName){
        if(dimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
            this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
            this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        }else{
            this.rightSideUIElementsContainer.removeComponent(this.rightSideUIPromptBox);
            this.rightSideUIElementsContainer.addComponent(this.rightSideUIElementsBox);
            this.currentDimensionTypePropertiesMap=null;
            this.dimensionTypePropertiesTable.getContainerDataSource().removeAllItems();
            List<PropertyTypeVO> dimensionTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveDimensionTypePropertiesInfo(this.discoverSpaceName,dimensionTypeName);
            this.currentDimensionTypePropertiesMap=new HashMap<String,PropertyTypeVO>();
            for(PropertyTypeVO currentPropertyTypeVO:dimensionTypePropertiesList){
                this.currentDimensionTypePropertiesMap.put(currentPropertyTypeVO.getPropertyName(),currentPropertyTypeVO);
                Object[] currentDimensionTypePropertiesInfo=new Object[]{
                        " "+currentPropertyTypeVO.getPropertyName(),
                        currentPropertyTypeVO.getPropertyAliasName(),
                        currentPropertyTypeVO.getPropertyType(),
                        ""+ currentPropertyTypeVO.isMandatory(),
                        ""+ currentPropertyTypeVO.isReadOnly(),
                        ""+ currentPropertyTypeVO.isNullable()
                };
                final Object currentDimensionTypeInfoKey = this.dimensionTypePropertiesTable.addItem(currentDimensionTypePropertiesInfo, null);

                if(dimensionTypeName.equals(currentPropertyTypeVO.getPropertySourceOwner())){
                    this.dimensionTypePropertiesTable.setItemIcon(currentDimensionTypeInfoKey,FontAwesome.CIRCLE_O);
                }else{
                    this.dimensionTypePropertiesTable.setItemIcon(currentDimensionTypeInfoKey,FontAwesome.REPLY_ALL);
                }
                this.dimensionTypePropertiesTable.setChildrenAllowed(currentDimensionTypeInfoKey,false);
                this.dimensionTypePropertiesTable.setColumnCollapsible(currentDimensionTypeInfoKey,false);
            }
        }
        this.currentSelectedDimensionTypeName=dimensionTypeName;
        this.createChildDimensionTypeButton.setEnabled(true);
        if(dimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
            this.removeDimensionTypeButton.setEnabled(false);
            this.editTypeAliasNameButton.setEnabled(false);
        }else{
            DataTypeStatisticMetrics dimensionTypeStatisticMetrics=getDimensionTypeStatisticMetrics(dimensionTypeName);
            if(dimensionTypeStatisticMetrics==null){
                this.removeDimensionTypeButton.setEnabled(false);
                this.editTypeAliasNameButton.setEnabled(false);
            }else{
                long typeDataCount=dimensionTypeStatisticMetrics.getTypeDataCount();
                int childTypeCount=getChildDimensionTypeCount(dimensionTypeName);
                if(childTypeCount>0){
                    this.removeDimensionTypeButton.setEnabled(false);
                }else{
                    this.removeDimensionTypeButton.setEnabled(true);
                }
                this.editTypeAliasNameButton.setEnabled(true);
            }
        }
        this.deleteDimensionTypePropertyButton.setEnabled(false);
        this.editDimensionTypePropertyAliasNameButton.setEnabled(false);
    }

    private void renderDimensionTypePropertySelectedUIElements(String propertyName){
        this.currentSelectedDimensionTypePropertyName=propertyName.trim();
        this.deleteDimensionTypePropertyButton.setEnabled(false);
        this.editDimensionTypePropertyAliasNameButton.setEnabled(false);
        if(this.currentDimensionTypePropertiesMap!=null){
            PropertyTypeVO currentSelectedPropertyTypeVO=this.currentDimensionTypePropertiesMap.get(propertyName.trim());
            if(currentSelectedPropertyTypeVO!=null){
                if(this.currentSelectedDimensionTypeName.equals(currentSelectedPropertyTypeVO.getPropertySourceOwner())){
                    this.deleteDimensionTypePropertyButton.setEnabled(true);
                    editDimensionTypePropertyAliasNameButton.setEnabled(true);
                }else{
                    this.deleteDimensionTypePropertyButton.setEnabled(false);
                    this.editDimensionTypePropertyAliasNameButton.setEnabled(false);
                }
            }
        }
    }

    private void executeCreateDimensionTypeOperation(){
        if(this.currentSelectedDimensionTypeName!=null){
            CreateDimensionTypePanel createDimensionTypePanel=new CreateDimensionTypePanel(this.currentUserClientInfo);
            createDimensionTypePanel.setDiscoverSpaceName(this.discoverSpaceName);
            createDimensionTypePanel.setParentDimensionType(this.currentSelectedDimensionTypeName);
            createDimensionTypePanel.setDimensionTypesManagementPanel(this);
            final Window window = new Window();
            window.setWidth(450.0f, Unit.PIXELS);
            window.setHeight(240.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(createDimensionTypePanel);
            createDimensionTypePanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }

    private void executeDeleteDimensionTypeOperation(){
        if(this.currentSelectedDimensionTypeName!=null){
            boolean isUsedInCommonDataRelationDefinition=
                    InfoDiscoverSpaceOperationUtil.checkInfoUsedInCommonDataRelationMappingDefinition(this.discoverSpaceName,"DIMENSION",this.currentSelectedDimensionTypeName,null,null);
            if(isUsedInCommonDataRelationDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据属性关联映射规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataDateDimensionDefinition=
                    InfoDiscoverSpaceOperationUtil.checkInfoUsedInDataDateDimensionMappingDefinition(this.discoverSpaceName,"DIMENSION",this.currentSelectedDimensionTypeName,null);
            if(isUsedInDataDateDimensionDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据与时间维度关联定义规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataPropertiesDuplicateDefinition=
                    InfoDiscoverSpaceOperationUtil.checkInfoUsedInDataPropertiesDuplicateMappingDefinition(this.discoverSpaceName,"DIMENSION",this.currentSelectedDimensionTypeName,null,null);
            if(isUsedInDataPropertiesDuplicateDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据属性复制规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            //do delete logic
            String deleteConfirmMessage=FontAwesome.INFO.getHtml()+
                    " 请确认是否删除维度类型  <b>"+this.currentSelectedDimensionTypeName +"</b>。";
            DataTypeStatisticMetrics dimensionTypeStatisticMetrics=getDimensionTypeStatisticMetrics(this.currentSelectedDimensionTypeName);
            long dataTypeCount=dimensionTypeStatisticMetrics.getTypeDataCount();
            if(dataTypeCount>0){
                deleteConfirmMessage=deleteConfirmMessage+"<span style='color:#CE0000;'>执行删除操作将会永久性的删除所有该类型的维度数据。</span><br/>注意：此项操作不可撤销执行结果。</span>";
            }
            Label confirmMessage= new Label(deleteConfirmMessage, ContentMode.HTML);
            final ConfirmDialog deleteDimensionTypeConfirmDialog = new ConfirmDialog();
            deleteDimensionTypeConfirmDialog.setConfirmMessage(confirmMessage);

            final DimensionTypesManagementPanel self=this;
            final String parentDimensionTypeName=getParentDimensionTypeName(this.currentSelectedDimensionTypeName);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteDimensionTypeConfirmDialog.close();

                    boolean deleteDimensionTypeResult=InfoDiscoverSpaceOperationUtil.deleteDimensionType(self.discoverSpaceName,currentSelectedDimensionTypeName);
                    if(deleteDimensionTypeResult){
                        self.updateDimensionTypesInfo(parentDimensionTypeName,currentSelectedDimensionTypeName,true);
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除维度类型成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除维度类型错误",
                                "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            deleteDimensionTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(deleteDimensionTypeConfirmDialog);
        }
    }

    public void updateDimensionTypesInfo(String parentDimensionType,String sourceDimensionType,boolean isDeleteAction){
        DiscoverSpaceStatisticMetrics newDiscoverSpaceStatisticMetrics=InfoDiscoverSpaceOperationUtil.getDiscoverSpaceStatisticMetrics(this.discoverSpaceName);
        renderDimensionTypesManagementInfo(newDiscoverSpaceStatisticMetrics);
        Collection objectIdCollection=this.dimensionTypesTreeTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.dimensionTypesTreeTable.getItem(itemId);
            String currentDimensionTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            if(currentDimensionTypeName.equals(parentDimensionType)){
                expandParentsDimensionTypes(itemId);
                if(isDeleteAction){
                    //refresh data for delete action
                }else{
                    //refresh data for create action
                    this.renderDimensionTypeSelectedUIElements(parentDimensionType);
                    this.dimensionTypesTreeTable.select(itemId);
                    this.dimensionTypesTreeTable.setCollapsed(itemId, false);
                }
            }
        }
    }

    private void expandParentsDimensionTypes(Object targetItemId){
        Object parentItemId=this.dimensionTypesTreeTable.getParent(targetItemId);
        if(parentItemId!=null){
            this.dimensionTypesTreeTable.setCollapsed(parentItemId, false);
            expandParentsDimensionTypes(parentItemId);
        }
    }

    private String getParentDimensionTypeName(String childDimensionTypeName){
        Collection objectIdCollection=this.dimensionTypesTreeTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.dimensionTypesTreeTable.getItem(itemId);
            String currentDimensionTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            if(currentDimensionTypeName.equals(childDimensionTypeName)){
                Object parentItemId=this.dimensionTypesTreeTable.getParent(itemId);
                Item parentItem=this.dimensionTypesTreeTable.getItem(parentItemId);
               return parentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            }
        }
        return null;
    }

    private DataTypeStatisticMetrics getDimensionTypeStatisticMetrics(String targetDimensionTypeName){
        List<DataTypeStatisticMetrics> dimensionsStatisticMetricsList=this.currentDiscoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:dimensionsStatisticMetricsList){
            String currentDimensionTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION,"");
            if(currentDimensionTypeName.equals(targetDimensionTypeName)){
                return currentDataTypeStatisticMetrics;
            }
        }
        return null;
    }

    private int getChildDimensionTypeCount(String childDimensionTypeName){
        Collection objectIdCollection=this.dimensionTypesTreeTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.dimensionTypesTreeTable.getItem(itemId);
            String currentDimensionTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            if(currentDimensionTypeName.equals(childDimensionTypeName)){
                String childTypeCountStr=currentItem.getItemProperty(CHILDTYPECOUNT_PROPERTY).getValue().toString();
                return Integer.parseInt(childTypeCountStr);
            }
        }
        return 0;
    }

    private void executeAddDimensionTypePropertyOperation(){
        if(this.currentSelectedDimensionTypeName!=null){
            CreateTypePropertyPanel createTypePropertyPanel=new CreateTypePropertyPanel(this.currentUserClientInfo);
            createTypePropertyPanel.setDiscoverSpaceName(this.discoverSpaceName);
            createTypePropertyPanel.setPropertyTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
            createTypePropertyPanel.setTypeName(this.currentSelectedDimensionTypeName);
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

    private void executeDeleteDimensionTypePropertyOperation(){
        if(this.currentSelectedDimensionTypeName!=null&&this.currentSelectedDimensionTypePropertyName!=null){
            boolean isUsedInCommonDataRelationDefinition=
                    InfoDiscoverSpaceOperationUtil.checkInfoUsedInCommonDataRelationMappingDefinition(this.discoverSpaceName,"DIMENSION",this.currentSelectedDimensionTypeName,this.currentSelectedDimensionTypePropertyName,null);
            if(isUsedInCommonDataRelationDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据属性关联映射规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataDateDimensionDefinition=
                    InfoDiscoverSpaceOperationUtil.checkInfoUsedInDataDateDimensionMappingDefinition(this.discoverSpaceName,"DIMENSION",this.currentSelectedDimensionTypeName,this.currentSelectedDimensionTypePropertyName);
            if(isUsedInDataDateDimensionDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据与时间维度关联定义规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataPropertiesDuplicateDefinition=
                    InfoDiscoverSpaceOperationUtil.checkInfoUsedInDataPropertiesDuplicateMappingDefinition(this.discoverSpaceName,"DIMENSION",this.currentSelectedDimensionTypeName,this.currentSelectedDimensionTypePropertyName,null);
            if(isUsedInDataPropertiesDuplicateDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据属性复制规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            //do delete logic
            Label confirmMessage= new Label(FontAwesome.INFO.getHtml()+
                    " 请确认是否删除维度类型 "+this.currentSelectedDimensionTypeName+" 中的属性 <b>"+this.currentSelectedDimensionTypePropertyName +"</b>.", ContentMode.HTML);
            final ConfirmDialog deleteDimensionTypeConfirmDialog = new ConfirmDialog();
            deleteDimensionTypeConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteDimensionTypeConfirmDialog.close();
                    boolean deleteDimensionTypePropertyResult=
                            InfoDiscoverSpaceOperationUtil.deleteDimensionTypeProperty(discoverSpaceName,currentSelectedDimensionTypeName,currentSelectedDimensionTypePropertyName);
                    if(deleteDimensionTypePropertyResult){
                        renderDimensionTypeSelectedUIElements(currentSelectedDimensionTypeName);
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除维度类型属性成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除维度类型属性错误",
                                "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            deleteDimensionTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(deleteDimensionTypeConfirmDialog);
        }
    }

    @Override
    public void createTypePropertyActionFinish(boolean actionResult) {
        if(this.currentSelectedDimensionTypeName!=null){
            renderDimensionTypeSelectedUIElements(this.currentSelectedDimensionTypeName);
        }
    }

    private void executeEditDimensionTypeAliasNameOperation(){
        if(this.currentSelectedDimensionTypeName!=null){
            String currentAliasName=InfoDiscoverSpaceOperationUtil.getTypeKindAliasName(this.discoverSpaceName,InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION,this.currentSelectedDimensionTypeName);
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
        boolean updateTypeAliasNameResult=InfoDiscoverSpaceOperationUtil.updateTypeKindAliasName(this.discoverSpaceName,InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION,this.currentSelectedDimensionTypeName,aliasName);
        if(updateTypeAliasNameResult){
            Notification resultNotification = new Notification("更新数据操作成功",
                    "修改类型别名成功", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());

            Collection objectIdCollection=this.dimensionTypesTreeTable.getContainerDataSource().getItemIds();
            Iterator idIterator=objectIdCollection.iterator();
            while(idIterator.hasNext()){
                Object itemId=idIterator.next();
                Item currentItem=this.dimensionTypesTreeTable.getItem(itemId);
                String currentDimensionTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
                if(currentDimensionTypeName.equals(this.currentSelectedDimensionTypeName)){
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

    private void executeEditDimensionTypePropertyAliasNameOperation(){
        if(this.currentSelectedDimensionTypeName!=null&&this.currentSelectedDimensionTypePropertyName!=null){
            String currentAliasName=InfoDiscoverSpaceOperationUtil.
                    getTypePropertyAliasName(this.discoverSpaceName,InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION,this.currentSelectedDimensionTypeName,this.currentSelectedDimensionTypePropertyName);
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
                updateTypePropertyAliasName(this.discoverSpaceName,InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION,this.currentSelectedDimensionTypeName,this.currentSelectedDimensionTypePropertyName,aliasName);
        if(updateTypeAliasNameResult){
            Notification resultNotification = new Notification("更新数据操作成功",
                    "修改属性别名成功", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());

            Collection objectIdCollection=this.dimensionTypePropertiesTable.getContainerDataSource().getItemIds();
            Iterator idIterator=objectIdCollection.iterator();
            while(idIterator.hasNext()){
                Object itemId=idIterator.next();
                Item currentItem=this.dimensionTypePropertiesTable.getItem(itemId);
                String currentDimensionTypePropertyName=currentItem.getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                if(currentDimensionTypePropertyName.trim().equals(this.currentSelectedDimensionTypePropertyName)){
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
