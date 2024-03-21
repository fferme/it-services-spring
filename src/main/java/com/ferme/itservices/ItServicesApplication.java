package com.ferme.itservices;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "IT-Services API", version = "1", description = "API para manutenção de ordens, itens e clientes"))
public class ItServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItServicesApplication.class, args);
    }

}