package com.wms.constant;

import com.wms.exception.EException;

import java.io.File;
import java.io.IOException;

public class ConfigConstant {

    private static final String TMP = "tmp";

    private static final String PDF = "pdf";

    public static final String FILE_URL;

    public static final String SYSTEM_URL;

    static {
        File file = new File("");
        String tmp = "";
        try {
            File newFile = new File(file.getCanonicalPath() + File.separator);
            tmp = newFile.getCanonicalPath();
        } catch (IOException e) {
            throw new EException(e.getMessage());
        }
        SYSTEM_URL = tmp + File.separator;
        FILE_URL = tmp + File.separator + TMP + File.separator;
    }

    public static final String CONF_BASE = SYSTEM_URL + "conf" + File.separator;
    public static final String FILE_IMG = "file" + File.separator + "img" + File.separator;

    public static final String CONF_BASE_MODBUS = CONF_BASE + File.separator + "modbus" + File.separator;
    public static final String CONF_BASE_DEFAULT_PERMISSIONS = CONF_BASE + File.separator + "permissions" + File.separator;


}
