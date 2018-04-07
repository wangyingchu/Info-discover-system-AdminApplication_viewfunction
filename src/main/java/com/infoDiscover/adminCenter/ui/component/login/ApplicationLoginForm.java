package com.infoDiscover.adminCenter.ui.component.login;

import com.infoDiscover.adminCenter.logic.common.SystemConfigUtil;
import com.infoDiscover.adminCenter.ui.AdminCenterApplicationUI;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class ApplicationLoginForm extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    public static final ThemeResource Login_picture = new ThemeResource("imgs/loginPic.jpg");
    public static final ThemeResource viewfunctionLogo_picture = new ThemeResource("imgs/productLogo_ID.png");
    private TextField adminUserNameField;
    private PasswordField passwordField;
    private AdminCenterApplicationUI containerApplicationUI;

    public ApplicationLoginForm(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo = currentUserClientInfo;

        int screenHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        int headerSpaceHeight=(int)(screenHeight*0.2);
        VerticalLayout loginFormLayout = new VerticalLayout();
        VerticalLayout heightSpaceDivLayout_0 = new VerticalLayout();
        heightSpaceDivLayout_0.setHeight(headerSpaceHeight,Unit.PIXELS);
        loginFormLayout.addComponent(heightSpaceDivLayout_0);
        addComponent(loginFormLayout);

        HorizontalLayout formLayout=new HorizontalLayout();
        formLayout.setMargin(true);

        //set login picture
        Embedded loginPicEmbedded=new Embedded(null, Login_picture);

        formLayout.addComponent(loginPicEmbedded);
        //set login spacing div
        HorizontalLayout spaceDivLayout=new HorizontalLayout();
        spaceDivLayout.setWidth("20px");
        formLayout.addComponent(spaceDivLayout);

        //set login form input fields
        VerticalLayout loginFormFieldLayout = new VerticalLayout();
        formLayout.addComponent(loginFormFieldLayout);

        VerticalLayout heightSpaceDivLayout_1 = new VerticalLayout();
        heightSpaceDivLayout_1.setHeight(10,Unit.PIXELS);
        loginFormFieldLayout.addComponent(heightSpaceDivLayout_1);

        Embedded viewfunctionLogoEmbedded=new Embedded(null, viewfunctionLogo_picture);
        loginFormFieldLayout.addComponent(viewfunctionLogoEmbedded);

        HorizontalLayout applicationLabelContainerLayout=new HorizontalLayout();
        HorizontalLayout applicationLabelSpacingLayout=new HorizontalLayout();
        applicationLabelSpacingLayout.setWidth(20,Unit.PIXELS);
        applicationLabelContainerLayout.addComponent(applicationLabelSpacingLayout);

        Label applicationLabel=new Label("高价值密度信息发现平台");
        applicationLabel.addStyleName(ValoTheme.LABEL_HUGE);
        applicationLabel.addStyleName(ValoTheme.LABEL_COLORED);
        applicationLabel.addStyleName(ValoTheme.LABEL_BOLD);
        applicationLabelContainerLayout.addComponent(applicationLabel);

        loginFormFieldLayout.addComponent(applicationLabelContainerLayout);

        FormLayout userLoginForm=new FormLayout();
        userLoginForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        adminUserNameField = new TextField("用户名称");
        adminUserNameField.setIcon(FontAwesome.USER);
        adminUserNameField.setWidth("220px");
        userLoginForm.addComponent(adminUserNameField);

        passwordField = new PasswordField("用户密码");
        passwordField.setIcon(FontAwesome.LOCK);
        passwordField.setWidth("220px");
        userLoginForm.addComponent(passwordField);

        HorizontalLayout loginFormBottomLayout=new HorizontalLayout();
        loginFormBottomLayout.setHeight(30,Unit.PIXELS);
        userLoginForm.addComponent(loginFormBottomLayout);

        loginFormFieldLayout.addComponent(userLoginForm);

        Button loginButton=new Button("登录系统");
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setIcon(FontAwesome.SIGN_IN);

        loginButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                doUserLogin();
            }
        });
        HorizontalLayout loginButtonContainerLayout=new HorizontalLayout();

        HorizontalLayout loginButtonSpacingLayout=new HorizontalLayout();
        loginButtonSpacingLayout.setWidth(250,Unit.PIXELS);
        loginButtonContainerLayout.addComponent(loginButtonSpacingLayout);
        loginButtonContainerLayout.addComponent(loginButton);
        loginFormFieldLayout.addComponent(loginButtonContainerLayout);

        Panel loginPanel=new Panel();
        loginPanel.setWidth(940,Unit.PIXELS);
        loginPanel.setContent(formLayout);

        HorizontalLayout loginFormPanelContainerLayout=new HorizontalLayout();
        loginFormPanelContainerLayout.setWidth(100,Unit.PERCENTAGE);
        loginFormPanelContainerLayout.addComponent(loginPanel);
        loginFormPanelContainerLayout.setComponentAlignment(loginPanel,Alignment.MIDDLE_CENTER);

        this.addComponent(loginFormPanelContainerLayout);
    }

    private void doUserLogin(){
        String userName=adminUserNameField.getValue();
        String userPWD=passwordField.getValue();
        if(userName.equals("")||userPWD.equals("")){
            Notification.show("请输入用户名称和用户密码",Notification.Type.TRAY_NOTIFICATION);
            return;
        }else{
            if(this.getContainerApplicationUI()!=null){
                boolean loginUnfoVerifyResult=SystemConfigUtil.verifyUserLoginInfo(userName,userPWD);
                if(loginUnfoVerifyResult){
                    this.getContainerApplicationUI().setLoginUserId(userName);
                    this.getContainerApplicationUI().renderOperationUI(this.currentUserClientInfo);
                }else{
                    Notification errorNotification = new Notification("用户登录失败",
                            "输入的用户名称与密码不匹配", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        }
    }

    public AdminCenterApplicationUI getContainerApplicationUI() {
        return containerApplicationUI;
    }

    public void setContainerApplicationUI(AdminCenterApplicationUI containerApplicationUI) {
        this.containerApplicationUI = containerApplicationUI;
    }
}
