package org.cherry.learn.config;

import org.cherry.learn.services.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tony on 2020/5/30.
 */

/*
    @Configuration 指明该类是一个配置类, 也就是相当于之前Spring的配置文件
    在之前的配置文件中,使用<bean><bean/> 标签添加组件
 */
@Configuration
public class MyConfiguration {

    //@Bean 将方法的返回值,添加到容器中, 容器中这个组件默认的ID就是方法名-helloService
    @Bean
    public HelloService helloService(){
        System.out.println("MyConfiguration @Bean Adding new Component helloService.");
        return new HelloService();
    }
}
