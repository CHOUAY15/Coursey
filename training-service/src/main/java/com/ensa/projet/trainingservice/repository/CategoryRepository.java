package com.ensa.projet.trainingservice.repository;
import com.ensa.projet.trainingservice.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAll();
}