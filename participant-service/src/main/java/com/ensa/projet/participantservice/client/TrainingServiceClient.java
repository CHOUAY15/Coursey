package com.ensa.projet.participantservice.client;

import com.ensa.projet.participantservice.dto.TrainingDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name="training-service")
public interface TrainingServiceClient {



    @CircuitBreaker(name = "trainingServiceCB", fallbackMethod = "getDefaultTraining")
    @GetMapping("/api/trainings/{id}")
    TrainingDTO getTraining(@PathVariable Integer id);


    default TrainingDTO getDefaultTraining(Integer id, Exception e) {
        return  TrainingDTO.builder()
                .id(id)
                .title("default title")
                .description("default description")
                .build();
    }


}
