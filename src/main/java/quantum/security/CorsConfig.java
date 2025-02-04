package quantum.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedOrigins("http://13.37.8.48", "http://localhost:3000", "http://quantum-library.com", "https://quantum-library.com", "https://api.quantum-library.com")
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .maxAge(-1);
            }
        };
    }
}