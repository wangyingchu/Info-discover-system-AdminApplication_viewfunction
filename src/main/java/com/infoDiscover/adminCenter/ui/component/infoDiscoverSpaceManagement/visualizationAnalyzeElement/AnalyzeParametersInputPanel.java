package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;
import java.util.Map;

/**
 * Created by wangychu on 3/20/17.
 */
public class AnalyzeParametersInputPanel extends VerticalLayout {

    public static final String NumberTypeProperties="NUMBER";
    public static final String DateTypeProperties="DATE";
    public static final String FloatTypeProperties="FLOAT";
    public static final String StringTypeProperties="STRING";

    private UserClientInfo currentUserClientInfo;
    private VerticalLayout parametersInputContainerLayout;
    private String visualizationChartType;
    private List<String> measurablePropertiesNameList;
    private List<String> datePropertiesNameList;
    private BaseChartParametersInput chartParametersInput;
    private VisualizationAnalyzePanel parentVisualizationAnalyzePanel;
    private Map<String,List<String>> dataTypePropertiesNameListMap;
    private List<String> geographicalCoordinatesPropertiesNameList;
    private List<String> stringPropertiesNameList;

    public AnalyzeParametersInputPanel(UserClientInfo userClientInfo,String visualizationChartType, Map<String,List<String>> dataTypePropertiesNameListMap) {
        this.currentUserClientInfo = userClientInfo;
        this.visualizationChartType=visualizationChartType;
        this.dataTypePropertiesNameListMap=dataTypePropertiesNameListMap;
        this.measurablePropertiesNameList=this.dataTypePropertiesNameListMap.get(NumberTypeProperties);
        this.datePropertiesNameList=this.dataTypePropertiesNameListMap.get(DateTypeProperties);
        this.stringPropertiesNameList=this.dataTypePropertiesNameListMap.get(StringTypeProperties);
        this.geographicalCoordinatesPropertiesNameList=this.dataTypePropertiesNameListMap.get(FloatTypeProperties);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        this.setHeight(browserWindowHeight-110,Unit.PIXELS);
        this.setWidth(100,Unit.PERCENTAGE);
        this.setMargin(false);
        this.setSpacing(false);

        this.parametersInputContainerLayout=new VerticalLayout();
        this.parametersInputContainerLayout.setWidth(100,Unit.PERCENTAGE);

        Panel parametersInputContainerPanel=new Panel();
        parametersInputContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        parametersInputContainerPanel.setContent(this.parametersInputContainerLayout);
        parametersInputContainerPanel.setHeight(browserWindowHeight-210,Unit.PIXELS);
        this.addComponent(parametersInputContainerPanel);

        VerticalLayout spacingLayout=new VerticalLayout();
        spacingLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(spacingLayout);
        spacingLayout.addStyleName("ui_appSectionLightDiv");

        HorizontalLayout showGraphButtonContainerLayout=new HorizontalLayout();
        addComponent(showGraphButtonContainerLayout);
        setComponentAlignment(showGraphButtonContainerLayout,Alignment.TOP_CENTER);
        setExpandRatio(showGraphButtonContainerLayout,1f);
        Button showGraphButton=new Button("显示数据可视化分析图", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                renderVisualizationGraph();
            }
        });
        showGraphButton.setIcon(FontAwesome.PLAY_CIRCLE);
        showGraphButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showGraphButton.addStyleName(ValoTheme.BUTTON_SMALL);
        showGraphButtonContainerLayout.addComponent(showGraphButton);
    }

    @Override
    public void attach() {
        super.attach();
        renderVisualizationAnalyzeConfigParameters();
    }

    private void renderVisualizationAnalyzeConfigParameters(){
        this.parametersInputContainerLayout.removeAllComponents();
        if(this.visualizationChartType!=null){
            if(VisualizationAnalyzePanel.TimeValuesAnalyzeChart.equals(this.visualizationChartType)){
                this.chartParametersInput=new TimeValuesChartParametersInput(this.currentUserClientInfo,this.measurablePropertiesNameList,this.datePropertiesNameList);
            }
            if(VisualizationAnalyzePanel.GeographicalCoordinatesAnalyzeChart.equals(this.visualizationChartType)){
                this.chartParametersInput=new GeographicalCoordinatesChartParametersInput(this.currentUserClientInfo,this.geographicalCoordinatesPropertiesNameList,this.stringPropertiesNameList);
            }
            if(VisualizationAnalyzePanel.BubbleAnalyzeChart.equals(this.visualizationChartType)){
                this.chartParametersInput=new BubbleChartParametersInput(this.currentUserClientInfo,this.measurablePropertiesNameList);
            }
            if(VisualizationAnalyzePanel.Values3DAnalyzeChart.equals(this.visualizationChartType)){
                this.chartParametersInput=new Values3DChartParametersInput(this.currentUserClientInfo,this.measurablePropertiesNameList);
            }
            if(VisualizationAnalyzePanel.Values3DPlusColorAnalyzeChart.equals(this.visualizationChartType)){
                this.chartParametersInput=new Values3DPlusColorChartParametersInput(this.currentUserClientInfo,this.measurablePropertiesNameList);
            }
            if(VisualizationAnalyzePanel.Scatter2DAnalyzeChart.equals(this.visualizationChartType)){
                this.chartParametersInput=new Scatter2DChartParametersInput(this.currentUserClientInfo,this.measurablePropertiesNameList,this.stringPropertiesNameList);
            }
            if(VisualizationAnalyzePanel.Scatter3DAnalyzeChart.equals(this.visualizationChartType)){
                this.chartParametersInput=new Scatter3DChartParametersInput(this.currentUserClientInfo,this.measurablePropertiesNameList,this.stringPropertiesNameList);
            }
            if(VisualizationAnalyzePanel.Scatter2DWithMathAnalyzeChart.equals(this.visualizationChartType)){
                this.chartParametersInput=new Scatter2DWithMathChartParametersInput(this.currentUserClientInfo,this.measurablePropertiesNameList,this.stringPropertiesNameList);
            }
            if(VisualizationAnalyzePanel.Scatter2DPlusSizeAnalyzeChart.equals(this.visualizationChartType)){
                this.chartParametersInput=new Scatter2DPlusSizeChartParametersInput(this.currentUserClientInfo,this.measurablePropertiesNameList,this.stringPropertiesNameList);
            }
        }
        this.parametersInputContainerLayout.addComponent(this.chartParametersInput);
    }

    private void renderVisualizationGraph(){
        boolean configParamsValidateResult=this.chartParametersInput.validateParametersInput();
        if(configParamsValidateResult){
            String queryString=this.chartParametersInput.getParametersQueryString();
            if(getParentVisualizationAnalyzePanel()!=null){
                getParentVisualizationAnalyzePanel().renderVisualizationAnalyzeGraph(queryString);
            }
        }else{
            Notification errorNotification = new Notification("数据校验错误","请在所有的可视化分析参数属性中输入合法数值", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public VisualizationAnalyzePanel getParentVisualizationAnalyzePanel() {
        return parentVisualizationAnalyzePanel;
    }

    public void setParentVisualizationAnalyzePanel(VisualizationAnalyzePanel parentVisualizationAnalyzePanel) {
        this.parentVisualizationAnalyzePanel = parentVisualizationAnalyzePanel;
    }
}
