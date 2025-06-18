package ru.ilug.business_card_website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BusinessCardWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessCardWebsiteApplication.class, args);
	}

}
