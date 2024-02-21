package com.jumia.FA;

import com.jumia.FA.config.LoginEventListener;
import com.jumia.FA.service.EmailService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.jumia.FA.repository")
@ComponentScan(basePackages = "com.jumia.FA")
@EntityScan(basePackages = "com.jumia.FA.entity")
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

	@Bean
	public LoginEventListener loginEventListener(EmailService emailService){
		return new LoginEventListener(emailService);
	}

}
