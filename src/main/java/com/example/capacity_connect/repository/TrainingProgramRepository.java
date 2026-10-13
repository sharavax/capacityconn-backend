package com.example.capacity_connect.repository;

import com.example.capacity_connect.model.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    List<TrainingProgram> findByStatusIgnoreCase(String status);
}
