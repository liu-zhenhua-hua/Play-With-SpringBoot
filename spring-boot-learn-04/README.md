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
@ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
public class ResourceProperties
// 可以设置和静态资源有关的参数, 缓存时间
```


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
访问资源的时候 localhost:8080/webjars/jquery/3.5.1/jquery.js <br>

2. "/**" 当你访问当前项目的任何资源
```java
"classpath:/META-INF/resources/",
"classpath:/resources/",
"classpath:/static/",
"classpath:/public/";
"/" -- 当前项目根路径, Spring Boot 2.1.7中没有看到该定义, 需要再仔细确认一下
```
localhost:8080/abc ---> 去静态资源文件夹里找abc <br>


3. 欢迎页, 静态资源文件夹下的所有index.html页面; 被"/**"映射
```java
//设置欢迎页
@Bean
public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext) {
	WelcomePageHandlerMapping welcomePageHandlerMapping = new WelcomePageHandlerMapping(
		new TemplateAvailabilityProviders(applicationContext), applicationContext, getWelcomePage(),
		this.mvcProperties.getStaticPathPattern());
		welcomePageHandlerMapping.setInterceptors(getInterceptors());
		return welcomePageHandlerMapping;
}
```

localhost:8080/  找index页面 <br>


4. 配置Favorite Icon <br>
所有的"**/favicon.ico" 都在静态资源文件夹下找
```java
@Configuration
		@ConditionalOnProperty(value = "spring.mvc.favicon.enabled", matchIfMissing = true)
		public static class FaviconConfiguration implements ResourceLoaderAware {

			private final ResourceProperties resourceProperties;

			private ResourceLoader resourceLoader;

			public FaviconConfiguration(ResourceProperties resourceProperties) {
				this.resourceProperties = resourceProperties;
			}

			@Override
			public void setResourceLoader(ResourceLoader resourceLoader) {
				this.resourceLoader = resourceLoader;
			}

			@Bean
			public SimpleUrlHandlerMapping faviconHandlerMapping() {
				SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
				mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
				mapping.setUrlMap(Collections.singletonMap("**/favicon.ico", faviconRequestHandler()));
				return mapping;
			}

			@Bean
			public ResourceHttpRequestHandler faviconRequestHandler() {
				ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
				requestHandler.setLocations(resolveFaviconLocations());
				return requestHandler;
			}

			private List<Resource> resolveFaviconLocations() {
				String[] staticLocations = getResourceLocations(this.resourceProperties.getStaticLocations());
				List<Resource> locations = new ArrayList<>(staticLocations.length + 1);
				Arrays.stream(staticLocations).map(this.resourceLoader::getResource).forEach(locations::add);
				locations.add(new ClassPathResource("/"));
				return Collections.unmodifiableList(locations);
			}

		}
```

### 设置自定义的静态资源路径

```properties
spring.resources.static-locations=classpath:/hello/, classpath:/second
```


### thymeleaf 模版引擎

引入thymeleaf模版引擎的Starter
```xml
	<!-- 引入Thymeleaf 模版引擎 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
```


### thymeleaf的使用&语法

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx" xmlns:jdbc="http://www.springframework.org/schema/jdbc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
     http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
     http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
     http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
     http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <context:component-scan base-package="com.mmall" annotation-config="true"/>

    <!--<context:annotation-config/>-->
    <aop:aspectj-autoproxy/>


    <import resource="applicationContext-datasource.xml"/>


</beans>
```
