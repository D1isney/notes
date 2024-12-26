package com.wms.dto;

import com.wms.constant.ConfigConstant;
import com.wms.utils.FastJsonUtils;
import com.wms.utils.ObjectUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultPermissionsDTO {

    public static List<String> getPermissionsDTO() {
        List<String> defaultPermissions = new ArrayList<>();
        try {
            String s = ObjectUtil.readString(ConfigConstant.CONF_BASE_DEFAULT_PERMISSIONS + File.separator + "default_permissions.json");
            Object[] array = FastJsonUtils.toArray(s, String.class);
            List<Object> collect = Arrays.stream(array).collect(Collectors.toList());
            List<String> finalDefaultPermissions = defaultPermissions;
            collect.forEach(c->{
                finalDefaultPermissions.add((String) c);
            });
        } catch (IOException e) {
            defaultPermissions = createDefaultPermissions();
        }
        return defaultPermissions;
    }


    public static List<String> createDefaultPermissions() {
        List<String> list = new ArrayList<>();
        list.add("/123");
        return list;
    }
}
