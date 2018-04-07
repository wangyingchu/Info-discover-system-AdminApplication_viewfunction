package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 4/11/17.
 */
public class AddNewDataForAnalyzePanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String discoverSpaceName;
    private TextField idPart1TextField;
    private TextField idPart2TextField;
    private ProcessingDataAnalyzePanel relatedProcessingDataAnalyzePanel;

    public AddNewDataForAnalyzePanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle addNewDataSectionTitle=new SecondarySectionTitle("添加新的待分析数据");
        addComponent(addNewDataSectionTitle);

        HorizontalLayout inputFieldContainerLayout=new HorizontalLayout();

        Label dataIdfieldName=new Label(FontAwesome.KEY.getHtml()+" 待分析数据 ID: <span style='font-style: italic;'># </span>", ContentMode.HTML);
        inputFieldContainerLayout.addComponent(dataIdfieldName);

        HorizontalLayout spacingDivLayout01=new HorizontalLayout();
        spacingDivLayout01.setWidth(5,Unit.PIXELS);
        inputFieldContainerLayout.addComponent(spacingDivLayout01);

        idPart1TextField=new TextField();
        idPart1TextField.addStyleName(ValoTheme.TEXTAREA_TINY);
        idPart1TextField.setWidth(80,Unit.PIXELS);
        idPart1TextField.setConverter(Long.class);
        idPart1TextField.setValue("0");
        inputFieldContainerLayout.addComponent(idPart1TextField);

        HorizontalLayout spacingDivLayout02=new HorizontalLayout();
        spacingDivLayout02.setWidth(5,Unit.PIXELS);
        inputFieldContainerLayout.addComponent(spacingDivLayout02);

        Label dataIdfieldNamePart2=new Label(" <span style='font-style: italic;'>: </span>", ContentMode.HTML);
        inputFieldContainerLayout.addComponent(dataIdfieldNamePart2);

        HorizontalLayout spacingDivLayout03=new HorizontalLayout();
        spacingDivLayout03.setWidth(5,Unit.PIXELS);
        inputFieldContainerLayout.addComponent(spacingDivLayout03);

        idPart2TextField=new TextField();
        idPart2TextField.addStyleName(ValoTheme.TEXTAREA_TINY);
        idPart2TextField.setWidth(80,Unit.PIXELS);
        idPart2TextField.setConverter(Long.class);
        idPart2TextField.setValue("0");
        inputFieldContainerLayout.addComponent(idPart2TextField);

        this.addComponent(inputFieldContainerLayout);

        HorizontalLayout actionButtonsContainerDivLayout=new HorizontalLayout();
        actionButtonsContainerDivLayout.setWidth(100,Unit.PERCENTAGE);

        Button addButton=new Button("确认添加", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addNewAnalyzeDataInfo();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        actionButtonsContainerDivLayout.addComponent(addButton);
        actionButtonsContainerDivLayout.setComponentAlignment(addButton,Alignment.MIDDLE_RIGHT);
        this.addComponent(actionButtonsContainerDivLayout);
    }

    public Window getContainerDialog() {
        return containerDialog;
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

    private void addNewAnalyzeDataInfo(){
        if(idPart1TextField.getValue()!=null&&idPart2TextField.getValue()!=null){
            if(idPart1TextField.isValid()&&idPart2TextField.isValid()){
                String dataItemId="#"+idPart1TextField.getConvertedValue()+":"+idPart2TextField.getConvertedValue();
                MeasurableValueVO measurableValueVO=null;
                try {
                    measurableValueVO = InfoDiscoverSpaceOperationUtil.getMeasurableValueById(getDiscoverSpaceName(), dataItemId);
                }catch (Exception e){
                    //e.printStackTrace();
                }
                if(measurableValueVO==null){
                    Notification errorNotification = new Notification("数据校验错误","系统中不存在ID为 "+dataItemId+" 的待分析数据", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }else{
                    ProcessingDataVO newProcessingDataVO=new ProcessingDataVO();
                    newProcessingDataVO.setDataTypeKind(measurableValueVO.getMeasurableTypeKind());
                    newProcessingDataVO.setDataTypeName(measurableValueVO.getMeasurableTypeName());
                    newProcessingDataVO.setDiscoverSpaceName(measurableValueVO.getDiscoverSpaceName());
                    newProcessingDataVO.setId(measurableValueVO.getId());
                    if(getRelatedProcessingDataAnalyzePanel()!=null){
                        boolean addResult=getRelatedProcessingDataAnalyzePanel().addNewProcessingData(newProcessingDataVO);
                        if(addResult){
                            getContainerDialog().close();
                        }
                    }
                }
            }else{
                Notification errorNotification = new Notification("数据校验错误","请输入合法的待分析数据 ID", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }
        }else{
            Notification errorNotification = new Notification("数据校验错误","请输入待分析数据 ID", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public ProcessingDataAnalyzePanel getRelatedProcessingDataAnalyzePanel() {
        return relatedProcessingDataAnalyzePanel;
    }

    public void setRelatedProcessingDataAnalyzePanel(ProcessingDataAnalyzePanel relatedProcessingDataAnalyzePanel) {
        this.relatedProcessingDataAnalyzePanel = relatedProcessingDataAnalyzePanel;
    }
}
