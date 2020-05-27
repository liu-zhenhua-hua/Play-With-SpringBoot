package org.cherry.learn;

import org.cherry.learn.bean.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class LearnApplicationTests {

	@Autowired
	Person person;

	@Test
	void contextLoads() {
		System.out.println(person);
	}

}
