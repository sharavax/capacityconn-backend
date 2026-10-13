package com.example.capacity_connect.controller;

import com.example.capacity_connect.model.LearningResource;
import com.example.capacity_connect.repository.LearningResourceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-resources")
@CrossOrigin(origins = "*")
public class LearningResourceController {

    private final LearningResourceRepository repository;

    public LearningResourceController(LearningResourceRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<LearningResource>> getAllResources() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningResource> getResourceById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LearningResource> createResource(@RequestBody LearningResource resource) {
        LearningResource saved = repository.save(resource);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningResource> updateResource(@PathVariable Long id, @RequestBody LearningResource details) {
        return repository.findById(id).map(r -> {
            r.setTitle(details.getTitle());
            r.setCategory(details.getCategory());
            r.setResourceType(details.getResourceType());
            r.setDescription(details.getDescription());
            r.setContentOrUrl(details.getContentOrUrl());
            r.setTags(details.getTags());
            r.setProgramId(details.getProgramId());
            return ResponseEntity.ok(repository.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
