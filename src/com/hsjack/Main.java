package com.hsjack;

import com.hsjack.modle.Student;
import com.hsjack.service.StudentManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StudentManager manager = new StudentManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n学生信息管理系统");
            System.out.println("1. 添加学生");
            System.out.println("2. 查询所有学生");
            System.out.println("3. 删除学生");
            System.out.println("4. 修改学生");
            System.out.println("5. 查询学生（按ID）");
            System.out.println("0. 退出");
            System.out.print("请选择操作：");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("学号: ");
                    String id = scanner.nextLine();
                    System.out.print("姓名: ");
                    String name = scanner.nextLine();
                    System.out.print("性别: ");
                    String gender = scanner.nextLine();
                    System.out.print("年龄: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("成绩: ");
                    double score = scanner.nextDouble();
                    scanner.nextLine();
                    Student student = new Student(id, name, gender, age, score);
                    manager.addStudent(student);
                    System.out.println("学生添加成功！");
                    break;
                case 2:
                    System.out.println("所有学生信息：");
                    for (Student s : manager.getAllStudents()) {
                        System.out.println(s);
                    }
                    break;
                case 3:
                    System.out.print("请输入要删除的学生学号: ");
                    String removeId = scanner.nextLine();
                    boolean removed = manager.removeStudentByID(removeId);
                    System.out.println(removed ? "删除成功！" : "未找到该学生！");
                    break;
                case 4:
                    System.out.println("请输入要修改的学生学号：");
                    // 检验是否有该学生
                    String updateID = scanner.nextLine();
                    Student oldStu = manager.getStudentById(updateID);
                    if (oldStu == null) {
                        System.out.println("未找到该学生！");
                    } else {
                        System.out.print("新姓名: ");
                        String newName = scanner.nextLine();
                        System.out.print("新性别: ");
                        String newGender = scanner.nextLine();
                        System.out.print("新年龄: ");
                        int newAge = scanner.nextInt();
                        System.out.print("新成绩: ");
                        double newScore = scanner.nextDouble();
                        scanner.nextLine();
                        Student newStu = new Student(updateID, newName, newGender, newAge, newScore);
                        boolean updated = manager.updateStudent(updateID, newStu);
                        System.out.println(updated ? "修改成功！" : "修改失败！");
                    }
                    break;
                case 5:
                    System.out.print("请输入学生学号: ");
                    String queryId = scanner.nextLine();
                    Student found = manager.getStudentById(queryId);
                    System.out.println(found != null ? found : "未找到该学生！");
                    break;
                case 0:
                    System.out.println("退出系统");
                    return;
                default:
                    System.out.println("无效选项，请重新输入。");
            }
        }
    }
}
