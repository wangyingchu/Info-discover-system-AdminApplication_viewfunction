package com.infoDiscover.adminCenter.ui.component;

import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.BusinessSolutionsManagementPanel;
import com.infoDiscover.adminCenter.ui.component.dataCollectionBusManagement.DataCollectionBusManagementPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceManagementPanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;

/**
 * Created by wangychu on 9/28/16.
 */
public class ApplicationContent extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    public ApplicationContent(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setWidth("100%");
        setHeight("100%");
        setSizeFull();

        TabSheet applicationContentTabSheet = new TabSheet();
        applicationContentTabSheet.addStyleName("framed padded-tabbar");

        InfoDiscoverSpaceManagementPanel infoDiscoverSpaceManagementPanel=new InfoDiscoverSpaceManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab infoDiscoverSpaceManagementTab = applicationContentTabSheet.addTab(infoDiscoverSpaceManagementPanel, "信息发现空间管理");
        infoDiscoverSpaceManagementTab.setIcon(FontAwesome.CUBES);
        infoDiscoverSpaceManagementTab.setClosable(false);
        infoDiscoverSpaceManagementTab.setEnabled(true);

        BusinessSolutionsManagementPanel businessSolutionsManagementPanel=new BusinessSolutionsManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab businessSolutionsManagementTab = applicationContentTabSheet.addTab(businessSolutionsManagementPanel, "业务解决方案管理");
        businessSolutionsManagementTab.setIcon(VaadinIcons.RECORDS);
        businessSolutionsManagementTab.setClosable(false);
        businessSolutionsManagementTab.setEnabled(true);

        DataCollectionBusManagementPanel dataCollectionBusManagementPanel=new DataCollectionBusManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab dataCollectionBusManagementTab = applicationContentTabSheet.addTab(dataCollectionBusManagementPanel, "数据采集总线管理");
        dataCollectionBusManagementTab.setIcon(FontAwesome.SITEMAP);
        dataCollectionBusManagementTab.setClosable(false);
        dataCollectionBusManagementTab.setEnabled(true);

        applicationContentTabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                 /*
                 System.out.println(event);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
            }
        });
        this.addComponent(applicationContentTabSheet);
    }
}
