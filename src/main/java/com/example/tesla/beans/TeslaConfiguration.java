package com.example.tesla.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TeslaConfiguration {

    public static final String LOCAL_ERROR_DB = "localErrorDb";
    @Bean
    ObjectMapper objectMapperBean() {
        return new ObjectMapper();
    }

    @Bean
    @Qualifier(LOCAL_ERROR_DB)
    List<String> localErrorDb() {
        return new ArrayList<>();
    }
}
