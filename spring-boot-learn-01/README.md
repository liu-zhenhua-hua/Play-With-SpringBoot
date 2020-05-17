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

### Import Dependency
