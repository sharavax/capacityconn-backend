package com.example.capacity_connect.controller;

import com.example.capacity_connect.model.Enrollment;
import com.example.capacity_connect.model.TrainingProgram;
import com.example.capacity_connect.model.User;
import com.example.capacity_connect.repository.EnrollmentRepository;
import com.example.capacity_connect.repository.TrainingProgramRepository;
import com.example.capacity_connect.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final TrainingProgramRepository programRepository;
    private final UserRepository userRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository,
                                TrainingProgramRepository programRepository,
                                UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.programRepository = programRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentRepository.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Enrollment>> getUserEnrollments(@PathVariable Long userId) {
        return ResponseEntity.ok(enrollmentRepository.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Enrollment> enrollUser(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long programId = Long.valueOf(payload.get("programId").toString());

        // Check if already enrolled
        Optional<Enrollment> existing = enrollmentRepository.findByUserIdAndProgramId(userId, programId);
        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }

        String userName = userRepository.findById(userId).map(User::getName).orElse("Student " + userId);
        String programTitle = programRepository.findById(programId).map(TrainingProgram::getTitle).orElse("Syllabus Plan " + programId);

        Enrollment enrollment = new Enrollment(
                null,
                userId,
                userName,
                programId,
                programTitle,
                LocalDate.now(),
                0,
                "ENROLLED",
                null
        );

        Enrollment saved = enrollmentRepository.save(enrollment);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<Enrollment> updateProgress(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        return enrollmentRepository.findById(id).map(enrollment -> {
            if (payload.containsKey("progressPercentage")) {
                int progress = Integer.parseInt(payload.get("progressPercentage").toString());
                enrollment.setProgressPercentage(progress);
                if (progress >= 100) {
                    enrollment.setStatus("COMPLETED");
                } else if (progress > 0) {
                    enrollment.setStatus("IN_PROGRESS");
                }
            }
            if (payload.containsKey("status")) {
                enrollment.setStatus(payload.get("status").toString());
            }
            if (payload.containsKey("score")) {
                enrollment.setScore(Double.parseDouble(payload.get("score").toString()));
            }
            return ResponseEntity.ok(enrollmentRepository.save(enrollment));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        if (!enrollmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        enrollmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
