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
字面量: 普通的值()