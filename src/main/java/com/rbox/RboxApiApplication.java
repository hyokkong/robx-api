package com.rbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RboxApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RboxApiApplication.class, args);
    }
}
