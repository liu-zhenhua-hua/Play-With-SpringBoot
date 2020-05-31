# Spring Boot 与日志

编写一个统一的接口层, 日志门面(日志的抽象接口), 在实际的开发中引入具体的实现即可.


日志门面(日志框架的抽象层) | 日志实现
---------------------- | --------------------------
JCL(Jakarta Commons Logging),          | Log4j JUL(java.util.logging), Log4j2 Logback
SLF4j(Simple Logging Facade for Java),
JBoss-logging