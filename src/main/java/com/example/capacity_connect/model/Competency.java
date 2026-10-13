package com.example.capacity_connect.model;

import jakarta.persistence.*;

@Entity
@Table(name = "competencies")
public class Competency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String category; // e.g., Data Structures, Database Systems, Software Engineering

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer targetBenchmarkLevel; // 1 to 5 scale

    @Column(length = 100)
    private String applicableDepartment;

    public Competency() {
    }

    public Competency(Long id, String name, String category, String description, Integer targetBenchmarkLevel, String applicableDepartment) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.targetBenchmarkLevel = targetBenchmarkLevel;
        this.applicableDepartment = applicableDepartment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTargetBenchmarkLevel() {
        return targetBenchmarkLevel;
    }

    public void setTargetBenchmarkLevel(Integer targetBenchmarkLevel) {
        this.targetBenchmarkLevel = targetBenchmarkLevel;
    }

    public String getApplicableDepartment() {
        return applicableDepartment;
    }

    public void setApplicableDepartment(String applicableDepartment) {
        this.applicableDepartment = applicableDepartment;
    }
}
