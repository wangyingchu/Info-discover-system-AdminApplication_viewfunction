package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationablesPathVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 5/8/17.
 */
public class RelationablesPathInfoTableActions extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private RelationablesPathVO relationablesPathInfo;
    private int pathIndex;
    private RelationablesPathInfoList containerRelationablesPathInfoList;

    public RelationablesPathInfoTableActions(UserClientInfo userClientInfo,RelationablesPathVO relationablesPathInfo,int pathIndex){
        this.currentUserClientInfo = userClientInfo;
        this.relationablesPathInfo = relationablesPathInfo;
        this.pathIndex = pathIndex;

        String pathStepsInfoLabelText= " 路径步数: "+relationablesPathInfo.getPathRelationRoute().size();
        Label pathStepsInfoLabel=new Label(pathStepsInfoLabelText, ContentMode.HTML);
        this.addComponent(pathStepsInfoLabel);
        this.setComponentAlignment(pathStepsInfoLabel, Alignment.MIDDLE_RIGHT);

        Label operationSpaceDivLabel=new Label("<span style='color:#AAAAAA;padding-left:10px;padding-right:5px;'>|</span>",ContentMode.HTML);
        this.addComponent(operationSpaceDivLabel);
        this.setComponentAlignment(operationSpaceDivLabel, Alignment.MIDDLE_RIGHT);

        Button showTypeDataDetailButton = new Button();
        showTypeDataDetailButton.setIcon(FontAwesome.EYE);
        showTypeDataDetailButton.setDescription("显示路径详情");
        showTypeDataDetailButton.addStyleName(ValoTheme.BUTTON_SMALL);
        showTypeDataDetailButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        showTypeDataDetailButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showPathRouteInfoPanel();
            }
        });
        addComponent(showTypeDataDetailButton);

        Button addToProcessingListButton = new Button();
        addToProcessingListButton.setIcon(VaadinIcons.PAINTBRUSH);
        addToProcessingListButton.setDescription("显示图形化路径信息");
        addToProcessingListButton.addStyleName(ValoTheme.BUTTON_SMALL);
        addToProcessingListButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        addToProcessingListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getContainerRelationablesPathInfoList().getContainerFindRelationInfoOfTwoAnalyzingDataPanel().showRelationablesPathGraph(relationablesPathInfo);
            }
        });
        addComponent(addToProcessingListButton);
    }

    private void showPathRouteInfoPanel(){
        RelationablesPathRouteDetailInfoPanel relationablesPathRouteDetailInfoPanel=
                new RelationablesPathRouteDetailInfoPanel(this.currentUserClientInfo,this.relationablesPathInfo,this.pathIndex);
        final Window window = new Window();
        window.setCaption(" 路径详情");
        window.setIcon(VaadinIcons.CONNECT_O);
        window.setWidth(600.0f, Unit.PIXELS);
        window.setHeight(700.0f, Unit.PIXELS);
        window.setResizable(false);
        window.setModal(true);
        window.setContent(relationablesPathRouteDetailInfoPanel);
        UI.getCurrent().addWindow(window);
    }

    public RelationablesPathInfoList getContainerRelationablesPathInfoList() {
        return containerRelationablesPathInfoList;
    }

    public void setContainerRelationablesPathInfoList(RelationablesPathInfoList containerRelationablesPathInfoList) {
        this.containerRelationablesPathInfoList = containerRelationablesPathInfoList;
    }
}
