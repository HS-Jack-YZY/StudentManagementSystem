package com.hsjack.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testConstructorAndGetters() {
        // Test constructor and all getters
        Student student = new Student("001", "张三", "男", 20, 85.5);
        
        assertEquals("001", student.getId());
        assertEquals("张三", student.getName());
        assertEquals("男", student.getGender());
        assertEquals(20, student.getAge());
        assertEquals(85.5, student.getScore());
    }

    @Test
    void testSetters() {
        // Test all setters
        Student student = new Student("001", "张三", "男", 20, 85.5);
        
        student.setId("002");
        student.setName("李四");
        student.setGender("女");
        student.setAge(21);
        student.setScore(90.0);
        
        assertEquals("002", student.getId());
        assertEquals("李四", student.getName());
        assertEquals("女", student.getGender());
        assertEquals(21, student.getAge());
        assertEquals(90.0, student.getScore());
    }

    @Test
    void testToString() {
        Student student = new Student("001", "张三", "男", 20, 85.5);
        String expected = "Student{id='001', name='张三', gender='男', age=20, score=85.5}";
        assertEquals(expected, student.toString());
    }

    @Test
    void testConstructorWithNullAndEmptyValues() {
        // Test edge cases with null and empty values
        Student student = new Student(null, "", null, 0, 0.0);
        
        assertNull(student.getId());
        assertEquals("", student.getName());
        assertNull(student.getGender());
        assertEquals(0, student.getAge());
        assertEquals(0.0, student.getScore());
    }

    @Test
    void testNegativeAgeAndScore() {
        // Test with negative values (business logic might need validation later)
        Student student = new Student("001", "测试", "男", -5, -10.0);
        
        assertEquals(-5, student.getAge());
        assertEquals(-10.0, student.getScore());
    }
}