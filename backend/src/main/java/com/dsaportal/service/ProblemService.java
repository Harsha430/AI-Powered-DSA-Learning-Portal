package com.dsaportal.service;

import com.dsaportal.dto.ProblemDto;
import com.dsaportal.entity.Problem;
import com.dsaportal.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProblemService {
    
    @Autowired
    private ProblemRepository problemRepository;
    
    public List<ProblemDto> getAllProblems() {
        return problemRepository.findAll()
                .stream()
                .map(ProblemDto::new)
                .collect(Collectors.toList());
    }
    
    public List<ProblemDto> getProblemsByDifficulty(Problem.Difficulty difficulty) {
        return problemRepository.findByDifficulty(difficulty)
                .stream()
                .map(ProblemDto::new)
                .collect(Collectors.toList());
    }
    
    public List<ProblemDto> getProblemsByTopic(Problem.Topic topic) {
        return problemRepository.findByTopic(topic)
                .stream()
                .map(ProblemDto::new)
                .collect(Collectors.toList());
    }
    
    public List<ProblemDto> getProblemsByFilters(Problem.Difficulty difficulty, Problem.Topic topic, String search) {
        return problemRepository.findByFilters(difficulty, topic, search)
                .stream()
                .map(ProblemDto::new)
                .collect(Collectors.toList());
    }
    
    public Optional<ProblemDto> getProblemById(Long id) {
        return problemRepository.findById(id)
                .map(ProblemDto::new);
    }
    
    public ProblemDto createProblem(Problem problem) {
        Problem savedProblem = problemRepository.save(problem);
        return new ProblemDto(savedProblem);
    }
    
    public Optional<ProblemDto> updateProblem(Long id, Problem problemDetails) {
        return problemRepository.findById(id)
                .map(problem -> {
                    problem.setTitle(problemDetails.getTitle());
                    problem.setDescription(problemDetails.getDescription());
                    problem.setDifficulty(problemDetails.getDifficulty());
                    problem.setTopic(problemDetails.getTopic());
                    problem.setInputFormat(problemDetails.getInputFormat());
                    problem.setOutputFormat(problemDetails.getOutputFormat());
                    problem.setConstraints(problemDetails.getConstraints());
                    problem.setTimeLimit(problemDetails.getTimeLimit());
                    problem.setMemoryLimit(problemDetails.getMemoryLimit());
                    return problemRepository.save(problem);
                })
                .map(ProblemDto::new);
    }
    
    public boolean deleteProblem(Long id) {
        if (problemRepository.existsById(id)) {
            problemRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<ProblemDto> getSolvedProblems(Long userId) {
        return problemRepository.findSolvedByUser(userId)
                .stream()
                .map(ProblemDto::new)
                .collect(Collectors.toList());
    }
    
    public List<ProblemDto> getUnsolvedProblems(Long userId) {
        return problemRepository.findUnsolvedByUser(userId)
                .stream()
                .map(ProblemDto::new)
                .collect(Collectors.toList());
    }
}
