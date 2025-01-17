package com.sixa.microservices.product.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI OrderServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("Order Service API")
                        .description("this is REST API for order Service")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("refer to the order service wiki documentation")
                        .url("http://order-service-wiki-url.com/docs"));
    }
}
