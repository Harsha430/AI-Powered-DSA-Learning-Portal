package com.dsaportal.dto;

import java.util.List;
import java.util.Map;

public class DashboardStats {
    private Long totalProblems;
    private Long solvedProblems;
    private Long totalSubmissions;
    private Double overallAccuracy;
    private Map<String, Double> accuracyByTopic;
    private List<ProblemDto> recommendedProblems;
    private List<SubmissionDto> recentSubmissions;

    // Constructors
    public DashboardStats() {}

    // Getters and Setters
    public Long getTotalProblems() {
        return totalProblems;
    }

    public void setTotalProblems(Long totalProblems) {
        this.totalProblems = totalProblems;
    }

    public Long getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(Long solvedProblems) {
        this.solvedProblems = solvedProblems;
    }

    public Long getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(Long totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }

    public Double getOverallAccuracy() {
        return overallAccuracy;
    }

    public void setOverallAccuracy(Double overallAccuracy) {
        this.overallAccuracy = overallAccuracy;
    }

    public Map<String, Double> getAccuracyByTopic() {
        return accuracyByTopic;
    }

    public void setAccuracyByTopic(Map<String, Double> accuracyByTopic) {
        this.accuracyByTopic = accuracyByTopic;
    }

    public List<ProblemDto> getRecommendedProblems() {
        return recommendedProblems;
    }

    public void setRecommendedProblems(List<ProblemDto> recommendedProblems) {
        this.recommendedProblems = recommendedProblems;
    }

    public List<SubmissionDto> getRecentSubmissions() {
        return recentSubmissions;
    }

    public void setRecentSubmissions(List<SubmissionDto> recentSubmissions) {
        this.recentSubmissions = recentSubmissions;
    }
}
