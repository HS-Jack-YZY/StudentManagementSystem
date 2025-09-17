package com.hsjack.service;

import com.hsjack.model.Student;
import com.hsjack.util.DatabaseUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    void testExportToCsvWithMultipleStudents() {
        StudentManager manager = new StudentManager();
        String testCsvFile = "test_export_multiple.csv";
        
        // Add test students to database
        manager.addStudent(new Student("001", "张三", "男", 20, 85.5));
        manager.addStudent(new Student("002", "李四", "女", 21, 92.0));
        manager.addStudent(new Student("003", "王五", "男", 22, 78.5));
        
        // Export to CSV
        assertDoesNotThrow(() -> manager.exportToCsv(testCsvFile));
        
        // Verify file was created and contains expected data
        File csvFile = new File(testCsvFile);
        assertTrue(csvFile.exists());
        
        // Clean up
        csvFile.delete();
    }
    
    @Test
    void testExportToCsvWithEmptyDatabase() {
        StudentManager manager = new StudentManager();
        String testCsvFile = "test_export_empty.csv";
        
        // Ensure database is empty
        List<Student> students = manager.getAllStudents();
        assertTrue(students.isEmpty());
        
        // Export empty database to CSV
        assertDoesNotThrow(() -> manager.exportToCsv(testCsvFile));
        
        // Verify file was created (should contain only header)
        File csvFile = new File(testCsvFile);
        assertTrue(csvFile.exists());
        
        // Clean up
        csvFile.delete();
    }
    
    @Test
    void testImportFromCsvWithAppendTrue() {
        StudentManager manager = new StudentManager();
        String testCsvFile = "test_import_append.csv";
        
        // Add existing student to database
        manager.addStudent(new Student("001", "现有学生", "男", 20, 85.0));
        
        // Create test CSV file
        createTestCsvFile(testCsvFile, "id,name,gender,age,score\n" +
                                      "002,导入学生1,女,21,90.0\n" +
                                      "003,导入学生2,男,22,88.0");
        
        // Import with append=true
        assertDoesNotThrow(() -> manager.importFromCsv(testCsvFile, true));
        
        // Verify all students exist (original + imported)
        List<Student> allStudents = manager.getAllStudents();
        assertEquals(3, allStudents.size());
        
        // Verify original student still exists
        Student original = manager.getStudentById("001");
        assertNotNull(original);
        assertEquals("现有学生", original.getName());
        
        // Verify imported students exist
        Student imported1 = manager.getStudentById("002");
        assertNotNull(imported1);
        assertEquals("导入学生1", imported1.getName());
        
        Student imported2 = manager.getStudentById("003");
        assertNotNull(imported2);
        assertEquals("导入学生2", imported2.getName());
        
        // Clean up
        new File(testCsvFile).delete();
    }
    
    @Test
    void testImportFromCsvWithAppendFalse() {
        StudentManager manager = new StudentManager();
        String testCsvFile = "test_import_replace.csv";
        
        // Add existing students to database
        manager.addStudent(new Student("001", "现有学生1", "男", 20, 85.0));
        manager.addStudent(new Student("002", "现有学生2", "女", 21, 90.0));
        
        // Verify existing students
        assertEquals(2, manager.getAllStudents().size());
        
        // Create test CSV file
        createTestCsvFile(testCsvFile, "id,name,gender,age,score\n" +
                                      "003,新导入学生1,男,22,88.0\n" +
                                      "004,新导入学生2,女,23,92.0");
        
        // Import with append=false (should replace existing data)
        assertDoesNotThrow(() -> manager.importFromCsv(testCsvFile, false));
        
        // Verify only imported students exist
        List<Student> allStudents = manager.getAllStudents();
        assertEquals(2, allStudents.size());
        
        // Verify original students are gone
        assertNull(manager.getStudentById("001"));
        assertNull(manager.getStudentById("002"));
        
        // Verify imported students exist
        Student imported1 = manager.getStudentById("003");
        assertNotNull(imported1);
        assertEquals("新导入学生1", imported1.getName());
        
        Student imported2 = manager.getStudentById("004");
        assertNotNull(imported2);
        assertEquals("新导入学生2", imported2.getName());
        
        // Clean up
        new File(testCsvFile).delete();
    }
    
    @Test
    void testImportFromCsvWithEmptyFile() {
        StudentManager manager = new StudentManager();
        String testCsvFile = "test_import_empty.csv";
        
        // Add existing student
        manager.addStudent(new Student("001", "现有学生", "男", 20, 85.0));
        
        // Create empty CSV file (only header)
        createTestCsvFile(testCsvFile, "id,name,gender,age,score\n");
        
        // Import with append=true
        assertDoesNotThrow(() -> manager.importFromCsv(testCsvFile, true));
        
        // Verify existing student still exists and no new students added
        List<Student> allStudents = manager.getAllStudents();
        assertEquals(1, allStudents.size());
        assertEquals("现有学生", allStudents.get(0).getName());
        
        // Clean up
        new File(testCsvFile).delete();
    }
    
    @Test
    void testImportFromNonExistentCsvFile() {
        StudentManager manager = new StudentManager();
        String nonExistentFile = "non_existent_file.csv";
        
        // Add existing student
        manager.addStudent(new Student("001", "现有学生", "男", 20, 85.0));
        
        // Import from non-existent file should not throw exception
        assertDoesNotThrow(() -> manager.importFromCsv(nonExistentFile, true));
        
        // Verify existing student still exists
        List<Student> allStudents = manager.getAllStudents();
        assertEquals(1, allStudents.size());
        assertEquals("现有学生", allStudents.get(0).getName());
    }
    
    @Test
    void testExportImportRoundTrip() {
        StudentManager manager = new StudentManager();
        String testCsvFile = "test_roundtrip.csv";
        
        // Add test students
        manager.addStudent(new Student("001", "张三", "男", 20, 85.5));
        manager.addStudent(new Student("002", "李四", "女", 21, 92.0));
        manager.addStudent(new Student("003", "王五", "男", 22, 78.5));
        
        // Export to CSV
        assertDoesNotThrow(() -> manager.exportToCsv(testCsvFile));
        
        // Clear database
        DatabaseUtils.saveToFile(new ArrayList<>());
        assertEquals(0, manager.getAllStudents().size());
        
        // Import back from CSV
        assertDoesNotThrow(() -> manager.importFromCsv(testCsvFile, false));
        
        // Verify all students are restored
        List<Student> restoredStudents = manager.getAllStudents();
        assertEquals(3, restoredStudents.size());
        
        // Verify specific students
        Student student1 = manager.getStudentById("001");
        assertNotNull(student1);
        assertEquals("张三", student1.getName());
        assertEquals("男", student1.getGender());
        assertEquals(20, student1.getAge());
        assertEquals(85.5, student1.getScore());
        
        Student student2 = manager.getStudentById("002");
        assertNotNull(student2);
        assertEquals("李四", student2.getName());
        assertEquals("女", student2.getGender());
        assertEquals(21, student2.getAge());
        assertEquals(92.0, student2.getScore());
        
        Student student3 = manager.getStudentById("003");
        assertNotNull(student3);
        assertEquals("王五", student3.getName());
        assertEquals("男", student3.getGender());
        assertEquals(22, student3.getAge());
        assertEquals(78.5, student3.getScore());
        
        // Clean up
        new File(testCsvFile).delete();
    }
    
    @Test
    void testExportToCsvWithInvalidPath() {
        StudentManager manager = new StudentManager();
        String invalidPath = "/invalid/path/test.csv";
        
        // Add test student
        manager.addStudent(new Student("001", "测试学生", "男", 20, 85.0));
        
        // Export to invalid path should not throw exception (graceful handling)
        assertDoesNotThrow(() -> manager.exportToCsv(invalidPath));
        
        // Verify file was not created in invalid location
        File invalidFile = new File(invalidPath);
        assertFalse(invalidFile.exists());
    }
    
    private void createTestCsvFile(String fileName, String content) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.print(content);
            }
        } catch (IOException e) {
            fail("Failed to create test CSV file: " + e.getMessage());
        }
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