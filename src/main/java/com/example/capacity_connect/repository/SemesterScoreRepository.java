package com.example.capacity_connect.repository;

import com.example.capacity_connect.model.SemesterScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterScoreRepository extends JpaRepository<SemesterScore, Long> {
    List<SemesterScore> findByUserId(Long userId);
    List<SemesterScore> findByUserIdAndSemester(Long userId, String semester);
    List<SemesterScore> findByCourseId(Long courseId);
    Optional<SemesterScore> findByUserIdAndCourseId(Long userId, Long courseId);
}
