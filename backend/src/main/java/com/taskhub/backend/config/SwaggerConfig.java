package com.taskhub.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI taskHubOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TaskHub API")
                        .description("API para gerenciamento de tarefas com autenticação JWT")
                        .version("v1.0")
                );
    }
}