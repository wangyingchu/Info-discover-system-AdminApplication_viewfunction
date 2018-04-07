package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.Date;

/**
 * Created by wangychu on 4/7/17.
 */

public class ExploreProcessingDataRelatedInfoPanel extends VerticalLayout  {

    private UserClientInfo currentUserClientInfo;
    private ProcessingDataVO processingData;
    private String typeInstanceRelationsDetailGraphQueryAddress;

    public ExploreProcessingDataRelatedInfoPanel(UserClientInfo userClientInfo,ProcessingDataVO processingData){
        this.setMargin(true);
        this.currentUserClientInfo = userClientInfo;
        this.processingData=processingData;
        this.setWidth(100,Unit.PERCENTAGE);
        int browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        int dataRelationGraphBrowserFrameHeight=browserWindowHeight-150;
        this.setHeight(dataRelationGraphBrowserFrameHeight,Unit.PIXELS);
        String discoverSpaceName=this.processingData.getDiscoverSpaceName();
        String dataId=this.processingData.getId();

        String dataInstanceQueryId=dataId.replaceAll("#","%23");
        dataInstanceQueryId=dataInstanceQueryId.replaceAll(":","%3a");
        long timeStampPostValue=new Date().getTime();
        typeInstanceRelationsDetailGraphQueryAddress= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+
                "infoAnalysePages/typeInstanceRelationAnalyse/typeInstanceRelationsExploreGraph.html?dataInstanceId="+dataInstanceQueryId+"&discoverSpace="+discoverSpaceName+"&timestamp="+timeStampPostValue;
        BrowserFrame dataRelationGraphBrowserFrame = new BrowserFrame();
        dataRelationGraphBrowserFrame.setSizeFull();
        dataRelationGraphBrowserFrame.setHeight(dataRelationGraphBrowserFrameHeight,Unit.PIXELS);
        int relationsCycleGraphHeight=dataRelationGraphBrowserFrameHeight-20;
        dataRelationGraphBrowserFrame.setSource(new ExternalResource(
                typeInstanceRelationsDetailGraphQueryAddress + "&graphHeight=" + relationsCycleGraphHeight));
        this.addComponent(dataRelationGraphBrowserFrame);
    }

    public ProcessingDataVO getProcessingData(){
        return this.processingData;
    }
}
