package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationablesPathVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 5/8/17.
 */
public class RelationablesPathInfoList extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Table relationablesPathInfoTable;
    private FindRelationInfoOfTwoAnalyzingDataPanel containerFindRelationInfoOfTwoAnalyzingDataPanel;

    public RelationablesPathInfoList(UserClientInfo userClientInfo){
        this.setMargin(new MarginInfo(true,false,true,false));
        int browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        this.setHeight(browserWindowHeight-210,Unit.PIXELS);
        this.setWidth(280,Unit.PIXELS);
        this.currentUserClientInfo = userClientInfo;

        this.relationablesPathInfoTable =new Table();
        this.relationablesPathInfoTable.setWidth(100, Unit.PERCENTAGE);
        this.relationablesPathInfoTable.setHeight(browserWindowHeight-210,Unit.PIXELS);
        this.relationablesPathInfoTable.addStyleName(ValoTheme.TABLE_SMALL);
        this.relationablesPathInfoTable.setSelectable(true);
        this.relationablesPathInfoTable.addContainerProperty(" 路径信息 ",RelationablesPathInfoTableActions.class,null);
        this.relationablesPathInfoTable.setColumnIcon(" 路径信息 ", VaadinIcons.CONNECT_O);
        this.relationablesPathInfoTable.setColumnAlignment(" 路径信息 ", Table.Align.RIGHT);
        this.relationablesPathInfoTable.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        addComponent(this.relationablesPathInfoTable);
    }

    public void renderRelationablesPathsList(List<RelationablesPathVO> pathList){
        Container dataContainer = this.relationablesPathInfoTable.getContainerDataSource();
        dataContainer.removeAllItems();
        Container queryResultDataContainer = new IndexedContainer();
        this.relationablesPathInfoTable.setContainerDataSource(queryResultDataContainer);
        this.relationablesPathInfoTable.addContainerProperty(" 路径信息 ",RelationablesPathInfoTableActions.class,null);
        this.relationablesPathInfoTable.setColumnIcon(" 路径信息 ", VaadinIcons.CONNECT_O);
        this.relationablesPathInfoTable.setColumnAlignment(" 路径信息 ", Table.Align.RIGHT);
        this.relationablesPathInfoTable.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        if(pathList!=null) {
            for(int i=0;i<pathList.size();i++){
                RelationablesPathVO currentRelationablesPathVO=pathList.get(i);
                RelationablesPathInfoTableActions currentRelationablesPathInfoTableActions=new RelationablesPathInfoTableActions(this.currentUserClientInfo,currentRelationablesPathVO,i);
                currentRelationablesPathInfoTableActions.setContainerRelationablesPathInfoList(this);
                String pathId = ""+i;
                Item newRecord = this.relationablesPathInfoTable.addItem("dataRelation_index_" + pathId);
                newRecord.getItemProperty(" 路径信息 ").setValue(currentRelationablesPathInfoTableActions);
            }
        }
    }

    public FindRelationInfoOfTwoAnalyzingDataPanel getContainerFindRelationInfoOfTwoAnalyzingDataPanel() {
        return containerFindRelationInfoOfTwoAnalyzingDataPanel;
    }

    public void setContainerFindRelationInfoOfTwoAnalyzingDataPanel(FindRelationInfoOfTwoAnalyzingDataPanel containerFindRelationInfoOfTwoAnalyzingDataPanel) {
        this.containerFindRelationInfoOfTwoAnalyzingDataPanel = containerFindRelationInfoOfTwoAnalyzingDataPanel;
    }
}
