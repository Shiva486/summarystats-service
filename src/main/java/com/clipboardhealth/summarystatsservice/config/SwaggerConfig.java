package com.clipboardhealth.summarystatsservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("SummaryStats-Service APIs")
                                .version("v0.0.1")
                                .license(new License().name("Problem Statement")
                                                      .url("https://docs.google.com/document/d/1VLeLbYSCdOmZzjNmKIcpguEtABO8aeQSmmnq0LOSmC8/edit"))
                                .contact(new Contact().email("shiva.chandra11@gmail.com")))
                .externalDocs(new ExternalDocumentation().description("Github Repo")
                                                         .url("https://github.com/Shiva486/summarystats-service"));
    }
}
