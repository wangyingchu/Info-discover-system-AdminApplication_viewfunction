package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangychu on 12/16/16.
 */
public class ProcessingDataList extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private List<ProcessingDataVO> processingDataVOList;
    private String processingDataType;
    private Table processingDataInstanceTable;
    private boolean isSelectable;

    public ProcessingDataList(UserClientInfo userClientInfo,boolean isSelectable) {
        this.currentUserClientInfo = userClientInfo;
        this.setWidth(100,Unit.PERCENTAGE);
        this.isSelectable=isSelectable;
        this.processingDataInstanceTable =new Table();
        this.processingDataInstanceTable.setWidth(100, Unit.PERCENTAGE);
        this.processingDataInstanceTable.setSelectable(true);
        addComponent(this.processingDataInstanceTable);
        if(this.isSelectable) {
            this.processingDataInstanceTable.addContainerProperty(" 选择", CheckBox.class, null);
            this.processingDataInstanceTable.setColumnIcon(" 选择", FontAwesome.CHECK_SQUARE_O);
            this.processingDataInstanceTable.setColumnWidth(" 选择", 70);
            this.processingDataInstanceTable.setColumnAlignment(" 选择", Table.Align.CENTER);
        }
        this.processingDataInstanceTable.addContainerProperty(" 待处理数据",Label.class,"");
        this.processingDataInstanceTable.setColumnIcon(" 待处理数据", VaadinIcons.BUTTON);

        this.processingDataInstanceTable.addContainerProperty(" 操作",ProcessingDataInstanceTableRowActions.class,null);
        this.processingDataInstanceTable.setColumnIcon(" 操作", FontAwesome.WRENCH);
        this.processingDataInstanceTable.setColumnWidth(" 操作", 130);
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public List<ProcessingDataVO> getProcessingDataVOList() {
        return processingDataVOList;
    }

    public void setProcessingDataVOList(List<ProcessingDataVO> processingDataVOList) {
        this.processingDataVOList = processingDataVOList;
    }

    public String getProcessingDataType() {
        return processingDataType;
    }

    public void setProcessingDataType(String processingDataType) {
        this.processingDataType = processingDataType;
    }

    @Override
    public void attach() {
        super.attach();
        if(getProcessingDataVOList()!=null){
            loadProcessingData();
        }
    }

    public void removeProcessingDataByDataId(String relationId){
        String targetItemId="processingDataInstance_index_" + relationId;
        Item targetItem=this.processingDataInstanceTable.getItem(targetItemId);
        if(targetItem!=null){
            this.processingDataInstanceTable.removeItem(targetItemId);
        }
    }

    public void refreshDataList(){
        if(getProcessingDataVOList()!=null){
            this.processingDataInstanceTable.removeAllItems();
            loadProcessingData();
        }
    }

    private void loadProcessingData(){
        for(ProcessingDataVO currentProcessingDataVO:getProcessingDataVOList()){
            String dataTypeKind=currentProcessingDataVO.getDataTypeKind();
            String dataId=currentProcessingDataVO.getId();
            String dataTypeName=currentProcessingDataVO.getDataTypeName();
            String processingDataInfoText=null;

            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(dataTypeKind)){
                processingDataInfoText= FontAwesome.TAGS.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;

            }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataTypeKind)){
                processingDataInfoText= FontAwesome.CLONE.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;

            }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataTypeKind)){
                processingDataInfoText= FontAwesome.SHARE_ALT.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
            }
            Label processingDataInfoLabel=new Label(processingDataInfoText, ContentMode.HTML);

            ProcessingDataInstanceTableRowActions processingDataInstanceTableRowActions=new ProcessingDataInstanceTableRowActions(this.currentUserClientInfo);
            processingDataInstanceTableRowActions.setContainerProcessingDataList(this);
            processingDataInstanceTableRowActions.setProcessingDataVO(currentProcessingDataVO);

            Item newRecord=this.processingDataInstanceTable.addItem("processingDataInstance_index_"+dataId);
            if(this.isSelectable) {
                newRecord.getItemProperty(" 选择").setValue(new CheckBox());
            }
            newRecord.getItemProperty(" 待处理数据").setValue(processingDataInfoLabel);
            newRecord.getItemProperty(" 操作").setValue(processingDataInstanceTableRowActions);
        }
    }

    public void setProcessingDataInstanceTableHeight(int tableHeight){
        this.processingDataInstanceTable.setHeight(tableHeight,Unit.PIXELS);
    }

    public List<String> getSelectedDataId(){
        List<String> selectedItemIdList=new ArrayList<String>();
        if(isSelectable) {
            Collection itemIdsCollection = this.processingDataInstanceTable.getVisibleItemIds();
            Iterator idsIterator = itemIdsCollection.iterator();
            while (idsIterator.hasNext()) {
                Object itemId=idsIterator.next();
                Item currentItem = this.processingDataInstanceTable.getItem(itemId);
                if (currentItem != null) {
                    Property slector=currentItem.getItemProperty(" 选择");
                    if(slector!=null&&slector.getValue()!=null){
                        CheckBox checkBoxElement=(CheckBox)slector.getValue();
                        if(checkBoxElement.getValue()){
                            String selectedDataId=itemId.toString().replaceFirst("processingDataInstance_index_","");
                            selectedItemIdList.add(selectedDataId);
                        }
                    }
                }
            }
        }
        return selectedItemIdList;
    }

    public List<ProcessingDataVO> getSelectedProcessingDataInfo(){
        List<ProcessingDataVO> selectedProcessingDataList=new ArrayList<>();
        List<String> selectedDataIdList=getSelectedDataId();
        for(String currentDataId:selectedDataIdList){
            for(ProcessingDataVO listProcessingData:this.processingDataVOList){
                if(listProcessingData.getId().equals(currentDataId)){
                    selectedProcessingDataList.add(listProcessingData);
                    break;
                }
            }
        }
        return selectedProcessingDataList;
    }
}
