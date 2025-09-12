package com.hsjack.service;

import com.hsjack.model.Student;
import com.hsjack.util.DatabaseUtils;
import com.hsjack.util.CsvUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private final GradeStatistics gradeStatistics;

    public StudentManager() {
        gradeStatistics = new GradeStatistics();
    }

    // 添加学生
    public void addStudent(Student student) {
        DatabaseUtils.addStudent(student);
    }

    // 根据ID删除学生
    public boolean removeStudentById(String id) {
        return DatabaseUtils.removeStudentById(id);
    }

    // 修改学生信息（根据ID查找并替换）
    public boolean updateStudent(String id, Student newStudent) {
        return DatabaseUtils.updateStudent(id, newStudent);
    }

    // 查询所有学生
    public List<Student> getAllStudents() {
        return DatabaseUtils.loadFromFile();
    }

    // 根据ID查询学生
    public Student getStudentById(String id) {
        return DatabaseUtils.getStudentById(id);
    }

    public void save() {
        // 使用数据库存储时，数据已经实时保存，此方法保持兼容性
        // 可以在这里添加额外的持久化逻辑，如备份等
    }

    public void exportToCsv(String fileName) {
        List<Student> students = getAllStudents();
        CsvUtils.exportToCsv(students, fileName);
    }

    public void importFromCsv(String fileName, boolean append) {
        List<Student> imported = CsvUtils.importFromCsv(fileName);
        if (!append) {
            // 清空现有数据
            DatabaseUtils.saveToFile(new ArrayList<>());
        }
        for (Student student : imported) {
            DatabaseUtils.addStudent(student);
        }
    }

    // 获取成绩统计分析服务
    public GradeStatistics getGradeStatistics() {
        return gradeStatistics;
    }
}
