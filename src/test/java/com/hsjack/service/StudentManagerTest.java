package com.hsjack.service;

import com.hsjack.model.Student;
import com.hsjack.util.DatabaseUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class StudentManagerTest {
    
    @BeforeEach
    void setUp() {
        // 清空数据库以确保测试隔离
        DatabaseUtils.saveToFile(new ArrayList<>());
    }
    
    @AfterEach
    void tearDown() {
        // 清理测试数据
        DatabaseUtils.saveToFile(new ArrayList<>());
    }
    
    @Test
    void testConstructor() {
        // Test that constructor initializes properly (loads empty list when no file exists)
        StudentManager manager = new StudentManager();
        assertNotNull(manager);
        List<Student> students = manager.getAllStudents();
        assertNotNull(students);
        assertTrue(students.isEmpty());
    }

    @Test
    void testAddAndGetStudent() {
        StudentManager manager = new StudentManager();
        Student s = new Student("001", "Jack", "男", 20, 88.5);
        manager.addStudent(s);

        Student found = manager.getStudentById("001");
        assertNotNull(found);
        assertEquals("Jack", found.getName());
    }

    @Test
    void testRemoveStudent() {
        StudentManager manager = new StudentManager();
        Student s = new Student("002", "Tom", "男", 21, 77.0);
        manager.addStudent(s);

        boolean removed = manager.removeStudentById("002");
        assertTrue(removed);
        assertNull(manager.getStudentById("002"));
    }

    @Test
    void testRemoveNonExistentStudent() {
        StudentManager manager = new StudentManager();
        // Try to remove a student that doesn't exist
        boolean removed = manager.removeStudentById("999");
        assertFalse(removed);
    }

    @Test
    void testUpdateStudent() {
        StudentManager manager = new StudentManager();
        Student s = new Student("003", "Amy", "女", 19, 92.0);
        manager.addStudent(s);

        Student newS = new Student("003", "Amy Lee", "男", 20, 95.0);
        boolean updated = manager.updateStudent("003", newS);
        assertTrue(updated);

        Student found = manager.getStudentById("003");
        assertEquals("Amy Lee", found.getName());
        assertEquals("男", found.getGender()); // Test that gender update works (bug fix)
        assertEquals(20, found.getAge());
        assertEquals(95.0, found.getScore());
    }
    
    @Test
    void testUpdateNonExistentStudent() {
        StudentManager manager = new StudentManager();
        // 必须让 StudentManager中存在数据，会在if判断之前的for循环判断时结束无法进入if branch
        manager.addStudent(new Student("003", "Amy", "女", 19, 92.0));
        Student newS = new Student("999", "NonExistent", "男", 20, 95.0);
        boolean updated = manager.updateStudent("999", newS);
        assertFalse(updated);
    }
    
    @Test
    void testGetAllStudents() {
        StudentManager manager = new StudentManager();
        
        // Initially empty
        List<Student> students = manager.getAllStudents();
        assertNotNull(students);
        assertTrue(students.isEmpty());
        
        // Add some students
        manager.addStudent(new Student("001", "Student1", "男", 20, 85.0));
        manager.addStudent(new Student("002", "Student2", "女", 21, 90.0));
        manager.addStudent(new Student("003", "Student3", "男", 22, 88.0));
        
        students = manager.getAllStudents();
        assertEquals(3, students.size());
        
        // Verify the returned list is a copy (defensive copying)
        students.clear();
        List<Student> originalStudents = manager.getAllStudents();
        assertEquals(3, originalStudents.size()); // Original should be unchanged
    }
    
    @Test
    void testGetStudentByIdNotFound() {
        StudentManager manager = new StudentManager();
        manager.addStudent(new Student("001", "Test", "男", 20, 85.0));
        
        Student found = manager.getStudentById("999");
        assertNull(found);
    }
    
    @Test
    void testGetStudentByIdWithNullId() {
        StudentManager manager = new StudentManager();
        manager.addStudent(new Student("001", "Test", "男", 20, 85.0));
        
        Student found = manager.getStudentById(null);
        assertNull(found);
    }
    
    @Test
    void testSaveMethod() {
        StudentManager manager = new StudentManager();
        
        // Add some students
        manager.addStudent(new Student("001", "SaveTest1", "男", 20, 85.0));
        manager.addStudent(new Student("002", "SaveTest2", "女", 21, 90.0));
        
        // Save method should not throw an exception (compatibility method for database version)
        assertDoesNotThrow(() -> manager.save());
        
        // Create new manager and verify it loads the saved data from database
        StudentManager newManager = new StudentManager();
        List<Student> loadedStudents = newManager.getAllStudents();
        assertEquals(2, loadedStudents.size());
        // The order in database is by ID, so we can still check the names
        assertEquals("SaveTest1", loadedStudents.get(0).getName());
        assertEquals("SaveTest2", loadedStudents.get(1).getName());
    }
    
    @Test
    void testAddMultipleStudentsWithSameId() {
        StudentManager manager = new StudentManager();
        
        // 在数据库版本中，相同ID的学生会被更新而不是添加
        manager.addStudent(new Student("001", "First", "男", 20, 85.0));
        manager.addStudent(new Student("001", "Second", "女", 21, 90.0)); // 这会更新第一个学生
        
        List<Student> students = manager.getAllStudents();
        assertEquals(1, students.size()); // 数据库版本中只有一个学生
        
        // getStudentById should return the updated student
        Student found = manager.getStudentById("001");
        assertEquals("Second", found.getName()); // 应该是更新后的名字
    }
    
    @Test
    void testRemoveStudentRemovesAllWithSameId() {
        StudentManager manager = new StudentManager();
        
        // 在数据库版本中，ID是唯一的，所以只会有一个学生
        manager.addStudent(new Student("001", "First", "男", 20, 85.0));
        manager.addStudent(new Student("002", "Different", "男", 22, 88.0));
        
        // Remove by ID should remove the student with that ID
        boolean removed = manager.removeStudentById("001");
        assertTrue(removed);
        
        List<Student> remaining = manager.getAllStudents();
        assertEquals(1, remaining.size());
        assertEquals("Different", remaining.get(0).getName());
    }
    
    @Test
    void testUpdateStudentWithNullValues() {
        StudentManager manager = new StudentManager();
        Student s = new Student("001", "Original", "男", 20, 85.0);
        manager.addStudent(s);
        
        Student newS = new Student("001", null, null, 0, 0.0);
        boolean updated = manager.updateStudent("001", newS);
        assertTrue(updated);
        
        Student found = manager.getStudentById("001");
        assertNull(found.getName());
        assertNull(found.getGender());
        assertEquals(0, found.getAge());
        assertEquals(0.0, found.getScore());
    }
    
    @Test
    void testGetGradeStatistics() {
        StudentManager manager = new StudentManager();
        
        // Test that GradeStatistics is available
        GradeStatistics gradeStats = manager.getGradeStatistics();
        assertNotNull(gradeStats);
    }
    
    @Test
    void testGradeStatisticsIntegration() {
        StudentManager manager = new StudentManager();
        
        // Add some test students
        manager.addStudent(new Student("001", "张三", "男", 20, 85.0));
        manager.addStudent(new Student("002", "李四", "女", 21, 92.0));
        manager.addStudent(new Student("003", "王五", "男", 22, 78.0));
        
        GradeStatistics gradeStats = manager.getGradeStatistics();
        List<Student> students = manager.getAllStudents();
        
        // Test basic statistics
        double average = gradeStats.calculateAverageScore(students);
        assertEquals(85.0, average, 0.001);
        
        double highest = gradeStats.findHighestScore(students);
        assertEquals(92.0, highest, 0.001);
        
        double lowest = gradeStats.findLowestScore(students);
        assertEquals(78.0, lowest, 0.001);
    }
}