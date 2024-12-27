package com.wms.dto;

import com.wms.constant.ConfigConstant;
import com.wms.exception.EException;
import com.wms.utils.FastJsonUtils;
import com.wms.utils.ObjectUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultPermissionsDTO {

    public static List<String> getDefaultPermissions() {
        List<String> defaultPermissions = new ArrayList<>();
        try {
            String s = ObjectUtil.readString(ConfigConstant.CONF_BASE_DEFAULT_PERMISSIONS + File.separator + "default_permissions.json");
            getList(defaultPermissions, s);
        } catch (IOException e) {
            defaultPermissions = createDefaultPermissions();
        }
        return defaultPermissions;
    }

    public static List<String> createDefaultPermissions() {
        List<String> list = new ArrayList<>();
        list.add("/");
        return list;
    }


    /**
     * 拿到重置的所有东西
     * @return 权限信息
     */
    public static RestDTO getRestPermissions() {
        RestDTO bean = new RestDTO();
        try {
            String json = ObjectUtil.readString(ConfigConstant.CONF_BASE_DEFAULT_PERMISSIONS + File.separator + "rest.json");
            bean = FastJsonUtils.toBean(json, RestDTO.class);
        } catch (IOException e) {
            throw new EException("读取配置文件错误，请检查配置文件！");
        }
        return bean;
    }

    private static void getList(List<String> defaultPermissions, String s) {
        Object[] array = FastJsonUtils.toArray(s, String.class);
        List<Object> collect = Arrays.stream(array).collect(Collectors.toList());
        collect.forEach(c -> {
            defaultPermissions.add((String) c);
        });
    }


}
