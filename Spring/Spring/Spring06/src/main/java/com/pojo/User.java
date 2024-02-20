package com.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//  等价于  <bean id="user" class="com.pojo.User"/>
//  @Component 组件
@Component
public class User {

    // 相当于<bean id="user" class="com.pojo.User">
    //      <property name="name" value="蛋蛋"/>
    //      </bean>
    @Value("蛋蛋")
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
