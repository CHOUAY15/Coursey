package com.ensa.projet.trainingservice.repository;
import com.ensa.projet.trainingservice.model.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
    List<Module> findByTrainingIdOrderByOrderIndexAsc(Integer trainingId);
}
