package com.ensa.projet.trainingservice.service.interfaces;

import com.ensa.projet.trainingservice.model.dao.QuizDTO;
import com.ensa.projet.trainingservice.model.dao.TrainingDTO;

import java.util.List;

import com.ensa.projet.trainingservice.model.dao.TrainingDTO;
import com.ensa.projet.trainingservice.model.dao.ModuleDTO;
import java.util.List;

public interface TrainingService {
     TrainingDTO createTraining(TrainingDTO trainingDTO);
     TrainingDTO updateTraining(Integer id, TrainingDTO trainingDTO);
     void deleteTraining(Integer id);
     TrainingDTO getTrainingById(Integer id);
     List<TrainingDTO> getAllTrainings();
     List<TrainingDTO> getTrainingsByCategory(Integer categoryId);
     ModuleDTO addModule(Integer trainingId, ModuleDTO moduleDTO);
     void publishTraining(Integer id);
     void unpublishTraining(Integer id);
}
