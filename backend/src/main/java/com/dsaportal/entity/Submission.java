package com.dsaportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Language language;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "time_taken")
    private Integer timeTaken; // milliseconds

    @Column(name = "memory_used")
    private Integer memoryUsed; // KB

    @Column(name = "test_cases_passed")
    private Integer testCasesPassed = 0;

    @Column(name = "total_test_cases")
    private Integer totalTestCases = 0;

    @Column(name = "accuracy")
    private Double accuracy = 0.0;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "judge0_token")
    private String judge0Token;
    
    @Column(name = "analysis_feedback", columnDefinition = "TEXT")
    private String analysisFeedback;
    
    @Column(name = "efficiency_score")
    private Double efficiencyScore = 0.0;

    // Constructors
    public Submission() {
        this.submittedAt = LocalDateTime.now();
    }

    public Submission(User user, Problem problem, String code, Language language) {
        this();
        this.user = user;
        this.problem = problem;
        this.code = code;
        this.language = language;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    public String getJudge0Token() {
        return judge0Token;
    }

    public void setJudge0Token(String judge0Token) {
        this.judge0Token = judge0Token;
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

    public enum Language {
        PYTHON, JAVA, CPP, JAVASCRIPT, C
    }

    public enum Status {
        PENDING, ACCEPTED, WRONG_ANSWER, TIME_LIMIT_EXCEEDED, 
        MEMORY_LIMIT_EXCEEDED, RUNTIME_ERROR, COMPILATION_ERROR
    }
}
