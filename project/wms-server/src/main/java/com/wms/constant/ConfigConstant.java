package com.wms.constant;

import java.io.File;
import java.io.IOException;

public class ConfigConstant {

    private static final String TMP = "tmp";

    private static final String PDF = "pdf";
    public static final String EXCEL_FILE_SUFFIX = ".xls";

    public static final String FILE_URL;

    public static final String SYSTEM_URL;

    static {
        File file = new File("");
        String tmp = "";
        try {
            File newFile = new File(file.getCanonicalPath() + File.separator);
            tmp = newFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SYSTEM_URL = tmp + File.separator;
        FILE_URL = tmp + File.separator + TMP + File.separator;
    }

    public static final String MAPPER_FILE_PATH = ConfigConstant.SYSTEM_URL + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper/**/*.xml";
    public static final String PDF_IMAGE = SYSTEM_URL + File.separator + PDF;
    public static final String CONF_BASE = SYSTEM_URL + "conf" + File.separator;
    public static final String LIB_PATH = SYSTEM_URL + "lib";
    public static final String TEMPLATE_PATH = SYSTEM_URL + File.separator + "template";
    public static final String FILE_IMG = "file" + File.separator + "img" + File.separator;
    public static final String IMG_PATH = SYSTEM_URL + FILE_IMG;

    public static final String CONF_BASE_MODBUS = CONF_BASE + File.separator + "modbus" + File.separator;


}
