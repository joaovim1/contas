package br.com.api.contas.config;  // Caminho onde você colocou o arquivo

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Configuração de CORS global
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite que o frontend acesse a API
        registry.addMapping("/**")
                .allowedOrigins("https://despesasjoao.netlify.app")  
                .allowedMethods("GET", "POST", "PUT", "DELETE")      
                .allowedHeaders("*")                                 
                .allowCredentials(true);                             
    }
}
