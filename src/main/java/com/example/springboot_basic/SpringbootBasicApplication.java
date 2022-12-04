package com.example.springboot_basic;

import com.example.springboot_basic.config.StorageProperties;
import com.example.springboot_basic.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SpringbootBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBasicApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService){
        return (args -> {
            storageService.init();
        });
    }

}
