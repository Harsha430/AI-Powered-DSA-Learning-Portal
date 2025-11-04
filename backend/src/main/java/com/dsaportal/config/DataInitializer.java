package com.dsaportal.config;

import com.dsaportal.entity.Problem;
import com.dsaportal.entity.TestCase;
import com.dsaportal.entity.User;
import com.dsaportal.repository.ProblemRepository;
import com.dsaportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@dsaportal.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(User.Role.ADMIN);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setLastLogin(LocalDateTime.now());
            userRepository.save(admin);
            System.out.println("Admin user created");
        }

        // Create test user
        if (!userRepository.existsByUsername("testuser")) {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setEmail("test@example.com");
            testUser.setPassword(passwordEncoder.encode("password123"));
            testUser.setRole(User.Role.USER);
            testUser.setCreatedAt(LocalDateTime.now());
            testUser.setLastLogin(LocalDateTime.now());
            userRepository.save(testUser);
            System.out.println("Test user created");
        }

        // Create sample problems
        if (problemRepository.count() == 0) {
            // Problem 1: Two Sum
            Problem problem1 = new Problem();
            problem1.setTitle("Two Sum");
            problem1.setDescription("Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\n\nYou can return the answer in any order.");
            problem1.setDifficulty(Problem.Difficulty.EASY);
            problem1.setTopic(Problem.Topic.ARRAYS);
            problem1.setInputFormat("First line contains n (2 ≤ n ≤ 10^4)\nSecond line contains n integers\nThird line contains target");
            problem1.setOutputFormat("Print two space-separated indices");
            problem1.setConstraints("2 ≤ n ≤ 10^4\n-10^9 ≤ nums[i] ≤ 10^9\n-10^9 ≤ target ≤ 10^9");
            problem1.setTimeLimit(1000);
            problem1.setMemoryLimit(256);
            problem1.setCreatedAt(LocalDateTime.now());
            problem1.setUpdatedAt(LocalDateTime.now());
            problem1 = problemRepository.save(problem1);

            // Add test cases for problem 1
            TestCase testCase1 = new TestCase();
            testCase1.setProblem(problem1);
            testCase1.setInputData("4\n2 7 11 15\n9");
            testCase1.setExpectedOutput("0 1");
            testCase1.setIsSample(true);
            problem1.setTestCases(new ArrayList<>(Arrays.asList(testCase1)));

            TestCase testCase2 = new TestCase();
            testCase2.setProblem(problem1);
            testCase2.setInputData("3\n3 2 4\n6");
            testCase2.setExpectedOutput("1 2");
            testCase2.setIsSample(false);
            problem1.getTestCases().add(testCase2);
            
            problemRepository.save(problem1);

            // Problem 2: Valid Parentheses
            Problem problem2 = new Problem();
            problem2.setTitle("Valid Parentheses");
            problem2.setDescription("Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\n\nAn input string is valid if:\n1. Open brackets must be closed by the same type of brackets.\n2. Open brackets must be closed in the correct order.\n3. Every close bracket has a corresponding open bracket of the same type.");
            problem2.setDifficulty(Problem.Difficulty.EASY);
            problem2.setTopic(Problem.Topic.STACK);
            problem2.setInputFormat("Single line containing string s");
            problem2.setOutputFormat("Print \"true\" if valid, \"false\" otherwise");
            problem2.setConstraints("1 ≤ s.length ≤ 10^4\ns consists of parentheses only");
            problem2.setTimeLimit(1000);
            problem2.setMemoryLimit(256);
            problem2.setCreatedAt(LocalDateTime.now());
            problem2.setUpdatedAt(LocalDateTime.now());
            problem2 = problemRepository.save(problem2);

            // Add test cases for problem 2
            TestCase testCase3 = new TestCase();
            testCase3.setProblem(problem2);
            testCase3.setInputData("()");
            testCase3.setExpectedOutput("true");
            testCase3.setIsSample(true);
            problem2.setTestCases(new ArrayList<>(Arrays.asList(testCase3)));

            TestCase testCase4 = new TestCase();
            testCase4.setProblem(problem2);
            testCase4.setInputData("()[]{}");
            testCase4.setExpectedOutput("true");
            testCase4.setIsSample(true);
            problem2.getTestCases().add(testCase4);

            TestCase testCase5 = new TestCase();
            testCase5.setProblem(problem2);
            testCase5.setInputData("(]");
            testCase5.setExpectedOutput("false");
            testCase5.setIsSample(true);
            problem2.getTestCases().add(testCase5);
            
            problemRepository.save(problem2);

            System.out.println("Sample problems created");
        }
    }
}
