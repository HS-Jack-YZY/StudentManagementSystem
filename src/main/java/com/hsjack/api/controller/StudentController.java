package com.hsjack.api.controller;

import com.hsjack.api.dto.ApiResponse;
import com.hsjack.api.dto.StudentDto;
import com.hsjack.model.Student;
import com.hsjack.service.StudentManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Student Management operations
 * Provides API endpoints for CRUD operations on students
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*") // Allow frontend access from any origin
public class StudentController {
    
    private final StudentManager studentManager;
    
    @Autowired
    public StudentController(StudentManager studentManager) {
        this.studentManager = studentManager;
    }
    
    /**
     * Get all students
     * GET /api/students
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentDto>>> getAllStudents() {
        try {
            List<Student> students = studentManager.getAllStudents();
            List<StudentDto> studentDtos = students.stream()
                    .map(StudentDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(studentDtos, "Students retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve students: " + e.getMessage()));
        }
    }
    
    /**
     * Get student by ID
     * GET /api/students/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDto>> getStudentById(@PathVariable String id) {
        try {
            Student student = studentManager.getStudentById(id);
            if (student != null) {
                return ResponseEntity.ok(ApiResponse.success(new StudentDto(student), "Student found"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Student not found with ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve student: " + e.getMessage()));
        }
    }
    
    /**
     * Add new student
     * POST /api/students
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudentDto>> addStudent(@Valid @RequestBody StudentDto studentDto) {
        try {
            // Check if student with same ID already exists
            if (studentManager.getStudentById(studentDto.getId()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error("Student with ID " + studentDto.getId() + " already exists"));
            }
            
            Student student = studentDto.toStudent();
            studentManager.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(new StudentDto(student), "Student added successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to add student: " + e.getMessage()));
        }
    }
    
    /**
     * Update existing student
     * PUT /api/students/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDto>> updateStudent(
            @PathVariable String id, 
            @Valid @RequestBody StudentDto studentDto) {
        try {
            // Ensure the ID in URL matches the ID in the body
            studentDto.setId(id);
            
            Student updatedStudent = studentDto.toStudent();
            boolean success = studentManager.updateStudent(id, updatedStudent);
            
            if (success) {
                return ResponseEntity.ok(ApiResponse.success(new StudentDto(updatedStudent), "Student updated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Student not found with ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update student: " + e.getMessage()));
        }
    }
    
    /**
     * Delete student by ID
     * DELETE /api/students/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String id) {
        try {
            boolean success = studentManager.removeStudentById(id);
            if (success) {
                return ResponseEntity.ok(ApiResponse.success(null, "Student deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Student not found with ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete student: " + e.getMessage()));
        }
    }
}