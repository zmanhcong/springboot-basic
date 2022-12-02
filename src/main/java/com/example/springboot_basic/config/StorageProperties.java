package com.example.springboot_basic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("storage")  //file sẽ được lưu ở application.properties với: storage.location.
@Data
public class StorageProperties {
    private String location;
}