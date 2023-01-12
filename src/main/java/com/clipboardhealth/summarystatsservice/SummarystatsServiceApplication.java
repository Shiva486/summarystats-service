package com.clipboardhealth.summarystatsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com.clipboardhealth.summarystatsservice" })
@EnableJpaRepositories(basePackages = "com.clipboardhealth.summarystatsservice.repository")
public class SummarystatsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SummarystatsServiceApplication.class, args);
    }

}
