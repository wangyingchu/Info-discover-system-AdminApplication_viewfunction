package com.infoDiscover.adminCenter.ui.util;

import com.github.wolfie.blackboard.Blackboard;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataListVO;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Window;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by wangychu on 9/28/16.
 */
public class UserClientInfo implements Serializable {

    private static final long serialVersionUID = 3930971222892866428L;
    private WebBrowser userWebBrowserInfo;
    private Blackboard eventBlackBoard;
    private Map<String,ProcessingDataListVO> discoverSpacesProcessingDataMap;
    private RuntimeWindowsRepository runtimeWindowsRepository;

    public WebBrowser getUserWebBrowserInfo() {
        return userWebBrowserInfo;
    }

    public void setUserWebBrowserInfo(WebBrowser userWebBrowserInfo) {
        this.userWebBrowserInfo = userWebBrowserInfo;
    }

    public Blackboard getEventBlackBoard() {
        return eventBlackBoard;
    }

    public void setEventBlackBoard(Blackboard eventBlackBoard) {
        this.eventBlackBoard = eventBlackBoard;
    }

    public Map<String, ProcessingDataListVO> getDiscoverSpacesProcessingDataMap() {
        return discoverSpacesProcessingDataMap;
    }

    public void setDiscoverSpacesProcessingDataMap(Map<String, ProcessingDataListVO> discoverSpacesProcessingDataMap) {
        this.discoverSpacesProcessingDataMap = discoverSpacesProcessingDataMap;
    }

    public RuntimeWindowsRepository getRuntimeWindowsRepository() {
        return runtimeWindowsRepository;
    }

    public void setRuntimeWindowsRepository(RuntimeWindowsRepository runtimeWindowsRepository) {
        this.runtimeWindowsRepository = runtimeWindowsRepository;
    }
}
