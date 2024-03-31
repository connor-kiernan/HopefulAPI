package uk.co.withingtonhopecf.hopefulapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HopefulApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HopefulApiApplication.class, args);
	}

}
