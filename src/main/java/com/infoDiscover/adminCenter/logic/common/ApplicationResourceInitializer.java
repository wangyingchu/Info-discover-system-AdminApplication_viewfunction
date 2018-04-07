package com.infoDiscover.adminCenter.logic.common;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by wangychu on 4/27/17.
 */
public class ApplicationResourceInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        InfoDiscoverSpaceOperationUtil.initMetaConfigDiscoverSpace();
        InfoDiscoverSpaceOperationUtil.refreshItemAliasNameCache();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        InfoDiscoverSpaceOperationUtil.clearItemAliasNameCache();
    }
}
