package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement;

import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import org.apache.cxf.jaxrs.client.WebClient;

public class InfoDiscoverSpaceRESTFulUtil {

    private static String dataAnalyzeApplicationBaseAddress= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+"ws/";
    private static WebClient webClient;

    public static void refreshDataAnalyzeApplicationDiscoverSpacesCacheInfo(){
        if(webClient==null){
            webClient = WebClient.create(dataAnalyzeApplicationBaseAddress);
            webClient.path("systemManagementService/refreshDiscoverSpacesCacheInfo/");
            webClient.type("application/xml").accept("application/xml");
        }

        //webClient.get();
    }
}
