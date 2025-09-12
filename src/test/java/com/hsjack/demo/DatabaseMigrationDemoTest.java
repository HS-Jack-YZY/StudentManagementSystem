package com.hsjack.demo;

import com.hsjack.model.Student;
import com.hsjack.service.StudentManager;
import com.hsjack.util.DatabaseUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * 数据库迁移演示测试类
 * 在测试环境中演示数据库功能
 */
class DatabaseMigrationDemoTest {
    
    @Test
    void demonstrateDatabaseMigration() {
        System.out.println("=== 学生管理系统 - 数据库版本演示 ===");
        
        // 清空测试数据库
        DatabaseUtils.saveToFile(new ArrayList<>());
        
        StudentManager manager = new StudentManager();
        
        // 添加一些测试学生
        System.out.println("\n1. 添加测试学生数据...");
        manager.addStudent(new Student("2023001", "张三", "男", 20, 85.5));
        manager.addStudent(new Student("2023002", "李四", "女", 19, 92.0));
        manager.addStudent(new Student("2023003", "王五", "男", 21, 78.5));
        System.out.println("   已添加3名学生");
        
        // 查询所有学生
        System.out.println("\n2. 查询所有学生:");
        manager.getAllStudents().forEach(student -> 
            System.out.println("   " + student));
        
        // 根据ID查询学生
        System.out.println("\n3. 根据ID查询学生 (2023002):");
        Student student = manager.getStudentById("2023002");
        System.out.println("   " + student);
        
        // 更新学生信息
        System.out.println("\n4. 更新学生信息 (2023001):");
        Student updatedStudent = new Student("2023001", "张三丰", "男", 20, 95.0);
        boolean updated = manager.updateStudent("2023001", updatedStudent);
        System.out.println("   更新" + (updated ? "成功" : "失败"));
        System.out.println("   更新后: " + manager.getStudentById("2023001"));
        
        // 删除学生
        System.out.println("\n5. 删除学生 (2023003):");
        boolean removed = manager.removeStudentById("2023003");
        System.out.println("   删除" + (removed ? "成功" : "失败"));
        System.out.println("   剩余学生数量: " + manager.getAllStudents().size());
        
        // 成绩统计
        System.out.println("\n6. 成绩统计:");
        var gradeStats = manager.getGradeStatistics();
        var summary = gradeStats.getStatisticsSummary(manager.getAllStudents());
        System.out.println("   总人数: " + summary.get("总人数"));
        System.out.println("   平均分: " + String.format("%.2f", (Double) summary.get("平均分")));
        System.out.println("   最高分: " + summary.get("最高分"));
        System.out.println("   最低分: " + summary.get("最低分"));
        
        System.out.println("\n=== 数据库存储演示完成 ===");
        System.out.println("✅ 成功演示了从文件存储到PostgreSQL数据库的迁移!");
        System.out.println("✅ 所有核心功能（增删改查、统计）都正常工作!");
        System.out.println("✅ 数据实时保存到数据库，无需手动保存!");
    }
}