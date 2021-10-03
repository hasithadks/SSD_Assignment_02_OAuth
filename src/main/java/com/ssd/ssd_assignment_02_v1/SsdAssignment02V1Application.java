package com.ssd.ssd_assignment_02_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:WebContentInterceptor.xml")
public class SsdAssignment02V1Application {

    public static void main(String[] args) {
        SpringApplication.run(SsdAssignment02V1Application.class, args);
    }

}
