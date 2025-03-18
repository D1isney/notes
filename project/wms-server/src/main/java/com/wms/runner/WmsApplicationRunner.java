package com.wms.runner;

import com.wms.connect.websocket.WebSocketServerWeb;
import com.wms.enums.WebSocketEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class WmsApplicationRunner implements ApplicationRunner {

    @Value("${webSocket.pushTime}")
    private Integer pushTime;

    private final ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);

    @Override
    public void run(ApplicationArguments args) {

        service.scheduleWithFixedDelay(() -> {
            WebSocketServerWeb.send(WebSocketEnum.SURPLUS_VIEW);
            WebSocketServerWeb.send(WebSocketEnum.WEEKLY_WORKLOAD_VIEW);
            WebSocketServerWeb.send(WebSocketEnum.INBOUND_AND_OUTBOUND_VOLUME_VIEW);
            WebSocketServerWeb.send(WebSocketEnum.ACTIVATION_VIEW);
            WebSocketServerWeb.send(WebSocketEnum.LOG_STATISTICS_VIEW);
        }, 0, pushTime, TimeUnit.SECONDS);

        log.info("定时任务启动完成----------{}", new Date());
    }


}
