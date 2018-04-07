package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;

public class ExportBusinessSolutionPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String businessSolutionName;

    private FileDownloader businessSolutionTemplateFileDownloader;
    private File businessSolutionTemplateFile;
    private Button exportBusinessSolutionButton;

    public ExportBusinessSolutionPanel(UserClientInfo userClientInfo,String businessSolutionName){
        this.currentUserClientInfo=userClientInfo;
        this.businessSolutionName=businessSolutionName;
        setSpacing(true);
        setMargin(true);
        // Export Business Solution Section
        MainSectionTitle addNewBusinessSolutionSectionTitle=new MainSectionTitle("导出业务解决方案模板");
        addComponent(addNewBusinessSolutionSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);
        form.setReadOnly(true);

        Label messageLabel=new Label(FontAwesome.INFO.getHtml()+" 以Zip格式导出业务解决方案模板 "+this.businessSolutionName+" 中定义的数据，使用该Zip文件可以在其他信息发现平台中重新导入业务解决方案模板中的信息.<br/><br/>", ContentMode.HTML);
        messageLabel.addStyleName(ValoTheme.LABEL_SMALL);
        form.addComponent(messageLabel);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        exportBusinessSolutionButton=new Button("导出业务解决方案模板");
        exportBusinessSolutionButton.setIcon(VaadinIcons.UPLOAD);
        exportBusinessSolutionButton.addStyleName("primary");
        footer.addComponent(exportBusinessSolutionButton);

        setupFileDownloader();
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void setupFileDownloader(){
        BusinessSolutionOperationUtil.generateBusinessSolutionTemplateFile(this.businessSolutionName);
        this.businessSolutionTemplateFile= BusinessSolutionOperationUtil.generateBusinessSolutionTemplateFile(this.businessSolutionName);
        Resource res = new FileResource(this.businessSolutionTemplateFile);
        if(this.businessSolutionTemplateFileDownloader==null){
            this.businessSolutionTemplateFileDownloader = new FileDownloader(res);
            this.businessSolutionTemplateFileDownloader.extend(exportBusinessSolutionButton);
        }else{
            this.businessSolutionTemplateFileDownloader.setFileDownloadResource(res);
        }
    }

}
