package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.factTypeManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.FactTypeDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.SolutionTypePropertyTypeDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.commonUseElement.CreateTypePropertyDefinitionPanel;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.AliasNameEditPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.AliasNameEditPanelInvoker;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.CreateTypePropertyPanelInvoker;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
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
 * Created by wangychu on 5/5/17.
 */
public class FactTypeDefinitionsManagementPanel extends VerticalLayout implements CreateTypePropertyPanelInvoker,AliasNameEditPanelInvoker {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private TreeTable factTypesTreeTable;
    private TreeTable factTypePropertiesTable;

    private String NAME_PROPERTY="事实类型名称";
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

    private Button createFactTypeButton;
    private Button editTypeAliasNameButton;
    private Button removeFactTypeButton;
    private Button deleteFactTypePropertyButton;
    private Button editFactTypePropertyAliasNameButton;

    private Map<String,SolutionTypePropertyTypeDefinitionVO> currentFactTypePropertiesMap;
    private String currentSelectedFactTypeName;
    private String currentSelectedFactTypePropertyName;

    public FactTypeDefinitionsManagementPanel(UserClientInfo currentUserClientInfo){
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

        Label leftSideTitle= new Label(FontAwesome.TASKS.getHtml() +" 事实类型", ContentMode.HTML);
        leftSideActionButtonPlacementLayout.addComponent(leftSideTitle);

        HorizontalLayout leftSideActionButtonsSpacingLayout2=new HorizontalLayout();
        leftSideActionButtonsSpacingLayout2.setWidth("20px");
        leftSideActionButtonPlacementLayout.addComponent(leftSideActionButtonsSpacingLayout2);

        this.createFactTypeButton=new Button("创建事实类型");
        this.createFactTypeButton.setEnabled(true);
        this.createFactTypeButton.setIcon(FontAwesome.PLUS_CIRCLE);
        this.createFactTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.createFactTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.createFactTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.createFactTypeButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeCreateFactTypeOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(createFactTypeButton);

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
                executeEditFactTypeAliasNameOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(editTypeAliasNameButton);

        this.removeFactTypeButton=new Button("删除事实类型");
        this.removeFactTypeButton.setEnabled(false);
        this.removeFactTypeButton.setIcon(FontAwesome.TRASH_O);
        this.removeFactTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.removeFactTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.removeFactTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.removeFactTypeButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeDeleteFactTypeOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(removeFactTypeButton);

        this.factTypesTreeTable = new TreeTable();
        this.factTypesTreeTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.factTypesTreeTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.factTypesTreeTable.setSizeFull();
        this.factTypesTreeTable.setSelectable(true);
        this.factTypesTreeTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.factTypesTreeTable.setNullSelectionAllowed(false);

        this.factTypesTreeTable.addContainerProperty(NAME_PROPERTY, String.class, "");
        this.factTypesTreeTable.addContainerProperty(ALIASNAME_PROPERTY, String.class, "");

        this.factTypesTreeTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedFactTypeName = itemClickEvent.getItem().getItemProperty(NAME_PROPERTY).getValue().toString();
                renderFactTypeSelectedUIElements(selectedFactTypeName);
            }
        });
        leftSideUIElementsContainer.addComponent(factTypesTreeTable);
        leftSideUIElementsContainer.addStyleName("ui_appElementRightSideSpacing");

        //right side elements
        this.rightSideUIElementsContainer=new VerticalLayout();
        elementPlacementLayout.addComponent(rightSideUIElementsContainer);

        this.rightSideUIPromptBox=new VerticalLayout();
        VerticalLayout messageHeightSpaceDiv=new VerticalLayout();
        messageHeightSpaceDiv.setHeight(30,Unit.PIXELS);
        this.rightSideUIPromptBox.addComponent(messageHeightSpaceDiv);
        Label functionMessage = new Label( FontAwesome.LIST_UL.getHtml()+" 选定事实类型包含的属性信息。", ContentMode.HTML);
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

        Button addFactTypePropertyButton=new Button("添加类型属性");
        addFactTypePropertyButton.setIcon(FontAwesome.PLUS_CIRCLE);
        addFactTypePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addFactTypePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        addFactTypePropertyButton.addStyleName("ui_appElementBottomSpacing");
        addFactTypePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeAddFactTypePropertyOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(addFactTypePropertyButton);

        Label spaceDivLabel2=new Label("|");
        rightSideActionButtonPlacementLayout. addComponent(spaceDivLabel2);

        this.editFactTypePropertyAliasNameButton=new Button("修改属性别名");
        this.editFactTypePropertyAliasNameButton.setIcon(FontAwesome.EDIT);
        this.editFactTypePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.editFactTypePropertyAliasNameButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.editFactTypePropertyAliasNameButton.addStyleName("ui_appElementBottomSpacing");
        this.editFactTypePropertyAliasNameButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeEditFactTypePropertyAliasNameOperation();
            }
        });
        rightSideActionButtonPlacementLayout.addComponent(this.editFactTypePropertyAliasNameButton);

        this.deleteFactTypePropertyButton =new Button("删除类型属性");
        this.deleteFactTypePropertyButton.setIcon(FontAwesome.TRASH_O);
        this.deleteFactTypePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.deleteFactTypePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.deleteFactTypePropertyButton.addStyleName("ui_appElementBottomSpacing");
        this.deleteFactTypePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeDeleteFactTypePropertyOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(this.deleteFactTypePropertyButton);

        this.factTypePropertiesTable = new TreeTable();
        this.factTypePropertiesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.factTypePropertiesTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.factTypePropertiesTable.setSizeFull();
        this.factTypePropertiesTable.setSelectable(true);
        this.factTypePropertiesTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.factTypePropertiesTable.setNullSelectionAllowed(false);

        this.factTypePropertiesTable.addContainerProperty(PROPERTYNAME_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(PROPERTYALIASNAME_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(PROPERTYTYPE_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(MUSTINPUT_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(READONLY_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(ALLOWNULL_PROPERTY, String.class, "");

        this.factTypePropertiesTable.setColumnWidth(PROPERTYTYPE_PROPERTY,100);
        this.factTypePropertiesTable.setColumnWidth(MUSTINPUT_PROPERTY,70);
        this.factTypePropertiesTable.setColumnWidth(READONLY_PROPERTY,70);
        this.factTypePropertiesTable.setColumnWidth(ALLOWNULL_PROPERTY,70);

        this.factTypePropertiesTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedFactTypeName=itemClickEvent.getItem().getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                renderFactTypePropertySelectedUIElements(selectedFactTypeName);
            }
        });
        this.rightSideUIElementsBox.addComponent(factTypePropertiesTable);
    }

    private void executeCreateFactTypeOperation(){
        CreateFactTypeDefinitionPanel createFactTypeDefinitionPanel=new CreateFactTypeDefinitionPanel(this.currentUserClientInfo);
        createFactTypeDefinitionPanel.setBusinessSolutionName(this.businessSolutionName);
        createFactTypeDefinitionPanel.setFactTypeDefinitionsManagementPanel(this);
        final Window window = new Window();
        window.setWidth(450.0f, Unit.PIXELS);
        window.setHeight(240.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(createFactTypeDefinitionPanel);
        createFactTypeDefinitionPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    private void executeDeleteFactTypeOperation(){
        if(this.currentSelectedFactTypeName !=null){
            boolean isUsedInCommonDataRelationDefinition=
                    BusinessSolutionOperationUtil.checkInfoUsedInCommonDataRelationMappingDefinition(getBusinessSolutionName(),"FACT",this.currentSelectedFactTypeName,null,null);
            if(isUsedInCommonDataRelationDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据属性关联映射规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataDateDimensionDefinition=
                    BusinessSolutionOperationUtil.checkInfoUsedInDataDateDimensionMappingDefinition(getBusinessSolutionName(),"FACT",this.currentSelectedFactTypeName,null);
            if(isUsedInDataDateDimensionDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据与时间维度关联定义规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataPropertiesDuplicateDefinition=
                    BusinessSolutionOperationUtil.checkInfoUsedInDataPropertiesDuplicateMappingDefinition(getBusinessSolutionName(),"FACT",this.currentSelectedFactTypeName,null,null);
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
                    " 请确认是否删除事实类型定义  <b>"+this.currentSelectedFactTypeName +"</b>。";
            Label confirmMessage= new Label(deleteConfirmMessage, ContentMode.HTML);
            final ConfirmDialog deleteFactTypeConfirmDialog = new ConfirmDialog();
            deleteFactTypeConfirmDialog.setConfirmMessage(confirmMessage);

            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteFactTypeConfirmDialog.close();

                    boolean deleteFactTypeResult=BusinessSolutionOperationUtil.deleteBusinessSolutionFactType(businessSolutionName, currentSelectedFactTypeName);
                    if(deleteFactTypeResult){
                        renderFactTypeDefinitionsManagementInfo(businessSolutionName);
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除事实类型定义成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除事实类型定义错误",
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

    private void executeAddFactTypePropertyOperation(){
        if(this.currentSelectedFactTypeName !=null){
            CreateTypePropertyDefinitionPanel createTypePropertyPanel=new CreateTypePropertyDefinitionPanel(this.currentUserClientInfo);
            createTypePropertyPanel.setBusinessSolutionName(this.getBusinessSolutionName());
            createTypePropertyPanel.setPropertyTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT);
            createTypePropertyPanel.setTypeName(this.currentSelectedFactTypeName);
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

    private void executeDeleteFactTypePropertyOperation(){
        if(this.currentSelectedFactTypeName !=null&&this.currentSelectedFactTypePropertyName !=null){
            boolean isUsedInCommonDataRelationDefinition=
                    BusinessSolutionOperationUtil.checkInfoUsedInCommonDataRelationMappingDefinition(getBusinessSolutionName(),"FACT",this.currentSelectedFactTypeName,this.currentSelectedFactTypePropertyName,null);
            if(isUsedInCommonDataRelationDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据属性关联映射规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataDateDimensionDefinition=
                    BusinessSolutionOperationUtil.checkInfoUsedInDataDateDimensionMappingDefinition(getBusinessSolutionName(),"FACT",this.currentSelectedFactTypeName,this.currentSelectedFactTypePropertyName);
            if(isUsedInDataDateDimensionDefinition){
                Notification errorNotification = new Notification("数据校验错误",
                        "本项数据已在数据与时间维度关联定义规则定义中使用", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            boolean isUsedInDataPropertiesDuplicateDefinition=
                    BusinessSolutionOperationUtil.checkInfoUsedInDataPropertiesDuplicateMappingDefinition(getBusinessSolutionName(),"FACT",this.currentSelectedFactTypeName,this.currentSelectedFactTypePropertyName,null);
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
                    " 请确认是否删除事实类型定义 "+this.currentSelectedFactTypeName +" 中的属性定义 <b>"+this.currentSelectedFactTypePropertyName +"</b>.", ContentMode.HTML);
            final ConfirmDialog deleteFactTypeConfirmDialog = new ConfirmDialog();
            deleteFactTypeConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteFactTypeConfirmDialog.close();
                    boolean deleteFactTypePropertyResult=
                            BusinessSolutionOperationUtil.deleteSolutionTypePropertyDefinition(businessSolutionName, InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,currentSelectedFactTypeName, currentSelectedFactTypePropertyName);
                    if(deleteFactTypePropertyResult){
                        renderFactTypeSelectedUIElements(currentSelectedFactTypeName);
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除事实类型属性定义成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除事实类型属性定义错误",
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

    @Override
    public void createTypePropertyActionFinish(boolean actionResult) {
        if(this.currentSelectedFactTypeName !=null){
            renderFactTypeSelectedUIElements(this.currentSelectedFactTypeName);
        }
    }

    public void renderFactTypeDefinitionsManagementInfo(String businessSolutionName){
        setBusinessSolutionName(businessSolutionName);
        this.factTypesTreeTable.getContainerDataSource().removeAllItems();
        clearFactTypeSelectStatus();

        List<FactTypeDefinitionVO> factTypeDefinitionsList= BusinessSolutionOperationUtil.getBusinessSolutionFactTypeList(getBusinessSolutionName());
        for(FactTypeDefinitionVO currentFactTypeDefinition:factTypeDefinitionsList){
            Object[] factTypeInfo=new Object[]{
                    currentFactTypeDefinition.getTypeName(),currentFactTypeDefinition.getTypeAliasName()
            };
            final Object currentFactTypeInfoKey = this.factTypesTreeTable.addItem(factTypeInfo, null);
            this.factTypesTreeTable.setChildrenAllowed(currentFactTypeInfoKey, false);
            this.factTypesTreeTable.setColumnCollapsible(currentFactTypeInfoKey, false);
        }
        this.factTypePropertiesTable.setWidth("100%");
    }

    private void clearFactTypeSelectStatus(){
        this.currentFactTypePropertiesMap =null;
        this.factTypePropertiesTable.getContainerDataSource().removeAllItems();
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        this.removeFactTypeButton.setEnabled(false);
        this.editTypeAliasNameButton.setEnabled(false);
        this.currentSelectedFactTypeName =null;
    }

    private void renderFactTypeSelectedUIElements(String factTypeName){
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIPromptBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIElementsBox);
        this.currentFactTypePropertiesMap =null;
        this.factTypePropertiesTable.getContainerDataSource().removeAllItems();
        List<SolutionTypePropertyTypeDefinitionVO> factTypePropertiesList=
                BusinessSolutionOperationUtil.getSolutionTypePropertiesInfo(getBusinessSolutionName(), InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,factTypeName);
        this.currentFactTypePropertiesMap =new HashMap<>();

        for(SolutionTypePropertyTypeDefinitionVO currentPropertyTypeVO:factTypePropertiesList){
            this.currentFactTypePropertiesMap.put(currentPropertyTypeVO.getPropertyName(), currentPropertyTypeVO);
            Object[] currentFactTypePropertiesInfo=new Object[]{
                    " "+currentPropertyTypeVO.getPropertyName(),
                    currentPropertyTypeVO.getPropertyAliasName(),
                    currentPropertyTypeVO.getPropertyType(),
                    ""+ currentPropertyTypeVO.isMandatory(),
                    ""+ currentPropertyTypeVO.isReadOnly(),
                    ""+ currentPropertyTypeVO.isNullable()
            };
            final Object currentFactTypeInfoKey = this.factTypePropertiesTable.addItem(currentFactTypePropertiesInfo, null);

            this.factTypePropertiesTable.setItemIcon(currentFactTypeInfoKey, FontAwesome.CIRCLE_O);

            this.factTypePropertiesTable.setChildrenAllowed(currentFactTypeInfoKey, false);
            this.factTypePropertiesTable.setColumnCollapsible(currentFactTypeInfoKey, false);
        }

        this.currentSelectedFactTypeName =factTypeName;
        this.removeFactTypeButton.setEnabled(true);
        this.editTypeAliasNameButton.setEnabled(true);
        this.deleteFactTypePropertyButton.setEnabled(false);
        this.editFactTypePropertyAliasNameButton.setEnabled(false);
    }

    private void renderFactTypePropertySelectedUIElements(String propertyName){
        this.currentSelectedFactTypePropertyName =propertyName.trim();
        this.deleteFactTypePropertyButton.setEnabled(false);
        this.editFactTypePropertyAliasNameButton.setEnabled(false);
        if(this.currentFactTypePropertiesMap !=null){
            SolutionTypePropertyTypeDefinitionVO currentSelectedPropertyTypeVO=this.currentFactTypePropertiesMap.get(propertyName.trim());
            if(currentSelectedPropertyTypeVO!=null){
                if(this.currentSelectedFactTypeName.equals(currentSelectedPropertyTypeVO.getPropertySourceOwner())){
                    this.deleteFactTypePropertyButton.setEnabled(true);
                    this.editFactTypePropertyAliasNameButton.setEnabled(true);
                }else{
                    this.deleteFactTypePropertyButton.setEnabled(false);
                    this.editFactTypePropertyAliasNameButton.setEnabled(false);
                }
            }
        }
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    private void executeEditFactTypeAliasNameOperation(){
        if(this.currentSelectedFactTypeName!=null){
            String currentAliasName=getFactTypeAliasName(this.currentSelectedFactTypeName);
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

    private String getFactTypeAliasName(String factType){
        Collection objectIdCollection=this.factTypesTreeTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.factTypesTreeTable.getItem(itemId);
            String currentFactTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
            if(currentFactTypeName.equals(factType)){
                String currentAliasName=currentItem.getItemProperty(ALIASNAME_PROPERTY).getValue().toString();
                return currentAliasName;
            }
        }
        return null;
    }

    private void executeEditFactTypePropertyAliasNameOperation(){
        if(this.currentSelectedFactTypeName!=null&&this.currentSelectedFactTypePropertyName!=null){
            String currentAliasName=getTypePropertyAliasName(this.currentSelectedFactTypePropertyName);
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
        Collection objectIdCollection=this.factTypePropertiesTable.getContainerDataSource().getItemIds();
        Iterator idIterator=objectIdCollection.iterator();
        while(idIterator.hasNext()){
            Object itemId=idIterator.next();
            Item currentItem=this.factTypePropertiesTable.getItem(itemId);
            String currentTypePropertyName=currentItem.getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
            if(currentTypePropertyName.trim().equals(propertyName)){
                String currentAliasName=currentItem.getItemProperty(PROPERTYALIASNAME_PROPERTY).getValue().toString();
                return currentAliasName;
            }
        }
        return null;
    }

    @Override
    public void editTypePropertyAliasNameAction(String aliasName) {
        boolean updateTypeAliasNameResult=BusinessSolutionOperationUtil.
                updateSolutionTypePropertyAliasName(getBusinessSolutionName(),InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,this.currentSelectedFactTypeName,this.currentSelectedFactTypePropertyName,aliasName);
        if(updateTypeAliasNameResult){
            Notification resultNotification = new Notification("更新数据操作成功",
                    "修改属性别名成功", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());

            Collection objectIdCollection=this.factTypePropertiesTable.getContainerDataSource().getItemIds();
            Iterator idIterator=objectIdCollection.iterator();
            while(idIterator.hasNext()){
                Object itemId=idIterator.next();
                Item currentItem=this.factTypePropertiesTable.getItem(itemId);
                String currentFactTypePropertyName=currentItem.getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                if(currentFactTypePropertyName.trim().equals(this.currentSelectedFactTypePropertyName)){
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
        boolean updateTypeAliasNameResult=BusinessSolutionOperationUtil.updateSolutionTypeAliasName(getBusinessSolutionName(),InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT,this.currentSelectedFactTypeName,aliasName);
        if(updateTypeAliasNameResult){
            Notification resultNotification = new Notification("更新数据操作成功",
                    "修改类型别名成功", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
            Collection objectIdCollection=this.factTypesTreeTable.getContainerDataSource().getItemIds();
            Iterator idIterator=objectIdCollection.iterator();
            while(idIterator.hasNext()){
                Object itemId=idIterator.next();
                Item currentItem=this.factTypesTreeTable.getItem(itemId);
                String currentDimensionTypeName=currentItem.getItemProperty(NAME_PROPERTY).getValue().toString();
                if(currentDimensionTypeName.equals(this.currentSelectedFactTypeName)){
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
}
