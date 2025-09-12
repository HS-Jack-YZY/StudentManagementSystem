package com.hsjack.service;

import com.hsjack.model.Student;
import com.hsjack.util.DatabaseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentManagerDatabaseTest {
    
    private StudentManager manager;
    
    @BeforeEach
    void setUp() {
        // 清空测试数据库
        DatabaseUtils.saveToFile(new ArrayList<>());
        manager = new StudentManager();
    }
    
    @Test
    void testAddAndGetStudent() {
        Student student = new Student("001", "张三", "男", 20, 85.5);
        manager.addStudent(student);
        
        Student retrieved = manager.getStudentById("001");
        assertNotNull(retrieved);
        assertEquals("001", retrieved.getId());
        assertEquals("张三", retrieved.getName());
    }
    
    @Test
    void testGetAllStudents() {
        manager.addStudent(new Student("001", "张三", "男", 20, 85.5));
        manager.addStudent(new Student("002", "李四", "女", 21, 90.0));
        
        List<Student> students = manager.getAllStudents();
        assertEquals(2, students.size());
    }
    
    @Test
    void testRemoveStudent() {
        manager.addStudent(new Student("001", "张三", "男", 20, 85.5));
        
        boolean removed = manager.removeStudentById("001");
        assertTrue(removed);
        
        Student retrieved = manager.getStudentById("001");
        assertNull(retrieved);
    }
    
    @Test
    void testUpdateStudent() {
        manager.addStudent(new Student("001", "张三", "男", 20, 85.5));
        
        Student newInfo = new Student("001", "张三新", "女", 21, 95.0);
        boolean updated = manager.updateStudent("001", newInfo);
        assertTrue(updated);
        
        Student retrieved = manager.getStudentById("001");
        assertNotNull(retrieved);
        assertEquals("张三新", retrieved.getName());
        assertEquals("女", retrieved.getGender());
        assertEquals(21, retrieved.getAge());
        assertEquals(95.0, retrieved.getScore());
    }
    
    @Test
    void testSaveMethod() {
        // save方法应该不会抛出异常（兼容性方法）
        assertDoesNotThrow(() -> manager.save());
    }
    
    @Test
    void testGetGradeStatistics() {
        GradeStatistics stats = manager.getGradeStatistics();
        assertNotNull(stats);
    }
}