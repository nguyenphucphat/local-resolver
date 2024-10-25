package com.ia03.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Value("${api.endpoint}")
  private String endPoint;

  @Bean
  public OpenAPI openApi() {
    Server server = new Server();
    server.setUrl(endPoint);
    server.setDescription("IA03 backend service development");

    Contact contact = new Contact();
    contact.setEmail("21120521@student.hcmus.edu.vn");
    contact.setName("Nguyen Phuc Phat");

    License license =
        new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    Info info =
        new Info().title("IA03 APIs").version("1.0.0-SNAPSHOT").contact(contact).license(license);

    final String securitySchema = "BearerAuth";

    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(securitySchema))
        .components(
            new Components()
                .addSecuritySchemes(
                    securitySchema,
                    new io.swagger.v3.oas.models.security.SecurityScheme()
                        .name(securitySchema)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT")))
        .info(info)
        .servers(Collections.singletonList(server));
  }
}
