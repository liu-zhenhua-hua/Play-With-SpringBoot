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

**@AutoConfigurationPackage** 自动配置包, 这个自动配置包中含有一条重要的语句 @Import(AutoConfigurationPackages.Registrar.class)
Spring的底层注解**@Import** 给容器导入一个组件; 导入的组件由**AutoConfigurationPackages.Registrar.class**
```java
它的作用就是将主配置类(@SpringBootApplication标注的类)的所在包及下面所有子包里面所有组件扫描到Spring容器中
```



**@EnableAutoConfiguration** : 中还有另外一个注解:
@Import(AutoConfigurationImportSelector.class), AutoConfigurationImportSelector: 我可以把它理解为导入组件选择器,
它将所有需要导入的组件以全类名的方式返回,这些组件就会被添加到容器中; 其实就是为容器导入很多的自动配置类(xxxAutoConfiguration)
就是给容器中导入这个场景需要的所有组件,并配置好这些组件;

具体导入哪些组件,请参看如下截图
![导入的组件](https://github.com/liu-zhenhua-hua/Play-With-SpringBoot/blob/master/spring-boot-learn-01/images/components.png)


有了自动配置类,免了我们手动编写配置注入功能组件等的工作<br/>
**SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class,classLoader)**: 从类路径下的**META-INF/spring.factories**中
获取**EnableAutoConfiguration**指定的值.


总结一下: Spring Boot在启动时从类路径下的"META-INF/spring.factories" 中获取EnableAutoConfiguration指定的值, 将这些值作为自动配置类
导入到容器中, 自动配置类就生效了, 帮我们进行自动配置工作. 以前是我们自己需要手动配置的东西, 现在自动配置类帮我们完成了这部分工作.



J2EE的整体整合解决方案和自动配置都在 **spring-boot-autoconfigure-2.2.6.RELEASE.jar** 文件中



### Spring Boot Initializer
Spring Boot 为开发者提供一个快速开发向导(https://start.spring.io/) 使用这种方式构建Spring Boot应用程序,Initializer 会为开发者自动
创建程序的主类, 开发者所需要做的就是编写自己的controller即可. 生成的项目中包含一个文件夹**resources**
