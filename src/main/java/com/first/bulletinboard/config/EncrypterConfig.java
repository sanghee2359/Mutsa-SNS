package com.first.bulletinboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncrypterConfig {
    @Bean
    public BCryptPasswordEncoder encoder() { // password μνΈν
        return new BCryptPasswordEncoder();
    }
}
