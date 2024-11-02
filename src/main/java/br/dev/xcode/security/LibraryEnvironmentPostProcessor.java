package br.dev.xcode.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class LibraryEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("keycloak.issuer", "http://localhost:9001/realms/ecommerce");
        properties.put("spring.security.oauth2.resourceserver.jwt.issuer-uri", "http://localhost:9001/realms/ecommerce");

        // Adiciona as propriedades ao ambiente
        environment.getPropertySources().addLast(new MapPropertySource("libraryProperties", properties));
    }
}
