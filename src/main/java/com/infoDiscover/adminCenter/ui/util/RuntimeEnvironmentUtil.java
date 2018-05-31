package com.infoDiscover.adminCenter.ui.util;

import com.viewfunction.infoDiscoverSystemAdminApplication.util.SpringRuntimeEnvironmentHandler;

public class RuntimeEnvironmentUtil {
    public static final String BINART_TEMP_FILE_DIR = "/BINARY_TEMP_FILES";

    public static String getBinaryTempFileDirLocation() {
        return SpringRuntimeEnvironmentHandler.getApplicationRootPath() +"/" + BINART_TEMP_FILE_DIR + "/";
    }
}
