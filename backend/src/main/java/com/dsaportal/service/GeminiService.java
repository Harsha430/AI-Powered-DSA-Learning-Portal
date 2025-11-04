package com.dsaportal.service;

import com.dsaportal.entity.Problem;
import com.dsaportal.entity.Submission;
import com.dsaportal.repository.ProblemRepository;
import com.dsaportal.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeminiService {
    
    @Autowired
    private ProblemRepository problemRepository;
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Value("${gemini.api-key}")
    private String geminiApiKey;
    
    @Value("${gemini.base-url}")
    private String geminiBaseUrl;
    
    private final WebClient webClient;
    
    public GeminiService() {
        this.webClient = WebClient.builder().build();
    }
    
    public List<Problem> getRecommendedProblems(Long userId) {
        try {
            // Get user's performance data
            Map<String, Double> accuracyByTopic = getUserAccuracyByTopic(userId);
            
            // Prepare prompt for Gemini
            String prompt = buildRecommendationPrompt(accuracyByTopic);
            
            // Call Gemini API
            String response = callGeminiAPI(prompt);
            
            // Parse response and get recommended problems
            return parseRecommendations(response, userId);
            
        } catch (Exception e) {
            System.err.println("Error getting recommendations from Gemini: " + e.getMessage());
            // Return some default problems if Gemini fails
            return problemRepository.findAll().stream()
                    .limit(5)
                    .collect(Collectors.toList());
        }
    }
    
    private Map<String, Double> getUserAccuracyByTopic(Long userId) {
        List<Object[]> results = submissionRepository.getAccuracyByTopicForUser(userId);
        Map<String, Double> accuracyByTopic = new HashMap<>();
        
        for (Object[] result : results) {
            String topic = (String) result[0];
            Double accuracy = (Double) result[1];
            accuracyByTopic.put(topic, accuracy);
        }
        
        return accuracyByTopic;
    }
    
    private String buildRecommendationPrompt(Map<String, Double> accuracyByTopic) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI tutor. A student has the following performance:\n\n");
        
        if (accuracyByTopic.isEmpty()) {
            prompt.append("This is a new student with no submission history.\n");
        } else {
            for (Map.Entry<String, Double> entry : accuracyByTopic.entrySet()) {
                prompt.append("Accuracy: ").append(String.format("%.1f", entry.getValue() * 100))
                      .append("% in ").append(entry.getKey()).append("\n");
            }
        }
        
        prompt.append("\nSuggest 3 problems by topic and difficulty to help them improve. ");
        prompt.append("Format your response as JSON with this structure:\n");
        prompt.append("{\n");
        prompt.append("  \"recommendations\": [\n");
        prompt.append("    {\"topic\": \"TOPIC_NAME\", \"difficulty\": \"EASY|MEDIUM|HARD\"},\n");
        prompt.append("    {\"topic\": \"TOPIC_NAME\", \"difficulty\": \"EASY|MEDIUM|HARD\"},\n");
        prompt.append("    {\"topic\": \"TOPIC_NAME\", \"difficulty\": \"EASY|MEDIUM|HARD\"}\n");
        prompt.append("  ]\n");
        prompt.append("}\n");
        
        return prompt.toString();
    }
    
    private String callGeminiAPI(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            Map<String, Object> part = new HashMap<>();
            
            part.put("text", prompt);
            content.put("parts", Arrays.asList(part));
            requestBody.put("contents", Arrays.asList(content));
            
            return webClient.post()
                    .uri(geminiBaseUrl + "/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
                    
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            return "{\"recommendations\": [{\"topic\": \"ARRAYS\", \"difficulty\": \"EASY\"}, {\"topic\": \"STRINGS\", \"difficulty\": \"EASY\"}, {\"topic\": \"TREES\", \"difficulty\": \"MEDIUM\"}]}";
        }
    }
    
    private List<Problem> parseRecommendations(String response, Long userId) {
        try {
            // Simple JSON parsing - in production, use a proper JSON library
            List<Problem> recommendations = new ArrayList<>();
            
            // Get solved problem IDs to avoid recommending already solved problems
            Set<Long> solvedProblemIds = submissionRepository.findByUserIdAndStatus(userId, Submission.Status.ACCEPTED)
                    .stream()
                    .map(s -> s.getProblem().getId())
                    .collect(Collectors.toSet());
            
            // Parse the response and find matching problems
            // This is a simplified parser - in production, use Jackson or Gson
            if (response.contains("ARRAYS")) {
                recommendations.addAll(problemRepository.findByTopic(Problem.Topic.ARRAYS)
                        .stream()
                        .filter(p -> !solvedProblemIds.contains(p.getId()))
                        .limit(1)
                        .collect(Collectors.toList()));
            }
            if (response.contains("STRINGS")) {
                recommendations.addAll(problemRepository.findByTopic(Problem.Topic.STRINGS)
                        .stream()
                        .filter(p -> !solvedProblemIds.contains(p.getId()))
                        .limit(1)
                        .collect(Collectors.toList()));
            }
            if (response.contains("TREES")) {
                recommendations.addAll(problemRepository.findByTopic(Problem.Topic.TREES)
                        .stream()
                        .filter(p -> !solvedProblemIds.contains(p.getId()))
                        .limit(1)
                        .collect(Collectors.toList()));
            }
            
            // If no recommendations found, return some default problems
            if (recommendations.isEmpty()) {
                recommendations = problemRepository.findAll()
                        .stream()
                        .filter(p -> !solvedProblemIds.contains(p.getId()))
                        .limit(3)
                        .collect(Collectors.toList());
            }
            
            return recommendations;
            
        } catch (Exception e) {
            System.err.println("Error parsing Gemini response: " + e.getMessage());
            return problemRepository.findAll().stream().limit(3).collect(Collectors.toList());
        }
    }
}
