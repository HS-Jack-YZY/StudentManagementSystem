package com.hsjack.util;

import com.hsjack.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class FileUtilsTest {
    
    private static final String TEST_FILE = "test_students.dat";
    private String originalFileName;
    
    @BeforeEach
    void setUp() throws Exception {
        // Backup the original file name using reflection to avoid modifying the original constant
        // For testing purposes, we'll use a different approach - just clean up test files
        // Delete any existing test file
        File testFile = new File(TEST_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    @AfterEach
    void tearDown() {
        // Clean up test file
        File testFile = new File(TEST_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
        
        // Also clean up the main students.dat file that might be created during tests
        File mainFile = new File("students.dat");
        if (mainFile.exists()) {
            mainFile.delete();
        }
    }

    @Test
    void testSaveAndLoadEmptyList() {
        // Test saving and loading an empty list
        List<Student> emptyList = new ArrayList<>();
        FileUtils.saveToFile(emptyList);
        
        List<Student> loadedList = FileUtils.loadFromFile();
        assertNotNull(loadedList);
        assertTrue(loadedList.isEmpty());
    }

    @Test
    void testSaveAndLoadSingleStudent() {
        // Test saving and loading a single student
        List<Student> students = new ArrayList<>();
        students.add(new Student("001", "张三", "男", 20, 85.5));
        
        FileUtils.saveToFile(students);
        
        List<Student> loadedStudents = FileUtils.loadFromFile();
        assertNotNull(loadedStudents);
        assertEquals(1, loadedStudents.size());
        
        Student loadedStudent = loadedStudents.get(0);
        assertEquals("001", loadedStudent.getId());
        assertEquals("张三", loadedStudent.getName());
        assertEquals("男", loadedStudent.getGender());
        assertEquals(20, loadedStudent.getAge());
        assertEquals(85.5, loadedStudent.getScore());
    }

    @Test
    void testSaveAndLoadMultipleStudents() {
        // Test saving and loading multiple students
        List<Student> students = new ArrayList<>();
        students.add(new Student("001", "张三", "男", 20, 85.5));
        students.add(new Student("002", "李四", "女", 21, 92.0));
        students.add(new Student("003", "王五", "男", 22, 78.5));
        
        FileUtils.saveToFile(students);
        
        List<Student> loadedStudents = FileUtils.loadFromFile();
        assertNotNull(loadedStudents);
        assertEquals(3, loadedStudents.size());
        
        // Verify first student
        assertEquals("001", loadedStudents.get(0).getId());
        assertEquals("张三", loadedStudents.get(0).getName());
        
        // Verify second student
        assertEquals("002", loadedStudents.get(1).getId());
        assertEquals("李四", loadedStudents.get(1).getName());
        
        // Verify third student
        assertEquals("003", loadedStudents.get(2).getId());
        assertEquals("王五", loadedStudents.get(2).getName());
    }

    @Test
    void testLoadFromNonExistentFile() {
        // Test loading when file doesn't exist - should return empty list
        // Make sure the file doesn't exist
        File file = new File("students.dat");
        if (file.exists()) {
            file.delete();
        }
        
        List<Student> loadedStudents = FileUtils.loadFromFile();
        assertNotNull(loadedStudents);
        assertTrue(loadedStudents.isEmpty());
    }

    @Test
    void testSaveWithNullStudentData() {
        // Test saving students with null values
        List<Student> students = new ArrayList<>();
        students.add(new Student(null, null, null, 0, 0.0));
        
        FileUtils.saveToFile(students);
        
        List<Student> loadedStudents = FileUtils.loadFromFile();
        assertNotNull(loadedStudents);
        assertEquals(1, loadedStudents.size());
        
        Student loadedStudent = loadedStudents.get(0);
        assertNull(loadedStudent.getId());
        assertNull(loadedStudent.getName());
        assertNull(loadedStudent.getGender());
        assertEquals(0, loadedStudent.getAge());
        assertEquals(0.0, loadedStudent.getScore());
    }

    @Test
    void testMultipleSaveAndLoadOperations() {
        // Test multiple save/load operations to ensure file integrity
        List<Student> students1 = new ArrayList<>();
        students1.add(new Student("001", "First", "男", 20, 85.0));
        FileUtils.saveToFile(students1);
        
        List<Student> loaded1 = FileUtils.loadFromFile();
        assertEquals(1, loaded1.size());
        assertEquals("First", loaded1.get(0).getName());
        
        // Save different data
        List<Student> students2 = new ArrayList<>();
        students2.add(new Student("002", "Second", "女", 21, 90.0));
        students2.add(new Student("003", "Third", "男", 22, 88.0));
        FileUtils.saveToFile(students2);
        
        List<Student> loaded2 = FileUtils.loadFromFile();
        assertEquals(2, loaded2.size());
        assertEquals("Second", loaded2.get(0).getName());
        assertEquals("Third", loaded2.get(1).getName());
    }
}