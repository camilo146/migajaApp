package com.migaja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MigajaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MigajaApplication.class, args);
    }
}
