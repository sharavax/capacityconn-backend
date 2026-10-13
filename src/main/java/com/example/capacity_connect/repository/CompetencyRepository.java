package com.example.capacity_connect.repository;

import com.example.capacity_connect.model.Competency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetencyRepository extends JpaRepository<Competency, Long> {
    List<Competency> findByCategoryIgnoreCase(String category);
}
