package com.wms.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage")
@Api(tags = "Controller-库位信息", value = "/storage")
public class StorageController {
}
