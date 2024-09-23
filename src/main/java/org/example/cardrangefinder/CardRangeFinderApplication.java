package org.example.cardrangefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CardRangeFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardRangeFinderApplication.class, args);
    }

}
