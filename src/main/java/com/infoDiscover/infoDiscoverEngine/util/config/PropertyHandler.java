package com.infoDiscover.infoDiscoverEngine.util.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyHandler {
	private static Properties _properties;		
	public static String DISCOVER_ENGINE_SERVICE_LOCATION="DISCOVER_ENGINE_SERVICE_LOCATION";
	public static String DISCOVER_ENGINE_ADMIN_ACCOUNT="DISCOVER_ENGINE_ADMIN_ACCOUNT";
	public static String DISCOVER_ENGINE_ADMIN_PWD="DISCOVER_ENGINE_ADMIN_PWD";
	public static String DISCOVER_SPACE_DATABASE_TYPE="DISCOVER_SPACE_DATABASE_TYPE";
	public static String DISCOVER_SPACE_STORAGE_MODE="DISCOVER_SPACE_STORAGE_MODE";
	public static String DISCOVER_DEFAULT_PREFIX = "DEFAULT_PREFIX";
	public static String META_CONFIG_DISCOVERSPACE = "META_CONFIG_DISCOVERSPACE";

	public static String getPropertyValue(String resourceFileName){
		_properties=new Properties();
		try {
			_properties.load(new FileInputStream(RuntimeEnvironmentHandler.getApplicationRootPath()+"InfoDiscoverEngineCfg.properties"));
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
		}
		return _properties.getProperty(resourceFileName);
	}
	
	public static void main(String[] args){
		System.out.println(getPropertyValue(PropertyHandler.DISCOVER_ENGINE_SERVICE_LOCATION));
	}
}
