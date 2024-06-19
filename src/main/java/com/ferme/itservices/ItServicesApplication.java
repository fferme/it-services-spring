package com.ferme.itservices;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "IT-Services API", version = "1", description = "API para manutenção de ordens, itens e clientes"))
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableCaching
@Slf4j
public class ItServicesApplication {
	public static void main(String[] args) {
		SpringApplication.run(ItServicesApplication.class, args);
	}
}