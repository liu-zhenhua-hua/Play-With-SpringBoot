package org.cherry.learn;

import org.cherry.learn.bean.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


@SpringBootTest
public class LearnApplicationTests {

	@Autowired
	Person person;


	@Autowired
	ApplicationContext ioc;


	@Test
	public void testHelloService(){
		boolean b = ioc.containsBean("helloService");

		if(b){
			System.out.println("We have helloService");
		}
	}


	@Test
	void contextLoads() {
		System.out.println(person);
	}

}
