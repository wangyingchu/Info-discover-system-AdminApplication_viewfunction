package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 11/7/16.
 */
public class QueryExploreParametersConfigInput extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private TextField pageSizeEditor;
    private TextField startPageEditor;
    private TextField endPageEditor;
    private TextField resultNumberEditor;
    private CheckBox distinctModeCheck;
    private QueryTypeDataInstancePanel containerQueryTypeDataInstancePanel;

    public QueryExploreParametersConfigInput(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        setSpacing(true);
        setMargin(true);

        MainSectionTitle setConfigParametersTitle =new MainSectionTitle("设定查询结果集参数");
        addComponent(setConfigParametersTitle);

        Label messagePromptLabel=new Label(FontAwesome.INFO_CIRCLE.getHtml()+" 设置查询最大返回结果数将忽略查询起始页以及查询结束页中的设定", ContentMode.HTML);
        messagePromptLabel.addStyleName(ValoTheme.LABEL_SMALL);
        addComponent(messagePromptLabel);

        FormLayout propertiesEditForm = new FormLayout();
        propertiesEditForm.setMargin(false);
        propertiesEditForm.setWidth("100%");
        propertiesEditForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        addComponent(propertiesEditForm);

        this.pageSizeEditor=new TextField("查询单页返回结果数");
        this.pageSizeEditor.setConverter(Integer.class);
        this.pageSizeEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null,null));
        propertiesEditForm.addComponent(this.pageSizeEditor);

        this.startPageEditor=new TextField("查询起始页");
        this.startPageEditor.setConverter(Integer.class);
        this.startPageEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null,null));
        propertiesEditForm.addComponent(this.startPageEditor);

        this.endPageEditor=new TextField("查询结束页");
        this.endPageEditor.setConverter(Integer.class);
        this.endPageEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null,null));
        propertiesEditForm.addComponent(this.endPageEditor);

        this.resultNumberEditor=new TextField("查询最大返回结果数");
        this.resultNumberEditor.setConverter(Long.class);
        this.resultNumberEditor.addValidator(new LongRangeValidator("该项属性值必须为LONG类型", null,null));
        propertiesEditForm.addComponent(this.resultNumberEditor);

        this.distinctModeCheck=new CheckBox("去除重复结果数据");
        propertiesEditForm.addComponent(this.distinctModeCheck);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        footer.setWidth("100%");
        Button confirmButton=new Button("确定", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(containerQueryTypeDataInstancePanel!=null){
                    if(pageSizeEditor.getValue()!=null){
                        Integer pageSizeValue=(Integer)pageSizeEditor.getConvertedValue();
                        containerQueryTypeDataInstancePanel.setQueryPageSize(pageSizeValue);
                    }
                    if(startPageEditor.getValue()!=null){
                        Integer startPageValue=(Integer)startPageEditor.getConvertedValue();
                        containerQueryTypeDataInstancePanel.setQueryStartPage(startPageValue);
                    }
                    if(endPageEditor.getValue()!=null){
                        Integer endPageValue=(Integer)endPageEditor.getConvertedValue();
                        containerQueryTypeDataInstancePanel.setQueryEndPage(endPageValue);
                    }
                    if(resultNumberEditor.getValue()!=null){
                        Long resultNumberValue=(Long)resultNumberEditor.getConvertedValue();
                        containerQueryTypeDataInstancePanel.setMaxResultNumber(resultNumberValue);
                    }else{
                        containerQueryTypeDataInstancePanel.setMaxResultNumber(0);
                    }
                    containerQueryTypeDataInstancePanel.setQueryDistinctMode(distinctModeCheck.getValue());
                }
                getContainerDialog().close();
            }
        });
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName("primary");
        footer.addComponent(confirmButton);
        addComponent(footer);
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setPageSize(int pageSize){
        this.pageSizeEditor.setValue(""+pageSize);
    }

    public void setStartPage(int startPage){
        this.startPageEditor.setValue(""+startPage);
    }

    public void setEndPage(int endPage){
        this.endPageEditor.setValue(""+endPage);
    }

    public void setResultNumber(long resultNumber){
        this.resultNumberEditor.setValue(""+resultNumber);
    }

    public void setDistinctMode(boolean modeValue){
        this.distinctModeCheck.setValue(modeValue);
    }

    public QueryTypeDataInstancePanel getContainerQueryTypeDataInstancePanel() {
        return containerQueryTypeDataInstancePanel;
    }

    public void setContainerQueryTypeDataInstancePanel(QueryTypeDataInstancePanel containerQueryTypeDataInstancePanel) {
        this.containerQueryTypeDataInstancePanel = containerQueryTypeDataInstancePanel;
    }
}
