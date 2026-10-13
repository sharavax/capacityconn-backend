package com.example.capacity_connect.repository;

import com.example.capacity_connect.model.LearningResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, Long> {
    List<LearningResource> findByCategoryIgnoreCase(String category);
    List<LearningResource> findByProgramId(Long programId);
}
