package com.example.capacity_connect.controller;

import com.example.capacity_connect.model.SemesterScore;
import com.example.capacity_connect.model.TrainingProgram;
import com.example.capacity_connect.model.User;
import com.example.capacity_connect.repository.SemesterScoreRepository;
import com.example.capacity_connect.repository.TrainingProgramRepository;
import com.example.capacity_connect.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class SemesterScoreController {

    private final SemesterScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final TrainingProgramRepository programRepository;

    public SemesterScoreController(SemesterScoreRepository scoreRepository,
                                   UserRepository userRepository,
                                   TrainingProgramRepository programRepository) {
        this.scoreRepository = scoreRepository;
        this.userRepository = userRepository;
        this.programRepository = programRepository;
    }

    @GetMapping
    public ResponseEntity<List<SemesterScore>> getAllScores() {
        return ResponseEntity.ok(scoreRepository.findAll());
    }

    @GetMapping("/student/{userId}")
    public ResponseEntity<Map<String, Object>> getStudentScoresReport(@PathVariable Long userId) {
        List<SemesterScore> scores = scoreRepository.findByUserId(userId);

        double totalCredits = 0.0;
        double totalWeightedPoints = 0.0;

        for (SemesterScore s : scores) {
            int cred = s.getCredits() != null ? s.getCredits() : 4;
            double gp = s.getGradePoints() != null ? s.getGradePoints() : 0.0;
            totalCredits += cred;
            totalWeightedPoints += (cred * gp);
        }

        double sgpa = totalCredits > 0 ? Math.round((totalWeightedPoints / totalCredits) * 100.0) / 100.0 : 0.0;

        Map<String, Object> report = new HashMap<>();
        report.put("userId", userId);
        report.put("scores", scores);
        report.put("totalCredits", totalCredits);
        report.put("sgpa", sgpa);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<SemesterScore>> getCourseScores(@PathVariable Long courseId) {
        return ResponseEntity.ok(scoreRepository.findByCourseId(courseId));
    }

    @PostMapping
    public ResponseEntity<SemesterScore> createScoreRecord(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long courseId = Long.valueOf(payload.get("courseId").toString());
        String semester = payload.getOrDefault("semester", "Semester 6").toString();
        Double internal1 = payload.containsKey("internal1") ? Double.valueOf(payload.get("internal1").toString()) : 16.0;
        Double internal2 = payload.containsKey("internal2") ? Double.valueOf(payload.get("internal2").toString()) : 17.5;
        Double assignment = payload.containsKey("assignmentScore") ? Double.valueOf(payload.get("assignmentScore").toString()) : 9.0;
        Double endSem = payload.containsKey("endSemScore") ? Double.valueOf(payload.get("endSemScore").toString()) : 42.0;
        Integer credits = payload.containsKey("credits") ? Integer.valueOf(payload.get("credits").toString()) : 4;

        String studentName = userRepository.findById(userId).map(User::getName).orElse("Student " + userId);
        String courseTitle = programRepository.findById(courseId).map(TrainingProgram::getTitle).orElse("Course " + courseId);

        SemesterScore record = scoreRepository.findByUserIdAndCourseId(userId, courseId)
                .orElse(new SemesterScore(null, userId, studentName, courseId, courseTitle, semester, internal1, internal2, assignment, endSem, credits));

        record.setInternal1(internal1);
        record.setInternal2(internal2);
        record.setAssignmentScore(assignment);
        record.setEndSemScore(endSem);
        record.setCredits(credits);
        record.recalculateGrade();

        SemesterScore saved = scoreRepository.save(record);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SemesterScore> updateScore(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        return scoreRepository.findById(id).map(record -> {
            if (payload.containsKey("internal1")) {
                record.setInternal1(Double.valueOf(payload.get("internal1").toString()));
            }
            if (payload.containsKey("internal2")) {
                record.setInternal2(Double.valueOf(payload.get("internal2").toString()));
            }
            if (payload.containsKey("assignmentScore")) {
                record.setAssignmentScore(Double.valueOf(payload.get("assignmentScore").toString()));
            }
            if (payload.containsKey("endSemScore")) {
                record.setEndSemScore(Double.valueOf(payload.get("endSemScore").toString()));
            }
            if (payload.containsKey("credits")) {
                record.setCredits(Integer.valueOf(payload.get("credits").toString()));
            }
            record.recalculateGrade();
            return ResponseEntity.ok(scoreRepository.save(record));
        }).orElse(ResponseEntity.notFound().build());
    }
}
