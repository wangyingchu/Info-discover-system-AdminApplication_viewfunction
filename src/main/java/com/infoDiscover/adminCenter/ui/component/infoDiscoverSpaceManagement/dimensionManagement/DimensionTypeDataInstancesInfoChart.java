package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.dimensionManagement;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.data.PieChartData;
import at.downdrown.vaadinaddons.highchartsapi.model.series.PieChartSeries;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

/**
 * Created by wangychu on 10/17/16.
 */
public class DimensionTypeDataInstancesInfoChart extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private ChartConfiguration pieConfiguration;
    private HighChart pieChart;

    public DimensionTypeDataInstancesInfoChart(UserClientInfo currentUserClientInfo) {
        this.setMargin(new MarginInfo(false,false,false,true));
        this.currentUserClientInfo = currentUserClientInfo;
        this.pieConfiguration = new ChartConfiguration();
        this.pieConfiguration.setChartType(ChartType.PIE);
        this.pieConfiguration.setBackgroundColor(Colors.WHITE);
    }

    public void renderDimensionTypeDataInstancesInfoChart(String discoverSpaceName,String dimensionTypeName,DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.removeAllComponents();
        PieChartSeries pieSpaceDataSeries = new PieChartSeries("维度类型数据分布");
        List<DataTypeStatisticMetrics> dimensionTypeDataList=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
        long dimensionTypeTotalDataCount;
        if(dimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
            dimensionTypeTotalDataCount=discoverSpaceStatisticMetrics.getSpaceDimensionDataCount();
        }else{
            dimensionTypeTotalDataCount=getChildDimensionTypeDataCount(dimensionTypeName,dimensionTypeDataList);
        }
        List<String> childDimensionTypesList=InfoDiscoverSpaceOperationUtil.retrieveChildDimensionTypesRuntimeInfo(discoverSpaceName,dimensionTypeName);
        for(String currentDimensionType:childDimensionTypesList){
            long currentChildDimensionTypeDataCount=getChildDimensionTypeDataCount(currentDimensionType,dimensionTypeDataList);
            PieChartData currentDimensionTypeData = new PieChartData(currentDimensionType, currentChildDimensionTypeDataCount);
            pieSpaceDataSeries.getData().add(currentDimensionTypeData);
            dimensionTypeTotalDataCount=dimensionTypeTotalDataCount-currentChildDimensionTypeDataCount;
        }
        if(!dimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
            PieChartData parentDimensionTypeData = new PieChartData(dimensionTypeName, dimensionTypeTotalDataCount);
            pieSpaceDataSeries.getData().add(parentDimensionTypeData);
        }
        this.pieConfiguration.getSeriesList().clear();
        this.pieConfiguration.getSeriesList().add(pieSpaceDataSeries);
        try {
            this.pieChart = HighChartFactory.renderChart(this.pieConfiguration);

            int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
            int browserWindowWidth= UI.getCurrent().getPage().getBrowserWindowWidth();

            int chartWidth=browserWindowWidth-1200;
            if(chartWidth>500){
                pieChart.setWidth(chartWidth, Unit.PIXELS);
            }else{
                pieChart.setWidth(500, Unit.PIXELS);
            }

            int chartHeight=browserWindowHeight-500;
            if(chartHeight>400){
                pieChart.setHeight(chartHeight, Unit.PIXELS);
            }else{
                pieChart.setHeight(400, Unit.PIXELS);
            }

            this.addComponent(this.pieChart);
            this.setComponentAlignment(pieChart, Alignment.MIDDLE_LEFT);
        } catch (HighChartsException e) {
            e.printStackTrace();
        }
    }

    private long getChildDimensionTypeDataCount(String dimensionTypeName,List<DataTypeStatisticMetrics> dimensionTypeDataList){
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:dimensionTypeDataList){
            String currentDimensionTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION,"");
            if(dimensionTypeName.equals(currentDimensionTypeName)){
                return currentDataTypeStatisticMetrics.getTypeDataCount();
            }
        }
        return 0;
    }

    public void cleanChartDisplay(){
        this.removeAllComponents();
    }
}
