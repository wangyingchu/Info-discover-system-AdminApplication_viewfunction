package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 9/29/16.
 */
public class InfoDiscoverSpaceBrowser extends VerticalLayout{

    private UserClientInfo currentUserClientInfo;
    private MenuBar.MenuItem createInfoDiscoverSpaceMenuItem;

    public InfoDiscoverSpaceBrowser(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        MenuBar operationMenuBar = getOperationMenuBar();
        operationMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        operationMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
        addComponent(operationMenuBar);

        Label spaceListLabel = new Label( FontAwesome.BARS.getHtml()+" 信息发现空间列表:", ContentMode.HTML);
        spaceListLabel.addStyleName(ValoTheme.LABEL_TINY);
        spaceListLabel.addStyleName("ui_appStandaloneElementPadding");
        spaceListLabel.addStyleName("ui_appSectionLightDiv");
        addComponent(spaceListLabel);

        InfoDiscoverSpacesList infoDiscoverSpacesList=new InfoDiscoverSpacesList(this.currentUserClientInfo);
        addComponent(infoDiscoverSpacesList);

        VerticalLayout spacingLayout=new VerticalLayout();
        addComponent(spacingLayout);
    }

    private MenuBar getOperationMenuBar() {
        MenuBar.Command click = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                executeOperationMenuItems(selectedItem.getId());
            }
        };
        MenuBar menubar = new MenuBar();
        menubar.setWidth("100%");
        MenuBar.MenuItem operationsPrompt = menubar.addItem("信息发现空间操作", null);
        operationsPrompt.setIcon(FontAwesome.LIST);
        createInfoDiscoverSpaceMenuItem = operationsPrompt.addItem("创建信息发现空间 ...", click);
        createInfoDiscoverSpaceMenuItem.setIcon(FontAwesome.PLUS_SQUARE);

        return menubar;
    }

    private void executeOperationMenuItems(int itemId){
        if(itemId==createInfoDiscoverSpaceMenuItem.getId()){
            CreateInfoDiscoverSpacePanel createInfoDiscoverSpacePanel=new CreateInfoDiscoverSpacePanel(this.currentUserClientInfo);
            final Window window = new Window();
            window.setWidth(450.0f, Unit.PIXELS);
            window.setHeight(205.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(createInfoDiscoverSpacePanel);
            createInfoDiscoverSpacePanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }
}
