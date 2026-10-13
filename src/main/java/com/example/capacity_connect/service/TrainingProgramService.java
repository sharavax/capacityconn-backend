package com.example.capacity_connect.service;

import com.example.capacity_connect.dto.TrainingProgramRequest;
import com.example.capacity_connect.dto.TrainingProgramResponse;

import java.util.List;

public interface TrainingProgramService {
    TrainingProgramResponse createTrainingProgram(TrainingProgramRequest request);
    List<TrainingProgramResponse> getAllTrainingPrograms();
    TrainingProgramResponse getTrainingProgramById(Long id);
    TrainingProgramResponse updateTrainingProgram(Long id, TrainingProgramRequest request);
    void deleteTrainingProgram(Long id);
}
