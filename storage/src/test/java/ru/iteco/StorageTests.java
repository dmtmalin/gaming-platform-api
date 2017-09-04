package ru.iteco;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.properties")
public class StorageTests {

	@SpringBootApplication
	@Import(StorageConfiguration.class)
	static class TestConfiguration {
	}

	@Test
	public void contextLoads() {
	}

}
