package com.example.tesla.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeslaConfiguration {
    @Bean
    ObjectMapper objectMapperBean() {
        return new ObjectMapper();
    }
}
