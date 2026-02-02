package vitor.gestaoevento.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;




@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gestão de Eventos API",
                version = "v1",
                description = "API para gestão de eventos com pagamento, QR Code e check-in",
                contact = @Contact(
                        name = "João Vitor Costa Rolim",
                        email = "vitorcostaofficial@gmail.com",
                        url = "https://github.com/j-vitordev"
                ),
                license = @License(
                        name = "MIT License"
                )

        ),

        security = @SecurityRequirement(name = "bearerAuth")

)
public class OpenApiConfig {
}



