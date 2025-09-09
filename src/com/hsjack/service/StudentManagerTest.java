package com.hsjack.service;

import com.hsjack.model.Student;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentManagerTest {
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
    void testUpdateStudent() {
        StudentManager manager = new StudentManager();
        Student s = new Student("003", "Amy", "女", 19, 92.0);
        manager.addStudent(s);

        Student newS = new Student("003", "Amy Lee", "女", 19, 95.0);
        boolean updated = manager.updateStudent("003", newS);
        assertTrue(updated);

        Student found = manager.getStudentById("003");
        assertEquals("Amy Lee", found.getName());
        assertEquals(95.0, found.getScore());
    }
}