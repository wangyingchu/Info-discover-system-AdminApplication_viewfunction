package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationablesPathVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Stack;

/**
 * Created by wangychu on 5/9/17.
 */
public class RelationablesPathRouteDetailInfoPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private RelationablesPathVO relationablesPathInfo;
    private int pathIndex;

    public RelationablesPathRouteDetailInfoPanel(UserClientInfo userClientInfo, RelationablesPathVO relationablesPathInfo, int pathIndex){
        this.currentUserClientInfo = userClientInfo;
        this.relationablesPathInfo = relationablesPathInfo;
        this.pathIndex = pathIndex;

        this.setMargin(true);
        this.setSpacing(true);

        HorizontalLayout analyzingDataInfoContainerLayout=new HorizontalLayout();
        Label analyzingDataLabel=new Label(FontAwesome.SQUARE_O.getHtml()+" 路径端点 : ", ContentMode.HTML);
        analyzingDataLabel.addStyleName(ValoTheme.LABEL_SMALL);
        analyzingDataInfoContainerLayout.addComponent(analyzingDataLabel);

        HorizontalLayout spacingDiv01Layout=new HorizontalLayout();
        spacingDiv01Layout.setWidth(5,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv01Layout);

        Label endPointDataIdsLabel=new Label(relationablesPathInfo.getEndPointRelationableAId()+" - "+relationablesPathInfo.getEndPointRelationableBId());
        endPointDataIdsLabel.addStyleName(ValoTheme.LABEL_SMALL);
        analyzingDataInfoContainerLayout.addComponent(endPointDataIdsLabel);

        HorizontalLayout spacingDiv02Layout=new HorizontalLayout();
        spacingDiv02Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv02Layout);

        Label pathIndexLabel=new Label(FontAwesome.SORT_NUMERIC_ASC.getHtml()+" 路径序号 : ", ContentMode.HTML);
        pathIndexLabel.addStyleName(ValoTheme.LABEL_SMALL);
        analyzingDataInfoContainerLayout.addComponent(pathIndexLabel);

        HorizontalLayout spacingDiv03Layout=new HorizontalLayout();
        spacingDiv03Layout.setWidth(5,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv03Layout);

        Label pathNumberLabel=new Label(""+(pathIndex+1));
        pathNumberLabel.addStyleName(ValoTheme.LABEL_SMALL);
        analyzingDataInfoContainerLayout.addComponent(pathNumberLabel);

        this.addComponent(analyzingDataInfoContainerLayout);

        Panel routeInfoPanel=new Panel();
        routeInfoPanel.setHeight(600,Unit.PIXELS);
        this.addComponent(routeInfoPanel);
        VerticalLayout routeInfoContainer=new VerticalLayout();
        routeInfoContainer.setMargin(true);
        routeInfoContainer.setSpacing(true);
        routeInfoPanel.setContent(routeInfoContainer);
        Stack<RelationValueVO> relationValueStack=relationablesPathInfo.getPathRelationRoute();

        for(int i=0;i<relationValueStack.size();i++){
            HorizontalLayout currentRouteStepInfoContainer=new HorizontalLayout();
            currentRouteStepInfoContainer.setWidth(100,Unit.PERCENTAGE);
            currentRouteStepInfoContainer.addStyleName("ui_appSectionLightDiv");

            Label stepIndexLabel=new Label(""+(i+1)+".", ContentMode.HTML);
            currentRouteStepInfoContainer.addComponent(stepIndexLabel);

            RelationValueVO currentStepRelation=relationValueStack.elementAt(i);

            RelationableValueVO fromRelationable=currentStepRelation.getFromRelationable();
            String fromRelationableType=fromRelationable.getRelationableTypeKind();
            String fromDataTypeName=fromRelationable.getRelationableTypeName();
            String fromDataId=fromRelationable.getId();
            String fromRelationableInfoText=null;
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(fromRelationableType)){
                fromRelationableInfoText= FontAwesome.TAGS.getHtml()+"维度数据  "+FontAwesome.KEY.getHtml()+" "+fromDataId;

            }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(fromRelationableType)){
                fromRelationableInfoText= FontAwesome.CLONE.getHtml()+"事实数据  "+FontAwesome.KEY.getHtml()+" "+fromDataId;
            }

            Label fromRelationableInfoLabel=new Label(fromRelationableInfoText, ContentMode.HTML);
            fromRelationableInfoLabel.setDescription(fromDataTypeName+" / "+fromDataId);
            fromRelationableInfoLabel.addStyleName(ValoTheme.LABEL_SMALL);
            currentRouteStepInfoContainer.addComponent(fromRelationableInfoLabel);

            HorizontalLayout currentSpacingLayoutA=new HorizontalLayout();
            currentSpacingLayoutA.setWidth(10,Unit.PIXELS);
            currentRouteStepInfoContainer.addComponent(currentSpacingLayoutA);

            Label relationDirectionLabel=new Label(VaadinIcons.ANGLE_DOUBLE_RIGHT.getHtml()+"      "+currentStepRelation.getId(), ContentMode.HTML);
            relationDirectionLabel.setDescription(currentStepRelation.getRelationTypeName()+" / "+currentStepRelation.getId());
            currentRouteStepInfoContainer.addComponent(relationDirectionLabel);

            HorizontalLayout currentSpacingLayoutB=new HorizontalLayout();
            currentSpacingLayoutB.setWidth(10,Unit.PIXELS);
            currentRouteStepInfoContainer.addComponent(currentSpacingLayoutB);

            RelationableValueVO toRelationable=currentStepRelation.getToRelationable();
            String toRelationableType=toRelationable.getRelationableTypeKind();
            String toDataTypeName=toRelationable.getRelationableTypeName();
            String toDataId=toRelationable.getId();
            String toRelationableInfoText=null;
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(toRelationableType)){
                toRelationableInfoText= FontAwesome.TAGS.getHtml()+"维度数据  "+FontAwesome.KEY.getHtml()+" "+toDataId;

            }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(toRelationableType)){
                toRelationableInfoText= FontAwesome.CLONE.getHtml()+"事实数据  "+FontAwesome.KEY.getHtml()+" "+toDataId;
            }

            Label toRelationableInfoLabel=new Label(toRelationableInfoText, ContentMode.HTML);
            toRelationableInfoLabel.setDescription(toDataTypeName+" / "+toDataId);
            toRelationableInfoLabel.addStyleName(ValoTheme.LABEL_SMALL);
            currentRouteStepInfoContainer.addComponent(toRelationableInfoLabel);

            routeInfoContainer.addComponent(currentRouteStepInfoContainer);
        }
    }
}
