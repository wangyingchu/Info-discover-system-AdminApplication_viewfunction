package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.TableColumnValueIcon;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement.CreateRelationPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement.CreateRelationPanelInvoker;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Date;

/**
 * Created by wangychu on 11/10/16.
 */
public class TypeDataInstanceDetailPanel extends VerticalLayout implements CreateRelationPanelInvoker {

    private UserClientInfo currentUserClientInfo;
    private MeasurableValueVO measurableValue;
    private Window containerDialog;
    private TypeDataInstancePropertiesEditorPanel typeDataInstancePropertiesEditorPanel;
    private VerticalLayout dataInteractionInfoLayout;
    private RelationableRelationsList relationableRelationsList;
    private Button showRelationsSwitchButton;
    private BrowserFrame dataRelationGraphBrowserFrame;
    private Button createNewRelationButton;
    private String typeInstanceRelationsCycleGraphQueryAddress;
    private int relationsCycleGraphHeight=700;

    public TypeDataInstanceDetailPanel(UserClientInfo userClientInfo,MeasurableValueVO measurableValue) {
        this.currentUserClientInfo = userClientInfo;
        this.measurableValue=measurableValue;
        this.setWidth(100,Unit.PERCENTAGE);
        setSpacing(true);
        setMargin(true);
        String dataTypeKind= this.measurableValue.getMeasurableTypeKind();
        String dataTypeName=this.measurableValue.getMeasurableTypeName();
        String discoverSpaceName=this.measurableValue.getDiscoverSpaceName();
        String dataId=this.measurableValue.getId();

        String dataInstanceQueryId=dataId.replaceAll("#","%23");
        dataInstanceQueryId=dataInstanceQueryId.replaceAll(":","%3a");
        typeInstanceRelationsCycleGraphQueryAddress= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+
                "infoAnalysePages/typeInstanceRelationAnalyse/typeInstanceRelationsCycleGraph.html?dataInstanceId="+dataInstanceQueryId+"&discoverSpace="+discoverSpaceName;

        String dataInstanceBasicInfoNoticeText;
        String dataInstanceTypeText;
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(dataTypeKind)){
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.TAGS.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
            dataInstanceTypeText="维度数据";
        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataTypeKind)){
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.CLONE.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
            dataInstanceTypeText="事实数据";
        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataTypeKind)){
            dataInstanceTypeText="关系数据";
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.SHARE_ALT.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
        }else{
            dataInstanceTypeText="数据";
            dataInstanceBasicInfoNoticeText="";
        }
        Label sectionActionBarLabel=new Label(dataInstanceBasicInfoNoticeText, ContentMode.HTML);
        SectionActionsBar dataTypeNoticeActionsBar = new SectionActionsBar(sectionActionBarLabel);
        addComponent(dataTypeNoticeActionsBar);

        HorizontalLayout dataInstanceDetailContainerLayout=new HorizontalLayout();
        dataInstanceDetailContainerLayout.setHeight(100,Unit.PERCENTAGE);
        addComponent(dataInstanceDetailContainerLayout);

        //left side properties editor
        VerticalLayout dataPropertyInfoLayout=new VerticalLayout();
        dataPropertyInfoLayout.setHeight(100,Unit.PERCENTAGE);
        dataPropertyInfoLayout.setWidth(560,Unit.PIXELS);

        HorizontalLayout dataPropertyInfoTitleContainerLayout=new HorizontalLayout();
        dataPropertyInfoTitleContainerLayout.setWidth(100,Unit.PERCENTAGE);
        dataPropertyInfoLayout.addComponent(dataPropertyInfoTitleContainerLayout);

        Label dataPropertyTitle= new Label(FontAwesome.LIST_UL.getHtml() +" "+dataInstanceTypeText+"属性", ContentMode.HTML);
        dataPropertyTitle.addStyleName(ValoTheme.LABEL_SMALL);
        dataPropertyTitle.addStyleName("ui_appSectionLightDiv");
        dataPropertyInfoTitleContainerLayout.addComponent(dataPropertyTitle);

        if(!InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataTypeKind)) {
            showRelationsSwitchButton = new Button();
            showRelationsSwitchButton.setIcon(VaadinIcons.EXPAND_SQUARE);
            showRelationsSwitchButton.setDescription("显示数据关联信息");
            showRelationsSwitchButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
            showRelationsSwitchButton.addStyleName(ValoTheme.BUTTON_SMALL);
            dataPropertyInfoTitleContainerLayout.addComponent(showRelationsSwitchButton);
            showRelationsSwitchButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    if (getContainerDialog() != null) {
                        if (getContainerDialog().getWindowMode().equals(WindowMode.MAXIMIZED)) {
                            getContainerDialog().setWindowMode(WindowMode.NORMAL);
                        } else {
                            getContainerDialog().setWindowMode(WindowMode.MAXIMIZED);
                        }
                    }
                }
            });
        }else{
            RelationValueVO relationValueVO=InfoDiscoverSpaceOperationUtil.getRelationById(this.measurableValue.getDiscoverSpaceName(),this.measurableValue.getId());
            if(relationValueVO!=null){
                RelationableValueVO fromRelationable=relationValueVO.getFromRelationable();
                Button relationOutDataButton = new Button();
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(fromRelationable.getRelationableTypeKind())){
                    relationOutDataButton.setIcon(FontAwesome.TAG);
                    relationOutDataButton.setDescription("关系发出维度数据");
                }else{
                    relationOutDataButton.setIcon(FontAwesome.SQUARE_O);
                    relationOutDataButton.setDescription("关系发出事实数据");
                }
                relationOutDataButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
                relationOutDataButton.addStyleName(ValoTheme.BUTTON_SMALL);
                dataPropertyInfoTitleContainerLayout.addComponent(relationOutDataButton);
                relationOutDataButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        showDataDetailInfoPanel(fromRelationable);
                    }
                });

                TableColumnValueIcon relationDirectionIcon=new TableColumnValueIcon(VaadinIcons.ANGLE_DOUBLE_RIGHT,null);
                dataPropertyInfoTitleContainerLayout.addComponent(relationDirectionIcon);

                RelationableValueVO toRelationable=relationValueVO.getToRelationable();
                Button relationInDataButton = new Button();
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(toRelationable.getRelationableTypeKind())){
                    relationInDataButton.setIcon(FontAwesome.TAG);
                    relationInDataButton.setDescription("关系指向维度数据");
                }else{
                    relationInDataButton.setIcon(FontAwesome.SQUARE_O);
                    relationInDataButton.setDescription("关系指向事实数据");
                }
                relationInDataButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
                relationInDataButton.addStyleName(ValoTheme.BUTTON_SMALL);
                dataPropertyInfoTitleContainerLayout.addComponent(relationInDataButton);
                relationInDataButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        showDataDetailInfoPanel(toRelationable);
                    }
                });
            }
        }
        dataPropertyInfoTitleContainerLayout.setExpandRatio(dataPropertyTitle,1);

        typeDataInstancePropertiesEditorPanel=new TypeDataInstancePropertiesEditorPanel(this.currentUserClientInfo,this.measurableValue);
        dataPropertyInfoLayout.addComponent(typeDataInstancePropertiesEditorPanel);

        dataInstanceDetailContainerLayout.addComponent(dataPropertyInfoLayout);

        if(!InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataTypeKind)) {
            //right side data relation info editor
            this.dataInteractionInfoLayout = new VerticalLayout();
            this.dataInteractionInfoLayout.setHeight(100, Unit.PERCENTAGE);
            this.dataInteractionInfoLayout.setWidth(550, Unit.PIXELS);
            this.dataInteractionInfoLayout.addStyleName("ui_appSubViewContainer");

            HorizontalLayout dataRelationInfoTitleContainerLayout = new HorizontalLayout();
            dataRelationInfoTitleContainerLayout.setWidth(100, Unit.PERCENTAGE);
            this.dataInteractionInfoLayout.addComponent(dataRelationInfoTitleContainerLayout);

            Label dataRelationInfoTitle = new Label(FontAwesome.INFO_CIRCLE.getHtml() + " " + dataInstanceTypeText + "关联信息", ContentMode.HTML);
            dataRelationInfoTitle.addStyleName(ValoTheme.LABEL_SMALL);
            dataRelationInfoTitle.addStyleName("ui_appSectionLightDiv");
            dataRelationInfoTitleContainerLayout.addComponent(dataRelationInfoTitle);

            Button refreshRelationsInfoButton = new Button();
            refreshRelationsInfoButton.setIcon(FontAwesome.REFRESH);
            refreshRelationsInfoButton.setDescription("刷新数据关联信息");
            refreshRelationsInfoButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
            refreshRelationsInfoButton.addStyleName(ValoTheme.BUTTON_SMALL);
            dataRelationInfoTitleContainerLayout.addComponent(refreshRelationsInfoButton);
            refreshRelationsInfoButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    relationableRelationsList.reloadRelationsInfo();
                    long timeStampPostValue=new Date().getTime();
                    dataRelationGraphBrowserFrame.setSource(new ExternalResource(
                            typeInstanceRelationsCycleGraphQueryAddress+"&graphHeight="+relationsCycleGraphHeight+"&timestamp="+timeStampPostValue));
                }
            });

            createNewRelationButton = new Button();
            createNewRelationButton.setIcon(FontAwesome.CHAIN);
            createNewRelationButton.setDescription("建立新的数据关联");
            createNewRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
            createNewRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
            dataRelationInfoTitleContainerLayout.addComponent(createNewRelationButton);
            createNewRelationButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    createNewRelation();
                }
            });

            dataRelationInfoTitleContainerLayout.setExpandRatio(dataRelationInfoTitle, 1);

            TabSheet tabs=new TabSheet();
            this.dataInteractionInfoLayout.addComponent(tabs);

            RelationableValueVO currentRelationableValueVO = new RelationableValueVO();
            currentRelationableValueVO.setDiscoverSpaceName(discoverSpaceName);
            currentRelationableValueVO.setRelationableTypeName(dataTypeName);
            currentRelationableValueVO.setRelationableTypeKind(dataTypeKind);
            currentRelationableValueVO.setId(dataId);
            this.relationableRelationsList = new RelationableRelationsList(this.currentUserClientInfo, currentRelationableValueVO);

            TabSheet.Tab relationsListLayoutTab =tabs.addTab(this.relationableRelationsList, "数据关系信息");
            relationsListLayoutTab.setIcon(VaadinIcons.INFO_CIRCLE_O);
            dataInstanceDetailContainerLayout.addComponent(this.dataInteractionInfoLayout);

            final String browserFrameCaption="DataInstanceRelationGraph";
            this.dataRelationGraphBrowserFrame = new BrowserFrame(browserFrameCaption);

            this.dataRelationGraphBrowserFrame.setSizeFull();
            TabSheet.Tab dataRelationGraphContainerLayoutTab =tabs.addTab(this.dataRelationGraphBrowserFrame, "数据关联网络图");
            dataRelationGraphContainerLayoutTab.setIcon(VaadinIcons.CLUSTER);

            tabs.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
                @Override
                public void selectedTabChange(TabSheet.SelectedTabChangeEvent selectedTabChangeEvent) {
                    if(browserFrameCaption.equals(selectedTabChangeEvent.getTabSheet().getSelectedTab().getCaption())){
                        createNewRelationButton.setEnabled(false);
                    }else{
                        createNewRelationButton.setEnabled(true);
                    }
                }
            });
        }
    }

    @Override
    public void attach() {
        super.attach();
        Window containerWindow=this.getContainerDialog();
        containerWindow.addWindowModeChangeListener(new Window.WindowModeChangeListener() {
            @Override
            public void windowModeChanged(Window.WindowModeChangeEvent windowModeChangeEvent) {
                setUIElementsSizeForWindowSizeChange();
            }
        });
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(this.measurableValue.getMeasurableTypeKind())){
            containerWindow.setResizable(false);
        }
        setUIElementsSizeForWindowSizeChange();
    }

    private void setUIElementsSizeForWindowSizeChange(){
        Window containerDialog=this.getContainerDialog();
        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        int browserWindowWidth=UI.getCurrent().getPage().getBrowserWindowWidth();
        int containerWindowDialogFixHeight=(int)containerDialog.getHeight();
        int typeInstanceDetailPropertiesEditorContainerPanelHeight=0;
        int dataRelationInfoLayoutWidth=500;
        int relationsListHeight;
        int dataRelationGraphBrowserFrameHeight;
        if (containerDialog.getWindowMode().equals(WindowMode.MAXIMIZED)){
            typeInstanceDetailPropertiesEditorContainerPanelHeight=browserWindowHeight-230;
            dataRelationInfoLayoutWidth=browserWindowWidth-600;
            relationsListHeight=browserWindowHeight-550;
            dataRelationGraphBrowserFrameHeight=browserWindowHeight-200;
            if(showRelationsSwitchButton!=null) {
                showRelationsSwitchButton.setIcon(VaadinIcons.INSERT);
                showRelationsSwitchButton.setDescription("隐藏数据关联交互信息");
            }
        }else{
            typeInstanceDetailPropertiesEditorContainerPanelHeight=containerWindowDialogFixHeight-230;
            relationsListHeight=50;
            dataRelationGraphBrowserFrameHeight=70;
            if(showRelationsSwitchButton!=null) {
                showRelationsSwitchButton.setIcon(VaadinIcons.EXPAND_SQUARE);
                showRelationsSwitchButton.setDescription("显示数据关联交互信息");
            }
        }
        this.typeDataInstancePropertiesEditorPanel.setPropertiesEditorContainerPanelHeight(typeInstanceDetailPropertiesEditorContainerPanelHeight);
        if(this.dataInteractionInfoLayout!=null){
            this.dataInteractionInfoLayout.setWidth(dataRelationInfoLayoutWidth,Unit.PIXELS);
        }
        if(this.relationableRelationsList!=null){
            this.relationableRelationsList.setRelationableRelationsTableHeight(relationsListHeight);
        }
        if(this.dataRelationGraphBrowserFrame!=null){
            this.dataRelationGraphBrowserFrame.setHeight(dataRelationGraphBrowserFrameHeight,Unit.PIXELS);
            this.relationsCycleGraphHeight=dataRelationGraphBrowserFrameHeight-20;
            dataRelationGraphBrowserFrame.setSource(new ExternalResource(
                    typeInstanceRelationsCycleGraphQueryAddress+"&graphHeight="+relationsCycleGraphHeight));
        }
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void createNewRelation(){
        String dataTypeKind= this.measurableValue.getMeasurableTypeKind();
        String dataTypeName=this.measurableValue.getMeasurableTypeName();
        String discoverSpaceName=this.measurableValue.getDiscoverSpaceName();
        String dataId=this.measurableValue.getId();

        RelationableValueVO currentRelationableValueVO=new RelationableValueVO();
        currentRelationableValueVO.setDiscoverSpaceName(discoverSpaceName);
        currentRelationableValueVO.setRelationableTypeName(dataTypeName);
        currentRelationableValueVO.setRelationableTypeKind(dataTypeKind);
        currentRelationableValueVO.setId(dataId);

        CreateRelationPanel createRelationPanel=new CreateRelationPanel(this.currentUserClientInfo,currentRelationableValueVO);
        createRelationPanel.setCreateRelationPanelInvoker(this);

        final Window window = new Window();
        window.setWidth(1150, Unit.PIXELS);
        window.setHeight(720,Unit.PIXELS);
        window.setCaptionAsHtml(true);
        window.setResizable(false);
        window.setDraggable(true);
        window.setModal(true);
        window.center();
        window.setContent(createRelationPanel);
        createRelationPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public void createRelationsActionFinish(boolean actionResult) {
        relationableRelationsList.reloadRelationsInfo();
        long timeStampPostValue=new Date().getTime();
        dataRelationGraphBrowserFrame.setSource(new ExternalResource(
                typeInstanceRelationsCycleGraphQueryAddress+"&graphHeight="+relationsCycleGraphHeight+"&timestamp="+timeStampPostValue));
    }

    private void showDataDetailInfoPanel(RelationableValueVO targetRelationableValueVO){
        MeasurableValueVO targetMeasurableValue=InfoDiscoverSpaceOperationUtil.getMeasurableValueById(targetRelationableValueVO.getDiscoverSpaceName(),targetRelationableValueVO.getId());
        if(targetMeasurableValue==null){
            Notification errorNotification = new Notification("获取数据错误",
                    "系统中不存在ID为 "+targetRelationableValueVO.getId()+" 的数据", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        String discoverSpaceName=targetMeasurableValue.getDiscoverSpaceName();
        String dataTypeName=targetMeasurableValue.getMeasurableTypeName();
        String dataId=targetMeasurableValue.getId();
        String targetWindowUID=discoverSpaceName+"_GlobalDataInstanceDetailWindow_"+dataTypeName+"_"+dataId;
        Window targetWindow=this.currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(discoverSpaceName,targetWindowUID);
        if(targetWindow!=null){
            targetWindow.bringToFront();
            //targetWindow.center();
        }else{
            String dataTypeKind= targetMeasurableValue.getMeasurableTypeKind();
            String dataDetailInfoTitle;
            if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION)){
                dataDetailInfoTitle="维度数据详细信息";
            }
            else if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT)){
                dataDetailInfoTitle="事实数据详细信息";
            }
            else{
                dataDetailInfoTitle="数据详细信息";
            }
            TypeDataInstanceDetailPanel typeDataInstanceDetailPanel=new TypeDataInstanceDetailPanel(this.currentUserClientInfo,targetMeasurableValue);
            final Window window = new Window(UICommonElementsUtil.generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
            window.setWidth(570, Unit.PIXELS);
            window.setHeight(800,Unit.PIXELS);
            window.setCaptionAsHtml(true);
            window.setResizable(true);
            window.setDraggable(true);
            window.setModal(false);
            window.center();
            window.setContent(typeDataInstanceDetailPanel);
            typeDataInstanceDetailPanel.setContainerDialog(window);
            window.addCloseListener(new Window.CloseListener() {
                @Override
                public void windowClose(Window.CloseEvent closeEvent) {
                    currentUserClientInfo.getRuntimeWindowsRepository().removeExistingWindow(discoverSpaceName,targetWindowUID);
                }
            });
            this.currentUserClientInfo.getRuntimeWindowsRepository().addNewWindow(discoverSpaceName,targetWindowUID,window);
            UI.getCurrent().addWindow(window);
        }
    }
}
