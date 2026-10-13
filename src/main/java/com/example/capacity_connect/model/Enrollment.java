package com.example.capacity_connect.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String userName;

    @Column(nullable = false)
    private Long programId;

    @Column(nullable = false, length = 255)
    private String programTitle;

    @Column(nullable = false)
    private LocalDate enrolledDate;

    @Column(nullable = false)
    private Integer progressPercentage; // 0 - 100

    @Column(nullable = false, length = 50)
    private String status; // ENROLLED, IN_PROGRESS, COMPLETED

    private Double score;

    public Enrollment() {
    }

    public Enrollment(Long id, Long userId, String userName, Long programId, String programTitle, LocalDate enrolledDate, Integer progressPercentage, String status, Double score) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.programId = programId;
        this.programTitle = programTitle;
        this.enrolledDate = enrolledDate;
        this.progressPercentage = progressPercentage;
        this.status = status;
        this.score = score;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
