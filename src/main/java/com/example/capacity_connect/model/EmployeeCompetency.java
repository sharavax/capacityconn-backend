package com.example.capacity_connect.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee_competencies")
public class EmployeeCompetency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long competencyId;

    @Column(nullable = false)
    private Integer currentLevel; // 1 to 5 scale

    @Column(nullable = false)
    private Integer targetLevel; // 1 to 5 scale

    private LocalDate lastAssessedDate;

    public EmployeeCompetency() {
    }

    public EmployeeCompetency(Long id, Long userId, Long competencyId, Integer currentLevel, Integer targetLevel, LocalDate lastAssessedDate) {
        this.id = id;
        this.userId = userId;
        this.competencyId = competencyId;
        this.currentLevel = currentLevel;
        this.targetLevel = targetLevel;
        this.lastAssessedDate = lastAssessedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCompetencyId() {
        return competencyId;
    }

    public void setCompetencyId(Long competencyId) {
        this.competencyId = competencyId;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(Integer targetLevel) {
        this.targetLevel = targetLevel;
    }

    public LocalDate getLastAssessedDate() {
        return lastAssessedDate;
    }

    public void setLastAssessedDate(LocalDate lastAssessedDate) {
        this.lastAssessedDate = lastAssessedDate;
    }

    public int getSkillGap() {
        return Math.max(0, (targetLevel != null ? targetLevel : 0) - (currentLevel != null ? currentLevel : 0));
    }
}
