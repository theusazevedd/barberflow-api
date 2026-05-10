package com.azevedo.barberflow.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_JWT = "bearer-jwt";

    @Bean
    OpenAPI barberflowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Barberflow API")
                        .description("""
                                REST API for barbershop scheduling (users, availability, appointments).

                                **Errors:** failed requests return JSON `ErrorResponse` with `timestamp` (`yyyy-MM-dd'T'HH:mm:ss`, same shape as slot datetimes), `status`, `error`, `message`, and `path`. Typical codes: **400** business/validation rules, **401** invalid credentials or missing/invalid JWT, **403** authenticated but not allowed (e.g. role), **404** resource not found, **409** conflict (e.g. duplicate booking).
                                """)
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_JWT))
                .components(new Components().addSecuritySchemes(BEARER_JWT,
                        new SecurityScheme()
                                .name(BEARER_JWT)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT obtained from `POST /auth/login` — paste only the token value (Swagger adds the `Bearer ` prefix).")));
    }
}
