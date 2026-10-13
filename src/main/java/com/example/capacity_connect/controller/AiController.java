package com.example.capacity_connect.controller;

import com.example.capacity_connect.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    private final GeminiService geminiService;

    public AiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chatWithLearningAssistant(@RequestBody Map<String, String> request) {
        String prompt = request.getOrDefault("prompt", "Hello");
        String response = geminiService.generateRAGResponse(prompt);
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/skill-gap-analysis")
    public ResponseEntity<Map<String, String>> analyzeSkillGaps(@RequestBody Map<String, Object> request) {
        String userName = (String) request.getOrDefault("userName", "Student");
        String role = (String) request.getOrDefault("role", "B.Tech Student");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> skillsData = (List<Map<String, Object>>) request.get("skills");

        String analysis = geminiService.analyzeSkillGaps(userName, role, skillsData != null ? skillsData : List.of());
        Map<String, String> result = new HashMap<>();
        result.put("analysis", analysis);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/generate-quiz")
    public ResponseEntity<Map<String, String>> generateQuiz(@RequestBody Map<String, Object> request) {
        String topic = (String) request.getOrDefault("topic", "Data Structures Basics");
        int count = request.containsKey("count") ? Integer.parseInt(request.get("count").toString()) : 4;

        String quizJson = geminiService.generateQuiz(topic, count);
        Map<String, String> result = new HashMap<>();
        result.put("quiz", quizJson);
        return ResponseEntity.ok(result);
    }
}
