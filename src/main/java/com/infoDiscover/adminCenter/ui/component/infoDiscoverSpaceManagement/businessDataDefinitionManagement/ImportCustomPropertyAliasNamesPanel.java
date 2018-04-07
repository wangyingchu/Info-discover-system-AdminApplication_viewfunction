package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.businessDataDefinitionManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.util.RuntimeEnvironmentUtil;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by wangychu on 02/06/2017.
 */
public class ImportCustomPropertyAliasNamesPanel extends VerticalLayout implements Upload.Receiver {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Window containerDialog;
    private CustomPropertyAliasNameManagementPanel relatedCustomPropertyAliasNameManagementPanel;
    private SectionActionsBar dataFieldActionsBar;
    private Upload upload;
    private OptionGroup existedAliasNameHandleMethodsOptionGroup;

    private String templateAliasNameDataFileName;
    private String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();

    public ImportCustomPropertyAliasNamesPanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        MainSectionTitle addNewPropertySectionTitle=new MainSectionTitle("导入自定义属性别名");
        addComponent(addNewPropertySectionTitle);

        dataFieldActionsBar=new SectionActionsBar(new Label("---" , ContentMode.HTML));
        addComponent(dataFieldActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        existedAliasNameHandleMethodsOptionGroup = new OptionGroup("重复属性别名定义处理策略");
        existedAliasNameHandleMethodsOptionGroup.addItem("覆盖重复别名");
        existedAliasNameHandleMethodsOptionGroup.addItem("忽略重复别名");
        existedAliasNameHandleMethodsOptionGroup.select("覆盖重复别名");
        existedAliasNameHandleMethodsOptionGroup.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        form.addComponent(existedAliasNameHandleMethodsOptionGroup);

        upload = new Upload(null, this);
        // make analyzing start immediatedly when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption("选择自定义属性别名数据文件");

        upload.addFailedListener(new Upload.FailedListener() {
            @Override
            public void uploadFailed(Upload.FailedEvent failedEvent) {
                Notification errorNotification = new Notification("数据传输错误",
                        "上传自定义属性别名数据文件 "+failedEvent.getFilename()+" 失败", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }
        });

        upload.addSucceededListener(new Upload.SucceededListener() {

            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                if("application/json".equals(event.getMIMEType())){
                    executeImportCustomPropertyAliasNames();
                }else{
                    Notification errorNotification = new Notification("数据校验错误",
                            "自定义属性别名数据文件必须是JSON格式的文件", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        });

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);
        footer.addComponent(upload);

        addComponent(form);
    }

    @Override
    public void attach() {
        super.attach();
        Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName(), ContentMode.HTML);
        dataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public CustomPropertyAliasNameManagementPanel getRelatedCustomPropertyAliasNameManagementPanel() {
        return relatedCustomPropertyAliasNameManagementPanel;
    }

    public void setRelatedCustomPropertyAliasNameManagementPanel(CustomPropertyAliasNameManagementPanel relatedCustomPropertyAliasNameManagementPanel) {
        this.relatedCustomPropertyAliasNameManagementPanel = relatedCustomPropertyAliasNameManagementPanel;
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        templateAliasNameDataFileName=tempFileDir+this.getDiscoverSpaceName()+new Date().getTime()+filename;
        FileOutputStream fos = null;
        // Output stream to write to
        File file = new File(templateAliasNameDataFileName);
        try {
            // Open the file for writing.
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            // Error while opening the file. Not reported here.
            e.printStackTrace();
            return null;
        }
        return fos;
    }

    private void executeImportCustomPropertyAliasNames(){
        String methodSelectedValue=existedAliasNameHandleMethodsOptionGroup.getValue().toString();
        String handleMethod=null;
        if("覆盖重复别名".equals(methodSelectedValue)){
            handleMethod=InfoDiscoverSpaceOperationUtil.ExistedPropertyAliasNameHandleMethod_REPLACE;
        }
        if("忽略重复别名".equals(methodSelectedValue)){
            handleMethod=InfoDiscoverSpaceOperationUtil.ExistedPropertyAliasNameHandleMethod_IGNORE;
        }
        boolean importDataResult=InfoDiscoverSpaceOperationUtil.
                importCustomPropertyAliasNamesFromJsonFile(this.getDiscoverSpaceName(),this.templateAliasNameDataFileName,handleMethod);
        if(importDataResult){
            this.containerDialog.close();
            this.getRelatedCustomPropertyAliasNameManagementPanel().renderCustomPropertyAliasInfo();
            Notification resultNotification = new Notification("添加数据操作成功",
                    "导入自定义属性别名成功", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification("导入自定义属性别名错误",
                    "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }
}
