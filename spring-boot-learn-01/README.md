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
运行这个类的main方法来启动SpringBoot应用