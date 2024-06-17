package com.cloud.MyGateway;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;


//  自定义故意配置userType，按照钻、金gold、银和yml配置的会员等级，以适配是否可以访问
@Component
public class MyRouterPredicateFactory extends AbstractRoutePredicateFactory<MyRouterPredicateFactory.Config> {

    public MyRouterPredicateFactory() {
        super(MyRouterPredicateFactory.Config.class);
    }

    //  可以简写方式
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("userType");
    }

    @Override
    public Predicate<ServerWebExchange> apply(MyRouterPredicateFactory.Config config) {
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                String userType = serverWebExchange.getRequest().getQueryParams().getFirst("userType");
                if (userType == null) {
                    return false;
                }

//                if (userType.equalsIgnoreCase(config.getUserType())){
//                    return true;
//                }
//                return false;
                //  如果说参数存在，就和config的数据进行比较
                return userType.equalsIgnoreCase(config.getUserType());
            }
        };
    }

    // 这个Config就是路由断言规则
    @Getter
    @Setter
    @Validated
    public static class Config {
        @NotEmpty
        private String userType;
    }
}