package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.factManagement;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.data.PieChartData;
import at.downdrown.vaadinaddons.highchartsapi.model.series.PieChartSeries;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

/**
 * Created by wangychu on 10/24/16.
 */
public class FactTypeDataInstancesInfoChart extends VerticalLayout {private UserClientInfo currentUserClientInfo;
    private ChartConfiguration pieConfiguration;
    private HighChart pieChart;

    public FactTypeDataInstancesInfoChart(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.pieConfiguration = new ChartConfiguration();
        this.pieConfiguration.setTitle("事实类型数据分布");
        this.pieConfiguration.setChartType(ChartType.PIE);
        this.pieConfiguration.setBackgroundColor(Colors.WHITE);
    }

    public void renderFactTypeDataInstancesInfoChart(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.removeAllComponents();
        PieChartSeries pieSpaceDataSeries = new PieChartSeries("事实类型数据分布");
        List<DataTypeStatisticMetrics> factTypeStatisticMetrics=discoverSpaceStatisticMetrics.getFactsStatisticMetrics();
        if(factTypeStatisticMetrics!=null){
            for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:factTypeStatisticMetrics){
                String factTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_FACT,"");
                long factTypeDataCount=currentDataTypeStatisticMetrics.getTypeDataCount();
                PieChartData currentFactTypeData = new PieChartData(factTypeName, factTypeDataCount);
                pieSpaceDataSeries.getData().add(currentFactTypeData);
            }
        }
        this.pieConfiguration.getSeriesList().clear();
        this.pieConfiguration.getSeriesList().add(pieSpaceDataSeries);
        try {
            this.pieChart = HighChartFactory.renderChart(this.pieConfiguration);
            pieChart.setHeight(85, Unit.PERCENTAGE);
            pieChart.setWidth(85, Unit.PERCENTAGE);
            this.addComponent(this.pieChart);
            this.setComponentAlignment(pieChart, Alignment.MIDDLE_LEFT);
        } catch (HighChartsException e) {
            e.printStackTrace();
        }
    }

    public void cleanChartDisplay(){
        this.removeAllComponents();
    }
}