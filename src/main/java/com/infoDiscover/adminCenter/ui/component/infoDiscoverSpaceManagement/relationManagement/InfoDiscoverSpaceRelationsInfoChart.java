package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

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
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

/**
 * Created by wangychu on 10/25/16.
 */
public class InfoDiscoverSpaceRelationsInfoChart extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private ChartConfiguration barConfiguration;
    private HighChart barChart;

    public InfoDiscoverSpaceRelationsInfoChart(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.barConfiguration = new ChartConfiguration();
        this.barConfiguration.setTitle("关系类型数据分布");
        this.barConfiguration.setChartType(ChartType.BAR);
        this.barConfiguration.setBackgroundColor(Colors.WHITE);
    }

    public void renderRelationsInfoChart(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.removeAllComponents();
        BarChartSeries barChartSeries = new BarChartSeries("关系类型数据总量");
        List<DataTypeStatisticMetrics> relationsStatisticMetricsList=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
        for(DataTypeStatisticMetrics currentMetrics:relationsStatisticMetricsList){
            String relationTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            barChartSeries.addData(new StringIntData(relationTypeName, (int) currentMetrics.getTypeDataCount()));
        }
        this.barConfiguration.getSeriesList().clear();
        this.barConfiguration.getSeriesList().add(barChartSeries);
        try {
            this.barChart = HighChartFactory.renderChart(this.barConfiguration);
            barChart.setHeight(500, Sizeable.Unit.PIXELS);
            barChart.setWidth(600, Sizeable.Unit.PIXELS);
            this.addComponent(this.barChart);
            this.setComponentAlignment(barChart, Alignment.MIDDLE_LEFT);
        } catch (HighChartsException e) {
            e.printStackTrace();
        }
    }
}
