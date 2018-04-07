package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.TableColumnValueIcon;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;

import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationChangeListener;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 12/8/16.
 */
public class RelationableRelationsList extends VerticalLayout implements TypeDataInstancePropertiesEditorInvoker{

    private UserClientInfo currentUserClientInfo;
    private RelationableValueVO relationableValueVO;
    private Table relationableRelationsTable;
    private HorizontalLayout paginationContainerLayout;
    private Label relationResultCountLabel;
    private int itemsPerRelationableRelationsTablePage=20;
    private MeasurablePropertiesGrid measurablePropertiesGrid;
    private long currentRelationsCount;
    private String currentRenderingPropertiesRelationId;
    private Pagination relationsDataPagination;

    public RelationableRelationsList(UserClientInfo userClientInfo, RelationableValueVO relationableValueVO) {
        this.currentUserClientInfo = userClientInfo;
        this.relationableValueVO = relationableValueVO;
        this.setWidth(100, Unit.PERCENTAGE);
        setSpacing(true);
        setMargin(true);

        HorizontalLayout relationsSummaryInfoContainerLayout = new HorizontalLayout();
        addComponent(relationsSummaryInfoContainerLayout);
        Label relationResultCountInfo = new Label(FontAwesome.SHARE_ALT_SQUARE.getHtml() + " 关联关系总量: ", ContentMode.HTML);
        relationResultCountInfo.addStyleName(ValoTheme.LABEL_TINY);
        relationResultCountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        relationsSummaryInfoContainerLayout.addComponent(relationResultCountInfo);
        relationsSummaryInfoContainerLayout.setComponentAlignment(relationResultCountInfo, Alignment.MIDDLE_LEFT);
        this.relationResultCountLabel = new Label("--");
        this.relationResultCountLabel.addStyleName("ui_appFriendlyElement");
        relationsSummaryInfoContainerLayout.addComponent(this.relationResultCountLabel);
        relationsSummaryInfoContainerLayout.setComponentAlignment(this.relationResultCountLabel, Alignment.MIDDLE_LEFT);

        this.relationableRelationsTable = new Table();
        this.relationableRelationsTable.setWidth(100, Unit.PERCENTAGE);
        this.relationableRelationsTable.setHeight(50, Unit.PIXELS);
        this.relationableRelationsTable.setSelectable(true);
        this.relationableRelationsTable.addStyleName(ValoTheme.TABLE_SMALL);
        this.relationableRelationsTable.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        this.relationableRelationsTable.setNullSelectionAllowed(false);

        this.relationableRelationsTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                renderRelationPropertiesInfo(itemClickEvent.getItem());
            }
        });
        addComponent(this.relationableRelationsTable);

        this.paginationContainerLayout = new HorizontalLayout();
        this.paginationContainerLayout.setWidth(100, Unit.PERCENTAGE);
        addComponent(this.paginationContainerLayout);

        this.measurablePropertiesGrid=new MeasurablePropertiesGrid();
        this.measurablePropertiesGrid.getPropertiesGridTitle().removeStyleName(ValoTheme.LABEL_H4);
        this.measurablePropertiesGrid.getPropertiesGridTitle().addStyleName(ValoTheme.LABEL_TINY);
        this.measurablePropertiesGrid.setTitleCaption(FontAwesome.LIST_UL.getHtml() + " 当前所选关系属性数据: ");
        this.measurablePropertiesGrid.setItemDirection(MeasurablePropertiesGrid.Direction.RIGHT, 2);
        this.measurablePropertiesGrid.setPropertiesGridHeight(200);

        measurablePropertiesGrid.addMenuItem("编辑关系属性......", FontAwesome.EDIT, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                renderRelationPropertiesEditorWindow();
            }
        });
        /*
        measurablePropertiesGrid.addMenuItem("Reload", FontAwesome.CIRCLE_O_NOTCH, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                Notification.show("Refresh: ");
                //modelTable.removeItem();
            }
        });
        */
        addComponent(measurablePropertiesGrid);
    }

    @Override
    public void attach() {
        super.attach();
        loadRelationsInfo();
        setInitPagination();
    }

    public void setRelationableRelationsTableHeight(int newTableHeight) {
        this.relationableRelationsTable.setHeight(newTableHeight, Unit.PIXELS);
    }

    private Pagination createPagination(long totalData, int initPageNumber) {
        final PaginationResource paginationResource = PaginationResource.newBuilder().setTotal(totalData).setPage(initPageNumber).setLimit(itemsPerRelationableRelationsTablePage).build();
        final Pagination pagination = new Pagination(paginationResource);
        return pagination;
    }

    private void renderRelationPropertiesInfo(Item relationItem){
        String propertiesGridTitleMessageStr=FontAwesome.LIST_UL.getHtml() + " 当前所选关系属性数据: ";
        this.measurablePropertiesGrid.removeItem();
        if(relationItem!=null){
            Property relationTypeProperty=relationItem.getItemProperty(" 关系类型");
            Property idProperty=relationItem.getItemProperty(" 关系ID");
            if(relationTypeProperty!=null&&relationTypeProperty.getValue()!=null){
                propertiesGridTitleMessageStr=propertiesGridTitleMessageStr+relationTypeProperty.getValue().toString()+" /";
            }
            if(idProperty!=null&&idProperty.getValue()!=null){
                propertiesGridTitleMessageStr=propertiesGridTitleMessageStr+FontAwesome.KEY.getHtml()+" "+idProperty.getValue().toString();
                this.currentRenderingPropertiesRelationId=idProperty.getValue().toString();
            }
            List<PropertyValueVO> relationProperties=InfoDiscoverSpaceOperationUtil.getMeasurablePropertiesById(relationableValueVO.getDiscoverSpaceName(),idProperty.getValue().toString());
            if(relationProperties.size()<=6){
                this.measurablePropertiesGrid.setItemDirection(MeasurablePropertiesGrid.Direction.RIGHT, 1);
            }else if(relationProperties.size()>6&&relationProperties.size()<=12){
                this.measurablePropertiesGrid.setItemDirection(MeasurablePropertiesGrid.Direction.RIGHT, 2);
            }else{
                this.measurablePropertiesGrid.setItemDirection(MeasurablePropertiesGrid.Direction.RIGHT, 3);
            }
            measurablePropertiesGrid.setItem(relationProperties);
        }
        this.measurablePropertiesGrid.setTitleCaption(propertiesGridTitleMessageStr);
    }

    public void removeRelationByRelationId(String relationId){
        String targetItemId="dataRelation_index_" + relationId;
        Item targetItem=this.relationableRelationsTable.getItem(targetItemId);
        if(targetItem!=null){
            boolean removeRelationResult=this.relationableRelationsTable.removeItem(targetItemId);
            if(removeRelationResult) {
                this.currentRelationsCount--;
                this.relationResultCountLabel.setValue("" + this.currentRelationsCount);

                int currentPaginationPage=this.relationsDataPagination.getCurrentPage();
                if(this.relationsDataPagination!=null){
                    this.relationsDataPagination.removeAllPageChangeListener();
                }
                this.paginationContainerLayout.removeAllComponents();

                if(currentPaginationPage*itemsPerRelationableRelationsTablePage>this.currentRelationsCount){
                    currentPaginationPage=currentPaginationPage-1;
                }
                this.relationsDataPagination = createPagination(this.currentRelationsCount, currentPaginationPage);
                this.relationsDataPagination.setItemsPerPageVisible(false);
                this.relationsDataPagination.addPageChangeListener(new PaginationChangeListener() {
                    @Override
                    public void changed(PaginationResource event) {
                        relationableRelationsTable.setCurrentPageFirstItemIndex(event.offset());
                    }
                });
                this.paginationContainerLayout.addComponent(this.relationsDataPagination);

                if(relationId.equals(this.currentRenderingPropertiesRelationId)){
                    renderRelationPropertiesInfo(null);
                    this.currentRenderingPropertiesRelationId=null;
                }
            }
        }
    }

    public void loadRelationsInfo(){
        List<RelationValueVO> relationValuesList = InfoDiscoverSpaceOperationUtil.getRelationableRelationsById(relationableValueVO.getDiscoverSpaceName(), relationableValueVO.getRelationableTypeKind(), relationableValueVO.getId());
        this.currentRelationsCount=relationValuesList.size();
        this.relationResultCountLabel.setValue("" + this.currentRelationsCount);

        Container dataContainer = this.relationableRelationsTable.getContainerDataSource();
        dataContainer.removeAllItems();
        Container queryResultDataContainer = new IndexedContainer();
        this.relationableRelationsTable.setContainerDataSource(queryResultDataContainer);

        this.relationableRelationsTable.addContainerProperty(" 关系方向", TableColumnValueIcon.class, null);
        this.relationableRelationsTable.setColumnHeader(" 关系方向", "   ");
        this.relationableRelationsTable.setColumnAlignment(" 关系方向", Table.Align.CENTER);
        this.relationableRelationsTable.setColumnWidth(" 关系方向", 30);

        this.relationableRelationsTable.addContainerProperty(" 操作", RelationableRelationsTableRowActions.class, null);
        this.relationableRelationsTable.setColumnIcon(" 操作", FontAwesome.WRENCH);
        this.relationableRelationsTable.setColumnAlignment(" 操作", Table.Align.CENTER);
        this.relationableRelationsTable.setColumnWidth(" 操作", 60);

        this.relationableRelationsTable.addContainerProperty(" 关系ID", String.class, "");
        this.relationableRelationsTable.setColumnIcon(" 关系ID", FontAwesome.KEY);
        this.relationableRelationsTable.setColumnWidth(" 关系ID", 120);

        this.relationableRelationsTable.addContainerProperty(" 关系类型", String.class, null);
        this.relationableRelationsTable.setColumnIcon(" 关系类型", FontAwesome.EXCHANGE);

        this.relationableRelationsTable.addContainerProperty(" 相关数据", RelationableInfoTableRowActions.class, null);
        this.relationableRelationsTable.setColumnIcon(" 相关数据", VaadinIcons.FILE_TREE_SMALL);
        this.relationableRelationsTable.setColumnAlignment(" 相关数据", Table.Align.RIGHT);
        //this.relationableRelationsTable.setColumnExpandRatio(" 相关数据", 0.8f);

        if (relationValuesList != null) {
            String currentRelationableId = this.relationableValueVO.getId();
            for (RelationValueVO currentRelationValueVO : relationValuesList) {
                String relationId = currentRelationValueVO.getId();
                Item newRecord = this.relationableRelationsTable.addItem("dataRelation_index_" + relationId);
                //If RelationValues have same id(related to themselves),the second will get a null return value
                if(newRecord!=null){
                    RelationableRelationsTableRowActions relationableRelationsTableRowActions = new RelationableRelationsTableRowActions(this.currentUserClientInfo, currentRelationValueVO);
                    relationableRelationsTableRowActions.setContainerRelationableRelationsList(this);
                    newRecord.getItemProperty(" 操作").setValue(relationableRelationsTableRowActions);
                    newRecord.getItemProperty(" 关系ID").setValue(relationId);
                    String relationType = currentRelationValueVO.getRelationTypeName();
                    newRecord.getItemProperty(" 关系类型").setValue(relationType);
                    RelationableValueVO fromRelationable = currentRelationValueVO.getFromRelationable();
                    RelationableValueVO toRelationable = currentRelationValueVO.getToRelationable();
                    if (fromRelationable.getId().equals(currentRelationableId)) {
                        //to other relationable
                        TableColumnValueIcon tableColumnValueIcon = new TableColumnValueIcon(VaadinIcons.ANGLE_DOUBLE_RIGHT, "关系由当前数据发出");
                        newRecord.getItemProperty(" 关系方向").setValue(tableColumnValueIcon);
                        RelationableInfoTableRowActions relationableInfoTableRowActions = new RelationableInfoTableRowActions(this.currentUserClientInfo, toRelationable);
                        newRecord.getItemProperty(" 相关数据").setValue(relationableInfoTableRowActions);
                    }
                    if (toRelationable.getId().equals(currentRelationableId)) {
                        //from other relationable
                        TableColumnValueIcon tableColumnValueIcon = new TableColumnValueIcon(FontAwesome.ANGLE_DOUBLE_LEFT, "关系指向当前数据");
                        newRecord.getItemProperty(" 关系方向").setValue(tableColumnValueIcon);
                        RelationableInfoTableRowActions relationableInfoTableRowActions = new RelationableInfoTableRowActions(this.currentUserClientInfo, fromRelationable);
                        newRecord.getItemProperty(" 相关数据").setValue(relationableInfoTableRowActions);
                    }
                }
            }
        }
        this.relationableRelationsTable.setPageLength(itemsPerRelationableRelationsTablePage);
    }

    public void setInitPagination(){
        if(this.relationsDataPagination!=null){
            this.relationsDataPagination.removeAllPageChangeListener();
        }
        this.paginationContainerLayout.removeAllComponents();
        int startPage = this.currentRelationsCount > 0 ? 1 : 0;
        this.relationsDataPagination = createPagination(this.currentRelationsCount, startPage);
        this.relationsDataPagination.setItemsPerPageVisible(false);
        this.relationsDataPagination.addPageChangeListener(new PaginationChangeListener() {
            @Override
            public void changed(PaginationResource event) {
                relationableRelationsTable.setCurrentPageFirstItemIndex(event.offset());
            }
        });
        this.paginationContainerLayout.addComponent(this.relationsDataPagination);
    }

    public void reloadRelationsInfo(){
        loadRelationsInfo();
        setInitPagination();
        renderRelationPropertiesInfo(null);
        this.currentRenderingPropertiesRelationId=null;
    }

    public void renderRelationPropertiesEditorWindow(){
        if( this.currentRenderingPropertiesRelationId==null){
            Notification errorNotification = new Notification("数据校验错误",
                    " 请选择需要编辑属性的关系数据", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        MeasurableValueVO targetMeasurableValue=InfoDiscoverSpaceOperationUtil.getMeasurableValueById(this.relationableValueVO.getDiscoverSpaceName(),this.currentRenderingPropertiesRelationId);
        if(targetMeasurableValue==null){
            Notification errorNotification = new Notification("获取数据错误",
                    "系统中不存在ID为 "+this.relationableValueVO.getId()+" 的数据", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        TypeDataInstancePropertiesEditorPanel typeDataInstancePropertiesEditorPanel=new TypeDataInstancePropertiesEditorPanel(this.currentUserClientInfo,targetMeasurableValue);
        typeDataInstancePropertiesEditorPanel.setPropertiesEditorContainerPanelHeight(460);
        VerticalLayout typeDataInstancePropertiesEditContainerLayout=new VerticalLayout();
        typeDataInstancePropertiesEditContainerLayout.setSpacing(true);
        typeDataInstancePropertiesEditContainerLayout.setMargin(true);

        SecondarySectionTitle updateTypePropertiesSectionTitle=new SecondarySectionTitle("编辑关系属性数据");
        typeDataInstancePropertiesEditContainerLayout.addComponent(updateTypePropertiesSectionTitle);

        String dataTypeName= targetMeasurableValue.getMeasurableTypeName();
        //String discoverSpaceName= targetMeasurableValue.getDiscoverSpaceName();
        String dataId= targetMeasurableValue.getId();
        //String dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.SHARE_ALT.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
        String dataInstanceBasicInfoNoticeText=FontAwesome.SHARE_ALT.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
        Label sectionActionBarLabel=new Label(dataInstanceBasicInfoNoticeText, ContentMode.HTML);
        SectionActionsBar dataTypeNoticeActionsBar = new SectionActionsBar(sectionActionBarLabel);
        typeDataInstancePropertiesEditContainerLayout.addComponent(dataTypeNoticeActionsBar);
        typeDataInstancePropertiesEditContainerLayout.addComponent(typeDataInstancePropertiesEditorPanel);

        final Window window = new Window();
        window.setWidth(550, Unit.PIXELS);
        window.setHeight(700,Unit.PIXELS);
        window.setCaptionAsHtml(true);
        window.setResizable(false);
        window.setDraggable(true);
        window.setModal(true);
        window.center();
        window.setContent(typeDataInstancePropertiesEditContainerLayout);
        typeDataInstancePropertiesEditorPanel.setContainerDialog(window);
        typeDataInstancePropertiesEditorPanel.setTypeDataInstancePropertiesEditorInvoker(this);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public void updateTypeDataInstancePropertiesActionFinish(boolean actionResult) {
        if(actionResult){
            String targetItemId="dataRelation_index_" + this.currentRenderingPropertiesRelationId;
            Item targetItem=this.relationableRelationsTable.getItem(targetItemId);
            if(targetItem!=null){
                renderRelationPropertiesInfo(targetItem);
            }
        }
    }
}