package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 5/1/17.
 */
public class SimilarRelationablesList extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Table similarRelationableTable;
    private List<RelationableValueVO> similarRelationables;
    private int browserWindowHeight;

    public SimilarRelationablesList(UserClientInfo userClientInfo,int browserWindowHeight){
        this.currentUserClientInfo = userClientInfo;
        this.browserWindowHeight=browserWindowHeight;
        this.similarRelationableTable =new Table();
        this.setWidth(420,Unit.PIXELS);

        this.similarRelationableTable.setWidth(100, Unit.PERCENTAGE);
        this.similarRelationableTable.setHeight(browserWindowHeight-200,Unit.PIXELS);
        this.similarRelationableTable.addStyleName(ValoTheme.TABLE_SMALL);
        this.similarRelationableTable.setSelectable(false);

        this.similarRelationableTable.addContainerProperty(" 相似数据",SimilarRelationableTableActions.class,null);
        this.similarRelationableTable.setColumnIcon(" 相似数据", VaadinIcons.FLIP_H);
        this.similarRelationableTable.setColumnAlignment(" 相似数据", Table.Align.RIGHT);
        addComponent(this.similarRelationableTable);
    }

    public void renderSimilarRelationablesList(List<RelationableValueVO> similarRelationables){
        this.similarRelationables=similarRelationables;
        Container dataContainer = this.similarRelationableTable.getContainerDataSource();
        dataContainer.removeAllItems();
        Container queryResultDataContainer = new IndexedContainer();
        this.similarRelationableTable.setContainerDataSource(queryResultDataContainer);
        this.similarRelationableTable.addContainerProperty(" 相似数据",SimilarRelationableTableActions.class,null);
        this.similarRelationableTable.setColumnIcon(" 相似数据", VaadinIcons.FLIP_H);
        this.similarRelationableTable.setColumnAlignment(" 相似数据", Table.Align.RIGHT);
        if (similarRelationables != null) {
            for(RelationableValueVO currentRelationableValueVO:similarRelationables){
                SimilarRelationableTableActions relationableInfoTableRowActions = new SimilarRelationableTableActions(this.currentUserClientInfo, currentRelationableValueVO);
                String relationId = currentRelationableValueVO.getId();
                Item newRecord = this.similarRelationableTable.addItem("dataRelation_index_" + relationId);
                newRecord.getItemProperty(" 相似数据").setValue(relationableInfoTableRowActions);
            }
        }
    }
}
