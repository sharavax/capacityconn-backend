package com.example.capacity_connect.repository;

import com.example.capacity_connect.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByUserId(Long userId);
    List<AttendanceRecord> findByCourseId(Long courseId);
    Optional<AttendanceRecord> findByUserIdAndCourseId(Long userId, Long courseId);
}
