package com.dsaportal.controller;

import com.dsaportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/users")
    public Object getUsers() {
        return userRepository.findAll();
    }
    
    @GetMapping("/admin")
    public Object getAdmin() {
        return userRepository.findByUsername("admin");
    }
}




