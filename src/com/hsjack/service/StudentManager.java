package com.hsjack.service;

import com.hsjack.modle.Student;
import java.util.List;
import java.util.ArrayList;

public class StudentManager {
    private final List<Student> students = new ArrayList<>();

    // 添加学生
    public void addStudent(Student student){
        students.add(student);
    }

    // 根据ID删除学生
    public boolean removeStudentByID(String id) {
        return students.removeIf(s -> s.getId().equals(id));
    }

    // 修改学生信息（根据ID查找并替换）
    public boolean updateStudent(String id, Student newStudent){
        for(Student s: students){
            if(s.getId().equals(id)){
                s.setName(newStudent.getName());
                s.setAge(newStudent.getAge());
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
    public Student getStudentById(String id){
        for(Student s: students){
            if (s.getId().equals(id)){
                return s;
            }
        }
        return null;
    }
}
