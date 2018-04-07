package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.dimensionManagement.standardDimensionTypeManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.dataMart.RelationDirection;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import java.util.List;

/**
 * Created by wangychu on 3/2/17.
 */
public class ChineseAdministrativeDivisionDimensionDataInitPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Window containerDialog;
    private TextField propertyName;
    private ChineseAdministrativeDivisionDimensionDataInitPanelInvoker chineseAdministrativeDivisionDimensionDataInitPanelInvoker;

    public ChineseAdministrativeDivisionDimensionDataInitPanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle sectionTitle=new MainSectionTitle("中国行政区划信息维度数据初始化");
        addComponent(sectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        propertyName = new TextField("所属世界国家地区信息维度类型前缀");
        propertyName.setRequired(true);
        form.addComponent(propertyName);
        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("确定", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                initDimensionTypeData();
            }
        });
        addButton.setIcon(FontAwesome.CHECK_CIRCLE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);

    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }


    public ChineseAdministrativeDivisionDimensionDataInitPanelInvoker getChineseAdministrativeDivisionDimensionDataInitPanelInvoker() {
        return chineseAdministrativeDivisionDimensionDataInitPanelInvoker;
    }

    public void setChineseAdministrativeDivisionDimensionDataInitPanelInvoker(ChineseAdministrativeDivisionDimensionDataInitPanelInvoker chineseAdministrativeDivisionDimensionDataInitPanelInvoker) {
        this.chineseAdministrativeDivisionDimensionDataInitPanelInvoker = chineseAdministrativeDivisionDimensionDataInitPanelInvoker;
    }

    private void initDimensionTypeData(){
        final String dimensionTypeNamePerfixStr= propertyName.getValue();
        if(dimensionTypeNamePerfixStr==null||dimensionTypeNamePerfixStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入所属世界国家地区信息维度类型前缀", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(dimensionTypeNamePerfixStr);
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入所属世界国家地区信息维度类型前缀 "+dimensionTypeNamePerfixStr+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(dimensionTypeNamePerfixStr);
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入所属世界国家地区信息维度类型前缀 "+dimensionTypeNamePerfixStr+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        String countriesAndRegionsDimensionTypeName=dimensionTypeNamePerfixStr+"_"+"geo_CountriesAndRegions";
        String geoBelongsToRelationTypeName=dimensionTypeNamePerfixStr+"_"+"geo_BelongsTo";

        boolean countriesAndRegionsDimensionTypeExistenceCheck=InfoDiscoverSpaceOperationUtil.checkDimensionTypeExistence(getDiscoverSpaceName(),countriesAndRegionsDimensionTypeName);
        if(!countriesAndRegionsDimensionTypeExistenceCheck){
            Notification errorNotification = new Notification("数据校验错误",
                    "初始化中国行政区划信息维度数据所需国家地区维度类型 "+countriesAndRegionsDimensionTypeName+" 不存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        boolean geoBelongsToRelationTypeExistenceCheck=InfoDiscoverSpaceOperationUtil.checkRelationTypeExistence(getDiscoverSpaceName(),geoBelongsToRelationTypeName);
        if(!geoBelongsToRelationTypeExistenceCheck){
            Notification errorNotification = new Notification("数据校验错误",
                    "初始化中国行政区划信息维度数据所需关系类型 "+geoBelongsToRelationTypeName+" 不存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        ExploreParameters exploreParameters=new ExploreParameters();
        exploreParameters.setType(countriesAndRegionsDimensionTypeName);
        EqualFilteringItem defaultFilteringItem=new EqualFilteringItem("ISO3122_2Code","ISO 3166-2:CN");
        exploreParameters.setDefaultFilteringItem(defaultFilteringItem);
        List<MeasurableValueVO> chinaDimensionList= InfoDiscoverSpaceOperationUtil.queryDimensions(getDiscoverSpaceName(),exploreParameters);

        if(chinaDimensionList.size()==0){
            Notification errorNotification = new Notification("数据校验错误",
                    "维度类型为 "+countriesAndRegionsDimensionTypeName+" 的世界国家地区信息数据中不存在中国的维度数据", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        if(chinaDimensionList.size()>1){
            Notification errorNotification = new Notification("数据校验错误",
                    "维度类型为 "+countriesAndRegionsDimensionTypeName+" 的世界国家地区信息数据中存在多个标识为中国的维度数据", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        long _ChinaDimensionRelationsCountCheck=InfoDiscoverSpaceOperationUtil.
                countDimensionRelationsById(getDiscoverSpaceName(),chinaDimensionList.get(0).getId(),geoBelongsToRelationTypeName, RelationDirection.TO);
        if(_ChinaDimensionRelationsCountCheck!=0){
            Notification errorNotification = new Notification("数据校验错误",
                    "针对维度类型为 "+countriesAndRegionsDimensionTypeName+" 的世界国家地区信息数据已执行过中国行政区划信息维度数据初始化操作", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        String confirmMessageString=" 请确认在维度类型为 "+dimensionTypeNamePerfixStr+" 的世界国家地区信息中初始化中国行政区划信息维度数据";
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog addDataConfirmDialog = new ConfirmDialog();
        addDataConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addDataConfirmDialog.close();
                boolean initDataResult= InfoDiscoverSpaceOperationUtil.initChineseAdministrativeDivisionDimensionData(getDiscoverSpaceName(),dimensionTypeNamePerfixStr,chinaDimensionList.get(0).getId());
                if(getChineseAdministrativeDivisionDimensionDataInitPanelInvoker()!=null){
                    getChineseAdministrativeDivisionDimensionDataInitPanelInvoker().initChineseAdministrativeDivisionDimensionDataActionFinish(initDataResult);
                }
                if(initDataResult){
                    getContainerDialog().close();
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "初始化中国行政区划信息维度数据成功,数据维度类型前缀为: "+dimensionTypeNamePerfixStr, Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("初始化中国行政区划信息维度数据错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
                containerDialog.close();
            }
        };
        addDataConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addDataConfirmDialog);
    }
}
