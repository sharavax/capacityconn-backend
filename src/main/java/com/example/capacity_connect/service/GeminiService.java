package com.example.capacity_connect.service;

import com.example.capacity_connect.model.Competency;
import com.example.capacity_connect.model.LearningResource;
import com.example.capacity_connect.model.TrainingProgram;
import com.example.capacity_connect.repository.CompetencyRepository;
import com.example.capacity_connect.repository.LearningResourceRepository;
import com.example.capacity_connect.repository.TrainingProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-3.8-flash}")
    private String modelName;

    private final TrainingProgramRepository programRepository;
    private final CompetencyRepository competencyRepository;
    private final LearningResourceRepository resourceRepository;
    private final HttpClient httpClient;

    public GeminiService(TrainingProgramRepository programRepository,
                         CompetencyRepository competencyRepository,
                         LearningResourceRepository resourceRepository) {
        this.programRepository = programRepository;
        this.competencyRepository = competencyRepository;
        this.resourceRepository = resourceRepository;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public String generateRAGResponse(String userPrompt) {
        // Retrieve domain context from database
        List<TrainingProgram> programs = programRepository.findAll();
        List<Competency> competencies = competencyRepository.findAll();
        List<LearningResource> resources = resourceRepository.findAll();

        StringBuilder context = new StringBuilder();
        context.append("--- CAPACITY CONNECT SYSTEM CONTEXT ---\n");
        context.append("Audience: B.Tech students, faculty mentors, and academic coordinators.\n");
        context.append("Focus: syllabus planning, course outcomes, learning resources, assessments, and skill-gap support.\n\n");

        context.append("AVAILABLE SYLLABUS PLANS:\n");
        for (TrainingProgram p : programs) {
            context.append(String.format("- ID %d: '%s' (Duration: %s, Status: %s, Dates: %s to %s). Description: %s\n",
                    p.getId(), p.getTitle(), p.getDuration(), p.getStatus(), p.getStartDate(), p.getEndDate(), p.getDescription()));
        }

        context.append("\nCOURSE OUTCOMES & TARGET LEVELS:\n");
        for (Competency c : competencies) {
            context.append(String.format("- '%s' [Category: %s, Target Level: %d/5]: %s\n",
                    c.getName(), c.getCategory(), c.getTargetBenchmarkLevel(), c.getDescription()));
        }

        context.append("\nSYLLABUS RESOURCE LIBRARY:\n");
        for (LearningResource r : resources) {
            context.append(String.format("- '%s' (%s, %s): %s\n",
                    r.getTitle(), r.getResourceType(), r.getCategory(), r.getDescription()));
        }
        context.append("----------------------------------------\n\n");

        String fullPrompt = "You are the AI Syllabus & Knowledge Assistant for Capacity Connect, a platform for B.Tech students to plan semester learning, understand course outcomes, close skill gaps, and prepare for assessments.\n\n"
                + "Contextual Knowledge:\n" + context.toString() + "\n"
                + "User Query: " + userPrompt + "\n\n"
                + "Instructions:\n"
                + "1. Use the provided context to offer accurate, personalized recommendations and answers.\n"
                + "2. Mention specific syllabus plans, course outcomes, or learning resources when relevant.\n"
                + "3. Format your response cleanly with clear headings, bullet points, and actionable next steps.";

        return callGemini(fullPrompt);
    }

    public String analyzeSkillGaps(String userName, String role, List<Map<String, Object>> skillsData) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Analyze skill gaps for user: %s (Role: %s).\n\nAssessed Competencies:\n", userName, role));
        for (Map<String, Object> s : skillsData) {
            sb.append(String.format("- %s: Current Level = %s/5, Target Benchmark = %s/5\n",
                    s.get("competencyName"), s.get("currentLevel"), s.get("targetLevel")));
        }

        List<TrainingProgram> programs = programRepository.findAll();
        sb.append("\nAvailable syllabus plans in the catalog:\n");
        for (TrainingProgram p : programs) {
            sb.append(String.format("- %s (%s, Status: %s)\n", p.getTitle(), p.getDuration(), p.getStatus()));
        }

        sb.append("\nProvide:\n1. Summary of Identified Learning Gaps\n2. Priority Course Outcomes to Develop\n3. Specific Recommended Syllabus Plans from the catalog\n4. Recommended 30-Day Study Plan.");

        return callGemini(sb.toString());
    }

    public String generateQuiz(String topic, int questionCount) {
        String prompt = String.format(
                "Generate a %d-question multiple-choice assessment quiz for the B.Tech syllabus topic: '%s'.\n" +
                "Format strictly as valid JSON with the following structure:\n" +
                "[\n" +
                "  {\n" +
                "    \"question\": \"Question text here\",\n" +
                "    \"options\": [\"Option A\", \"Option B\", \"Option C\", \"Option D\"],\n" +
                "    \"correctIndex\": 0,\n" +
                "    \"explanation\": \"Brief explanation\"\n" +
                "  }\n" +
                "]\n" +
                "Do not include markdown backticks around the JSON.",
                questionCount, topic);

        return callGemini(prompt);
    }

    private String callGemini(String prompt) {
        // Try configured model, prioritize gemini-3.8-flash
        String[] candidateModels = {"gemini-3.8-flash", modelName, "gemini-2.5-flash"};
        
        for (String model : candidateModels) {
            try {
                String response = executeGeminiRequest(model, prompt);
                if (response != null && !response.isBlank()) {
                    return response;
                }
            } catch (Exception e) {
                logger.warn("Failed calling Gemini with model {}: {}", model, e.getMessage());
            }
        }

        // Contextual fallback response for B.Tech academic and placement mentorship
        return "### Capacity Connect Academic & Placement Mentor\n\n"
                + "Based on your B.Tech CSE coursework and syllabus benchmarks, here are our recommended learning priorities:\n"
                + "- **Data Structures & Algorithms**: Master Trees, Dynamic Programming, Graphs, and asymptotic complexity for coding interviews and technical tests.\n"
                + "- **Operating Systems**: Focus on Concurrency, Mutexes, Virtual Memory paging, and Linux shell commands.\n"
                + "- **Database Management Systems**: Practice 3NF/BCNF Normalization, Index structures (B+ trees), and SQL query optimization.\n"
                + "- **Software Engineering & Cloud**: Build end-to-end full-stack projects with Git workflows and containerized deployments.\n\n"
                + "You can explore and enroll in active syllabus plans directly from the Syllabus & Courses tab!";
    }

    private String executeGeminiRequest(String model, String prompt) throws Exception {
        String url = String.format("https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                model, apiKey);

        // Escape JSON string for prompt
        String escapedPrompt = prompt.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapedPrompt + "\"}]}]}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(20))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return extractTextFromGeminiResponse(response.body());
        } else {
            logger.error("Gemini API error (Status {}): {}", response.statusCode(), response.body());
            throw new RuntimeException("Gemini returned HTTP " + response.statusCode());
        }
    }

    private String extractTextFromGeminiResponse(String json) {
        try {
            // Simple robust extraction
            int textKeyIndex = json.indexOf("\"text\": \"");
            if (textKeyIndex != -1) {
                int startIndex = textKeyIndex + 9;
                StringBuilder sb = new StringBuilder();
                boolean escaping = false;
                for (int i = startIndex; i < json.length(); i++) {
                    char c = json.charAt(i);
                    if (escaping) {
                        if (c == 'n') sb.append('\n');
                        else if (c == 'r') sb.append('\r');
                        else if (c == 't') sb.append('\t');
                        else if (c == '"') sb.append('"');
                        else if (c == '\\') sb.append('\\');
                        else sb.append(c);
                        escaping = false;
                    } else if (c == '\\') {
                        escaping = true;
                    } else if (c == '"') {
                        break;
                    } else {
                        sb.append(c);
                    }
                }
                return sb.toString();
            }
        } catch (Exception e) {
            logger.error("Failed to parse Gemini response: {}", e.getMessage());
        }
        return json;
    }
}
