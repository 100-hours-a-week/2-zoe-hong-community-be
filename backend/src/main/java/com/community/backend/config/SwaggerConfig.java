package com.community.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Community API",
        version = "1.0",
        description = "커뮤니티 API 문서"
))
public class SwaggerConfig {
}
