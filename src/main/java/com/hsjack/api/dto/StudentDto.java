package com.hsjack.api.dto;

import com.hsjack.model.Student;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for Student API operations
 * Provides validation and serialization for REST API
 */
public class StudentDto {
    
    @NotBlank(message = "Student ID is required")
    private String id;
    
    @NotBlank(message = "Student name is required")
    private String name;
    
    @NotBlank(message = "Student gender is required")
    private String gender;
    
    @NotNull(message = "Student age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 150, message = "Age must be at most 150")
    private Integer age;
    
    @NotNull(message = "Student score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score must be at most 100")
    private Double score;
    
    // Default constructor for JSON deserialization
    public StudentDto() {}
    
    // Constructor from Student entity
    public StudentDto(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.gender = student.getGender();
        this.age = student.getAge();
        this.score = student.getScore();
    }
    
    // Convert to Student entity
    public Student toStudent() {
        return new Student(id, name, gender, age, score);
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public Double getScore() {
        return score;
    }
    
    public void setScore(Double score) {
        this.score = score;
    }
}