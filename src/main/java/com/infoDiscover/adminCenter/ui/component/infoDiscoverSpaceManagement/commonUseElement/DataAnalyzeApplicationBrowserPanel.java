package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.VerticalLayout;

import java.util.Date;

/**
 * Created by wangychu on 3/2/17.
 */
public class DataAnalyzeApplicationBrowserPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String dataAnalyzeApplicationBaseAddress;
    private String discoverSpaceName;
    private BrowserFrame dataAnalyzeApplicationBrowserFrame;

    public DataAnalyzeApplicationBrowserPanel(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        this.setSizeFull();
        this.dataAnalyzeApplicationBrowserFrame = new BrowserFrame();
        this.dataAnalyzeApplicationBrowserFrame.setSizeFull();
        this.addComponent(this.dataAnalyzeApplicationBrowserFrame);
    }

    @Override
    public void attach() {
        super.attach();
        long timeStampPostValue=new Date().getTime();
        dataAnalyzeApplicationBrowserFrame.setSource(new ExternalResource(
                getDataAnalyzeApplicationBaseAddress()+"?discoverSpace="+discoverSpaceName+"&timestamp="+timeStampPostValue));
    }

    public String getDataAnalyzeApplicationBaseAddress() {
        return dataAnalyzeApplicationBaseAddress;
    }

    public void setDataAnalyzeApplicationBaseAddress(String dataAnalyzeApplicationBaseAddress) {
        this.dataAnalyzeApplicationBaseAddress = dataAnalyzeApplicationBaseAddress;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }
}
