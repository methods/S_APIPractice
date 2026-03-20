package com.example.HearingsDemo.config;


import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("MI Assembly Area Validation API")
                .version("1.0.0")
                .description("Internal API for the Common Platform MI Reporting Regression Test Suite. " +
                    "Used to verify data landed in the MI Assembly Area PostgreSQL database."));
    }
}