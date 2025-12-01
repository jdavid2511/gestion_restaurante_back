package com.prueba.gestion_restaurante_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableAsync
public class GestionRestauranteBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionRestauranteBackApplication.class, args);
	}

}
