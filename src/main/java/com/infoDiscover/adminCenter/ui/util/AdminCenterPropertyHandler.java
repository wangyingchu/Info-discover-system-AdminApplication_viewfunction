package com.infoDiscover.adminCenter.ui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.viewfunction.infoDiscoverSystemAdminApplication.util.SpringRuntimeEnvironmentHandler;

/**
 * Created by wangychu on 2/21/17.
 */
public class AdminCenterPropertyHandler {
    private static Properties _properties;
    public static String INFO_ANALYSE_SERVICE_ROOT_LOCATION="INFO_ANALYSE_SERVICE_ROOT_LOCATION";
    public static String META_CONFIG_DISCOVERSPACE="META_CONFIG_DISCOVERSPACE";
    public static String BUILDIN_ADMINISTRATOR_ACCOUNTNAME="BUILDIN_ADMINISTRATOR_ACCOUNTNAME";
    public static String BUILDIN_ADMINISTRATOR_ACCOUNTPWD="BUILDIN_ADMINISTRATOR_ACCOUNTPWD";

    public static String getPropertyValue(String resourceFileName){
        _properties=new Properties();
        try {
            _properties.load(new FileInputStream(SpringRuntimeEnvironmentHandler.getApplicationRootPath()+"/config/AdminCenterCfg.properties"));
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        }
        return _properties.getProperty(resourceFileName);
    }
}
