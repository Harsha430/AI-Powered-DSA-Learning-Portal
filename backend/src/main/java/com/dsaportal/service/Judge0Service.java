package com.dsaportal.service;

import com.dsaportal.entity.Submission;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class Judge0Service {
    
    @Value("${judge0.base-url}")
    private String judge0BaseUrl;
    
    @Value("${judge0.api-key}")
    private String judge0ApiKey;
    
    @Value("${judge0.host}")
    private String judge0Host;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public Judge0Service() {
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }
    
    public String submitCode(Submission submission) {
        try {
            System.out.println("Submitting code to Judge0...");
            System.out.println("Judge0 URL: " + judge0BaseUrl);
            System.out.println("Language: " + submission.getLanguage());
            System.out.println("Code: " + submission.getCode().substring(0, Math.min(100, submission.getCode().length())));
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("source_code", submission.getCode());
            requestBody.put("language_id", getLanguageId(submission.getLanguage()));
            
            String testInput = getTestInput(submission);
            String expectedOutput = getExpectedOutput(submission);
            
            // Only add stdin and expected_output if we have test cases
            if (!testInput.isEmpty()) {
                requestBody.put("stdin", testInput);
            }
            if (!expectedOutput.isEmpty()) {
                requestBody.put("expected_output", expectedOutput);
            }
            
            requestBody.put("cpu_time_limit", 2.0); // 2 seconds default
            requestBody.put("memory_limit", 128000); // 128MB default
            
            System.out.println("Request body: " + requestBody);
            
            String response = webClient.post()
                    .uri(judge0BaseUrl + "/submissions")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header("X-RapidAPI-Key", judge0ApiKey)
                    .header("X-RapidAPI-Host", judge0Host)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            System.out.println("Judge0 response: " + response);
            
            // Parse response to get token
            return extractTokenFromResponse(response);
            
        } catch (Exception e) {
            System.err.println("Error submitting code to Judge0: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public Submission.Status getSubmissionResult(String token) {
        try {
            System.out.println("Getting submission result for token: " + token);
            
            String response = webClient.get()
                    .uri(judge0BaseUrl + "/submissions/" + token)
                    .header("X-RapidAPI-Key", judge0ApiKey)
                    .header("X-RapidAPI-Host", judge0Host)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            System.out.println("Judge0 result response: " + response);
            
            return parseSubmissionStatus(response);
            
        } catch (Exception e) {
            System.err.println("Error getting submission result from Judge0: " + e.getMessage());
            e.printStackTrace();
            return Submission.Status.RUNTIME_ERROR;
        }
    }
    
    public Submission.Status getSubmissionResultWithPolling(String token) {
        int maxAttempts = 10;
        int attempt = 0;
        
        while (attempt < maxAttempts) {
            try {
                Submission.Status status = getSubmissionResult(token);
                
                // If status is not pending, return it
                if (status != Submission.Status.PENDING) {
                    return status;
                }
                
                // Wait 2 seconds before next attempt
                Thread.sleep(2000);
                attempt++;
                
            } catch (Exception e) {
                System.err.println("Error in polling attempt " + attempt + ": " + e.getMessage());
                attempt++;
            }
        }
        
        // If max attempts reached, return runtime error
        return Submission.Status.RUNTIME_ERROR;
    }
    
    private int getLanguageId(Submission.Language language) {
        switch (language) {
            case PYTHON: return 71; // Python 3
            case JAVA: return 62; // Java
            case CPP: return 54; // C++ (GCC 9.2.0)
            case JAVASCRIPT: return 63; // Node.js 12.14.0
            case C: return 50; // C (GCC 9.2.0)
            default: return 71; // Default to Python
        }
    }
    
    private String getTestInput(Submission submission) {
        // Get the first test case input
        if (submission.getProblem().getTestCases() != null && !submission.getProblem().getTestCases().isEmpty()) {
            String input = submission.getProblem().getTestCases().get(0).getInputData();
            System.out.println("Test input: " + input);
            return input != null ? input : "";
        }
        System.out.println("No test cases found, using empty input");
        return "";
    }
    
    private String getExpectedOutput(Submission submission) {
        // Get the first test case expected output
        if (submission.getProblem().getTestCases() != null && !submission.getProblem().getTestCases().isEmpty()) {
            String output = submission.getProblem().getTestCases().get(0).getExpectedOutput();
            System.out.println("Expected output: " + output);
            return output != null ? output : "";
        }
        System.out.println("No test cases found, using empty expected output");
        return "";
    }
    
    private String extractTokenFromResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.has("token")) {
                return jsonNode.get("token").asText();
            }
        } catch (Exception e) {
            System.err.println("Error parsing token from response: " + e.getMessage());
        }
        return null;
    }
    
    private Submission.Status parseSubmissionStatus(String response) {
        try {
            System.out.println("Parsing status from response: " + response);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.has("status")) {
                JsonNode statusNode = jsonNode.get("status");
                if (statusNode.has("id")) {
                    int statusId = statusNode.get("id").asInt();
                    String statusDescription = statusNode.has("description") ? statusNode.get("description").asText() : "Unknown";
                    System.out.println("Judge0 status ID: " + statusId + ", Description: " + statusDescription);
                    
                    switch (statusId) {
                        case 1: // In Queue
                        case 2: // Processing
                            return Submission.Status.PENDING;
                        case 3: // Accepted
                            return Submission.Status.ACCEPTED;
                        case 4: // Wrong Answer
                            return Submission.Status.WRONG_ANSWER;
                        case 5: // Time Limit Exceeded
                            return Submission.Status.TIME_LIMIT_EXCEEDED;
                        case 6: // Memory Limit Exceeded
                            return Submission.Status.MEMORY_LIMIT_EXCEEDED;
                        case 7: // Output Limit Exceeded
                            return Submission.Status.RUNTIME_ERROR;
                        case 8: // Presentation Error
                            return Submission.Status.WRONG_ANSWER;
                        case 9: // Runtime Error
                            return Submission.Status.RUNTIME_ERROR;
                        case 10: // Compilation Error
                            return Submission.Status.COMPILATION_ERROR;
                        case 11: // Runtime Error (SIGSEGV)
                            return Submission.Status.RUNTIME_ERROR;
                        case 12: // Runtime Error (SIGFPE)
                            return Submission.Status.RUNTIME_ERROR;
                        case 13: // Runtime Error (SIGABRT)
                            return Submission.Status.RUNTIME_ERROR;
                        case 14: // Runtime Error (NZEC)
                            return Submission.Status.RUNTIME_ERROR;
                        case 15: // Runtime Error (Other)
                            return Submission.Status.RUNTIME_ERROR;
                        default: 
                            System.out.println("Unknown status ID: " + statusId);
                            return Submission.Status.RUNTIME_ERROR;
                    }
                }
            }
            
            // If no status found, check for error messages
            if (jsonNode.has("error")) {
                System.out.println("Judge0 error: " + jsonNode.get("error").asText());
                return Submission.Status.RUNTIME_ERROR;
            }
            
        } catch (Exception e) {
            System.err.println("Error parsing submission status: " + e.getMessage());
            e.printStackTrace();
        }
        return Submission.Status.RUNTIME_ERROR;
    }
}
