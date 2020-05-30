package org.cherry.learn.bean;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tony on 2020/5/27.
 */
/*
    @ConfigurationProperties 会告诉Spring Person类中的属性都是配置文件中的属性
    prefix = "person" 指的是配置文件中person下的所有属性进行一一映射

    另外这个组件只用是容器中的组件, 才能提供功能

    @ConfigurationProperties 是支持JSR-303校验的,
    例如下面的 @Email


    @ConfigurationProperties(prefix = "person") 默认是从全局配置文件中读取配置信息
    如果想读取其它配置文件, 可以使用@PropertySource注解, value值可以是以数组的方式

 */
@PropertySource(value = {"classpath:application.properites"})
@Component
@ConfigurationProperties(prefix = "person")
@Validated
public class Person {

    @Email
    private String lastName;
    private Integer age;

    private Boolean boss;
    private Date birth;


    private Map<String,Object> maps;

    private List<Object> lists;

    private Dog dog;


    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", age=" + age +
                ", boss=" + boss +
                ", birth=" + birth +
                ", maps=" + maps +
                ", lists=" + lists +
                ", dog=" + dog +
                '}';
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBoss(Boolean boss) {
        this.boss = boss;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setMaps(Map<String, Object> maps) {
        this.maps = maps;
    }

    public void setLists(List<Object> lists) {
        this.lists = lists;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }


    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    public Boolean getBoss() {
        return boss;
    }

    public Date getBirth() {
        return birth;
    }

    public Map<String, Object> getMaps() {
        return maps;
    }

    public List<Object> getLists() {
        return lists;
    }

    public Dog getDog() {
        return dog;
    }



}
