# spring-boot-learn-01
First Project of Spring Boot.
"Hello World" Level Spring Boot Application

### Parent Project (Maven)
```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.6.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    It has their own parent project

      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>2.2.6.RELEASE</version>
        <relativePath>../../spring-boot-dependencies</relativePath>
      </parent>
```

### Import Dependency Spring Boot Starter
```xml
    <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
```

Spring Boot provides lots of 'Starter', developer can import the starter which they want.

### Main class, Entry's Class
```java
@SpringBootApplication
public class HelloCherryApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloCherryApplication.class,args);
    }
}
```

**@SpringBootAppliction**: SpringBootApplication注解,标注在某个类上说明这个类是SpringBoot的主配置类,SpringBoot就应该
运行这个类的main方法来启动SpringBoot应用,将这个注解展开来看,具体内容如下

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
    ...
}
```

**@SpringBootConfiguration** SpringBoot的配置类,标注在某个类上, 表示是一个Spring Boot的配置类; 该注解上还有一个注解
**@Configuration**: 这个是Spring的基础注解, 配置类 --- 配置文件, 实际上这个配置类也是一个容器中的一个组件 **@Component**



**@EnableAutoConfiguration**: 开启自动配置功能;只有这样自动配置才能生效;

```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
    ...
}
```
