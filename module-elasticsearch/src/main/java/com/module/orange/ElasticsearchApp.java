package com.module.orange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.module"})
public class ElasticsearchApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ElasticsearchApp.class, args);
    }
}
