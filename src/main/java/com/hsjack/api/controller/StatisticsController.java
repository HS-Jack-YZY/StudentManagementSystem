package com.hsjack.api.controller;

import com.hsjack.api.dto.ApiResponse;
import com.hsjack.model.Student;
import com.hsjack.service.GradeStatistics;
import com.hsjack.service.StudentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Grade Statistics operations
 * Provides API endpoints for statistical analysis of student grades
 */
@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {
    
    private final StudentManager studentManager;
    private final GradeStatistics gradeStatistics;
    
    @Autowired
    public StatisticsController(StudentManager studentManager) {
        this.studentManager = studentManager;
        this.gradeStatistics = studentManager.getGradeStatistics();
    }
    
    /**
     * Get basic statistics summary
     * GET /api/statistics/summary
     */
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatisticsSummary() {
        try {
            List<Student> students = studentManager.getAllStudents();
            Map<String, Object> summary = gradeStatistics.getStatisticsSummary(students);
            return ResponseEntity.ok(ApiResponse.success(summary, "Statistics summary retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve statistics summary: " + e.getMessage()));
        }
    }
    
    /**
     * Get score distribution
     * GET /api/statistics/distribution
     */
    @GetMapping("/distribution")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> getScoreDistribution() {
        try {
            List<Student> students = studentManager.getAllStudents();
            Map<String, Integer> distribution = gradeStatistics.getScoreDistribution(students);
            return ResponseEntity.ok(ApiResponse.success(distribution, "Score distribution retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve score distribution: " + e.getMessage()));
        }
    }
    
    /**
     * Get statistics by gender
     * GET /api/statistics/by-gender
     */
    @GetMapping("/by-gender")
    public ResponseEntity<ApiResponse<Map<String, Map<String, Object>>>> getStatisticsByGender() {
        try {
            List<Student> students = studentManager.getAllStudents();
            Map<String, Map<String, Object>> genderStats = gradeStatistics.getStatisticsByGender(students);
            return ResponseEntity.ok(ApiResponse.success(genderStats, "Gender statistics retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve gender statistics: " + e.getMessage()));
        }
    }
    
    /**
     * Get average score
     * GET /api/statistics/average
     */
    @GetMapping("/average")
    public ResponseEntity<ApiResponse<Double>> getAverageScore() {
        try {
            List<Student> students = studentManager.getAllStudents();
            double average = gradeStatistics.calculateAverageScore(students);
            return ResponseEntity.ok(ApiResponse.success(average, "Average score calculated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to calculate average score: " + e.getMessage()));
        }
    }
    
    /**
     * Get highest score
     * GET /api/statistics/highest
     */
    @GetMapping("/highest")
    public ResponseEntity<ApiResponse<Double>> getHighestScore() {
        try {
            List<Student> students = studentManager.getAllStudents();
            double highest = gradeStatistics.findHighestScore(students);
            return ResponseEntity.ok(ApiResponse.success(highest, "Highest score retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve highest score: " + e.getMessage()));
        }
    }
    
    /**
     * Get lowest score
     * GET /api/statistics/lowest
     */
    @GetMapping("/lowest")
    public ResponseEntity<ApiResponse<Double>> getLowestScore() {
        try {
            List<Student> students = studentManager.getAllStudents();
            double lowest = gradeStatistics.findLowestScore(students);
            return ResponseEntity.ok(ApiResponse.success(lowest, "Lowest score retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve lowest score: " + e.getMessage()));
        }
    }
}