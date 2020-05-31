# Spring Boot 与日志

编写一个统一的接口层, 日志门面(日志的抽象接口), 在实际的开发中引入具体的实现即可.


日志门面(日志框架的抽象层) | 日志实现
---------------------- | --------------------------
JCL(Jakarta Commons Logging), SLF4j(Simple Logging Facade for Java),JBoss-logging | Log4j,JUL(java.util.logging), Log4j2,Logback



开发人员从左侧选一个抽象层, 再从右边选一个日志实现即可
JCL, JBoss-logging,这两个不能用

日志抽象我们选择: **SLF4j**,
日志实现我们选择: **Logback**


Spring Boot: 