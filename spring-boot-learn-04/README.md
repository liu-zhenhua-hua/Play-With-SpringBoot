### Spring Boot Web开发

使用Spring Boot: <br>
1. 创建Spring Boot应用, 选中我们需要的模块(Starters) 也就是各种各样的场景 <br>
2. Spring Boot已经默认将这些场景配置好了, 开发人员只需要在配置文件中指定少量的配置就可以运行起来 <br>
3. 编写实际的业务代码 <br>

**自动配置原理** <br>
各个场景Spring Boot帮我们配置了什么? 能不能修改? 能修改哪些配置,能不能扩展 <br>
```java
xxxxAutoConfiguration: 帮助开发者给容器中自动配置组件
xxxxProperties: 配置类封装配置文件的内容
```


**Spring Boot对静态资源的映射规则**

```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
	if (!this.resourceProperties.isAddMappings()) {
		logger.debug("Default resource handling disabled");
		return;
		}
	    Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
		CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
		if (!registry.hasMappingForPattern("/webjars/**")) {
			customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/")
				.setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
		}
		String staticPathPattern = this.mvcProperties.getStaticPathPattern();
		if (!registry.hasMappingForPattern(staticPathPattern)) {
			    customizeResourceHandlerRegistration(registry.addResourceHandler(staticPathPattern)
					.addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
					.setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
		}
	}
```
1. 所有的/webjars/**, 都去classpath:/META-INF/resources/webjars/ 找资源 <br>
webjars 以jar包的方式引入静态资源 (https://www.webjars.org/); 例如下面的例子

```xml
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>jquery</artifactId>
    <version>3.5.1</version>
</dependency>
```

