package com.example.capacity_connect.dto;

import com.example.capacity_connect.model.TrainingProgram;
import java.time.LocalDate;

public class TrainingProgramResponse {

    private Long id;
    private String title;
    private String description;
    private String duration;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public TrainingProgramResponse() {
    }

    public TrainingProgramResponse(Long id, String title, String description, String duration, LocalDate startDate, LocalDate endDate, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public static TrainingProgramResponse fromEntity(TrainingProgram program) {
        if (program == null) {
            return null;
        }
        return new TrainingProgramResponse(
                program.getId(),
                program.getTitle(),
                program.getDescription(),
                program.getDuration(),
                program.getStartDate(),
                program.getEndDate(),
                program.getStatus()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
