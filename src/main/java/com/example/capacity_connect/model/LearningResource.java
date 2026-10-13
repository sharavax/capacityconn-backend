package com.example.capacity_connect.model;

import jakarta.persistence.*;

@Entity
@Table(name = "learning_resources")
public class LearningResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String category; // e.g. Programming, Operating Systems, Data Science & AI

    @Column(nullable = false, length = 50)
    private String resourceType; // DOCUMENT, VIDEO, HANDBOOK, RESEARCH_PAPER, PROTOCOL

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String contentOrUrl;

    @Column(length = 255)
    private String tags;

    private Long programId;

    public LearningResource() {
    }

    public LearningResource(Long id, String title, String category, String resourceType, String description, String contentOrUrl, String tags, Long programId) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.resourceType = resourceType;
        this.description = description;
        this.contentOrUrl = contentOrUrl;
        this.tags = tags;
        this.programId = programId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentOrUrl() {
        return contentOrUrl;
    }

    public void setContentOrUrl(String contentOrUrl) {
        this.contentOrUrl = contentOrUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }
}
