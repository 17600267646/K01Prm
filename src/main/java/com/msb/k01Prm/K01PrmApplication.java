package com.msb.k01Prm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.msb.k01Prm.mapper")
@EnableRabbit
public class K01PrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(K01PrmApplication.class, args);
	}

}
