package com.mycompany.ecoserverweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.mycompany")
@EntityScan("com.mycompany.ecoserverpersistence.models")
@EnableJpaRepositories("com.mycompany.ecoserverpersistence.repositories")
public class EcoServerApplication {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(EcoServerApplication.class, args);
    }
}
