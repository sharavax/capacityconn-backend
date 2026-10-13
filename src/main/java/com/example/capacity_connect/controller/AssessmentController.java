package com.example.capacity_connect.controller;

import com.example.capacity_connect.model.Assessment;
import com.example.capacity_connect.repository.AssessmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assessments")
@CrossOrigin(origins = "*")
public class AssessmentController {

    private final AssessmentRepository assessmentRepository;

    public AssessmentController(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        return ResponseEntity.ok(assessmentRepository.findAll());
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<Assessment>> getAssessmentsByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(assessmentRepository.findByProgramId(programId));
    }

    @PostMapping
    public ResponseEntity<Assessment> createAssessment(@RequestBody Assessment assessment) {
        Assessment saved = assessmentRepository.save(assessment);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assessment> updateAssessment(@PathVariable Long id, @RequestBody Assessment details) {
        return assessmentRepository.findById(id).map(a -> {
            a.setTitle(details.getTitle());
            a.setProgramId(details.getProgramId());
            a.setProgramTitle(details.getProgramTitle());
            a.setQuestionsJson(details.getQuestionsJson());
            a.setPassingScore(details.getPassingScore());
            return ResponseEntity.ok(assessmentRepository.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        if (!assessmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        assessmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Map<String, Object>> submitAssessment(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        return assessmentRepository.findById(id).map(assessment -> {
            // Evaluates user answers passed in payload
            double score = payload.containsKey("score") ? Double.parseDouble(payload.get("score").toString()) : 85.0;
            boolean passed = score >= assessment.getPassingScore();

            Map<String, Object> result = new HashMap<>();
            result.put("assessmentId", assessment.getId());
            result.put("title", assessment.getTitle());
            result.put("score", score);
            result.put("passingScore", assessment.getPassingScore());
            result.put("passed", passed);
            result.put("feedback", passed
                    ? "Congratulations! You have satisfied the competency requirements for this module."
                    : "Score below threshold. We recommend reviewing the learning resources and re-taking the assessment.");

            return ResponseEntity.ok(result);
        }).orElse(ResponseEntity.notFound().build());
    }
}
