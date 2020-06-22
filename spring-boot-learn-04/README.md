### Spring Boot Web开发

(https://note.clboy.cn/#/backend/springboot/_sidebar -- Spring Boot 课程笔记)
(https://help.github.com/en/enterprise/2.15/user/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) Generating new SSH Key and Adding it into SSH-Agent
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
    定义错误代码生成规则
    Automatic use of a ConfigurableWebBindingInitializer bean (covered later in this document).
    开发者可以配置一个ConfigurableWebBindingInitializer 来替换默认的 (添加到容器中即可)

    初始化WebDataBinder;
    请求数据 ----> JavaBean;

If you want to keep Spring Boot MVC features and you want to add additional MVC configuration
(interceptors, formatters, view controllers, and other features),
you can add your own @Configuration class of type WebMvcConfigurer but without @EnableWebMvc.

如果开发人员想添加额外的MVC Configuration 例如: interceptor, formatters, view Controller 等等
**编写一个配置类(@Configuration) 并且是WebMvcConfigurer类型; 不能标注@EnableWebMvc 注解** 这种方式既保留了所有的自动配置, 也能用我们的扩展配置

1. WebMvcAutoConfiguration 是SpringMVC的自动配置类,在它的内部有个静态内部类 WebMvcAutoConfigurationAdapter 来实现WebMvcConfigurer 这个接口
2. 具体参看 WebMvcAutoConfiguration 就会了解, Spring Boot默认的MVC配置, 以及自定义的MVC配置, 是如何起作用的 重点: 静态内部类的@Import(EnableWebMvcConfiguration.class)

If you wish to provide custom instances of RequestMappingHandlerMapping, RequestMappingHandlerAdapter,
or ExceptionHandlerExceptionResolver,you can declare a WebMvcRegistrationsAdapter instance to provide such components.

If you want to take complete control of Spring MVC, you can add your own @Configuration annotated with @EnableWebMvc.

如果开发人员想全面接管SpringMVC 就自定义一个配置类(@Configuration) 并使用@EnableWebMvc 注解


### 如何修改Spring Boot的默认配置
模式: <br>
    1. Spring Boot在自动配置很多组件的时候, 先看容器中有没有用户自己配置的(@Bean, @Component)如果有就使用用户配置的, 如果没有, 才自动配置,如果有些组件可以有多个(ViewResolver) 将用户配置的和自己默认的组合起来 <br>
    2. 在Spring Boot 中会有非常多的xxxConfigurer 帮助我们进行扩展配置
<br>


###




### Spring Boot 国际化开发
1. 编写国际化配置文件 <br>
2. 使用ResourceBundleMessageSource管理国际化资源文件 <br>
3. 在页面fmt:message取出国际化内容 <br>

以上这三条是以前SpringMVC开发国际化的步骤. <br>


使用Spring Boot开发国际化的步骤
1. 编写国际化的配置文件, 抽取页面需要显示的国际化消息
```properties
login.btn=Sign In
login.password=Password
login.remember=Remember Me
login.tip=Please sign in
login.username=Username
```

2. Spring Boot自动配置好了管理国际化资源文件的组件: (**MessageSourceAutoConfiguration**)
```java
@Configuration
@ConditionalOnMissingBean(value = MessageSource.class, search = SearchStrategy.CURRENT)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Conditional(ResourceBundleCondition.class)
@EnableConfigurationProperties
public class MessageSourceAutoConfiguration {

	private static final Resource[] NO_RESOURCES = {};

    /*
       MessageSourceProperties 类中定义了一个basename

       private String basename = "messages";

       我们的配置文件(国际化配置文件) 可以直接放在类路径下叫message.properties,

    */

	@Bean
	@ConfigurationProperties(prefix = "spring.messages")
	public MessageSourceProperties messageSourceProperties() {
		return new MessageSourceProperties();
	}

	@Bean
	public MessageSource messageSource(MessageSourceProperties properties) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		if (StringUtils.hasText(properties.getBasename())) {
			messageSource.setBasenames(StringUtils
					.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(properties.getBasename())));
		}
		if (properties.getEncoding() != null) {
			messageSource.setDefaultEncoding(properties.getEncoding().name());
		}
		messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
		Duration cacheDuration = properties.getCacheDuration();
		if (cacheDuration != null) {
			messageSource.setCacheMillis(cacheDuration.toMillis());
		}
		messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
		messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
		return messageSource;
	}
```
3. 去页面获取国际化信息(thymeleaf 中使用 th:text="#{}" 获取国际化信息)

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">
		<title>Signin Template for Bootstrap</title>
		<!-- Bootstrap core CSS -->
		<link href="asserts/css/bootstrap.min.css" th:href="@{/webjars/bootstrap/4.0.0/css/bootstrap.css}" rel="stylesheet">
		<!-- Custom styles for this template -->
		<link href="asserts/css/signin.css" th:href="@{/asserts/css/signin.css}" rel="stylesheet">
	</head>

	<body class="text-center">
		<form class="form-signin" action="dashboard.html">
			<img class="mb-4" th:href="@{asserts/img/bootstrap-solid.svg}" src="asserts/img/bootstrap-solid.svg" alt="" width="72" height="72">
			<h1 class="h3 mb-3 font-weight-normal" th:text="#{login.tip}">Please sign in</h1>
			<label class="sr-only" th:text="#{login.username}">Username</label>
			<input type="text" class="form-control" placeholder="Username" th:placeholder="#{login.username}" required="" autofocus="">
			<label class="sr-only" th:text="#{login.password}">Password</label>
			<input type="password" class="form-control" placeholder="Password" th:placeholder="#{login.password}" required="">
			<div class="checkbox mb-3">
				<label>
          <input type="checkbox" value="remember-me"> [[#{login.remember}]]
        </label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit" th:text="#{login.btn}">Sign in</button>
			<p class="mt-5 mb-3 text-muted">© 2017-2018</p>
			<a class="btn btn-sm">中文</a>
			<a class="btn btn-sm">English</a>
		</form>

	</body>

</html>

```
以上的页面是根据浏览器的语言设置,切换国际化 <br>


如何根据页面上的链接,切换国际化信息

原理: 国际化local(区域信息对象); LocalResolver(获取区域信息对象)
```java
@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = "spring.mvc", name = "locale")
		public LocaleResolver localeResolver() {
			if (this.mvcProperties.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
				return new FixedLocaleResolver(this.mvcProperties.getLocale());
			}
			//AcceptHeaderLocaleResolver() 根据请求头的区域信息,获取Locale
			AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
			localeResolver.setDefaultLocale(this.mvcProperties.getLocale());
			return localeResolver;
		}
```

4.点击链接切换国际化, 自定义一个LocaleResolver来实现
```java
/*
    在链接上添加区域信息
 */

public class MyLocaleResolver implements LocaleResolver{
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String l = request.getParameter("l");
        Locale locale = Locale.getDefault();

        if(!StringUtils.isEmpty(l)){
            String[] split = l.split("_");
            locale = new Locale(split[0],split[1]);
        }


        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {

    }
}
```



错误消息的处理语句(thymeleaf 语法)
```html
<p style="color:red" th:text="${msg}" th:if="${not #strings.isEmpty(msg)}"></p>
```


### 拦截器登录检查

```java
public class LoginHandlerInterceptor implements HandlerInterceptor {
    //方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Object user = request.getSession().getAttribute("loginUserName");
        if(user == null){
            //未登录
            request.setAttribute("msg","You don't have authorized Login First ");
            request.getRequestDispatcher("/").forward(request,response);
            return false;
        }else{
            //已登录
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
```


该项目未完待续

....................... <br>


### Spring Boot默认的错误处理机制

Spring Boot 错误处理的原理:**ErrorMvcAutoConfiguration** 错误处理的自动配置,给容器添加了如下组件: <br>
    1. DefaultErrorAttributes <br>
    2. BasicErrorController <br>
    3. ErrorPageCustomizer <br>
    4. DefaultErrorViewResolver <br>


错误处理步骤:
一旦系统出现4xx或者5xx之类的错误, **ErrorPageCustomizer**就会生效(定制错误的响应规则) 出现错误之后来到/error请求,就会被**BasicErrorController**处理

```java
//ErrorPageCustomizer
/**
 * Path of the error controller.
 */
 @Value("${error.path:/error}")
 private String path = "/error"; // 系统出现错误之后来到error请求进行处理(web.xml注册的错误页面规则)

```

```java

    //BasicErrorController
    @Controller
    @RequestMapping("${server.error.path:${error.path:/error}}")
    public class BasicErrorController extends AbstractErrorController {

            @RequestMapping(produces = MediaType.TEXT_HTML_VALUE) //产生html类型的数据, 浏览器发送的请求由它来处理
        	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        		HttpStatus status = getStatus(request);
        		Map<String, Object> model = Collections
        				.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
        		response.setStatus(status.value());
        		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        		return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
        	}

        	@RequestMapping
        	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        		Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        		HttpStatus status = getStatus(request);
        		return new ResponseEntity<>(body, status);
        	}

    }
```

响应页面 (去哪个页面)是由**DefaultErrorViewResolver** 解析得到的
```java
	protected ModelAndView resolveErrorView(HttpServletRequest request, HttpServletResponse response, HttpStatus status,
			Map<String, Object> model) {
		for (ErrorViewResolver resolver : this.errorViewResolvers) {
			ModelAndView modelAndView = resolver.resolveErrorView(request, status, model);
			if (modelAndView != null) {
				return modelAndView;
			}
		}
		return null;
	}
```

DefaultErrorViewResolver 是如何解析的
```java
@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
		ModelAndView modelAndView = resolve(String.valueOf(status.value()), model);
		if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
			modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
		}
		return modelAndView;
	}

	private ModelAndView resolve(String viewName, Map<String, Object> model) {
	    //Spring Boot默认会去到一个页面, error/404
		String errorViewName = "error/" + viewName;

		//模版引擎可以解析这个这个页面地址,就用模版引擎解析
		TemplateAvailabilityProvider provider = this.templateAvailabilityProviders.getProvider(errorViewName,
				this.applicationContext);
		if (provider != null) {
		    //如果模版引擎可用的情况下返回到 errorViewName,指定的视图地址
			return new ModelAndView(errorViewName, model);
		}
		//模版不可用的情况下,就在静态资源下找errorViewName对应的页面 error/404.html
		return resolveResource(errorViewName, model);
	}
```


### 定制Spring Boot的错误处理

1.  如何定制错误的页面
    有模版引擎的情况下, error/状态码 [将错误页面命名为 错误状态码.html, 放在模版引擎 templates/error/404.html 这样的方式],发生此状态码的错误就会
    来到对应的页面, 我们可以使用4xx和5xx作为错误页面的文件名来匹配这种类型的所有错误,精确优先, 优先寻找精确的状态码. 页面能获取的信息: timestamp 时间戳,
    status: 状态码, error: 错误提示,exception: 异常对象, message: 异常消息, errors: JSR303数据校验的错误都在这里

    如果没有模版引擎(模版引擎找不到这个页面), 静态资源文件夹下找; 以上都没有错误页面, 就是默认来到Spring Boot默认的错误提示页面;

2.  如何定制错误数据(其它终端访问应用返回的JSON数据)
    1) 自定义异常处理&返回定制的JSON数据

    2) 传入自己的错误状态码





### 配置嵌入式容器

Spring Boot 默认使用的是嵌入式的Tomcat Servlet容器, 我们的问题? 如何定制和修改Servlet容器的相关配置, SpringBoot 能不能支持其它的Servlet容器

1. 如何定制和修改Servlet容器相关的配置: 修改server有关的配置(ServerProperties 类[也是编写一个EmbeddedServletContainerCustomizer] )
```properties
server.port=8099
server.servlet.context-path=/jobs

//通用的Servlet容器设置
server.xxx
//Tomcat的设置
server.tomcat.xxx
```
2. 编写一个EmbeddedServletContainerCustomizer : 嵌入式的Servlet容器的定制器
   Spring Boot中有很多xxxCustomizer 帮助我们定制配置


3. 注册Servlet 三大组件Servlet, Filter, Listener