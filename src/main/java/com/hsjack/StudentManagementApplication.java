package com.hsjack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the Student Management System Backend
 * This replaces the console-based Main class and provides REST API endpoints
 */
@SpringBootApplication
public class StudentManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
    }
}