# spring-boot-learn-02

### Spring Boot Project yaml配置学习笔记

Spring Boot 所使用的全局配置文件

application.properties
application.yml


首先说下一配置文件的作用: 修改Spring Boot自动配置的默认值,Spring Boot本身在底层给我们配置好了.

application.properties 这个文件是常见, 常用的


### 1.YAML (YAML Ain't Markup Language)

以前的配置文件: 大多数使用的是: **xxxxx.xml文件**

YAML: 以数据为中心, 比JSON, xml等更适合做配置文件;

YAML: 配置的例子
```yaml
server:
    port: 8081
```

XML: 配置的例子
```xml
    <server>
        <port>8081</port>
    </server>
```

### 2. YAML 基本语法
K: V 表示一对键值对(K与V 之间要有空格)
以**空格**的缩进来控制层级关系; 只要是左对齐的一列数据,都是同一个层级的, 举个例子
```yaml
server:
    port: 8088
    path: /hello
```
属性和值是区分大小写的

### 3. YAML 值的写法
**字面量: 普通的值(数值,字符串,布尔值)**

    K: V, 这里的V就是我们需要提供的值,字面值直接写就可以, 字符串默认不需要使用单引号或双引号来引用

**对象,Map (属性和值, 对应的语法就是K: V, 键值对)**

    对象还是键值对的方式: K: V, 例子如下
```yaml

        person:
            lastName: James
            age: 30
```

    对象的行内写法:
```yaml

  person: {lastName: James,age: 30}

```


**数组(List, Set)**
用- 值, 表示数组中的一个值
```yaml

pets:
    - cat
    - dog
    - pig

```
数字的行内写法:
```yaml
pets: [cat,dog,pig]

```

### 4. YAML 配置文件的具体例子

**配置文件的内容**
```yaml
person:
    lastName: James
    age: 30
    boss: false
    birth: 2017/12/13
    maps: {K1: V1,K2: V2}
    lists:
      - Mark
      - Dennis
      - Spring
    dog:
      name: Koggle
      age: 2

```
**Java Bean的内容**
```java
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

```

**导入配置文件处理器**
```xml
<!-- @ConfigurationProperties, 配置文件解析器 -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-configuration-processor</artifactId>
	<optional>true</optional>
</dependency>

这个是根据官方文档的说明, 在pom.xml文件中添加这个依赖,这样@ConfigurationProperties 才起作用

```

### application.properties 配置文件
Spring Boot 除了可以使用yaml作为应用的配置文件, 还可以使用application.properties作为应用的配置无论使用哪种
配置文件,开发人员可以使用@ConfigurationProperties 注解将配置文件加载到Bean中



### @Value注解获取值与@ConfigurationProperties注解获取值比较


ID              | @ConfigurationProperties            | @Value
----------------| ------------------------------------| ---------------------------------
功能              | 批量注入配置文件中的属性                           |逐一注入
松散绑定(松散语法)              | 支持                               | 不支持
SpEL              | 不支持		                  | 支持
JSR303数据校验     | 支持                           | 不支持
复杂类型封装(Map)   |支持                           |不支持


如果开发人员只是在某个业务逻辑中需要获取一下配置文件中的某项值, 可以使用**@Value**注解, 如果我们专门编写
一个JavaBean和配置文件进行映射时, 我们就是直接使用**@ConfigurationProperties**


### 配置文件注入值数据校验
```java
/*
    @ConfigurationProperties 会告诉Spring Person类中的属性都是配置文件中的属性
    prefix = "person" 指的是配置文件中person下的所有属性进行一一映射

    另外这个组件只用是容器中的组件, 才能提供功能

    @ConfigurationProperties 是支持JSR-303校验的,
    例如下面的 @Email
 */
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



```

### @PropertySource & @ImportResource
**@PropertySource: 加载指定的配置文件,并可以指定多个值** @ConfigurationProperties 默认是从全局配置文件中读取配置信息,
```java
/*
    @ConfigurationProperties 会告诉Spring Person类中的属性都是配置文件中的属性
    prefix = "person" 指的是配置文件中person下的所有属性进行一一映射

    另外这个组件只用是容器中的组件, 才能提供功能

    @ConfigurationProperties 是支持JSR-303校验的,
    例如下面的 @Email


    @ConfigurationProperties(prefix = "person") 默认是从全局配置文件中读取配置信息
    如果想读取其它配置文件, 可以使用@PropertySource注解, value值可以是以数组的方式

 */
@PropertySource(value = {"classpath:person.properties"})
@Component
@ConfigurationProperties(prefix = "person")
//@Validated
public class Person {

    //@Email
    private String lastName;
    private Integer age;

    private Boolean boss;
    private Date birth;


    private Map<String,Object> maps;

    private List<Object> lists;

    private Dog dog;
```

**@ImportResource** : 导入Spring的配置文件, 让配置文件里的内容生效
Spring Boot里面没有Spring的配置文件, 开发人员自己编写的配置文件, 也不能自动识别
想让Spring的配置文件生效, 加载进来, 此时把**@ImportResource** 注解标注在一个配置类上

```xml
@ImportResource 导入Spring的配置文件
```

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@ImportResource(locations = {"classpath:beans.xml"})
@SpringBootApplication
public class LearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnApplication.class, args);
	}

}
```
**在使用Spring Boot开发是不再推荐编写Spring的配置文件了**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">


    <bean id="helloService" class="org.cherry.learn.services.HelloService">

    </bean>
</beans>
```
**Spring Boot推荐给容器添加组件的方式,推荐全注解的方式**
1.配置类 也就是 对应之前Spring配置文件
2.使用 **@Bean** 给容器中添加组件
```java
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
```
### 配置文件占位符(properties文件, yml文件都支持)
#### 1, 随机数
```java
${random.int}, ${random.long}
${random.int(10)}, ${random.int[1024,65536]}
```
#### 2,站位符获取之前的配置的值, 如果没有可以使用(:)冒号指定默认值
```properties
person.lastName=Tim${random.uuid}
person.age=${random.int}
person.boss=false
person.birth=2017/12/15
person.maps.K1=V1
person.maps.K2=V2
person.lists=a,b,c
person.dog.name=${person.hello:hello}_dog
person.dog.age=3
```

### Profile 多环境支持
#### 1, 多Profile文件
我们在开发的时候, 可以创建多个application-{profile}.properties/yml, 默认使用application.properties的配置



#### 2, yml支持多文档块方式
```yaml
server:
  port: 8080

spring:
  profiles:
    active: dev
---
server:
  port: 8081

spring:
  profiles: dev

---
server:
  port: 8083

spring:
  profiles: prod
```

#### 3, 激活指定的Profile
1. 在配置文件中指定: **spring.profile.active=dev** <br>
2. 命令行的方式: java -jar spring-boot-application.jar --spring.profiles.active=dev <br>
   在开发测试过程中可以配置命令行参数<br>
3. 虚拟机参数