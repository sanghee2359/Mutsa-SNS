package com.first.bulletinboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableJpaAuditing
@SpringBootApplication
public class FirstmissionBulletinBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstmissionBulletinBoardApplication.class, args);
	}

}
