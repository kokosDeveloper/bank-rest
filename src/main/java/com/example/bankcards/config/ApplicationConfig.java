package com.example.bankcards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Random;

@Configuration
public class ApplicationConfig {
    @Bean
    public Random random(){
        return new Random();
    }
    @Bean
    public AuditorAware<Long> auditorAware(){
        return new ApplicationAuditAware();
    }
}
