package com.ecommerence.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@EnableWebMvc
@EnableScheduling
@EnableEurekaClient
@SpringBootApplication
public class ECommercePlatformApplication {


    public static void main(String[] args) {
        SpringApplication.run(ECommercePlatformApplication.class, args);
    }
}
