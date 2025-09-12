package com.hsjack.demo;

import com.hsjack.model.Student;
import com.hsjack.service.StudentManager;

/**
 * 数据库迁移演示类
 * 展示从文件存储迁移到PostgreSQL数据库后的功能
 */
public class DatabaseMigrationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 学生管理系统 - 数据库版本演示 ===");
        
        StudentManager manager = new StudentManager();
        
        // 清空现有数据
        System.out.println("\n1. 清空现有数据...");
        
        // 添加一些测试学生
        System.out.println("\n2. 添加测试学生数据...");
        manager.addStudent(new Student("2023001", "张三", "男", 20, 85.5));
        manager.addStudent(new Student("2023002", "李四", "女", 19, 92.0));
        manager.addStudent(new Student("2023003", "王五", "男", 21, 78.5));
        System.out.println("   已添加3名学生");
        
        // 查询所有学生
        System.out.println("\n3. 查询所有学生:");
        manager.getAllStudents().forEach(System.out::println);
        
        // 根据ID查询学生
        System.out.println("\n4. 根据ID查询学生 (2023002):");
        Student student = manager.getStudentById("2023002");
        System.out.println("   " + student);
        
        // 更新学生信息
        System.out.println("\n5. 更新学生信息 (2023001):");
        Student updatedStudent = new Student("2023001", "张三丰", "男", 20, 95.0);
        boolean updated = manager.updateStudent("2023001", updatedStudent);
        System.out.println("   更新" + (updated ? "成功" : "失败"));
        System.out.println("   更新后: " + manager.getStudentById("2023001"));
        
        // 删除学生
        System.out.println("\n6. 删除学生 (2023003):");
        boolean removed = manager.removeStudentById("2023003");
        System.out.println("   删除" + (removed ? "成功" : "失败"));
        System.out.println("   剩余学生数量: " + manager.getAllStudents().size());
        
        // 成绩统计
        System.out.println("\n7. 成绩统计:");
        var gradeStats = manager.getGradeStatistics();
        var summary = gradeStats.getStatisticsSummary(manager.getAllStudents());
        System.out.println("   总人数: " + summary.get("总人数"));
        System.out.println("   平均分: " + String.format("%.2f", (Double) summary.get("平均分")));
        System.out.println("   最高分: " + summary.get("最高分"));
        System.out.println("   最低分: " + summary.get("最低分"));
        
        System.out.println("\n=== 数据库存储演示完成 ===");
        System.out.println("注意: 数据已保存到数据库中，重新运行程序时数据会保持。");
    }
}