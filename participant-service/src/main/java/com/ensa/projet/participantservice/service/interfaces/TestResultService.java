package com.ensa.projet.participantservice.service.interfaces;

import com.ensa.projet.participantservice.entities.TestResult;
import com.ensa.projet.participantservice.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TestResultService {


    // Récupérer les résultats de tests par participant ID
    List<TestResult> getTestResultsByParticipantId(Integer participantId) ;


}
