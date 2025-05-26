package com.api.sss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI SwaggerAPI() {
		SecurityScheme apiKey = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.in(SecurityScheme.In.HEADER)
			.name("Authorization")
			.scheme("bearer")
			.bearerFormat("JWT");

		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("Bearer Token");

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("Bearer Token", apiKey))
			.addSecurityItem(securityRequirement)
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("EyeStock Swagger")
			.description("EyeStock Server API 명세서")
			.version("1.0.0");
	}

}
