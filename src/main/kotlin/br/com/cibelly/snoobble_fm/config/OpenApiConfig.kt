package br.com.cibelly.scrobbledelia.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        val openAPI = OpenAPI()

        openAPI.info = Info()
            .title("ScrobbleDelia API")
            .description("Rede social de música baseada em scrobbles do Last.fm")
            .version("1.0.0")

        openAPI.components = Components().addSecuritySchemes(
            "bearerAuth",
            SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        )

        openAPI.addSecurityItem(SecurityRequirement().addList("bearerAuth"))

        return openAPI
    }
}