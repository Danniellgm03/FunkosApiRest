package com.funkoSpring.funkoSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FunkoSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunkoSpringApplication.class, args);
	}

}
