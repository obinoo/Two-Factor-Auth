package com.jumia.FA;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jumia.FA.entity"})
@OpenAPIDefinition(
		info = @Info(
				title = "2FA",
				description = "Learning Purposes",
				contact = @Contact(
						name = "Jalopy",
						email = "nnubiacobinna@gmail.com"
				)
		)
)

public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
