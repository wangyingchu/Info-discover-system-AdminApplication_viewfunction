package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataListVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.ProcessingDataOperationPanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 12/16/16.
 */
public class CreateRelationPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private RelationableValueVO relationableValueVO;
    private VerticalLayout relationPropertiesEditorUIElementContainer;
    private RelationPropertiesEditorPanel relationPropertiesEditorPanel;
    private Window containerDialog;
    private ComboBox relationTypeSelectorCombox;
    private OptionGroup relationDirectionOptionGroup;
    private ProcessingDataOperationPanel processingDataOperationPanel;
    private CreateRelationPanelInvoker createRelationPanelInvoker;

    public CreateRelationPanel(UserClientInfo userClientInfo,RelationableValueVO relationableValueVO) {
        this.currentUserClientInfo=userClientInfo;
        this.relationableValueVO=relationableValueVO;
        this.setSpacing(true);
        this.setMargin(true);

        MainSectionTitle createNewRelationSectionTitle=new MainSectionTitle("建立数据关联");
        addComponent(createNewRelationSectionTitle);

        String discoverSpaceName=this.relationableValueVO.getDiscoverSpaceName();
        String relationableTypeKind=this.relationableValueVO.getRelationableTypeKind();
        String relationableTypeName=this.relationableValueVO.getRelationableTypeName();
        String dataId=this.relationableValueVO.getId();
        String dataInstanceBasicInfoNoticeText;
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(relationableTypeKind)){
            dataInstanceBasicInfoNoticeText= FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.TAGS.getHtml()+" "+relationableTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(relationableTypeKind)){
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.CLONE.getHtml()+" "+relationableTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
        }else{
            dataInstanceBasicInfoNoticeText="";
        }
        Label sectionActionBarLabel=new Label(dataInstanceBasicInfoNoticeText, ContentMode.HTML);
        SectionActionsBar relationableValueInfoNoticeActionsBar = new SectionActionsBar(sectionActionBarLabel);
        addComponent(relationableValueInfoNoticeActionsBar);

        HorizontalLayout createRelationUIElementContainerLayout=new HorizontalLayout();
        createRelationUIElementContainerLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(createRelationUIElementContainerLayout);

        //left side element
        VerticalLayout relationDataUIElementContainer=new VerticalLayout();
        createRelationUIElementContainerLayout.addComponent(relationDataUIElementContainer);
        Label dataPropertyTitle= new Label(FontAwesome.INFO_CIRCLE.getHtml() +" 数据关联信息", ContentMode.HTML);
        dataPropertyTitle.addStyleName(ValoTheme.LABEL_SMALL);
        dataPropertyTitle.addStyleName("ui_appSectionLightDiv");
        relationDataUIElementContainer.addComponent(dataPropertyTitle);
        relationDataUIElementContainer.setWidth(485,Unit.PIXELS);
        FormLayout relationBasicInfoForm=new FormLayout();
        relationBasicInfoForm.setSpacing(true);
        relationBasicInfoForm.setMargin(true);
        relationBasicInfoForm.setHeight(65,Unit.PIXELS);
        relationBasicInfoForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        relationDataUIElementContainer.addComponent(relationBasicInfoForm);

        relationTypeSelectorCombox=new ComboBox("关系类型名称");
        relationTypeSelectorCombox.setIcon(FontAwesome.EXCHANGE);
        relationTypeSelectorCombox.setTextInputAllowed(false);
        relationTypeSelectorCombox.setNullSelectionAllowed(false);
        relationBasicInfoForm.addComponent(relationTypeSelectorCombox);
        loadRelationTypesInfo(relationTypeSelectorCombox);
        relationTypeSelectorCombox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                String relationTypeName=valueChangeEvent.getProperty().getValue().toString();
                renderRelationPropertiesEditor(relationTypeName);
            }
        });

        relationDirectionOptionGroup = new OptionGroup("关系指向方向");
        relationDirectionOptionGroup.setIcon(FontAwesome.HAND_O_RIGHT);
        relationDirectionOptionGroup.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        relationDirectionOptionGroup.addItems("当前数据", "相关数据","双向关系");
        relationDirectionOptionGroup.select("当前数据");
        relationBasicInfoForm.addComponent(relationDirectionOptionGroup);

        this.relationPropertiesEditorUIElementContainer=new VerticalLayout();
        relationDataUIElementContainer.addComponent(this.relationPropertiesEditorUIElementContainer);

        //right side element
        VerticalLayout relatedDataUIElementContainer=new VerticalLayout();
        relatedDataUIElementContainer.addStyleName("ui_appSubViewContainer");
        createRelationUIElementContainerLayout.addComponent(relatedDataUIElementContainer);
        createRelationUIElementContainerLayout.setExpandRatio(relatedDataUIElementContainer,1);

        Label relatedDataInfoTitle= new Label(FontAwesome.DATABASE.getHtml() +" 相关数据对象", ContentMode.HTML);
        relatedDataInfoTitle.addStyleName(ValoTheme.LABEL_SMALL);
        relatedDataInfoTitle.addStyleName("ui_appSectionLightDiv");
        relatedDataUIElementContainer.addComponent(relatedDataInfoTitle);
        processingDataOperationPanel =new ProcessingDataOperationPanel(this.currentUserClientInfo,false,true,false);
        processingDataOperationPanel.setDiscoverSpaceName(discoverSpaceName);
        processingDataOperationPanel.setDataTablesHeight(425);
        ProcessingDataListVO targetProcessingDataList=this.currentUserClientInfo.getDiscoverSpacesProcessingDataMap().get(discoverSpaceName);
        if(targetProcessingDataList==null){
            targetProcessingDataList=new ProcessingDataListVO(discoverSpaceName);
            currentUserClientInfo.getDiscoverSpacesProcessingDataMap().put(discoverSpaceName,targetProcessingDataList);
        }
        processingDataOperationPanel.setProcessingDataList(targetProcessingDataList);
        relatedDataUIElementContainer.addComponent(processingDataOperationPanel);

        HorizontalLayout actionButtonContainerLayout=new HorizontalLayout();
        actionButtonContainerLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(actionButtonContainerLayout);

        HorizontalLayout actionButtonsLayout=new HorizontalLayout();

        HorizontalLayout buttonsSpaceDivLayout0=new HorizontalLayout();
        buttonsSpaceDivLayout0.setWidth(180,Unit.PIXELS);
        actionButtonsLayout.addComponent(buttonsSpaceDivLayout0);

        Button addRelationButton=new Button("建立数据关联");
        addRelationButton.setIcon(FontAwesome.PLUS_SQUARE);
        addRelationButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        addRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addRelations();
            }
        });
        actionButtonsLayout.addComponent(addRelationButton);

        HorizontalLayout buttonsSpaceDivLayout1=new HorizontalLayout();
        buttonsSpaceDivLayout1.setWidth(20,Unit.PIXELS);
        actionButtonsLayout.addComponent(buttonsSpaceDivLayout1);

        Button closeAddRelationWindowButton=new Button("取消");
        closeAddRelationWindowButton.setIcon(FontAwesome.TIMES);
        closeAddRelationWindowButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    if(getContainerDialog()!=null){
                        getContainerDialog().close();
                    }
                }
            }
        );
        actionButtonsLayout.addComponent(closeAddRelationWindowButton);

        actionButtonContainerLayout.addComponent(actionButtonsLayout);
        actionButtonContainerLayout.setComponentAlignment(actionButtonsLayout,Alignment.MIDDLE_LEFT);
    }

    private void loadRelationTypesInfo(ComboBox relationTypeSelectorCombox){
        String discoverSpaceName=this.relationableValueVO.getDiscoverSpaceName();
        DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics=InfoDiscoverSpaceOperationUtil.getDiscoverSpaceStatisticMetrics(discoverSpaceName);
        List<DataTypeStatisticMetrics> relationTypesInfoList=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:relationTypesInfoList){
            String relationTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            relationTypeSelectorCombox.addItem(relationTypeName);
        }
    }

    private void renderRelationPropertiesEditor(String relationTypeName){
        this.relationPropertiesEditorUIElementContainer.removeAllComponents();
        String discoverSpaceName=this.relationableValueVO.getDiscoverSpaceName();
        this.relationPropertiesEditorPanel=new RelationPropertiesEditorPanel(this.currentUserClientInfo);
        this.relationPropertiesEditorPanel.setDiscoverSpaceName(discoverSpaceName);
        this.relationPropertiesEditorPanel.setDataInstanceTypeName(relationTypeName);
        this.relationPropertiesEditorPanel.setEditFormHeight(355);
        this.relationPropertiesEditorUIElementContainer.addComponent(this.relationPropertiesEditorPanel);
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void addRelations(){
        if(relationTypeSelectorCombox.getValue()==null){
            Notification errorNotification = new Notification("数据校验错误",
                    "请选择关系类型名称", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(!this.relationPropertiesEditorPanel.validatePropertiesValue()){
            return;
        }
        List<String> selectedDimensionsIdList = this.processingDataOperationPanel.getSelectedDimensionsId();
        List<String> selectedFactsIdList = this.processingDataOperationPanel.getSelectedFactsId();
        if(selectedDimensionsIdList.size()==0&&selectedFactsIdList.size()==0){
            Notification errorNotification = new Notification("数据校验错误",
                    "请选择至少一个相关联的数据对象", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        String relationTypeName=relationTypeSelectorCombox.getValue().toString();
        String relationDirectionStr=relationDirectionOptionGroup.getValue().toString();
        String relationDirection=InfoDiscoverSpaceOperationUtil.RELATION_DIRECTION_FROM;
        if(relationDirectionStr.equals("当前数据")){
            relationDirection=InfoDiscoverSpaceOperationUtil.RELATION_DIRECTION_FROM;
        }
        if(relationDirectionStr.equals("相关数据")){
            relationDirection=InfoDiscoverSpaceOperationUtil.RELATION_DIRECTION_TO;
        }
        if(relationDirectionStr.equals("双向关系")){
            relationDirection=InfoDiscoverSpaceOperationUtil.RELATION_DIRECTION_BOTH;
        }
        String finalRelationDirection = relationDirection;
        List<PropertyValueVO> relationProperties=this.relationPropertiesEditorPanel.retrievePropertyValueObjects();

        int totalRelationCount=selectedDimensionsIdList.size()+selectedFactsIdList.size();
        String confirmMessageString=" 请确认为数据 "+this.relationableValueVO.getRelationableTypeName()+" /"+this.relationableValueVO.getId()+" 建立"+totalRelationCount+"项类型为 "+relationTypeName+" 的数据关联";
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog addDataConfirmDialog = new ConfirmDialog();
        addDataConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addDataConfirmDialog.close();
                boolean createRelationsResult=InfoDiscoverSpaceOperationUtil.createRelations(relationableValueVO,relationTypeName, finalRelationDirection,
                        relationProperties,selectedDimensionsIdList,selectedFactsIdList);
                if(createRelationsResult){
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "建立数据关联成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("建立数据关联错误",
                            "发生服务器端错误,请确认所有参与建立关联的数据项均处于数据合法状态", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
                if(getCreateRelationPanelInvoker()!=null){
                    getCreateRelationPanelInvoker().createRelationsActionFinish(createRelationsResult);
                }
            }
        };
        addDataConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addDataConfirmDialog);
    }

    public CreateRelationPanelInvoker getCreateRelationPanelInvoker() {
        return createRelationPanelInvoker;
    }

    public void setCreateRelationPanelInvoker(CreateRelationPanelInvoker createRelationPanelInvoker) {
        this.createRelationPanelInvoker = createRelationPanelInvoker;
    }
}
