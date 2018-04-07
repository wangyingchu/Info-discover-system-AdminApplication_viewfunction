package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dataMappingManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.DataMappingDefinitionVO;
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
 * Created by wangychu on 6/30/17.
 */
public class CommonDataRelationMappingDefinitionEditPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private TreeTable dataRelationMappingDefinitionsTable;
    private Button removeRelationMappingRuleButton;
    private Item currentSelectedDefinitionItem;

    public CommonDataRelationMappingDefinitionEditPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        this.setMargin(new MarginInfo(true,false,false,false));

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int dataDisplayElementHeight=screenHeight-470;

        HorizontalLayout actionButtonPlacementLayout=new HorizontalLayout();
        this.addComponent(actionButtonPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout1=new HorizontalLayout();
        actionButtonsSpacingLayout1.setWidth("10px");
        actionButtonPlacementLayout.addComponent(actionButtonsSpacingLayout1);

        Label actionTitle= new Label(FontAwesome.LIST.getHtml() +" 数据属性关联映射规则", ContentMode.HTML);
        actionButtonPlacementLayout.addComponent(actionTitle);

        HorizontalLayout actionButtonsSpacingLayout2=new HorizontalLayout();
        actionButtonsSpacingLayout2.setWidth("20px");
        actionButtonPlacementLayout.addComponent(actionButtonsSpacingLayout2);

        Button createRelationMappingRuleButton=new Button("创建数据属性关联映射规则");
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

        Label spaceDivLabel1=new Label("|");
        actionButtonPlacementLayout. addComponent(spaceDivLabel1);

        this.removeRelationMappingRuleButton=new Button("删除关联映射规则");
        this.removeRelationMappingRuleButton.setEnabled(false);
        this.removeRelationMappingRuleButton.setIcon(FontAwesome.TRASH_O);
        this.removeRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.removeRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.removeRelationMappingRuleButton.addStyleName("ui_appElementBottomSpacing");
        actionButtonPlacementLayout.addComponent(removeRelationMappingRuleButton);
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
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源属性数据类型", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源属性最小值", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源属性最大值", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("目标属性值", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("关联关系类型", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("数据关联方向", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("不存在映射处理策略", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("目标数据类型", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("目标类型名称", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("目标属性名称", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("目标属性数据类型", String.class, "");

        this.dataRelationMappingDefinitionsTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                currentSelectedDefinitionItem=itemClickEvent.getItem();
                removeRelationMappingRuleButton.setEnabled(true);
            }
        });
        this.addComponent(this.dataRelationMappingDefinitionsTable);
    }

    public void renderCommonDataRelationMappingDefinitionInfo(String businessSolutionName){
        this.removeRelationMappingRuleButton.setEnabled(false);
        this.dataRelationMappingDefinitionsTable.removeAllItems();
        this.currentSelectedDefinitionItem=null;
        List<DataMappingDefinitionVO> dataMappingDefinitionsList= BusinessSolutionOperationUtil.getCommonDataRelationMappingDefinitionList(businessSolutionName);
        for(DataMappingDefinitionVO currentDataMappingDefinitionVO:dataMappingDefinitionsList){
            String sourceMinValue=currentDataMappingDefinitionVO.getMinValue()!=null?currentDataMappingDefinitionVO.getMinValue():"";
            String sourceMaxValue=currentDataMappingDefinitionVO.getMaxValue()!=null?currentDataMappingDefinitionVO.getMaxValue():"";
            String targetPropertyValue=currentDataMappingDefinitionVO.getRangeResult()!=null?currentDataMappingDefinitionVO.getRangeResult():"";
            Object[] newDefinitionInfo=new Object[]{
                    currentDataMappingDefinitionVO.getSourceDataTypeKind(),
                    currentDataMappingDefinitionVO.getSourceDataTypeName(),
                    currentDataMappingDefinitionVO.getSourceDataPropertyName(),
                    currentDataMappingDefinitionVO.getSourceDataPropertyType(),
                    sourceMinValue,
                    sourceMaxValue,
                    targetPropertyValue,
                    currentDataMappingDefinitionVO.getRelationTypeName(),
                    currentDataMappingDefinitionVO.getRelationDirection(),
                    currentDataMappingDefinitionVO.getMappingNotExistHandleMethod(),
                    currentDataMappingDefinitionVO.getTargetDataTypeKind(),
                    currentDataMappingDefinitionVO.getTargetDataTypeName(),
                    currentDataMappingDefinitionVO.getTargetDataPropertyName(),
                    currentDataMappingDefinitionVO.getTargetDataPropertyType()
            };
            final Object newDataItemKey =this.dataRelationMappingDefinitionsTable.addItem(newDefinitionInfo,null);
            this.dataRelationMappingDefinitionsTable.setChildrenAllowed(newDataItemKey, false);
        }
    }

    private void executeCreateDataMappingRuleOperation(){
        CommonDataRelationMappingDefinitionEditor commonDataRelationMappingDefinitionEditor=new CommonDataRelationMappingDefinitionEditor(this.currentUserClientInfo);
        commonDataRelationMappingDefinitionEditor.setBusinessSolutionName(getBusinessSolutionName());
        commonDataRelationMappingDefinitionEditor.setRelatedCommonDataRelationMappingDefinitionEditPanel(this);
        final Window window = new Window();
        window.setWidth(780.0f, Unit.PIXELS);
        window.setHeight(600.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(commonDataRelationMappingDefinitionEditor);
        commonDataRelationMappingDefinitionEditor.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    private void executeDeleteRelationMappingDefinitionOperation(){
        if(this.currentSelectedDefinitionItem!=null){
            Label confirmMessage= new Label(FontAwesome.INFO.getHtml()+
                    " 请确认是否删除选定的数据属性关联映射规则.", ContentMode.HTML);
            final ConfirmDialog deleteDefinitionConfirmDialog = new ConfirmDialog();
            deleteDefinitionConfirmDialog.setConfirmMessage(confirmMessage);
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    deleteDefinitionConfirmDialog.close();

                    DataMappingDefinitionVO definitionToDelete=new DataMappingDefinitionVO();
                    definitionToDelete.setSolutionName(getBusinessSolutionName());
                    definitionToDelete.setSourceDataPropertyName(currentSelectedDefinitionItem.getItemProperty("源属性名称").getValue().toString());
                    definitionToDelete.setSourceDataPropertyType(currentSelectedDefinitionItem.getItemProperty("源属性数据类型").getValue().toString());
                    definitionToDelete.setSourceDataTypeKind(currentSelectedDefinitionItem.getItemProperty("源数据类型").getValue().toString());
                    definitionToDelete.setSourceDataTypeName(currentSelectedDefinitionItem.getItemProperty("源类型名称").getValue().toString());
                    definitionToDelete.setRelationDirection(currentSelectedDefinitionItem.getItemProperty("数据关联方向").getValue().toString());
                    definitionToDelete.setRelationTypeName(currentSelectedDefinitionItem.getItemProperty("关联关系类型").getValue().toString());
                    definitionToDelete.setMappingNotExistHandleMethod(currentSelectedDefinitionItem.getItemProperty("不存在映射处理策略").getValue().toString());
                    definitionToDelete.setTargetDataPropertyName(currentSelectedDefinitionItem.getItemProperty("目标属性名称").getValue().toString());
                    definitionToDelete.setTargetDataPropertyType(currentSelectedDefinitionItem.getItemProperty("目标属性数据类型").getValue().toString());
                    definitionToDelete.setTargetDataTypeKind(currentSelectedDefinitionItem.getItemProperty("目标数据类型").getValue().toString());
                    definitionToDelete.setTargetDataTypeName(currentSelectedDefinitionItem.getItemProperty("目标类型名称").getValue().toString());
                    if(currentSelectedDefinitionItem.getItemProperty("源属性最小值")!=null){
                        definitionToDelete.setMinValue(currentSelectedDefinitionItem.getItemProperty("源属性最小值").getValue().toString());
                    }
                    if(currentSelectedDefinitionItem.getItemProperty("源属性最大值")!=null){
                        definitionToDelete.setMaxValue(currentSelectedDefinitionItem.getItemProperty("源属性最大值").getValue().toString());
                    }
                    if(currentSelectedDefinitionItem.getItemProperty("目标属性值")!=null){
                        definitionToDelete.setRangeResult(currentSelectedDefinitionItem.getItemProperty("目标属性值").getValue().toString());
                    }
                    boolean deleteDefinitionResult=BusinessSolutionOperationUtil.deleteCommonDataRelationMappingDefinition(getBusinessSolutionName(),definitionToDelete);
                    if(deleteDefinitionResult){
                        renderCommonDataRelationMappingDefinitionInfo(getBusinessSolutionName());
                        Notification resultNotification = new Notification("删除数据操作成功",
                                "删除数据属性关联映射规则成功", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());
                    }else{
                        Notification errorNotification = new Notification("删除数据属性关联映射规则错误",
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
}
