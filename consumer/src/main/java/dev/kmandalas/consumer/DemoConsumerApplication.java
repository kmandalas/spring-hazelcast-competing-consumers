package dev.kmandalas.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoConsumerApplication.class, args);
	}

}
