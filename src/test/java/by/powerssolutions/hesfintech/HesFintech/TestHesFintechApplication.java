package by.powerssolutions.hesfintech.HesFintech;

import org.springframework.boot.SpringApplication;

public class TestHesFintechApplication {

	public static void main(String[] args) {
		SpringApplication.from(HesFintechApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
