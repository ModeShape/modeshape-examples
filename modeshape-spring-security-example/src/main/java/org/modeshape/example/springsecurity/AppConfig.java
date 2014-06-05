package org.modeshape.example.springsecurity;

import org.modeshape.example.springsecurity.jcr.ModeShapeRepositoryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author M.Sarhan
 */

@Configuration
@EnableWebMvc
@ComponentScan
public class AppConfig extends WebMvcConfigurerAdapter {

    @Bean
    ModeShapeRepositoryFactory repositoryFactory() {
        ModeShapeRepositoryFactory factory = new ModeShapeRepositoryFactory();
        factory.setConfiguration(new ClassPathResource("repository-config.json"));
        return factory;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/");
    }

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/pages/");
        resolver.setSuffix(".jsp");

        return resolver;
    }
}
