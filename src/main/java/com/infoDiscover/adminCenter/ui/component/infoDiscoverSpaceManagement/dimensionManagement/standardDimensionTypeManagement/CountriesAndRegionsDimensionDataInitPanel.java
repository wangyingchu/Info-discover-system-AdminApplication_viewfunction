package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.dimensionManagement.standardDimensionTypeManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 3/1/17.
 */
public class CountriesAndRegionsDimensionDataInitPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Window containerDialog;
    private TextField propertyName;
    private CountriesAndRegionsDimensionDataInitPanelInvoker countriesAndRegionsDimensionDataInitPanelInvoker;

    public CountriesAndRegionsDimensionDataInitPanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle sectionTitle=new MainSectionTitle("世界国家地区信息维度数据初始化");
        addComponent(sectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        propertyName = new TextField("数据维度类型前缀");
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

    public CountriesAndRegionsDimensionDataInitPanelInvoker getCountriesAndRegionsDimensionDataInitPanelInvoker() {
        return countriesAndRegionsDimensionDataInitPanelInvoker;
    }

    public void setCountriesAndRegionsDimensionDataInitPanelInvoker(CountriesAndRegionsDimensionDataInitPanelInvoker countriesAndRegionsDimensionDataInitPanelInvoker) {
        this.countriesAndRegionsDimensionDataInitPanelInvoker = countriesAndRegionsDimensionDataInitPanelInvoker;
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void initDimensionTypeData(){
        final String dimensionTypeNamePerfixStr= propertyName.getValue();
        if(dimensionTypeNamePerfixStr==null||dimensionTypeNamePerfixStr.trim().equals("")){
            Notification errorNotification = new Notification("数据校验错误",
                    "请输入数据维度类型前缀", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean isSingleByteString= UICommonElementsUtil.checkIsSingleByteString(dimensionTypeNamePerfixStr);
        if(!isSingleByteString){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入数据维度类型前缀 "+dimensionTypeNamePerfixStr+" 中包含非ASCII字符", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean containsSpecialChars= UICommonElementsUtil.checkContainsSpecialChars(dimensionTypeNamePerfixStr);
        if(containsSpecialChars){
            Notification errorNotification = new Notification("数据校验错误",
                    "当前输入数据维度类型前缀 "+dimensionTypeNamePerfixStr+" 中包含禁止使用字符: ` = , ; : \" ' . [ ] < > & 空格", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        String continentsDimensionTypeName=dimensionTypeNamePerfixStr+"_"+"geo_Continents";
        String countriesAndRegionsDimensionTypeName=dimensionTypeNamePerfixStr+"_"+"geo_CountriesAndRegions";
        String geoBelongsToRelationTypeName=dimensionTypeNamePerfixStr+"_"+"geo_BelongsTo";

        boolean continentsDimensionTypeExistenceCheck=InfoDiscoverSpaceOperationUtil.checkDimensionTypeExistence(getDiscoverSpaceName(),continentsDimensionTypeName);
        if(continentsDimensionTypeExistenceCheck){
            Notification errorNotification = new Notification("数据校验错误",
                    "初始化世界国家地区信息维度数据所需洲际维度类型 "+continentsDimensionTypeName+" 已存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        boolean countriesAndRegionsDimensionTypeExistenceCheck=InfoDiscoverSpaceOperationUtil.checkDimensionTypeExistence(getDiscoverSpaceName(),countriesAndRegionsDimensionTypeName);
        if(countriesAndRegionsDimensionTypeExistenceCheck){
            Notification errorNotification = new Notification("数据校验错误",
                    "初始化世界国家地区信息维度数据所需国家地区维度类型 "+countriesAndRegionsDimensionTypeName+" 已存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        boolean geoBelongsToRelationTypeExistenceCheck=InfoDiscoverSpaceOperationUtil.checkRelationTypeExistence(getDiscoverSpaceName(),geoBelongsToRelationTypeName);
        if(geoBelongsToRelationTypeExistenceCheck){
            Notification errorNotification = new Notification("数据校验错误",
                    "初始化世界国家地区信息维度数据所需关系类型 "+geoBelongsToRelationTypeName+" 已存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        String confirmMessageString=" 请确认初始化前缀为 "+dimensionTypeNamePerfixStr+" 的世界国家地区信息维度数据";
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+confirmMessageString, ContentMode.HTML);

        final ConfirmDialog addDataConfirmDialog = new ConfirmDialog();
        addDataConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addDataConfirmDialog.close();

                boolean initDataResult=InfoDiscoverSpaceOperationUtil.initCountriesAndRegionsDimensionData(getDiscoverSpaceName(),dimensionTypeNamePerfixStr);
                if(getCountriesAndRegionsDimensionDataInitPanelInvoker()!=null){
                    getCountriesAndRegionsDimensionDataInitPanelInvoker().initCountriesAndRegionsDimensionDataActionFinish(initDataResult);
                }
                if(initDataResult){
                    getContainerDialog().close();
                    Notification resultNotification = new Notification("添加数据操作成功",
                            "初始化世界国家地区信息维度数据成功,数据维度类型前缀为: "+dimensionTypeNamePerfixStr, Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("初始化世界国家地区信息维度数据错误",
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
