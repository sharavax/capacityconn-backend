package com.example.capacity_connect.repository;

import com.example.capacity_connect.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findByProgramId(Long programId);
    Optional<Enrollment> findByUserIdAndProgramId(Long userId, Long programId);
}
