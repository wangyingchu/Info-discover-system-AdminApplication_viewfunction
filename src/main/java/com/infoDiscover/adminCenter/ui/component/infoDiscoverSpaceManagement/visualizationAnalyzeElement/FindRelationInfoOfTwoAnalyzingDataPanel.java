package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationablesPathVO;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 * Created by wangychu on 4/13/17.
 */
public class FindRelationInfoOfTwoAnalyzingDataPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Label analyzingData1IdLabel;
    private Label analyzingData2IdLabel;
    private Label pathDataInfoLabel;
    private List<String> pathDataIdList;
    private BrowserFrame pathsDetailGraphBrowserFrame;
    private RelationablesPathInfoList relationablesPathInfoList;
    private int browserWindowHeight;
    private final static String shortestPathInfoGraphBaseAddress= AdminCenterPropertyHandler.
            getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+"infoAnalysePages/typeInstanceRelationAnalyse/typeInstancesShortestPathExploreGraph.html";
    private final static String specifiedPathInfoGraphBaseAddress= AdminCenterPropertyHandler.
            getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+"infoAnalysePages/typeInstanceRelationAnalyse/typeInstancesSpecifiedPathExploreGraph.html";
    private final static String pathsInfoGraphBaseAddress= AdminCenterPropertyHandler.
            getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+"infoAnalysePages/typeInstanceRelationAnalyse/typeInstancesPathsExploreGraph.html";
    public FindRelationInfoOfTwoAnalyzingDataPanel(UserClientInfo userClientInfo){
        this.setMargin(true);
        this.currentUserClientInfo = userClientInfo;
        this.pathDataIdList = new ArrayList<>();
        browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        HorizontalLayout twoAnalyzingDataInfoContainerLayout=new HorizontalLayout();
        twoAnalyzingDataInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        twoAnalyzingDataInfoContainerLayout.addStyleName("ui_appSectionLightDiv");
        this.addComponent(twoAnalyzingDataInfoContainerLayout);

        HorizontalLayout analyzingDataInfoContainerLayout=new HorizontalLayout();
        Label analyzingData_1Label=new Label(FontAwesome.SQUARE_O.getHtml()+" 数据项 (1) : ", ContentMode.HTML);
        analyzingDataInfoContainerLayout.addComponent(analyzingData_1Label);
        analyzingDataInfoContainerLayout.setComponentAlignment(analyzingData_1Label, Alignment.MIDDLE_LEFT);

        HorizontalLayout spacingDiv01Layout=new HorizontalLayout();
        spacingDiv01Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv01Layout);
        analyzingDataInfoContainerLayout.setComponentAlignment(spacingDiv01Layout, Alignment.MIDDLE_LEFT);

        analyzingData1IdLabel=new Label("-");
        analyzingDataInfoContainerLayout.addComponent(analyzingData1IdLabel);
        analyzingDataInfoContainerLayout.setComponentAlignment(analyzingData1IdLabel, Alignment.MIDDLE_LEFT);

        HorizontalLayout spacingDiv02Layout=new HorizontalLayout();
        spacingDiv02Layout.setWidth(20,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv02Layout);
        analyzingDataInfoContainerLayout.setComponentAlignment(spacingDiv02Layout, Alignment.MIDDLE_LEFT);

        Label analyzingData_2Label=new Label(FontAwesome.SQUARE_O.getHtml()+" 数据项 (2) : ", ContentMode.HTML);
        analyzingDataInfoContainerLayout.addComponent(analyzingData_2Label);
        analyzingDataInfoContainerLayout.setComponentAlignment(analyzingData_2Label, Alignment.MIDDLE_LEFT);

        HorizontalLayout spacingDiv03Layout=new HorizontalLayout();
        spacingDiv03Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv03Layout);
        analyzingDataInfoContainerLayout.setComponentAlignment(spacingDiv03Layout, Alignment.MIDDLE_LEFT);

        analyzingData2IdLabel=new Label("-");
        analyzingDataInfoContainerLayout.addComponent(analyzingData2IdLabel);
        analyzingDataInfoContainerLayout.setComponentAlignment(analyzingData2IdLabel, Alignment.MIDDLE_LEFT);

        HorizontalLayout spacingDiv04Layout=new HorizontalLayout();
        spacingDiv04Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv04Layout);
        analyzingDataInfoContainerLayout.setComponentAlignment(spacingDiv04Layout, Alignment.MIDDLE_LEFT);

        MenuBar.Command pathExploreMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedCommandName=selectedItem.getText();
                if("发现算法默认最短关联路径".equals(selectedCommandName)){
                    showShortestPathRelation();
                }
                if("发现所有关联路径".equals(selectedCommandName)){
                    showAllPathRelations();
                }
                if("发现最短５条关联路径".equals(selectedCommandName)){
                    showShortest5PathRelations();
                }
                if("发现最长５条关联路径".equals(selectedCommandName)){
                    showLongest5PathRelations();
                }
            }
        };

        MenuBar basicRelationExploreMenuBar = new MenuBar();
        basicRelationExploreMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        basicRelationExploreMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
        analyzingDataInfoContainerLayout.addComponent(basicRelationExploreMenuBar);
        MenuBar.MenuItem pathExploreRootItem=basicRelationExploreMenuBar.addItem("基础路径关系发现", VaadinIcons.SEARCH, null);
        pathExploreRootItem.addItem("发现算法默认最短关联路径", FontAwesome.CIRCLE_THIN, pathExploreMenuItemCommand);
        pathExploreRootItem.addItem("发现所有关联路径", FontAwesome.CIRCLE_THIN, pathExploreMenuItemCommand);
        pathExploreRootItem.addItem("发现最短５条关联路径", FontAwesome.CIRCLE_THIN, pathExploreMenuItemCommand);
        pathExploreRootItem.addItem("发现最长５条关联路径", FontAwesome.CIRCLE_THIN, pathExploreMenuItemCommand);
        analyzingDataInfoContainerLayout.setComponentAlignment(basicRelationExploreMenuBar, Alignment.TOP_LEFT);

        Label operationSpaceDivLabel=new Label("<span style='color:#AAAAAA;padding-left:10px;padding-right:5px;'>|</span>",ContentMode.HTML);
        analyzingDataInfoContainerLayout.addComponent(operationSpaceDivLabel);
        analyzingDataInfoContainerLayout.setComponentAlignment(operationSpaceDivLabel, Alignment.MIDDLE_RIGHT);

        Label findPathsCrossNodesLabel=new Label(VaadinIcons.INPUT.getHtml()+" 发现通过指定节点的路径关系",ContentMode.HTML);
        findPathsCrossNodesLabel.addStyleName(ValoTheme.LABEL_SMALL);
        analyzingDataInfoContainerLayout.addComponent(findPathsCrossNodesLabel);
        analyzingDataInfoContainerLayout.setComponentAlignment(findPathsCrossNodesLabel, Alignment.MIDDLE_RIGHT);

        Button addPathedNodesButton=new Button();
        addPathedNodesButton.setIcon(FontAwesome.PLUS_SQUARE);
        addPathedNodesButton.setDescription("添加路径通过的数据节点");
        addPathedNodesButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        analyzingDataInfoContainerLayout.addComponent(addPathedNodesButton);
        addPathedNodesButton.addStyleName(ValoTheme.BUTTON_SMALL);
        analyzingDataInfoContainerLayout.setComponentAlignment(addPathedNodesButton, Alignment.TOP_LEFT);
        FindRelationInfoOfTwoAnalyzingDataPanel self=this;
        addPathedNodesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                AddNewDataForPathsExplorePanel addNewDataForPathsExplorePanel=new AddNewDataForPathsExplorePanel(currentUserClientInfo);
                addNewDataForPathsExplorePanel.setDiscoverSpaceName(discoverSpaceName);
                addNewDataForPathsExplorePanel.setRelatedFindRelationInfoOfTwoAnalyzingDataPanel(self);
                final Window window = new Window();
                window.setWidth(360.0f, Unit.PIXELS);
                window.setHeight(180.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(addNewDataForPathsExplorePanel);
                addNewDataForPathsExplorePanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        Button clearPathedNodesButton=new Button();
        clearPathedNodesButton.setIcon(FontAwesome.ERASER);
        clearPathedNodesButton.setDescription("清除路径通过的数据节点");
        clearPathedNodesButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        analyzingDataInfoContainerLayout.addComponent(clearPathedNodesButton);
        clearPathedNodesButton.addStyleName(ValoTheme.BUTTON_SMALL);
        analyzingDataInfoContainerLayout.setComponentAlignment(clearPathedNodesButton, Alignment.TOP_LEFT);

        clearPathedNodesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                pathDataIdList.clear();
                pathDataInfoLabel.setValue("-");
            }
        });

        Label findPathsCrossNodesLabel2=new Label("&nbsp;&nbsp;:&nbsp;&nbsp;",ContentMode.HTML);
        analyzingDataInfoContainerLayout.addComponent(findPathsCrossNodesLabel2);
        analyzingDataInfoContainerLayout.setComponentAlignment(findPathsCrossNodesLabel2, Alignment.MIDDLE_LEFT);

        Panel pathDataInfoPanel=new Panel();
        pathDataInfoPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        pathDataInfoPanel.setWidth(350,Unit.PIXELS);
        HorizontalLayout pathDataInfoLayout=new HorizontalLayout();
        pathDataInfoLayout.setSpacing(false);
        pathDataInfoLayout.setMargin(false);
        pathDataInfoPanel.setContent(pathDataInfoLayout);

        pathDataInfoLabel=new Label("-");
        pathDataInfoLabel.addStyleName(ValoTheme.LABEL_TINY);
        pathDataInfoLabel.addStyleName("ui_appFriendlyElement");
        pathDataInfoLayout.addComponent(pathDataInfoLabel);

        analyzingDataInfoContainerLayout.addComponent(pathDataInfoPanel);
        analyzingDataInfoContainerLayout.setComponentAlignment(pathDataInfoPanel, Alignment.MIDDLE_LEFT);

        Button showPathsCrossSpecificNodesRelationButton=new Button();
        showPathsCrossSpecificNodesRelationButton.setIcon(VaadinIcons.PLAY_CIRCLE);
        showPathsCrossSpecificNodesRelationButton.setDescription("搜索通过指定节点的路径关系");
        analyzingDataInfoContainerLayout.addComponent(showPathsCrossSpecificNodesRelationButton);
        showPathsCrossSpecificNodesRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showPathsCrossSpecificNodesRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        analyzingDataInfoContainerLayout.setComponentAlignment(showPathsCrossSpecificNodesRelationButton, Alignment.TOP_LEFT);
        showPathsCrossSpecificNodesRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showPathsConnectedWithSpecialData();
            }
        });

        Label operationSpaceDivLabel2=new Label("<span style='color:#AAAAAA;'>|</span>",ContentMode.HTML);
        analyzingDataInfoContainerLayout.addComponent(operationSpaceDivLabel2);
        analyzingDataInfoContainerLayout.setComponentAlignment(operationSpaceDivLabel2, Alignment.MIDDLE_RIGHT);

        twoAnalyzingDataInfoContainerLayout.addComponent(analyzingDataInfoContainerLayout);

        HorizontalLayout pathsDetailInfoContainerLayout=new HorizontalLayout();
        pathsDetailInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        pathsDetailInfoContainerLayout.setHeight(browserWindowHeight-210,Unit.PIXELS);
        this.addComponent(pathsDetailInfoContainerLayout);
        this.addComponent(pathsDetailInfoContainerLayout);

        relationablesPathInfoList=new RelationablesPathInfoList(this.currentUserClientInfo);
        relationablesPathInfoList.setContainerFindRelationInfoOfTwoAnalyzingDataPanel(this);
        pathsDetailInfoContainerLayout.addComponent(relationablesPathInfoList);
        pathsDetailGraphBrowserFrame = new BrowserFrame();
        pathsDetailGraphBrowserFrame.setSizeFull();
        pathsDetailGraphBrowserFrame.setHeight(browserWindowHeight-200,Unit.PIXELS);
        pathsDetailInfoContainerLayout.addComponent(pathsDetailGraphBrowserFrame);
        pathsDetailInfoContainerLayout.setExpandRatio(pathsDetailGraphBrowserFrame,1f);
    }

    public void addFirstAnalyzingData(String processingDataId){
        analyzingData1IdLabel.setValue(processingDataId);
        cleanCurrentResult();
    }

    public void addSecondAnalyzingData(String processingDataId){
        analyzingData2IdLabel.setValue(processingDataId);
        cleanCurrentResult();
    }

    public void addPathExploreDatas(String dataId){
        if(!this.pathDataIdList.contains(dataId)){
            this.pathDataIdList.add(dataId);
        }
        StringBuffer dataIdsListStr=new StringBuffer();
        for(int i=0;i<pathDataIdList.size();i++){
            String currentDataId=pathDataIdList.get(i);
            if(i!=pathDataIdList.size()-1){
                dataIdsListStr.append(currentDataId+",");
            }else{
                dataIdsListStr.append(currentDataId);
            }
        }
        this.pathDataInfoLabel.setValue(dataIdsListStr.toString());
    }

    private void cleanCurrentResult(){
        this.relationablesPathInfoList.renderRelationablesPathsList(null);
        this.pathsDetailGraphBrowserFrame.setSource(null);
        this.pathDataIdList.clear();
        this.pathDataInfoLabel.setValue("-");
    }

    private void showShortestPathRelation(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        RelationablesPathVO shortestPath=InfoDiscoverSpaceOperationUtil.getShortestPathBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
        if(shortestPath!=null){
            List<RelationablesPathVO> pathInfoList=new ArrayList<>();
            pathInfoList.add(shortestPath);
            relationablesPathInfoList.renderRelationablesPathsList(pathInfoList);
            long timeStampPostValue=new Date().getTime();
            String relationableAId=analyzingData1IdLabel.getValue();
            String relationableBId=analyzingData2IdLabel.getValue();
            String relationableAIdCode=relationableAId.replaceAll("#","%23");
            relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
            String relationableBIdCode=relationableBId.replaceAll("#","%23");
            relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
            String graphLocationFullAddress=
                    this.shortestPathInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                            "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+
                            "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
        }else{
            this.pathsDetailGraphBrowserFrame.setSource(null);
            Notification resultNotification = new Notification("未发现关联路径",
                    "在数据项　"+analyzingData1IdLabel.getValue()+" 与　"+analyzingData2IdLabel.getValue()+"之间未发现关联路径", Notification.Type.WARNING_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    private void showAllPathRelations(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean hasShortestPath=InfoDiscoverSpaceOperationUtil.hasShortestPathBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
        if(hasShortestPath){
            List<RelationablesPathVO> pathInfoList= InfoDiscoverSpaceOperationUtil.getAllPathsBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
            relationablesPathInfoList.renderRelationablesPathsList(pathInfoList);
            this.pathsDetailGraphBrowserFrame.setSource(null);
            /*
            long timeStampPostValue=new Date().getTime();
            String relationableAId=analyzingData1IdLabel.getValue();
            String relationableBId=analyzingData2IdLabel.getValue();
            String relationableAIdCode=relationableAId.replaceAll("#","%23");
            relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
            String relationableBIdCode=relationableBId.replaceAll("#","%23");
            relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
            String graphLocationFullAddress=
                    this.allPathsInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                            "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+
                            "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
            */
        }else{
            this.pathsDetailGraphBrowserFrame.setSource(null);
            Notification resultNotification = new Notification("未发现关联路径",
                    "在数据项　"+analyzingData1IdLabel.getValue()+" 与　"+analyzingData2IdLabel.getValue()+"之间未发现关联路径", Notification.Type.WARNING_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    private void showShortest5PathRelations(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean hasShortestPath=InfoDiscoverSpaceOperationUtil.hasShortestPathBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
        if(hasShortestPath){
            long timeStampPostValue=new Date().getTime();
            String relationableAId=analyzingData1IdLabel.getValue();
            String relationableBId=analyzingData2IdLabel.getValue();
            String relationableAIdCode=relationableAId.replaceAll("#","%23");
            relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
            String relationableBIdCode=relationableBId.replaceAll("#","%23");
            relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
            String graphLocationFullAddress=
                    this.pathsInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                            "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+"&pathNumber=5&pathType=SHORTEST"+
                            "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
            List<RelationablesPathVO> pathInfoList= InfoDiscoverSpaceOperationUtil.getShortestPathsBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue(),5);
            relationablesPathInfoList.renderRelationablesPathsList(pathInfoList);
        }else{
            this.pathsDetailGraphBrowserFrame.setSource(null);
            Notification resultNotification = new Notification("未发现关联路径",
                    "在数据项　"+analyzingData1IdLabel.getValue()+" 与　"+analyzingData2IdLabel.getValue()+"之间未发现关联路径", Notification.Type.WARNING_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    private void showLongest5PathRelations(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean hasShortestPath=InfoDiscoverSpaceOperationUtil.hasShortestPathBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
        if(hasShortestPath){
            long timeStampPostValue=new Date().getTime();
            String relationableAId=analyzingData1IdLabel.getValue();
            String relationableBId=analyzingData2IdLabel.getValue();
            String relationableAIdCode=relationableAId.replaceAll("#","%23");
            relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
            String relationableBIdCode=relationableBId.replaceAll("#","%23");
            relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
            String graphLocationFullAddress=
                    this.pathsInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                            "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+"&pathNumber=5&pathType=LONGEST"+
                            "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
            List<RelationablesPathVO> pathInfoList= InfoDiscoverSpaceOperationUtil.getLongestPathsBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue(),5);
            relationablesPathInfoList.renderRelationablesPathsList(pathInfoList);
        }else{
            this.pathsDetailGraphBrowserFrame.setSource(null);
            Notification resultNotification = new Notification("未发现关联路径",
                    "在数据项　"+analyzingData1IdLabel.getValue()+" 与　"+analyzingData2IdLabel.getValue()+"之间未发现关联路径", Notification.Type.WARNING_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    private void showPathsConnectedWithSpecialData(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean hasShortestPath=InfoDiscoverSpaceOperationUtil.hasShortestPathBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
        if(hasShortestPath){
            long timeStampPostValue=new Date().getTime();
            String relationableAId=analyzingData1IdLabel.getValue();
            String relationableBId=analyzingData2IdLabel.getValue();
            String relationableAIdCode=relationableAId.replaceAll("#","%23");
            relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
            String relationableBIdCode=relationableBId.replaceAll("#","%23");
            relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");

            String graphLocationFullAddress=null;
            if(pathDataIdList.size()==0){
                graphLocationFullAddress=
                        this.pathsInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                                "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+"&pathNumber=5&pathType=ALL"+
                                "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            }else{
                StringBuffer pathNodeIdsListStr=new StringBuffer();
                for(int i=0;i<pathDataIdList.size();i++){
                    String currentDataId=pathDataIdList.get(i);
                    String enCodedID=currentDataId.replaceFirst("#","%23").replaceFirst(":","%3a");
                    if(i!=pathDataIdList.size()-1){
                        pathNodeIdsListStr.append(enCodedID+",");
                    }else{
                        pathNodeIdsListStr.append(enCodedID);
                    }
                }
                graphLocationFullAddress=
                        this.pathsInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                                "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+"&pathType=PATHDATA"+
                                "&pathDataIds="+pathNodeIdsListStr.toString()+
                                "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            }
            this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
            List<RelationablesPathVO> pathInfoList= InfoDiscoverSpaceOperationUtil.getPathsContainPointedDatasBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue(),pathDataIdList);
            relationablesPathInfoList.renderRelationablesPathsList(pathInfoList);
        }else{
            this.pathsDetailGraphBrowserFrame.setSource(null);
            Notification resultNotification = new Notification("未发现关联路径",
                    "在数据项　"+analyzingData1IdLabel.getValue()+" 与　"+analyzingData2IdLabel.getValue()+"之间未发现关联路径", Notification.Type.WARNING_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    public void showRelationablesPathGraph(RelationablesPathVO pathInfo){
        Stack<RelationValueVO> relationsStack=pathInfo.getPathRelationRoute();
        StringBuffer relationsListStr=new StringBuffer();
        for(int i=0;i<relationsStack.size();i++){
            String currentDataId=relationsStack.elementAt(i).getId();
            String enCodedID=currentDataId.replaceFirst("#","%23").replaceFirst(":","%3a");
            if(i!=relationsStack.size()-1){
                relationsListStr.append(enCodedID+",");
            }else{
                relationsListStr.append(enCodedID);
            }
        }
        this.pathsDetailGraphBrowserFrame.setSource(null);
        long timeStampPostValue=new Date().getTime();
        String relationableAId=analyzingData1IdLabel.getValue();
        String relationableBId=analyzingData2IdLabel.getValue();
        String relationableAIdCode=relationableAId.replaceAll("#","%23");
        relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
        String relationableBIdCode=relationableBId.replaceAll("#","%23");
        relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
        String graphLocationFullAddress=
                this.specifiedPathInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                        "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+
                        "&pathRelationIds="+relationsListStr+

                        "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
        this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }
}
