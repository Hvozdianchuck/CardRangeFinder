package org.example.cardrangefinder.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/cardinfo", "myuser", "password")
                .locations("classpath:db/migration")
                .cleanOnValidationError(true)
                .load();
        flyway.migrate();
        return flyway;
    }
}
