package com.example.capacity_connect.model;

import jakarta.persistence.*;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long programId;

    @Column(nullable = false, length = 255)
    private String programTitle;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionsJson;

    @Column(nullable = false)
    private Integer passingScore; // e.g. 70

    public Assessment() {
    }

    public Assessment(Long id, Long programId, String programTitle, String title, String questionsJson, Integer passingScore) {
        this.id = id;
        this.programId = programId;
        this.programTitle = programTitle;
        this.title = title;
        this.questionsJson = questionsJson;
        this.passingScore = passingScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionsJson() {
        return questionsJson;
    }

    public void setQuestionsJson(String questionsJson) {
        this.questionsJson = questionsJson;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }
}
