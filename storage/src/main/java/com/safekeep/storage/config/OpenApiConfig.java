package com.safekeep.storage.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Penitipan barang dimas",
                version = "0.1",
                description = "Penitipan",
                contact = @Contact(
                        name = "dimasnurfaouzi@gmail.com"
                )
        )
)
public class OpenApiConfig {
}
