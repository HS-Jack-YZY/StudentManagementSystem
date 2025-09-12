package com.hsjack.util;

import com.hsjack.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUtilsTest {
    
    @BeforeEach
    void setUp() {
        // 清空测试数据库中的数据
        DatabaseUtils.saveToFile(new ArrayList<>());
    }
    
    @Test
    void testAddAndGetStudent() {
        Student student = new Student("001", "张三", "男", 20, 85.5);
        
        boolean added = DatabaseUtils.addStudent(student);
        assertTrue(added);
        
        Student retrieved = DatabaseUtils.getStudentById("001");
        assertNotNull(retrieved);
        assertEquals("001", retrieved.getId());
        assertEquals("张三", retrieved.getName());
        assertEquals("男", retrieved.getGender());
        assertEquals(20, retrieved.getAge());
        assertEquals(85.5, retrieved.getScore());
    }
    
    @Test
    void testGetNonExistentStudent() {
        Student student = DatabaseUtils.getStudentById("999");
        assertNull(student);
    }
    
    @Test
    void testRemoveStudent() {
        Student student = new Student("002", "李四", "女", 21, 90.0);
        DatabaseUtils.addStudent(student);
        
        boolean removed = DatabaseUtils.removeStudentById("002");
        assertTrue(removed);
        
        Student retrieved = DatabaseUtils.getStudentById("002");
        assertNull(retrieved);
    }
    
    @Test
    void testRemoveNonExistentStudent() {
        boolean removed = DatabaseUtils.removeStudentById("999");
        assertFalse(removed);
    }
    
    @Test
    void testUpdateStudent() {
        Student original = new Student("003", "王五", "男", 22, 88.0);
        DatabaseUtils.addStudent(original);
        
        Student updated = new Student("003", "王五新", "女", 23, 95.0);
        boolean result = DatabaseUtils.updateStudent("003", updated);
        assertTrue(result);
        
        Student retrieved = DatabaseUtils.getStudentById("003");
        assertNotNull(retrieved);
        assertEquals("003", retrieved.getId());
        assertEquals("王五新", retrieved.getName());
        assertEquals("女", retrieved.getGender());
        assertEquals(23, retrieved.getAge());
        assertEquals(95.0, retrieved.getScore());
    }
    
    @Test
    void testUpdateNonExistentStudent() {
        Student student = new Student("999", "不存在", "男", 20, 80.0);
        boolean result = DatabaseUtils.updateStudent("999", student);
        assertFalse(result);
    }
    
    @Test
    void testSaveAndLoadMultipleStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("004", "赵六", "男", 19, 78.0));
        students.add(new Student("005", "钱七", "女", 20, 92.0));
        students.add(new Student("006", "孙八", "男", 21, 85.0));
        
        DatabaseUtils.saveToFile(students);
        
        List<Student> loaded = DatabaseUtils.loadFromFile();
        assertEquals(3, loaded.size());
        
        // 验证数据按ID排序
        assertEquals("004", loaded.get(0).getId());
        assertEquals("005", loaded.get(1).getId());
        assertEquals("006", loaded.get(2).getId());
    }
    
    @Test
    void testSaveOverwritesExistingData() {
        // 先添加一个学生
        DatabaseUtils.addStudent(new Student("007", "初始", "男", 20, 80.0));
        
        // 验证学生存在
        assertNotNull(DatabaseUtils.getStudentById("007"));
        
        // 保存新的学生列表（应该覆盖现有数据）
        List<Student> newStudents = new ArrayList<>();
        newStudents.add(new Student("008", "新学生", "女", 21, 90.0));
        
        DatabaseUtils.saveToFile(newStudents);
        
        // 验证旧数据被清除，新数据存在
        assertNull(DatabaseUtils.getStudentById("007"));
        assertNotNull(DatabaseUtils.getStudentById("008"));
        
        List<Student> loaded = DatabaseUtils.loadFromFile();
        assertEquals(1, loaded.size());
        assertEquals("008", loaded.get(0).getId());
    }
    
    @Test
    void testLoadFromEmptyDatabase() {
        List<Student> students = DatabaseUtils.loadFromFile();
        assertNotNull(students);
        assertTrue(students.isEmpty());
    }
}