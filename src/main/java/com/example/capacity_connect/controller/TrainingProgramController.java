package com.example.capacity_connect.controller;

import com.example.capacity_connect.dto.TrainingProgramRequest;
import com.example.capacity_connect.dto.TrainingProgramResponse;
import com.example.capacity_connect.service.TrainingProgramService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/training-programs")
@CrossOrigin(origins = "*")
public class TrainingProgramController {

    private final TrainingProgramService trainingProgramService;

    public TrainingProgramController(TrainingProgramService trainingProgramService) {
        this.trainingProgramService = trainingProgramService;
    }

    @PostMapping
    public ResponseEntity<TrainingProgramResponse> createTrainingProgram(
            @Valid @RequestBody TrainingProgramRequest request) {
        TrainingProgramResponse response = trainingProgramService.createTrainingProgram(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TrainingProgramResponse>> getAllTrainingPrograms() {
        List<TrainingProgramResponse> responseList = trainingProgramService.getAllTrainingPrograms();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingProgramResponse> getTrainingProgramById(@PathVariable Long id) {
        TrainingProgramResponse response = trainingProgramService.getTrainingProgramById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingProgramResponse> updateTrainingProgram(
            @PathVariable Long id,
            @Valid @RequestBody TrainingProgramRequest request) {
        TrainingProgramResponse response = trainingProgramService.updateTrainingProgram(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingProgram(@PathVariable Long id) {
        trainingProgramService.deleteTrainingProgram(id);
        return ResponseEntity.noContent().build();
    }
}
