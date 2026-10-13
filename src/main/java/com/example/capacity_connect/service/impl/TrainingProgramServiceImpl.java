package com.example.capacity_connect.service.impl;

import com.example.capacity_connect.dto.TrainingProgramRequest;
import com.example.capacity_connect.dto.TrainingProgramResponse;
import com.example.capacity_connect.exception.ResourceNotFoundException;
import com.example.capacity_connect.model.TrainingProgram;
import com.example.capacity_connect.repository.TrainingProgramRepository;
import com.example.capacity_connect.service.TrainingProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrainingProgramServiceImpl implements TrainingProgramService {

    private final TrainingProgramRepository repository;

    public TrainingProgramServiceImpl(TrainingProgramRepository repository) {
        this.repository = repository;
    }

    @Override
    public TrainingProgramResponse createTrainingProgram(TrainingProgramRequest request) {
        validateDates(request);

        TrainingProgram program = new TrainingProgram();
        mapRequestToEntity(request, program);

        TrainingProgram savedProgram = repository.save(program);
        return TrainingProgramResponse.fromEntity(savedProgram);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingProgramResponse> getAllTrainingPrograms() {
        return repository.findAll()
                .stream()
                .map(TrainingProgramResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingProgramResponse getTrainingProgramById(Long id) {
        TrainingProgram program = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram", "id", id));
        return TrainingProgramResponse.fromEntity(program);
    }

    @Override
    public TrainingProgramResponse updateTrainingProgram(Long id, TrainingProgramRequest request) {
        validateDates(request);

        TrainingProgram program = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram", "id", id));

        mapRequestToEntity(request, program);

        TrainingProgram updatedProgram = repository.save(program);
        return TrainingProgramResponse.fromEntity(updatedProgram);
    }

    @Override
    public void deleteTrainingProgram(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TrainingProgram", "id", id);
        }
        repository.deleteById(id);
    }

    private void validateDates(TrainingProgramRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be earlier than start date");
            }
        }
    }

    private void mapRequestToEntity(TrainingProgramRequest request, TrainingProgram program) {
        program.setTitle(request.getTitle().trim());
        program.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        program.setDuration(request.getDuration().trim());
        program.setStartDate(request.getStartDate());
        program.setEndDate(request.getEndDate());
        program.setStatus(request.getStatus().trim().toUpperCase());
    }
}
