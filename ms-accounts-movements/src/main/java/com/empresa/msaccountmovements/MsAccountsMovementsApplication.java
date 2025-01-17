package com.empresa.msaccountmovements;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class MsAccountsMovementsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAccountsMovementsApplication.class, args);
	}

}
