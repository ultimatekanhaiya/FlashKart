package com.inventApper.flashkart.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration



@SecurityScheme(
        name = "scheme1",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(

        info = @Info(
                title = "FlashKart e-commerce API ",
                description = "This is backed of FlashKart e-commerce app",
                version = "1.0V",
                contact = @Contact(
                        name = "Kanhaiya Sharma",
                        email = "ultimatekanhaiya@gmail.com",
                        url = "https://www.linkedin.com/in/kanhaiya-sharma-581801161/"
                ),
                license = @License(
                        name = "OPEN License",
                        url = "https://www.linkedin.com/in/kanhaiya-sharma-581801161/"
                )
        )
        ,
        externalDocs = @ExternalDocumentation(
                description = "This is external docs",
                url = "https://www.linkedin.com/in/kanhaiya-sharma-581801161/"
        )
)
public class SwaggerConfig {
}
