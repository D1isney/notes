package com.thorough.component;

import com.thorough.event.UserRegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Component1 {

    private static final Logger log = LoggerFactory.getLogger(Component1.class);

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void register() {
        log.info("register");
        eventPublisher.publishEvent(new UserRegisterEvent(this));
    }
}
