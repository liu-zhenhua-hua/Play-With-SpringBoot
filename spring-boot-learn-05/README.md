### Docker 容器技术



#### Docker 容器的基本操作
软件镜像 ---- 运行环境 ---- 产生一个容器 <br>

具体操作步骤:
```shell
搜索镜像
[root@localhost~]# docker search tomcat
拉取镜像
[root@loclahost~]# docker pull tomcat:tag
查看镜像文件
[root@localhost~]# docker images

启动容器 -d 表示后台运行, --name 给容器起个名字
[root@localhost~]# docker run --name mytomcat -d tomcat:latest

查看运行中的容器
[root@localhost~]# docker ps

查看所有的容器
[root@localhost~]# docker ps -a

停止运行的容器
[root@localhost~]# docker stop 4u6io76ps (Container ID)

删除容器
[root@localhost~]# docker rm container-id

-p 主机端口映射到容器内部端口
[root@localhost~]# docker run -d -p 1000:8080 tomcat

查看容器日志
[root@localhost~]# docker logs container-name/container-id
```
更多的参考命令:(https://docs.docker.com/engine/reference/commandline/docker/)
注意: docker可以使用一个镜像, 启动多个容器, 容器与容器之间相互独立, 互不干扰



### Spring Boot 与数据访问

```xml
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
        	<artifactId>mysql-connector-java</artifactId>
        	<scope>runtime</scope>
        	<version>5.1.37</version>
        </dependency>
```


数据源配置
```yml
spring:
  datasource:
    username: foundation
    password: Foundation@
    url: jdbc:mysql://123.:3306/foundation
    initialization-mode: always #Spring Boot2.0之后,需要指定这个参数, 这样Spring Boot才能自动执行SQL脚本文件
    driver-class-name: com.mysql.jdbc.Driver
```

数据源相关的配置类**(DataSourceProperties)**, 自动配置原理(org.springframework.boot.autoconfigure.jdbc)

参考 DataSourceConfiguration 根据配置创建数据源 <br>

Spring Boot 如何自定义数据源
```java
/**
	 * Generic DataSource configuration.
	 */
	@Configuration
	@ConditionalOnMissingBean(DataSource.class)
	@ConditionalOnProperty(name = "spring.datasource.type")
	static class Generic {

		@Bean
		public DataSource dataSource(DataSourceProperties properties) {
			return properties.initializeDataSourceBuilder().build();
		}

	}
```