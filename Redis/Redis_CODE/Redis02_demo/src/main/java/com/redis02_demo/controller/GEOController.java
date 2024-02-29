package com.redis02_demo.controller;

import com.redis02_demo.service.GEOService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "美团地图位置附近的酒店推送GEO")
@RestController
@Slf4j
public class GEOController {

    @Resource
    private GEOService geoService;


    @ApiOperation("添加坐标GeoADD")
    @GetMapping("/geoAdd")
    public String geoADD(){
        return geoService.geoAdd();
    }

    @ApiOperation("获取经纬度坐标GEO")
    @GetMapping("/geoPos")
    public Point position(String member){
        return geoService.position(member);
    }


    @ApiOperation("获取经纬度生成的base32编码值geoHash")
    @GetMapping("/geoHash")
    public String hash(String member){
        return geoService.hash(member);
    }

    @ApiOperation("获取两个给定位置之间的距离")
    @GetMapping("/geoDist")
    public Distance distance(String member1 ,String member2){
        return geoService.distance(member1,member2);
    }


    @ApiOperation("通过经纬度查找北京王府井附近的")
    @GetMapping("/geoRadius")
    public GeoResults radiusByXY(){
        return geoService.radiusByXY();
    }

    @ApiOperation("通过地方查找附近，本例写死天安门作为地址")
    @GetMapping("/geoRadiusByMember")
    public GeoResults radiusByMember(){
        return geoService.radiusByMember();
    }

}
