package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.factManagement;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.data.base.StringIntData;
import at.downdrown.vaadinaddons.highchartsapi.model.series.BarChartSeries;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

/**
 * Created by wangychu on 10/21/16.
 */
public class InfoDiscoverSpaceFactsInfoChart extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private ChartConfiguration barConfiguration;
    private HighChart barChart;

    public InfoDiscoverSpaceFactsInfoChart(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.barConfiguration = new ChartConfiguration();
        this.barConfiguration.setTitle("事实类型数据分布");
        this.barConfiguration.setChartType(ChartType.BAR);
        this.barConfiguration.setBackgroundColor(Colors.WHITE);
    }

    public void renderFactsInfoChart(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.removeAllComponents();
        BarChartSeries barChartSeries = new BarChartSeries("事实类型数据总量");
        List<DataTypeStatisticMetrics> factsStatisticMetricsList=discoverSpaceStatisticMetrics.getFactsStatisticMetrics();
        for(DataTypeStatisticMetrics currentMetrics:factsStatisticMetricsList){
            String factTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION,"");
            barChartSeries.addData(new StringIntData(factTypeName, (int) currentMetrics.getTypeDataCount()));
        }
        this.barConfiguration.getSeriesList().clear();
        this.barConfiguration.getSeriesList().add(barChartSeries);
        try {
            this.barChart = HighChartFactory.renderChart(this.barConfiguration);

            int browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
            int browserWindowWidth= UI.getCurrent().getPage().getBrowserWindowWidth();

            int chartWidth=browserWindowWidth-1200;
            if(chartWidth>600){
                barChart.setWidth(chartWidth, Unit.PIXELS);
            }else{
                barChart.setWidth(600, Unit.PIXELS);
            }

            int chartHeight=browserWindowHeight-500;
            if(chartHeight>500){
                barChart.setHeight(chartHeight, Unit.PIXELS);
            }else{
                barChart.setHeight(500, Unit.PIXELS);
            }

            this.addComponent(this.barChart);
            this.setComponentAlignment(barChart, Alignment.MIDDLE_LEFT);
        } catch (HighChartsException e) {
            e.printStackTrace();
        }
    }
}
