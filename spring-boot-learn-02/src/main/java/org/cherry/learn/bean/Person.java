package org.cherry.learn.bean;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {

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