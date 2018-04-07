package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 5/4/17.
 */
public class BusinessSolutionBrowser extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private MenuBar.MenuItem createBusinessSolutionMenuItem;
    private MenuBar.MenuItem importBusinessSolutionMenuItem;

    public BusinessSolutionBrowser(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        MenuBar operationMenuBar = getOperationMenuBar();
        operationMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        operationMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
        addComponent(operationMenuBar);

        Label spaceListLabel = new Label( FontAwesome.BARS.getHtml()+" 业务解决方案模板列表:", ContentMode.HTML);
        spaceListLabel.addStyleName(ValoTheme.LABEL_TINY);
        spaceListLabel.addStyleName("ui_appStandaloneElementPadding");
        spaceListLabel.addStyleName("ui_appSectionLightDiv");
        addComponent(spaceListLabel);

        BusinessSolutionsList businessSolutionsList=new BusinessSolutionsList(this.currentUserClientInfo);
        addComponent(businessSolutionsList);

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
        MenuBar.MenuItem operationsPrompt = menubar.addItem("业务解决方案操作", null);
        operationsPrompt.setIcon(FontAwesome.LIST);
        createBusinessSolutionMenuItem = operationsPrompt.addItem("创建业务解决方案模板 ...", click);
        createBusinessSolutionMenuItem.setIcon(FontAwesome.PLUS_SQUARE);
        importBusinessSolutionMenuItem = operationsPrompt.addItem("导入业务解决方案模板 ...", click);
        importBusinessSolutionMenuItem.setIcon(VaadinIcons.ADD_DOCK);
        return menubar;
    }

    private void executeOperationMenuItems(int itemId){
        if(itemId==createBusinessSolutionMenuItem.getId()){
            CreateBusinessSolutionPanel createBusinessSolutionPanel=new CreateBusinessSolutionPanel(this.currentUserClientInfo);
            final Window window = new Window();
            window.setWidth(450.0f, Unit.PIXELS);
            window.setHeight(205.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(createBusinessSolutionPanel);
            createBusinessSolutionPanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
        if(itemId==importBusinessSolutionMenuItem.getId()){
            ImportBusinessSolutionPanel importBusinessSolutionPanel=new ImportBusinessSolutionPanel(this.currentUserClientInfo);
            final Window window = new Window();
            window.setWidth(450.0f, Unit.PIXELS);
            window.setHeight(240.0f, Unit.PIXELS);
            window.setResizable(false);
            window.center();
            window.setModal(true);
            window.setContent(importBusinessSolutionPanel);
            importBusinessSolutionPanel.setContainerDialog(window);
            UI.getCurrent().addWindow(window);
        }
    }
}
