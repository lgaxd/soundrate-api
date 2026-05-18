package br.com.fiap.soundrate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SoundrateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoundrateApplication.class, args);
	}

}
