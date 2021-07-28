package com.bernardguiang.SnackOverflow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import com.bernardguiang.SnackOverflow.service.DatabaseSeeder;

//@SpringBootTest
@MockBeans(@MockBean(DatabaseSeeder.class)) //The mock will replace any existing bean of the same type in the application context								
class SnackOverflowApplicationTest {		// It's used here to prevent the Database Seeder from creating fake/default data from
											// context events which makes loading the context fail when when unit testing
	@Test
	void contextLoads() {
	}

}
