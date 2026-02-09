package com.geunnseung.daengnyangrefactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DaengnyangRefactorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaengnyangRefactorApplication.class, args);
    }

}
