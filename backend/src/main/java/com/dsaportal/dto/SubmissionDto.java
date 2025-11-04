package com.dsaportal.dto;

import com.dsaportal.entity.Submission;

import java.time.LocalDateTime;

public class SubmissionDto {
    private Long id;
    private Long userId;
    private Long problemId;
    private String problemTitle;
    private String code;
    private String language;
    private String status;
    private Integer timeTaken;
    private Integer memoryUsed;
    private Integer testCasesPassed;
    private Integer totalTestCases;
    private Double accuracy;
    private String errorMessage;
    private LocalDateTime submittedAt;
    private String analysisFeedback;
    private Double efficiencyScore;

    // Constructors
    public SubmissionDto() {}

    public SubmissionDto(Submission submission) {
        this.id = submission.getId();
        this.userId = submission.getUser().getId();
        this.problemId = submission.getProblem().getId();
        this.problemTitle = submission.getProblem().getTitle();
        this.code = submission.getCode();
        this.language = submission.getLanguage().name();
        this.status = submission.getStatus() != null ? submission.getStatus().name() : null;
        this.timeTaken = submission.getTimeTaken();
        this.memoryUsed = submission.getMemoryUsed();
        this.testCasesPassed = submission.getTestCasesPassed();
        this.totalTestCases = submission.getTotalTestCases();
        this.accuracy = submission.getAccuracy();
        this.errorMessage = submission.getErrorMessage();
        this.submittedAt = submission.getSubmittedAt();
        this.analysisFeedback = submission.getAnalysisFeedback();
        this.efficiencyScore = submission.getEfficiencyScore();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Integer getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Integer memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public Integer getTestCasesPassed() {
        return testCasesPassed;
    }

    public void setTestCasesPassed(Integer testCasesPassed) {
        this.testCasesPassed = testCasesPassed;
    }

    public Integer getTotalTestCases() {
        return totalTestCases;
    }

    public void setTotalTestCases(Integer totalTestCases) {
        this.totalTestCases = totalTestCases;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public String getAnalysisFeedback() {
        return analysisFeedback;
    }
    
    public void setAnalysisFeedback(String analysisFeedback) {
        this.analysisFeedback = analysisFeedback;
    }
    
    public Double getEfficiencyScore() {
        return efficiencyScore;
    }
    
    public void setEfficiencyScore(Double efficiencyScore) {
        this.efficiencyScore = efficiencyScore;
    }
}
