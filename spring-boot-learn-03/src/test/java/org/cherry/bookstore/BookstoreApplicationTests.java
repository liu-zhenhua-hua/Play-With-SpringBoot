package org.cherry.bookstore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookstoreApplicationTests {


	//记录器
	Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void contextLoads() {

		System.out.println();
		/*
			日志的级别
			由底到高  trace<debug<info<warn<error

			可以调整输出日志的级别, 日志就只会在这个级别以后的高级别生效
		 */
		logger.trace("This is Trace Information ");
		logger.debug("This is Debug Information ");

		//Spring Boot 默认是的info级别的日志输出级别, 可在配置文件中进行调整设置, 没有指定级别的就用Spring Boot默认规定的
		//级别
		logger.info("This is infor Information ");
		logger.warn("This is warn Information ");
		logger.error("This is error Information ");

	}

}
