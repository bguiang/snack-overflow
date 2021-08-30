package com.bernardguiang.SnackOverflow;

import org.junit.jupiter.api.Test;

//@SpringBootTest
//@MockBeans(@MockBean(DatabaseSeeder.class)) //The mock will replace any existing bean of the same type in the application context								
class SnackOverflowApplicationTest {		// It's used here to prevent the Database Seeder from creating fake/default data from
											// context events which makes loading the context fail when when unit testing
	@Test
	void contextLoads() {
	}

}
