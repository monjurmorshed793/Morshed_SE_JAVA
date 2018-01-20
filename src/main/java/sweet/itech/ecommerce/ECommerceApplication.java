package sweet.itech.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import sweet.itech.ecommerce.config.ApplicationProperties;

@Component
@EnableAutoConfiguration
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})

public class ECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}
}
