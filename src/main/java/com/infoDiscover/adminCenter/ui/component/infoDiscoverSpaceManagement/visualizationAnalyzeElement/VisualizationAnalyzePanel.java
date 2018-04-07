package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;

import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by wangychu on 3/19/17.
 */
public class VisualizationAnalyzePanel extends VerticalLayout {

    public static final String TimeValuesAnalyzeChart="时间序列数据分析";
    public static final String GeographicalCoordinatesAnalyzeChart="数据地理坐标分布分析";
    public static final String BubbleAnalyzeChart="2维平面数据气泡图分析";
    public static final String Values3DAnalyzeChart="3维空间数据分布分析";
    public static final String Values3DPlusColorAnalyzeChart="3维空间数据分布＋第４维数据密度分析";
    public static final String ScatterAnalyzeChart="散点图数据分析...";
    public static final String Scatter2DAnalyzeChart="2维平面数据属性值散点分布图分析";
    public static final String Scatter3DAnalyzeChart="3维空间数据属性值散点分布图分析";
    public static final String Scatter2DWithMathAnalyzeChart="2维平面数据属性值散点分布＋基础数理统计分析";
    public static final String Scatter2DPlusSizeAnalyzeChart="2维平面数据属性值散点分布＋第３维数据密度分析";

    private UserClientInfo currentUserClientInfo;
    private String querySQL;
    private String visualizationChartType;
    private SectionActionsBar dataTypeNoticeActionsBar;
    private Button querySQLButton;
    private AnalyzeParametersInputPanel analyzeParametersInputPanel;
    private MeasurableTypeVO measurableTypeVO;
    private String visualizationAnalyzeGraphRootQueryAddress;
    private String measurableTypeString;
    private int visualizationGraphHeight;
    private BrowserFrame visualizationGraphBrowserFrame;

    public VisualizationAnalyzePanel(UserClientInfo userClientInfo,String visualizationChartType,MeasurableTypeVO measurableTypeVO) {
        this.currentUserClientInfo = userClientInfo;
        this.visualizationChartType=visualizationChartType;
        this.measurableTypeVO=measurableTypeVO;
        this.setWidth(100,Unit.PERCENTAGE);
        this.setMargin(new MarginInfo(true,true,true,true));
        //this.setSpacing(true);

        this.dataTypeNoticeActionsBar = new SectionActionsBar(new Label("---", ContentMode.HTML));
        addComponent(this.dataTypeNoticeActionsBar);

        this.querySQLButton=new Button("查询条件 SQL");
        this.querySQLButton.setIcon(FontAwesome.FILE_TEXT_O);
        this.querySQLButton.addStyleName(ValoTheme.BUTTON_SMALL);
        this.querySQLButton.addStyleName(ValoTheme.BUTTON_LINK);
        this.querySQLButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showQuerySQL();
            }
        });

        List<PropertyTypeVO> measurablePropertiesList=null;

        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(measurableTypeVO.getMeasurableTypeKind())){
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+measurableTypeVO.getDiscoverSpaceName()+" /"+FontAwesome.TAGS.getHtml()+" "+measurableTypeVO.getMeasurableTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            this.dataTypeNoticeActionsBar.addActionComponent(this.querySQLButton);
            measurablePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveDimensionTypePropertiesInfo(measurableTypeVO.getDiscoverSpaceName(),measurableTypeVO.getMeasurableTypeName());
            measurableTypeString="DIMENSION";
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(measurableTypeVO.getMeasurableTypeKind())){
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+measurableTypeVO.getDiscoverSpaceName()+" /"+FontAwesome.CLONE.getHtml()+" "+measurableTypeVO.getMeasurableTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            this.dataTypeNoticeActionsBar.addActionComponent(this.querySQLButton);
            measurablePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveFactTypePropertiesInfo(measurableTypeVO.getDiscoverSpaceName(),measurableTypeVO.getMeasurableTypeName());
            measurableTypeString="FACT";
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(measurableTypeVO.getMeasurableTypeKind())){
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+measurableTypeVO.getDiscoverSpaceName()+" /"+FontAwesome.SHARE_ALT.getHtml()+" "+measurableTypeVO.getMeasurableTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            this.dataTypeNoticeActionsBar.addActionComponent(this.querySQLButton);
            measurablePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveRelationTypePropertiesInfo(measurableTypeVO.getDiscoverSpaceName(),measurableTypeVO.getMeasurableTypeName());
            measurableTypeString="RELATION";
        }

        List<String> measurablePropertiesNameList=new ArrayList<>();
        List<String> datePropertiesNameList=new ArrayList<>();
        List<String> geographicalCoordinatesPropertiesNameList=new ArrayList<>();
        List<String> stringPropertiesNameList=new ArrayList<>();

        if(measurablePropertiesList!=null){
            for(PropertyTypeVO currentPropertyTypeVO:measurablePropertiesList){
                if((""+PropertyType.INT).equals(currentPropertyTypeVO.getPropertyType())||
                        (""+PropertyType.SHORT).equals(currentPropertyTypeVO.getPropertyType())||
                        (""+PropertyType.LONG).equals(currentPropertyTypeVO.getPropertyType())||
                        (""+PropertyType.FLOAT).equals(currentPropertyTypeVO.getPropertyType())||
                        (""+PropertyType.DOUBLE).equals(currentPropertyTypeVO.getPropertyType())
                        ){
                    measurablePropertiesNameList.add(currentPropertyTypeVO.getPropertyName());
                }
                if((""+PropertyType.DATE).equals(currentPropertyTypeVO.getPropertyType())){
                    datePropertiesNameList.add(currentPropertyTypeVO.getPropertyName());
                }
                if((""+PropertyType.FLOAT).equals(currentPropertyTypeVO.getPropertyType())||
                        (""+PropertyType.DOUBLE).equals(currentPropertyTypeVO.getPropertyType())){
                    geographicalCoordinatesPropertiesNameList.add(currentPropertyTypeVO.getPropertyName());
                }
                if((""+PropertyType.STRING).equals(currentPropertyTypeVO.getPropertyType())){
                    stringPropertiesNameList.add(currentPropertyTypeVO.getPropertyName());
                }
            }
        }

        HorizontalSplitPanel visualizationAnalyzeSplitPanel = new HorizontalSplitPanel();
        visualizationAnalyzeSplitPanel.setSizeFull();
        visualizationAnalyzeSplitPanel.setSplitPosition(300, Unit.PIXELS);
        addComponent(visualizationAnalyzeSplitPanel);
        //left side
        Map<String,List<String>> dataTypePropertiesNameListMap=new HashMap<>();
        dataTypePropertiesNameListMap.put(AnalyzeParametersInputPanel.NumberTypeProperties,measurablePropertiesNameList);
        dataTypePropertiesNameListMap.put(AnalyzeParametersInputPanel.DateTypeProperties,datePropertiesNameList);
        dataTypePropertiesNameListMap.put(AnalyzeParametersInputPanel.FloatTypeProperties,geographicalCoordinatesPropertiesNameList);
        dataTypePropertiesNameListMap.put(AnalyzeParametersInputPanel.StringTypeProperties,stringPropertiesNameList);

        this.analyzeParametersInputPanel=new AnalyzeParametersInputPanel(this.currentUserClientInfo,this.visualizationChartType,dataTypePropertiesNameListMap);
        this.analyzeParametersInputPanel.setParentVisualizationAnalyzePanel(this);
        visualizationAnalyzeSplitPanel.setFirstComponent(this.analyzeParametersInputPanel);

        //right side
        final String browserFrameCaption="VisualizationAnalyzeGraph";
        this.visualizationGraphBrowserFrame = new BrowserFrame(browserFrameCaption);
        this.visualizationGraphBrowserFrame.setSizeFull();
        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        this.visualizationGraphBrowserFrame.setHeight(browserWindowHeight-110,Unit.PIXELS);
        visualizationAnalyzeSplitPanel.setSecondComponent(this.visualizationGraphBrowserFrame);

        visualizationAnalyzeGraphRootQueryAddress= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION);
        visualizationGraphHeight=browserWindowHeight-130;
    }

    @Override
    public void attach() {
        super.attach();
    }

    private void showQuerySQL(){
        final Window window = new Window();
        window.setCaption(" 查询条件 SQL");
        window.setIcon(FontAwesome.FILE_TEXT_O);
        VerticalLayout containerLayout=new VerticalLayout();
        containerLayout.setSpacing(true);
        containerLayout.setMargin(true);
        Panel sqlTextPanel=new Panel();
        sqlTextPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        sqlTextPanel.setWidth(380,Unit.PIXELS);
        sqlTextPanel.setHeight(200,Unit.PIXELS);
        Label querySqlLabel=new Label();
        querySqlLabel.addStyleName(ValoTheme.LABEL_COLORED);
        querySqlLabel.setValue(getQuerySQL());
        sqlTextPanel.setContent(querySqlLabel);
        containerLayout.addComponent(sqlTextPanel);
        window.setWidth(400.0f, Unit.PIXELS);
        window.setResizable(false);
        window.setModal(true);
        window.setContent(containerLayout);
        UI.getCurrent().addWindow(window);
    }

    public String getQuerySQL() {
        return querySQL;
    }

    public void setQuerySQL(String querySQL) {
        this.querySQL = querySQL;
    }

    public void renderVisualizationAnalyzeGraph(String parametersQueryString){
        String visualizationAnalyzeGraphURL=null;
        String querySQLBase64=null;
        try {
            querySQLBase64 = Base64.getEncoder().encodeToString(getQuerySQL().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(TimeValuesAnalyzeChart.equals(this.visualizationChartType)){
            visualizationAnalyzeGraphURL=this.visualizationAnalyzeGraphRootQueryAddress+
                    "infoAnalysePages/typeDataStatisticsAnalyse/measurableTypeDataStatistics_vis_TimeValuesChart.html?" +
                    "discoverSpace="+measurableTypeVO.getDiscoverSpaceName()+
                    "&measurableName="+measurableTypeVO.getMeasurableTypeName()+
                    "&measurableType="+measurableTypeString+
                    "&graphHeight="+visualizationGraphHeight+parametersQueryString;
        }
        if(GeographicalCoordinatesAnalyzeChart.equals(this.visualizationChartType)){
            visualizationAnalyzeGraphURL=this.visualizationAnalyzeGraphRootQueryAddress+
                    "infoAnalysePages/typeDataStatisticsAnalyse/measurableTypeDataStatistics_leaflet_MapChart.html?" +
                    "discoverSpace="+measurableTypeVO.getDiscoverSpaceName()+
                    "&measurableName="+measurableTypeVO.getMeasurableTypeName()+
                    "&measurableType="+measurableTypeString+
                    "&graphHeight="+visualizationGraphHeight+parametersQueryString;
        }
        if(BubbleAnalyzeChart.equals(this.visualizationChartType)){
            visualizationAnalyzeGraphURL=this.visualizationAnalyzeGraphRootQueryAddress+
                    "infoAnalysePages/typeDataStatisticsAnalyse/measurableTypeDataStatistics_highcharts_BubbleChart.html?" +
                    "discoverSpace="+measurableTypeVO.getDiscoverSpaceName()+
                    "&measurableName="+measurableTypeVO.getMeasurableTypeName()+
                    "&measurableType="+measurableTypeString+
                    "&graphHeight="+visualizationGraphHeight+parametersQueryString;
        }if(Values3DAnalyzeChart.equals(this.visualizationChartType)){
            visualizationAnalyzeGraphURL=this.visualizationAnalyzeGraphRootQueryAddress+
                    "infoAnalysePages/typeDataStatisticsAnalyse/measurableTypeDataStatistics_plotly_Values3dChart.html?" +
                    "discoverSpace="+measurableTypeVO.getDiscoverSpaceName()+
                    "&measurableName="+measurableTypeVO.getMeasurableTypeName()+
                    "&measurableType="+measurableTypeString+
                    "&graphHeight="+visualizationGraphHeight+parametersQueryString;
        }if(Values3DPlusColorAnalyzeChart.equals(this.visualizationChartType)){
            visualizationAnalyzeGraphURL=this.visualizationAnalyzeGraphRootQueryAddress+
                    "infoAnalysePages/typeDataStatisticsAnalyse/measurableTypeDataStatistics_vis_Values3dChart.html?" +
                    "discoverSpace="+measurableTypeVO.getDiscoverSpaceName()+
                    "&measurableName="+measurableTypeVO.getMeasurableTypeName()+
                    "&measurableType="+measurableTypeString+
                    "&graphHeight="+visualizationGraphHeight+parametersQueryString;
        }
        if(Scatter2DAnalyzeChart.equals(this.visualizationChartType)){
            visualizationAnalyzeGraphURL=this.visualizationAnalyzeGraphRootQueryAddress+
                    "infoAnalysePages/typeDataStatisticsAnalyse/measurableTypeDataStatistics_highcharts_ScatterChart.html?" +
                    "discoverSpace="+measurableTypeVO.getDiscoverSpaceName()+
                    "&measurableName="+measurableTypeVO.getMeasurableTypeName()+
                    "&measurableType="+measurableTypeString+
                    "&graphHeight="+visualizationGraphHeight+parametersQueryString;
        }
        if(Scatter2DWithMathAnalyzeChart.equals(this.visualizationChartType)){
            visualizationAnalyzeGraphURL=this.visualizationAnalyzeGraphRootQueryAddress+
                    "infoAnalysePages/typeDataStatisticsAnalyse/measurableTypeDataStatistics_echarts_ScatterChart.html?" +
                    "discoverSpace="+measurableTypeVO.getDiscoverSpaceName()+
                    "&measurableName="+measurableTypeVO.getMeasurableTypeName()+
                    "&measurableType="+measurableTypeString+
                    "&graphHeight="+visualizationGraphHeight+parametersQueryString;
        }
        if(Scatter3DAnalyzeChart.equals(this.visualizationChartType)){
            visualizationAnalyzeGraphURL=this.visualizationAnalyzeGraphRootQueryAddress+
                    "infoAnalysePages/typeDataStatisticsAnalyse/measurableTypeDataStatistics_plotly_Scatter3dChart.html?" +
                    "discoverSpace="+measurableTypeVO.getDiscoverSpaceName()+
                    "&measurableName="+measurableTypeVO.getMeasurableTypeName()+
                    "&measurableType="+measurableTypeString+
                    "&graphHeight="+visualizationGraphHeight+parametersQueryString;
        }
        if(Scatter2DPlusSizeAnalyzeChart.equals(this.visualizationChartType)){
            visualizationAnalyzeGraphURL=this.visualizationAnalyzeGraphRootQueryAddress+
                    "infoAnalysePages/typeDataStatisticsAnalyse/measurableTypeDataStatistics_plotly_Scatter2dChart.html?" +
                    "discoverSpace="+measurableTypeVO.getDiscoverSpaceName()+
                    "&measurableName="+measurableTypeVO.getMeasurableTypeName()+
                    "&measurableType="+measurableTypeString+
                    "&graphHeight="+visualizationGraphHeight+parametersQueryString;
        }
        visualizationAnalyzeGraphURL=visualizationAnalyzeGraphURL+"&timeStamp="+new Date().getTime();
        if(querySQLBase64!=null){
            visualizationAnalyzeGraphURL=visualizationAnalyzeGraphURL+"&querySQL="+querySQLBase64;
        }
        this.visualizationGraphBrowserFrame.setSource(new ExternalResource(visualizationAnalyzeGraphURL));
    }
}
