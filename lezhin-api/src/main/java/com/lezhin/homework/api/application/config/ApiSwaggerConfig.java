package com.lezhin.homework.api.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Configuration
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server Url")})
@SecuritySchemes({
        @SecurityScheme(
                type = SecuritySchemeType.HTTP,
                name = "Authorization",
                scheme = "Bearer",
                description = "JWT"
        ),
})
public class ApiSwaggerConfig {

    @Bean
    public GroupedOpenApi appApi() {
        String[] paths = {"/**"};
        return GroupedOpenApi.builder()
                .group("Lezhin Api")
                .pathsToMatch(paths)
                .addOperationCustomizer(operationCustomizer())
                .build();
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operation.security(List.of(new SecurityRequirement().addList("Authorization")));
            return operation;
        };
    }

}
