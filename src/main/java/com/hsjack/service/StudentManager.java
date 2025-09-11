package com.hsjack.service;

import com.hsjack.model.Student;
import com.hsjack.util.FileUtils;
import com.hsjack.util.CsvUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private final List<Student> students;
    private final GradeStatistics gradeStatistics;

    public StudentManager() {
        students = FileUtils.loadFromFile();
        gradeStatistics = new GradeStatistics();
    }

    // 添加学生
    public void addStudent(Student student) {
        students.add(student);
    }

    // 根据ID删除学生
    public boolean removeStudentById(String id) {
        return students.removeIf(s -> s.getId().equals(id));
    }

    // 修改学生信息（根据ID查找并替换）
    public boolean updateStudent(String id, Student newStudent) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                s.setName(newStudent.getName());
                s.setGender(newStudent.getGender());
                s.setAge(newStudent.getAge());
                s.setScore(newStudent.getScore());
                return true;
            }
        }
        return false;
    }

    // 查询所有学生
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // 根据ID查询学生
    public Student getStudentById(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public void save() {
        FileUtils.saveToFile(students);
    }

    public void exportToCsv(String fileName) {
        CsvUtils.exportToCsv(students, fileName);
    }

    public void importFromCsv(String fileName, boolean append) {
        List<Student> imported = CsvUtils.importFromCsv(fileName);
        if (append) {
            students.addAll(imported);
        } else {
            students.clear();
            students.addAll(imported);
        }
    }

    // 获取成绩统计分析服务
    public GradeStatistics getGradeStatistics() {
        return gradeStatistics;
    }
}
