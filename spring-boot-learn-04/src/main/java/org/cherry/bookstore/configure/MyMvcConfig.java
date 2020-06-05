package org.cherry.bookstore.configure;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    Spring Framework 5.0 之前, 开发人员对SpringMVC 进行扩展 需要扩展WebMvcConfigurerAdapter 这个适配器
    Spring Framework 5.0 开始  WebMvcConfigurer 使用这个接口, 不再使用那个适配类, Java 8 中对接口支持默认方法,
    所以就直接实现这个 WebMvcConfigurer 接口就可以.

    import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
 */



/**
 * Created by tony on 2020/6/4.
 */

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/myurl").setViewName("success");
    }

    
}
