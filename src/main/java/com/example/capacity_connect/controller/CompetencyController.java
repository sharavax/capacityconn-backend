package com.example.capacity_connect.controller;

import com.example.capacity_connect.model.Competency;
import com.example.capacity_connect.model.EmployeeCompetency;
import com.example.capacity_connect.repository.CompetencyRepository;
import com.example.capacity_connect.repository.EmployeeCompetencyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/competencies")
@CrossOrigin(origins = "*")
public class CompetencyController {

    private final CompetencyRepository competencyRepository;
    private final EmployeeCompetencyRepository employeeCompetencyRepository;

    public CompetencyController(CompetencyRepository competencyRepository,
                                EmployeeCompetencyRepository employeeCompetencyRepository) {
        this.competencyRepository = competencyRepository;
        this.employeeCompetencyRepository = employeeCompetencyRepository;
    }

    @GetMapping
    public ResponseEntity<List<Competency>> getAllCompetencies() {
        return ResponseEntity.ok(competencyRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Competency> createCompetency(@RequestBody Competency competency) {
        Competency saved = competencyRepository.save(competency);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Competency> updateCompetency(@PathVariable Long id, @RequestBody Competency details) {
        return competencyRepository.findById(id).map(comp -> {
            comp.setName(details.getName());
            comp.setCategory(details.getCategory());
            comp.setDescription(details.getDescription());
            comp.setTargetBenchmarkLevel(details.getTargetBenchmarkLevel());
            comp.setApplicableDepartment(details.getApplicableDepartment());
            return ResponseEntity.ok(competencyRepository.save(comp));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetency(@PathVariable Long id) {
        if (!competencyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        competencyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserCompetencies(@PathVariable Long userId) {
        List<Competency> allCompetencies = competencyRepository.findAll();
        List<EmployeeCompetency> userAssessed = employeeCompetencyRepository.findByUserId(userId);
        Map<Long, EmployeeCompetency> assessedMap = new HashMap<>();
        for (EmployeeCompetency ec : userAssessed) {
            assessedMap.put(ec.getCompetencyId(), ec);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Competency c : allCompetencies) {
            Map<String, Object> item = new HashMap<>();
            item.put("competencyId", c.getId());
            item.put("competencyName", c.getName());
            item.put("category", c.getCategory());
            item.put("description", c.getDescription());
            item.put("targetBenchmarkLevel", c.getTargetBenchmarkLevel());
            
            EmployeeCompetency assessed = assessedMap.get(c.getId());
            int current = assessed != null && assessed.getCurrentLevel() != null ? assessed.getCurrentLevel() : 1;
            int target = assessed != null && assessed.getTargetLevel() != null ? assessed.getTargetLevel() : c.getTargetBenchmarkLevel();
            int gap = Math.max(0, target - current);

            item.put("currentLevel", current);
            item.put("targetLevel", target);
            item.put("skillGap", gap);
            item.put("lastAssessedDate", assessed != null ? assessed.getLastAssessedDate() : null);
            response.add(item);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{userId}/assess")
    public ResponseEntity<EmployeeCompetency> assessCompetency(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> payload) {
        Long competencyId = Long.valueOf(payload.get("competencyId").toString());
        Integer currentLevel = Integer.valueOf(payload.get("currentLevel").toString());
        Integer targetLevel = payload.containsKey("targetLevel")
                ? Integer.valueOf(payload.get("targetLevel").toString())
                : 5;

        EmployeeCompetency record = employeeCompetencyRepository
                .findByUserIdAndCompetencyId(userId, competencyId)
                .orElse(new EmployeeCompetency());

        record.setUserId(userId);
        record.setCompetencyId(competencyId);
        record.setCurrentLevel(currentLevel);
        record.setTargetLevel(targetLevel);
        record.setLastAssessedDate(LocalDate.now());

        EmployeeCompetency saved = employeeCompetencyRepository.save(record);
        return ResponseEntity.ok(saved);
    }
}
