package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.relationTypeManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.RelationTypeDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.SolutionTypePropertyTypeDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.commonUseElement.CreateTypePropertyDefinitionPanel;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.AliasNameEditPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.AliasNameEditPanelInvoker;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.CreateTypePropertyPanelInvoker;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
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
 * Created by wangychu on 6/28/17.
 */
public class RelationTypeDefinitionsManagementPanel extends VerticalLayout implements AliasNameEditPanelInvoker,CreateTypePropertyPanelInvoker {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private TreeTable relationTypesTreeTable;
    private TreeTable relationTypePropertiesTable;

    private String NAME_PROPERTY="关系类型名称";
    private String ALIASNAME_PROPERTY="类型别名";

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
    private Button editTypeAliasNameButton;
    private Button deleteRelationTypePropertyButton;
    private Button editRelationTypePropertyAliasNameButton;
    private String currentSelectedRelationTypeName;
    private String currentSelectedRelationTypePropertyName;
    private Map<String,SolutionTypePropertyTypeDefinitionVO> currentRelationTypePropertiesMap;
    private List<RelationTypeDefinitionVO> relationTypeDefinitionsList;
    private List<String> alreadyRenderedTypeList;

    public RelationTypeDefinitionsManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int dataDisplayElementHeight=screenHeight-450;

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

        this.createChildRelationTypeButton=new Button("创建子关系类型");
        this.createChildRelationTypeButton.setEnabled(false);
        this.createChildRelationTypeButton.setIcon(FontAwesome.PLUS_CIRCLE);
        this.createChildRelationTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.createChildRelationTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.createChildRelationTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.createChildRelationTypeButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeCreateRelationTypeOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(createChildRelationTypeButton);

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
                executeEditRelationTypeAliasNameOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(editTypeAliasNameButton);

        this.removeRelationTypeButton=new Button("删除关系类型");
        this.removeRelationTypeButton.setEnabled(false);
        this.removeRelationTypeButton.setIcon(FontAwesome.TRASH_O);
        this.removeRelationTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.removeRelationTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.removeRelationTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.removeRelationTypeButton.addClickListener(new Button.ClickListener(){
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
        addRelationTypePropertyButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeAddRelationTypePropertyOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(addRelationTypePropertyButton);

        Label spaceDivLabel2=new Label("|");
        rightSideActionButtonPlacementLayout. addComponent(spaceDivLabel2);

        this.editRelationTypePropertyAliasNameButton=new Button("修改属性别名");
        this.editRelationTypePropertyAliasNameButton.setIcon(FontAwesome.EDIT);
        this.editRelationTypePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.editRelationTypePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.editRelationTypePropertyAliasNameButton.addStyleName("ui_appElementBottomSpacing");
        this.editRelationTypePropertyAliasNameButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeEditRelationTypePropertyAliasNameOperation();
            }
        });
        rightSideActionButtonPlacementLayout.addComponent(this.editRelationTypePropertyAliasNameButton);

        this.deleteRelationTypePropertyButton=new Button("删除类型属性");
        this.deleteRelationTypePropertyButton.setIcon(FontAwesome.TRASH_O);
        this.deleteRelationTypePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.deleteRelationTypePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.deleteRelationTypePropertyButton.addStyleName("ui_appElementBottomSpacing");
        this.deleteRelationTypePropertyButton.addClickListener(new Button.ClickListener(){
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

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    public void renderRelationTypeDefinitionsManagementInfo(String businessSolutionName){
        setBusinessSolutionName(businessSolutionName);
        this.relationTypesTreeTable.getContainerDataSource().removeAllItems();
        clearRelationTypeSelectStatus();
        this.relationTypeDefinitionsList = BusinessSolutionOperationUtil.getBusinessSolutionRelationTypeList(getBusinessSolutionName());
        this.alreadyRenderedTypeList=new ArrayList<>();
        Object[] rootRelationTypeInfo=new Object[]{
                InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME,
                ""
        };
        final Object rootRelationTypeInfoKey = this.relationTypesTreeTable.addItem(rootRelationTypeInfo, null);
        for(RelationTypeDefinitionVO currentChildRelationType:relationTypeDefinitionsList){
            setRelationTypesTreeTableData(rootRelationTypeInfoKey,InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME,currentChildRelationType);
        }
        this.relationTypesTreeTable.setCollapsed(rootRelationTypeInfoKey, false);
        this.relationTypePropertiesTable.setWidth("100%");
    }

    private void clearRelationTypeSelectStatus(){
        this.currentRelationTypePropertiesMap=null;
        this.relationTypePropertiesTable.getContainerDataSource().removeAllItems();
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        this.createChildRelationTypeButton.setEnabled(false);
        this.removeRelationTypeButton.setEnabled(false);
        this.editTypeAliasNameButton.setEnabled(false);
        this.currentSelectedRelationTypeName=null;
    }

    private void setRelationTypesTreeTableData(Object parentTreeDataKey,String parentRelationType,RelationTypeDefinitionVO currentRelationTypeVO){
        if(!currentRelationTypeVO.getParentTypeName().equals(parentRelationType)){
            return;
        }
        if(this.alreadyRenderedTypeList.contains(currentRelationTypeVO.getTypeName())){
            return;
        }
        Object[] currentRelationTypeInfo=new Object[]{
                currentRelationTypeVO.getTypeName(),
                currentRelationTypeVO.getTypeAliasName()
        };
        final Object currentRelationTypeInfoKey = this.relationTypesTreeTable.addItem(currentRelationTypeInfo, null);
        this.relationTypesTreeTable.setParent(currentRelationTypeInfoKey, parentTreeDataKey);
        List<RelationTypeDefinitionVO> childRelationTypesList= getChildRelationTypeList(currentRelationTypeVO.getTypeName());
        if(childRelationTypesList.size()==0){
            this.relationTypesTreeTable.setChildrenAllowed(currentRelationTypeInfoKey,false);
            this.relationTypesTreeTable.setColumnCollapsible(currentRelationTypeInfoKey,false);
        }
        this.alreadyRenderedTypeList.add(currentRelationTypeVO.getTypeName());
        for(RelationTypeDefinitionVO currentChildRelationType:childRelationTypesList){
            setRelationTypesTreeTableData(currentRelationTypeInfoKey,currentRelationTypeVO.getTypeName(),currentChildRelationType);
        }
    }

    private List<RelationTypeDefinitionVO> getChildRelationTypeList(String relationTypeName){
        List<RelationTypeDefinitionVO> childRelationTypeList=new ArrayList<>();
        if(this.relationTypeDefinitionsList==null){
            return childRelationTypeList;
        }else{
            for(RelationTypeDefinitionVO currentTypeDefinition:this.relationTypeDefinitionsList){
                if(currentTypeDefinition.getParentTypeName().equals(relationTypeName)){
                    childRelationTypeList.add(currentTypeDefinition);
                }
            }
            return childRelationTypeList;
        }
    }

    public void renderRelationTypeSelectedUIElements(String relationTypeName){
        if(relationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
            this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        }else{
            this.rightSideUIElementsContainer.removeComponent(this.rightSideUIPromptBox);
            this.rightSideUIElementsContainer.addComponent(this.rightSideUIElementsBox);
            this.currentRelationTypePropertiesMap=null;
            this.relationTypePropertiesTable.getContainerDataSource().removeAllItems();
            List<SolutionTypePropertyTypeDefinitionVO> relationTypePropertiesList=
                    BusinessSolutionOperationUtil.getSolutionTypePropertiesInfo(getBusinessSolutionName(), InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,relationTypeName);

            this.currentRelationTypePropertiesMap=new HashMap<String,SolutionTypePropertyTypeDefinitionVO>();
            for(SolutionTypePropertyTypeDefinitionVO currentPropertyTypeVO:relationTypePropertiesList){
                this.currentRelationTypePropertiesMap.put(currentPropertyTypeVO.getPropertyName(),currentPropertyTypeVO);
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
                    this.relationTypePropertiesTable.setItemIcon(currentRelationTypeInfoKey,FontAwesome.CIRCLE_O);
                }else{
                    this.relationTypePropertiesTable.setItemIcon(currentRelationTypeInfoKey,FontAwesome.REPLY_ALL);
                }
                this.relationTypePropertiesTable.setChildrenAllowed(currentRelationTypeInfoKey,false);
                this.relationTypePropertiesTable.setColumnCollapsible(currentRelationTypeInfoKey,false);
            }
        }
        this.currentSelectedRelationTypeName=relationTypeName;
        this.createChildRelationTypeButton.setEnabled(true);
        if(relationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            this.removeRelationTypeButton.setEnabled(false);
            this.editTypeAliasNameButton.setEnabled(false);
        }else{
            int childTypeCount=getChildRelationTypeList(relationTypeName).size();
            if(childTypeCount>0){
                this.removeRelationTypeButton.setEnabled(false);
            }else{
                this.removeRelationTypeButton.setEnabled(true);
            }
            this.editTypeAliasNameButton.setEnabled(true);
        }
        this.deleteRelationTypePropertyButton.setEnabled(false);
        this.editRelationTypePropertyAliasNameButton.setEnabled(false);
    }

    private void executeCreateRelationTypeOperation() {
        if (this.currentSelectedRelationTypeName != null) {
            CreateRelationTypeDefinitionPanel createDRelationTypeDefinitionPanel = new CreateRelationTypeDefinitionPanel(this.currentUserClientInfo);
            createDRelationTypeDefinitionPanel.setBusinessSolutionName(this.businessSolutionName);
            createDRelationTypeDefinitionPanel.setParentRelationType(this.currentSelectedRelationTypeName);
            createDRelationTypeDefinitionPanel.setRelationTypeDefinitionsManagementPanel(this);
            final Window window = new Window();
            window.setWidth(450.0f, Unit.PIXELS);
            window.setHeight(240.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(createDRelationTypeDefinitionPanel);
            createDRelationTypeDefinitionPanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }

    public void updateRelationTypesInfo(String parentRelationType,String sourceRelationType,boolean isDeleteAction){
        renderRelationTypeDefinitionsManagementInfo(getBusinessSolutionName());
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

    private void executeDeleteRelationTypeOperation(){
        if(this.currentSelectedRelationTypeName!=null){
            boolean isUsedInCommonDataRelationDefinition=
                    BusinessSolutionOperationUtil.checkInfoUsedInCommonDataRelationMappingDefinition(getBusinessSolutionName(),"RELATION",this.currentSelectedRelationTypeName,null,null);
            if(isUsedInCommonDataRelationDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据属性关联映射规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataDateDimensionDefinition=
                    BusinessSolutionOperationUtil.checkInfoUsedInDataDateDimensionMappingDefinition(getBusinessSolutionName(),"RELATION",this.currentSelectedRelationTypeName,null);
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
            Label confirmMessage= new Label(deleteConfirmMessage, ContentMode.HTML);
            final ConfirmDialog deleteRelationTypeConfirmDialog = new ConfirmDialog();
            deleteRelationTypeConfirmDialog.setConfirmMessage(confirmMessage);

            final RelationTypeDefinitionsManagementPanel self=this;
            final String parentRelationTypeName=getParentRelationTypeName(this.currentSelectedRelationTypeName);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteRelationTypeConfirmDialog.close();

                    boolean deleteRelationTypeResult=BusinessSolutionOperationUtil.deleteBusinessSolutionRelationType(businessSolutionName, currentSelectedRelationTypeName);
                    if(deleteRelationTypeResult){
                        self.updateRelationTypesInfo(parentRelationTypeName,currentSelectedRelationTypeName,true);
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

    private String getParentRelationTypeName(String childRelationTypeName){
        Collection objectIdCollection=this.relationTypesTreeTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.relationTypesTreeTable.getItem(itemId);
            String currentRelationTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            if(currentRelationTypeName.equals(childRelationTypeName)){
                Object parentItemId=this.relationTypesTreeTable.getParent(itemId);
                Item parentItem=this.relationTypesTreeTable.getItem(parentItemId);
                return parentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            }
        }
        return null;
    }

    private void executeEditRelationTypeAliasNameOperation(){
        if(this.currentSelectedRelationTypeName!=null){
            String currentAliasName=getRelationTypeAliasName(this.currentSelectedRelationTypeName);
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

    private String getRelationTypeAliasName(String relationType){
        Collection objectIdCollection=this.relationTypesTreeTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.relationTypesTreeTable.getItem(itemId);
            String currentRelationTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            if(currentRelationTypeName.equals(relationType)){
                String currentAliasName=currentItem.getItemProperty(ALIASNAME_PROPERTY).getValue().toString();
                return currentAliasName;
            }
        }
        return null;
    }

    @Override
    public void editTypePropertyAliasNameAction(String aliasName) {
        boolean updateTypeAliasNameResult=BusinessSolutionOperationUtil.
                updateSolutionTypePropertyAliasName(getBusinessSolutionName(),InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,this.currentSelectedRelationTypeName,this.currentSelectedRelationTypePropertyName,aliasName);
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
                String currentRelationTypePropertyName=currentItem.getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                if(currentRelationTypePropertyName.trim().equals(this.currentSelectedRelationTypePropertyName)){
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

    @Override
    public void editTypeAliasNameAction(String aliasName) {
        boolean updateTypeAliasNameResult=BusinessSolutionOperationUtil.updateSolutionTypeAliasName(getBusinessSolutionName(),InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,this.currentSelectedRelationTypeName,aliasName);
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
                String currentRelationTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
                if(currentRelationTypeName.equals(this.currentSelectedRelationTypeName)){
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

    private void executeAddRelationTypePropertyOperation(){
        if(this.currentSelectedRelationTypeName !=null){
            CreateTypePropertyDefinitionPanel createTypePropertyPanel=new CreateTypePropertyDefinitionPanel(this.currentUserClientInfo);
            createTypePropertyPanel.setBusinessSolutionName(this.getBusinessSolutionName());
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

    @Override
    public void createTypePropertyActionFinish(boolean actionResult) {
        if(this.currentSelectedRelationTypeName !=null){
            renderRelationTypeSelectedUIElements(this.currentSelectedRelationTypeName);
        }
    }

    private void renderRelationTypePropertySelectedUIElements(String propertyName){
        this.currentSelectedRelationTypePropertyName=propertyName.trim();
        this.deleteRelationTypePropertyButton.setEnabled(false);
        this.editRelationTypePropertyAliasNameButton.setEnabled(false);
        if(this.currentRelationTypePropertiesMap!=null){
            SolutionTypePropertyTypeDefinitionVO currentSelectedPropertyTypeVO=this.currentRelationTypePropertiesMap.get(propertyName.trim());
            if(currentSelectedPropertyTypeVO!=null){
                if(this.currentSelectedRelationTypeName.equals(currentSelectedPropertyTypeVO.getPropertySourceOwner())){
                    this.deleteRelationTypePropertyButton.setEnabled(true);
                    editRelationTypePropertyAliasNameButton.setEnabled(true);
                }else{
                    this.deleteRelationTypePropertyButton.setEnabled(false);
                    this.editRelationTypePropertyAliasNameButton.setEnabled(false);
                }
            }
        }
    }

    private void executeEditRelationTypePropertyAliasNameOperation(){
        if(this.currentSelectedRelationTypeName!=null&&this.currentSelectedRelationTypePropertyName!=null){
            String currentAliasName=getTypePropertyAliasName(this.currentSelectedRelationTypePropertyName);
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

    private String getTypePropertyAliasName(String propertyName){
        Collection objectIdCollection=this.relationTypePropertiesTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.relationTypePropertiesTable.getItem(itemId);
            String currentTypePropertyName=currentItem.getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
            if(currentTypePropertyName.trim().equals(propertyName)){
                String currentAliasName=currentItem.getItemProperty(PROPERTYALIASNAME_PROPERTY).getValue().toString();
                return currentAliasName;
            }
        }
        return null;
    }

    private void executeDeleteRelationTypePropertyOperation(){
        if(this.currentSelectedRelationTypeName !=null&&this.currentSelectedRelationTypePropertyName !=null){
            //do delete logic
            Label confirmMessage= new Label(FontAwesome.INFO.getHtml()+
                    " 请确认是否删除关系类型定义 "+this.currentSelectedRelationTypeName +" 中的属性定义 <b>"+this.currentSelectedRelationTypePropertyName +"</b>.", ContentMode.HTML);
            final ConfirmDialog deleteFactTypeConfirmDialog = new ConfirmDialog();
            deleteFactTypeConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteFactTypeConfirmDialog.close();
                    boolean deleteFactTypePropertyResult=
                            BusinessSolutionOperationUtil.deleteSolutionTypePropertyDefinition(businessSolutionName, InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION,currentSelectedRelationTypeName, currentSelectedRelationTypePropertyName);
                    if(deleteFactTypePropertyResult){
                        renderRelationTypeSelectedUIElements(currentSelectedRelationTypeName);
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除关系类型属性定义成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除关系类型属性定义错误",
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
