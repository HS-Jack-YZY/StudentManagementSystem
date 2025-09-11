package com.hsjack;

import com.hsjack.model.Student;
import com.hsjack.service.StudentManager;
import com.hsjack.service.GradeStatistics;

import java.util.Scanner;
import java.util.List;
import java.util.Map;

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
            System.out.println("6. 成绩统计与分析");
            System.out.println("0. 退出");
            System.out.print("请选择操作：");

            try {
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
                        boolean removed = manager.removeStudentById(removeId);
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
                    case 6:
                        showGradeStatistics(manager, scanner);
                        break;
                    case 0:
                        manager.save();
                        System.out.println("退出系统");
                        return;
                    default:
                        System.out.println("无效选项，请重新输入。");
                }
            } catch (Exception e) {
                System.out.println("输入错误，请输入数字！");
                scanner.nextLine(); // 清空错误输入
                continue;
            }
        }
    }
    
    /**
     * 显示成绩统计与分析菜单
     */
    private static void showGradeStatistics(StudentManager manager, Scanner scanner) {
        List<Student> students = manager.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("当前没有学生数据，无法进行统计分析！");
            return;
        }
        
        GradeStatistics gradeStats = manager.getGradeStatistics();
        
        System.out.println("\n=== 成绩统计与分析 ===");
        System.out.println("1. 基础统计信息");
        System.out.println("2. 成绩分布统计");
        System.out.println("3. 按性别统计");
        System.out.println("0. 返回主菜单");
        System.out.print("请选择统计类型：");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    showBasicStatistics(students, gradeStats);
                    break;
                case 2:
                    showScoreDistribution(students, gradeStats);
                    break;
                case 3:
                    showGenderStatistics(students, gradeStats);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效选项！");
            }
        } catch (Exception e) {
            System.out.println("输入错误，请输入数字！");
            scanner.nextLine();
        }
    }
    
    /**
     * 显示基础统计信息
     */
    private static void showBasicStatistics(List<Student> students, GradeStatistics gradeStats) {
        Map<String, Object> summary = gradeStats.getStatisticsSummary(students);
        
        System.out.println("\n=== 基础统计信息 ===");
        System.out.println("总人数：" + summary.get("总人数"));
        System.out.printf("平均分：%.2f\n", (Double) summary.get("平均分"));
        System.out.printf("最高分：%.1f\n", (Double) summary.get("最高分"));
        System.out.printf("最低分：%.1f\n", (Double) summary.get("最低分"));
    }
    
    /**
     * 显示成绩分布统计
     */
    private static void showScoreDistribution(List<Student> students, GradeStatistics gradeStats) {
        Map<String, Integer> distribution = gradeStats.getScoreDistribution(students);
        
        System.out.println("\n=== 成绩分布统计 ===");
        System.out.println("优秀(90-100)：" + distribution.get("优秀(90-100)") + " 人");
        System.out.println("良好(80-89)：" + distribution.get("良好(80-89)") + " 人");
        System.out.println("中等(70-79)：" + distribution.get("中等(70-79)") + " 人");
        System.out.println("及格(60-69)：" + distribution.get("及格(60-69)") + " 人");
        System.out.println("不及格(0-59)：" + distribution.get("不及格(0-59)") + " 人");
    }
    
    /**
     * 显示按性别统计的信息
     */
    private static void showGenderStatistics(List<Student> students, GradeStatistics gradeStats) {
        Map<String, Map<String, Object>> genderStats = gradeStats.getStatisticsByGender(students);
        
        System.out.println("\n=== 按性别统计 ===");
        for (Map.Entry<String, Map<String, Object>> entry : genderStats.entrySet()) {
            String gender = entry.getKey();
            Map<String, Object> stats = entry.getValue();
            
            System.out.println(gender + "生统计：");
            System.out.println("  人数：" + stats.get("人数"));
            System.out.printf("  平均分：%.2f\n", (Double) stats.get("平均分"));
            System.out.printf("  最高分：%.1f\n", (Double) stats.get("最高分"));
            System.out.printf("  最低分：%.1f\n", (Double) stats.get("最低分"));
        }
    }
}
