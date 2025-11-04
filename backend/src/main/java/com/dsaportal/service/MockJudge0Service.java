package com.dsaportal.service;

import com.dsaportal.entity.Submission;
import org.springframework.stereotype.Service;

@Service
public class MockJudge0Service {
    
    public String submitCode(Submission submission) {
        System.out.println("Mock Judge0: Submitting code...");
        System.out.println("Language: " + submission.getLanguage());
        System.out.println("Code: " + submission.getCode().substring(0, Math.min(100, submission.getCode().length())));
        
        // Return a mock token
        return "mock-token-" + System.currentTimeMillis();
    }
    
    public Submission.Status getSubmissionResult(String token) {
        System.out.println("Mock Judge0: Getting result for token: " + token);
        
        // Simulate different results based on code content
        // This is just for testing - in real implementation, you'd call Judge0 API
        
        // For now, let's return ACCEPTED for simple test cases
        return Submission.Status.ACCEPTED;
    }
}
