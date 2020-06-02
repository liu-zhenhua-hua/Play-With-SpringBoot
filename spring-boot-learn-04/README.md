### Spring Boot Web开发

使用Spring Boot: <br>
1. 创建Spring Boot应用, 选中我们需要的模块(Starters) 也就是各种各样的场景 <br>
2. Spring Boot已经默认将这些场景配置好了, 开发人员只需要在配置文件中指定少量的配置就可以运行起来 <br>
3. 编写实际的业务代码 <br>

**自动配置原理** <br>
各个场景Spring Boot帮我们配置了什么? 能不能修改? 能修改哪些配置,能不能扩展 <br>
```java
xxxxAutoConfiguration: 帮助开发者给容器中自动配置组件

```

