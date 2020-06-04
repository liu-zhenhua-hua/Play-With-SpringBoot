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

具体的语法参看官网的用户手册 <br>


### Spring MVC Auto Configuration

Spring Boot provides auto-configuration for Spring MVC that works well with most applications.

Spring Boot 已经配置好了SpringMVC

The auto-configuration adds the following features on top of Spring’s defaults:
下面就是Spring Boot对SpringMVC的默认配置

    Inclusion of ContentNegotiatingViewResolver and BeanNameViewResolver beans.
    自动配置了 ViewResolver(视图解析器: 根据方法的返回值得到视图对象(View),视图对象决定如何渲染(转发? 重定向?)

    ContentNegotiatingViewResolver的作用是组合所有的视图解析器的
    如何定制: 我们自己可以给容器中添加一个视图解析器; 自动将其组合进来;

    Support for serving static resources, including support for WebJars (covered later in this document)).
    静态资源文件夹路径

    Static index.html support. 静态首页访问
    Custom Favicon support (covered later in this document). favicon.ico


    Automatic registration of Converter, GenericConverter, and Formatter beans.
    Converter: 转换器; public String hello(User user): 类型转换使用Converter.
    Formatter: 格式化器;

    自己添加的格式化器转换器, 我们只需要放在容器中即可
    Support for HttpMessageConverters (covered later in this document).

    HttpMessageConverters :  SpringMVC 用来转换Http请求和响应的; User ---> JSON

    HttpMessageConverters : 是从容器中, 获取所有的HttpMessageConverter:

    自己给容器中添加HttpMessageConverter, 只需要将自己的组件注册到容器中(@Bean,@Component)



    Automatic registration of MessageCodesResolver (covered later in this document).
    Automatic use of a ConfigurableWebBindingInitializer bean (covered later in this document).



If you want to keep Spring Boot MVC features and you want to add additional MVC configuration
(interceptors, formatters, view controllers, and other features),
you can add your own @Configuration class of type WebMvcConfigurer but without @EnableWebMvc.
If you wish to provide custom instances of RequestMappingHandlerMapping, RequestMappingHandlerAdapter,
or ExceptionHandlerExceptionResolver,you can declare a WebMvcRegistrationsAdapter instance to provide such components.

If you want to take complete control of Spring MVC, you can add your own @Configuration annotated with @EnableWebMvc.


