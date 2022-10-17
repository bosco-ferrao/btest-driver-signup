package com.btest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@SpringBootApplication
public class StartDriverApplication {

    // start everything
    public static void main(String[] args) {
        SpringApplication.run(StartDriverApplication.class, args);
    }

    // run this only on profile 'demo', avoid run this in test
    @Profile("demo")
    @Bean
    CommandLineRunner initDatabase(DriverRepository repository) {
        return args -> {
            repository.save(new Driver("Bosco1", "Address1", Boolean.TRUE));
            repository.save(new Driver("Bosco2", "Address2", Boolean.FALSE));
            repository.save(new Driver("Bosco3", "Address3", Boolean.TRUE));
        };
    }
}