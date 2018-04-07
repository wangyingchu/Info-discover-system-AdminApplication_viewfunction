package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.runtimeInfo;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionTitle;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceDetail;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.DiscoverSpaceComponentSelectedEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 10/3/16.
 */
public class InfoDiscoverSpaceRuntimeGeneralInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private TextField spaceDataStorageLocation;
    private TextField spaceDataOwner;
    private TextField spaceDataDiskSize;
    private TextField spaceTotalDataSize;
    private TextField factTotalDataCount;
    private TextField dimensionTotalDataCount;
    private TextField relationTotalDataCount;
    private InfoDiscoverSpaceRuntimeGeneralInfoChart infoDiscoverSpaceRuntimeGeneralInfoChart;
    private InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail;
    private String discoverSpaceName;
    private MainSectionTitle mainSectionTitle;
    private MenuBar.MenuItem businessSolutionSelectRootItem;
    private MenuBar businessSolutionSelectorMenuBar;
    private MenuBar.Command applyBusinessSolutionMenuItemCommand;

    public InfoDiscoverSpaceRuntimeGeneralInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        mainSectionTitle=new MainSectionTitle("信息发现空间名称");
        addComponent(mainSectionTitle);

        SecondarySectionTitle secondarySectionTitle=new SecondarySectionTitle("空间状态基本信息");
        this.setWidth("100%");

        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);

        FormLayout generalInfoForm = new FormLayout();
        generalInfoForm.setMargin(false);
        generalInfoForm.setWidth("100%");
        generalInfoForm.addStyleName("light");

        this.spaceTotalDataSize = new TextField("空间数据总量");
        this.spaceTotalDataSize.setRequired(false);
        this.spaceTotalDataSize.setReadOnly(true);
        generalInfoForm.addComponent(spaceTotalDataSize);
        this.factTotalDataCount = new TextField("事实数据总量");
        this.factTotalDataCount.setRequired(false);
        this.factTotalDataCount.setReadOnly(true);
        generalInfoForm.addComponent(factTotalDataCount);
        this.dimensionTotalDataCount = new TextField("维度数据总量");
        this.dimensionTotalDataCount.setRequired(false);
        this.dimensionTotalDataCount.setReadOnly(true);
        generalInfoForm.addComponent(dimensionTotalDataCount);
        this.relationTotalDataCount = new TextField("关系数据总量");
        this.relationTotalDataCount.setRequired(false);
        this.relationTotalDataCount.setReadOnly(true);
        generalInfoForm.addComponent(relationTotalDataCount);
        this.spaceDataStorageLocation = new TextField("空间数据存储路径");
        this.spaceDataStorageLocation.setRequired(false);
        this.spaceDataStorageLocation.setReadOnly(true);
        generalInfoForm.addComponent(spaceDataStorageLocation);
        this.spaceDataOwner = new TextField("空间数据所有人");
        this.spaceDataOwner.setRequired(false);
        this.spaceDataOwner.setReadOnly(true);
        generalInfoForm.addComponent(spaceDataOwner);
        this.spaceDataDiskSize = new TextField("空间数据磁盘消耗");
        this.spaceDataDiskSize.setRequired(false);
        this.spaceDataDiskSize.setReadOnly(true);
        generalInfoForm.addComponent(spaceDataDiskSize);

        VerticalLayout generalInfoContainer=new VerticalLayout();
        generalInfoContainer.addComponent(secondarySectionTitle);
        generalInfoContainer.addComponent(generalInfoForm);
        elementPlacementLayout.addComponent(generalInfoContainer);

        this.infoDiscoverSpaceRuntimeGeneralInfoChart=new InfoDiscoverSpaceRuntimeGeneralInfoChart(this.currentUserClientInfo);
        elementPlacementLayout.addComponent(infoDiscoverSpaceRuntimeGeneralInfoChart);
        elementPlacementLayout.setComponentAlignment(infoDiscoverSpaceRuntimeGeneralInfoChart, Alignment.MIDDLE_LEFT);

        HorizontalLayout actionButtonsPlacementLayout=new HorizontalLayout();
        addComponent(actionButtonsPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout=new HorizontalLayout();
        actionButtonsSpacingLayout.setWidth("10px");
        actionButtonsPlacementLayout.addComponent(actionButtonsSpacingLayout);

        Button refreshDiscoverSpaceInfoButton=new Button("刷新基本信息数据");
        refreshDiscoverSpaceInfoButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        refreshDiscoverSpaceInfoButton.addStyleName(ValoTheme.BUTTON_TINY);
        refreshDiscoverSpaceInfoButton.setIcon(FontAwesome.REFRESH);

        final InfoDiscoverSpaceRuntimeGeneralInfo self=this;
        refreshDiscoverSpaceInfoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(self.parentInfoDiscoverSpaceDetail!=null){
                    self.parentInfoDiscoverSpaceDetail.renderDiscoverSpaceDetail();
                }
            }
        });
        actionButtonsPlacementLayout.addComponent(refreshDiscoverSpaceInfoButton);

        HorizontalLayout spaceDivLayout=new HorizontalLayout();
        spaceDivLayout.setWidth("15px");
        actionButtonsPlacementLayout.addComponent(spaceDivLayout);

        this.businessSolutionSelectorMenuBar = new MenuBar();
        this.businessSolutionSelectorMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
        this.businessSolutionSelectorMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        actionButtonsPlacementLayout.addComponent(this.businessSolutionSelectorMenuBar);
        this.applyBusinessSolutionMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedCommandName=selectedItem.getText();
                applyBusinessSolution(selectedCommandName);
            }
        };
        businessSolutionSelectRootItem=this.businessSolutionSelectorMenuBar.addItem("启用业务解决方案", VaadinIcons.PACKAGE, null);

        Label spaceDivLabel2=new Label("|");
        actionButtonsPlacementLayout. addComponent(spaceDivLabel2);

        Button deleteDiscoverSpaceButton=new Button("删除此信息发现空间");
        deleteDiscoverSpaceButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        deleteDiscoverSpaceButton.addStyleName(ValoTheme.BUTTON_SMALL);
        deleteDiscoverSpaceButton.setIcon(FontAwesome.TRASH_O);
        deleteDiscoverSpaceButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(self.parentInfoDiscoverSpaceDetail!=null){
                    self.parentInfoDiscoverSpaceDetail.deleteCurrentDiscoverSpace();
                }
            }
        });
        actionButtonsPlacementLayout.addComponent(deleteDiscoverSpaceButton);

        VerticalLayout spacingLayout=new VerticalLayout();
        addComponent(spacingLayout);
    }

    public void renderRuntimeGeneralInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.mainSectionTitle.setValue(this.discoverSpaceName);
        this.infoDiscoverSpaceRuntimeGeneralInfoChart.renderRuntimeGeneralInfoChart(discoverSpaceStatisticMetrics);
        this.spaceTotalDataSize.setReadOnly(false);
        this.spaceTotalDataSize.setValue(""+discoverSpaceStatisticMetrics.getSpaceFullDataCount());
        this.spaceTotalDataSize.setReadOnly(true);

        this.factTotalDataCount.setReadOnly(false);
        this.factTotalDataCount.setValue(""+discoverSpaceStatisticMetrics.getSpaceFactDataCount());
        this.factTotalDataCount.setReadOnly(true);

        this.dimensionTotalDataCount.setReadOnly(false);
        this.dimensionTotalDataCount.setValue(""+discoverSpaceStatisticMetrics.getSpaceDimensionDataCount());
        this.dimensionTotalDataCount.setReadOnly(true);

        this.relationTotalDataCount.setReadOnly(false);
        this.relationTotalDataCount.setValue(""+discoverSpaceStatisticMetrics.getSpaceRelationDataCount());
        this.relationTotalDataCount.setReadOnly(true);

        this.spaceDataStorageLocation.setReadOnly(false);
        this.spaceDataStorageLocation.setValue(discoverSpaceStatisticMetrics.getSpaceLocation());
        this.spaceDataStorageLocation.setReadOnly(true);

        this.spaceDataOwner.setReadOnly(false);
        this.spaceDataOwner.setValue(discoverSpaceStatisticMetrics.getSpaceOwner());
        this.spaceDataOwner.setReadOnly(true);

        this.spaceDataDiskSize.setReadOnly(false);
        this.spaceDataDiskSize.setValue(""+discoverSpaceStatisticMetrics.getSpaceDiskSize());
        this.spaceDataDiskSize.setReadOnly(true);

        int totalDataTypeCount=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics().size()+
                discoverSpaceStatisticMetrics.getFactsStatisticMetrics().size()+
                discoverSpaceStatisticMetrics.getRelationsStatisticMetrics().size();
        if(totalDataTypeCount>0){
            businessSolutionSelectRootItem.setEnabled(false);
        }else{
            businessSolutionSelectRootItem.removeChildren();
            List<String> solutionsList= BusinessSolutionOperationUtil.getExistBusinessSolutions();
            if(solutionsList!=null){
                for(String currentSolution:solutionsList){
                    businessSolutionSelectRootItem.addItem(currentSolution, VaadinIcons.CLIPBOARD_TEXT, this.applyBusinessSolutionMenuItemCommand);
                }
            }
            businessSolutionSelectRootItem.setEnabled(true);
        }
    }

    public void setParentInfoDiscoverSpaceDetail(InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail) {
        this.parentInfoDiscoverSpaceDetail = parentInfoDiscoverSpaceDetail;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    private void applyBusinessSolution(String businessSolutionName){
        //do apply solution logic
        String confirmMessageString=" 请确认是否在信息发现空间 <b>"+this.discoverSpaceName+"</b> 中启用业务解决方案 <b>"+businessSolutionName+"</b> ,执行启用操作将会在信息发现空间中创建该业务解决方案模板中定义的所有维度类型,事实类型,关系类型以及其他数据关联映射信息和自定义属性别名信息。<br/>注意：此项操作不可撤销执行结果。";
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);
        final ConfirmDialog applyBusinessSolutionConfirmDialog = new ConfirmDialog();
        applyBusinessSolutionConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                applyBusinessSolutionConfirmDialog.close();
                boolean applySolutionResult=InfoDiscoverSpaceOperationUtil.applyBusinessSolution(discoverSpaceName,businessSolutionName);
                if(applySolutionResult){
                    DiscoverSpaceComponentSelectedEvent discoverSpaceComponentSelectedEvent=new DiscoverSpaceComponentSelectedEvent(discoverSpaceName);
                    currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceComponentSelectedEvent);
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "启用业务解决方案成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("启用业务解决方案错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        applyBusinessSolutionConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(applyBusinessSolutionConfirmDialog);
    }
}
