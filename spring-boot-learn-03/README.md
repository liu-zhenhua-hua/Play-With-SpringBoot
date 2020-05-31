# Spring Boot 与日志

编写一个统一的接口层, 日志门面(日志的抽象接口), 在实际的开发中引入具体的实现即可.


日志门面(日志框架的抽象层) | 日志实现
---------------------- | --------------------------
JCL(Jakarta Commons Logging), SLF4j(Simple Logging Facade for Java),JBoss-logging | Log4j,JUL(java.util.logging), Log4j2,Logback



开发人员从左侧选一个抽象层, 再从右边选一个日志实现即可
JCL, JBoss-logging,这两个不能用

日志抽象我们选择: **SLF4j** <br>
日志实现我们选择: **Logback**


Spring Boot: 底层是Spring框架, Spring框架默认是用JCL (Jakarta Commons Logging) <br>

Spring Boot: 选用的是SLF4j和Logback.



### SLF4j的使用
#### 1, 如何在系统中使用SLF4j
在日后的开发中, 日志记录方法的调用, 不应该直接来调用日志的实现类, 而是调用日志抽象层里的方法<br>
每一个日志实现框架都有自己的配置文件, 使用SLF4j以后, 配置文件还是做成日志实现框架的配置