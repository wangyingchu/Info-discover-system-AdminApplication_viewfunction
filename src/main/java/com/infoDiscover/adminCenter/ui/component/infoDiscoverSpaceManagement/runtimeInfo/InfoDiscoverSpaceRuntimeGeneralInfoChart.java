package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.runtimeInfo;
import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.data.PieChartData;
import at.downdrown.vaadinaddons.highchartsapi.model.series.PieChartSeries;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 10/3/16.
 */
public class InfoDiscoverSpaceRuntimeGeneralInfoChart extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private ChartConfiguration pieConfiguration;
    private HighChart pieChart;

    public InfoDiscoverSpaceRuntimeGeneralInfoChart(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.pieConfiguration = new ChartConfiguration();
        this.pieConfiguration.setTitle("信息发现空间数据分布");
        this.pieConfiguration.setChartType(ChartType.PIE);
        this.pieConfiguration.setBackgroundColor(Colors.WHITE);
    }

    public void renderRuntimeGeneralInfoChart(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.removeAllComponents();
        long totalDataCount=discoverSpaceStatisticMetrics.getSpaceFullDataCount();
        long factDataCount=discoverSpaceStatisticMetrics.getSpaceFactDataCount();
        long dimensionDataCount=discoverSpaceStatisticMetrics.getSpaceDimensionDataCount();
        long relationDataCount=discoverSpaceStatisticMetrics.getSpaceRelationDataCount();
        long otherDataCount=totalDataCount-factDataCount-dimensionDataCount-relationDataCount;
        PieChartSeries pieSpaceDataSeries = new PieChartSeries("信息发现空间数据");
        PieChartData factData = new PieChartData("事实数据", factDataCount);
        PieChartData dimensionData = new PieChartData("维度数据",dimensionDataCount);
        PieChartData relationData = new PieChartData("关系数据",relationDataCount);
        PieChartData otherData = new PieChartData("其他数据",otherDataCount);
        pieSpaceDataSeries.getData().add(factData);
        pieSpaceDataSeries.getData().add(dimensionData);
        pieSpaceDataSeries.getData().add(relationData);
        pieSpaceDataSeries.getData().add(otherData);

        this.pieConfiguration.getSeriesList().clear();
        this.pieConfiguration.getSeriesList().add(pieSpaceDataSeries);
        try {
            this.pieChart = HighChartFactory.renderChart(this.pieConfiguration);
            pieChart.setHeight(380, Unit.PIXELS);
            pieChart.setWidth(380, Unit.PIXELS);
            this.addComponent(this.pieChart);
            this.setComponentAlignment(pieChart, Alignment.MIDDLE_LEFT);
        } catch (HighChartsException e) {
            e.printStackTrace();
        }
    }
}
