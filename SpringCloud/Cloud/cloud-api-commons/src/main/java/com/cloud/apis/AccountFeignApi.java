package com.cloud.apis;

import com.cloud.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-account-service")
public interface AccountFeignApi {


    @PostMapping(value = "/account/decrease")
    ResultData<?> decrease(@RequestParam("userId") Long userId, @RequestParam("money") Long amount);
}
