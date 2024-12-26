package com.ensa.projet.participantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class ParticipantServiceApplication {
    //microservice 1

    public static void main(String[] args) {
        SpringApplication.run(ParticipantServiceApplication.class, args);
    }

}
