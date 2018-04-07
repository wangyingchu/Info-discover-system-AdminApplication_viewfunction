package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event.BusinessSolutionCreatedEvent;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.util.RuntimeEnvironmentUtil;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.*;
import java.util.Date;

/**
 * Created by wangychu on 5/5/17.
 */
public class ImportBusinessSolutionPanel extends VerticalLayout implements Upload.Receiver {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private Upload upload;

    private String tempSolutionFileName;
    private String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
    private FileOutputStream fos;

    public ImportBusinessSolutionPanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        // Add New Business Solution Section
        MainSectionTitle addNewBusinessSolutionSectionTitle=new MainSectionTitle("导入业务解决方案模板");
        addComponent(addNewBusinessSolutionSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);
        form.setReadOnly(true);

        Label messageLabel=new Label(FontAwesome.INFO.getHtml()+" 上传Zip格式的业务解决方案模板定义文件并在当前信息发现平台中创建该模板.<br/><br/>", ContentMode.HTML);
        messageLabel.addStyleName(ValoTheme.LABEL_SMALL);
        form.addComponent(messageLabel);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        upload = new Upload(null, this);
        upload.setImmediate(true);
        upload.setButtonCaption("导入业务解决方案模板");
        upload.addFailedListener(new Upload.FailedListener() {
            @Override
            public void uploadFailed(Upload.FailedEvent failedEvent) {
                Notification errorNotification = new Notification("数据传输错误",
                        "上传业务解决方案模板文件 "+failedEvent.getFilename()+" 失败", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }
        });

        upload.addSucceededListener(new Upload.SucceededListener() {

            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                if("application/zip".equals(event.getMIMEType())){
                    executeImportBusinessSolutionData();
                }else{
                    Notification errorNotification = new Notification("数据校验错误",
                            "业务解决方案模板文件必须是ZIP格式的文件", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        });
        footer.addComponent(upload);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public OutputStream receiveUpload(String fileName, String fileMIMEType) {
        tempSolutionFileName=tempFileDir+new Date().getTime()+fileName;
        fos = null;
        // Output stream to write to
        File file = new File(tempSolutionFileName);
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

    private void executeImportBusinessSolutionData(){
        boolean importSolutionResult=BusinessSolutionOperationUtil.importBusinessSolutionTemplateFromZipFile(tempSolutionFileName);
        if(importSolutionResult){
            this.containerDialog.close();
            BusinessSolutionCreatedEvent businessSolutionCreatedEvent=new BusinessSolutionCreatedEvent(null);
            currentUserClientInfo.getEventBlackBoard().fire(businessSolutionCreatedEvent);

            Notification resultNotification = new Notification("添加数据操作成功",
                    "导入业务解决方案模板成功", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification("导入业务解决方案模板错误",
                    "业务解决方案已存在或发生服务器端错误", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    protected void finalize() {
        try {
            super.finalize();
            if(fos!=null) {
                fos.close();
            }
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
    }
}
