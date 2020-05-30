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
01              | ArrayList                           |
02              | Array                               | [None]
03              | Stack, Queue		                  | [None]
04              | LinkedList                          | [None]
