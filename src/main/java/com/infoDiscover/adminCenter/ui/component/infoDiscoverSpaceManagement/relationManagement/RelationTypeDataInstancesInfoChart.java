package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

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
 * Created by wangychu on 10/25/16.
 */
public class RelationTypeDataInstancesInfoChart extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private ChartConfiguration pieConfiguration;
    private HighChart pieChart;

    public RelationTypeDataInstancesInfoChart(UserClientInfo currentUserClientInfo) {
        this.setMargin(new MarginInfo(false,false,false,true));
        this.currentUserClientInfo = currentUserClientInfo;
        this.pieConfiguration = new ChartConfiguration();
        this.pieConfiguration.setChartType(ChartType.PIE);
        this.pieConfiguration.setBackgroundColor(Colors.WHITE);
    }

    public void renderRelationTypeDataInstancesInfoChart(String discoverSpaceName, String relationTypeName, DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.removeAllComponents();
        PieChartSeries pieSpaceDataSeries = new PieChartSeries("关系类型数据分布");
        List<DataTypeStatisticMetrics> relationTypeDataList=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
        long relationTypeTotalDataCount;
        if(relationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            relationTypeTotalDataCount=discoverSpaceStatisticMetrics.getSpaceRelationDataCount();
        }else{
            relationTypeTotalDataCount= getChildRelationTypeDataCount(relationTypeName, relationTypeDataList);
        }
        List<String> childRelationTypesList= InfoDiscoverSpaceOperationUtil.retrieveChildRelationTypesRuntimeInfo(discoverSpaceName, relationTypeName);
        for(String currentRelationType:childRelationTypesList){
            long currentChildRelationTypeDataCount= getChildRelationTypeDataCount(currentRelationType, relationTypeDataList);
            PieChartData currentRelationTypeData = new PieChartData(currentRelationType, currentChildRelationTypeDataCount);
            pieSpaceDataSeries.getData().add(currentRelationTypeData);
            relationTypeTotalDataCount=relationTypeTotalDataCount-currentChildRelationTypeDataCount;
        }
        if(!relationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
            PieChartData parentRelationTypeData = new PieChartData(relationTypeName, relationTypeTotalDataCount);
            pieSpaceDataSeries.getData().add(parentRelationTypeData);
        }
        this.pieConfiguration.getSeriesList().clear();
        this.pieConfiguration.getSeriesList().add(pieSpaceDataSeries);
        try {
            this.pieChart = HighChartFactory.renderChart(this.pieConfiguration);

            int browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
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

    private long getChildRelationTypeDataCount(String relationTypeName, List<DataTypeStatisticMetrics> relationTypeDataList){
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:relationTypeDataList){
            String currentRelationTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            if(relationTypeName.equals(currentRelationTypeName)){
                return currentDataTypeStatisticMetrics.getTypeDataCount();
            }
        }
        return 0;
    }

    public void cleanChartDisplay(){
        this.removeAllComponents();
    }
}
