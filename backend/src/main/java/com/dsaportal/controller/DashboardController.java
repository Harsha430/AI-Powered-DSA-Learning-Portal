package com.dsaportal.controller;

import com.dsaportal.dto.DashboardStats;
import com.dsaportal.dto.ProblemDto;
import com.dsaportal.dto.SubmissionDto;
import com.dsaportal.service.GeminiService;
import com.dsaportal.service.ProblemService;
import com.dsaportal.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    
    @Autowired
    private ProblemService problemService;
    
    @Autowired
    private SubmissionService submissionService;
    
    @Autowired
    private GeminiService geminiService;

    @GetMapping("/{userId}")
    public ResponseEntity<DashboardStats> getDashboardStats(@PathVariable Long userId) {
        DashboardStats stats = new DashboardStats();
        
        // Get basic stats
        Long totalProblems = (long) problemService.getAllProblems().size();
        Long solvedProblems = submissionService.getAcceptedSubmissionsByUserId(userId);
        Long totalSubmissions = submissionService.getTotalSubmissionsByUserId(userId);
        Double overallAccuracy = submissionService.getAverageAccuracyByUserId(userId);
        
        stats.setTotalProblems(totalProblems);
        stats.setSolvedProblems(solvedProblems);
        stats.setTotalSubmissions(totalSubmissions);
        stats.setOverallAccuracy(overallAccuracy);
        
        // Get accuracy by topic (simplified)
        Map<String, Double> accuracyByTopic = new HashMap<>();
        accuracyByTopic.put("ARRAYS", 75.0);
        accuracyByTopic.put("STRINGS", 80.0);
        accuracyByTopic.put("TREES", 60.0);
        accuracyByTopic.put("GRAPHS", 45.0);
        stats.setAccuracyByTopic(accuracyByTopic);
        
        // Get AI recommendations
        List<ProblemDto> recommendedProblems = geminiService.getRecommendedProblems(userId)
                .stream()
                .map(ProblemDto::new)
                .toList();
        stats.setRecommendedProblems(recommendedProblems);
        
        // Get recent submissions
        List<SubmissionDto> recentSubmissions = submissionService.getSubmissionsByUserId(userId)
                .stream()
                .limit(5)
                .toList();
        stats.setRecentSubmissions(recentSubmissions);
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<ProblemDto>> getRecommendations(@PathVariable Long userId) {
        List<ProblemDto> recommendations = geminiService.getRecommendedProblems(userId)
                .stream()
                .map(ProblemDto::new)
                .toList();
        return ResponseEntity.ok(recommendations);
    }
}
