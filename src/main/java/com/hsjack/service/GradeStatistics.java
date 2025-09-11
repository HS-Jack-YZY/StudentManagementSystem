package com.hsjack.service;

import com.hsjack.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成绩统计与分析服务类
 * 提供学生成绩的各种统计分析功能
 */
public class GradeStatistics {
    
    /**
     * 计算学生平均成绩
     * @param students 学生列表
     * @return 平均成绩，空列表返回0.0
     */
    public double calculateAverageScore(List<Student> students) {
        if (students == null || students.isEmpty()) {
            return 0.0;
        }
        
        double sum = students.stream()
                .mapToDouble(Student::getScore)
                .sum();
        
        return sum / students.size();
    }
    
    /**
     * 找出最高成绩
     * @param students 学生列表
     * @return 最高成绩，空列表返回0.0
     */
    public double findHighestScore(List<Student> students) {
        if (students == null || students.isEmpty()) {
            return 0.0;
        }
        
        return students.stream()
                .mapToDouble(Student::getScore)
                .max()
                .orElse(0.0);
    }
    
    /**
     * 找出最低成绩
     * @param students 学生列表
     * @return 最低成绩，空列表返回0.0
     */
    public double findLowestScore(List<Student> students) {
        if (students == null || students.isEmpty()) {
            return 0.0;
        }
        
        return students.stream()
                .mapToDouble(Student::getScore)
                .min()
                .orElse(0.0);
    }
    
    /**
     * 获取成绩分布统计
     * @param students 学生列表
     * @return 各分数段的学生数量分布
     */
    public Map<String, Integer> getScoreDistribution(List<Student> students) {
        Map<String, Integer> distribution = new HashMap<>();
        
        // 初始化各分数段
        distribution.put("优秀(90-100)", 0);
        distribution.put("良好(80-89)", 0);
        distribution.put("中等(70-79)", 0);
        distribution.put("及格(60-69)", 0);
        distribution.put("不及格(0-59)", 0);
        
        if (students == null || students.isEmpty()) {
            return distribution;
        }
        
        for (Student student : students) {
            double score = student.getScore();
            if (score >= 90) {
                distribution.put("优秀(90-100)", distribution.get("优秀(90-100)") + 1);
            } else if (score >= 80) {
                distribution.put("良好(80-89)", distribution.get("良好(80-89)") + 1);
            } else if (score >= 70) {
                distribution.put("中等(70-79)", distribution.get("中等(70-79)") + 1);
            } else if (score >= 60) {
                distribution.put("及格(60-69)", distribution.get("及格(60-69)") + 1);
            } else {
                distribution.put("不及格(0-59)", distribution.get("不及格(0-59)") + 1);
            }
        }
        
        return distribution;
    }
    
    /**
     * 获取统计摘要信息
     * @param students 学生列表
     * @return 包含总人数、平均分、最高分、最低分的统计摘要
     */
    public Map<String, Object> getStatisticsSummary(List<Student> students) {
        Map<String, Object> summary = new HashMap<>();
        
        if (students == null || students.isEmpty()) {
            summary.put("总人数", 0);
            summary.put("平均分", 0.0);
            summary.put("最高分", 0.0);
            summary.put("最低分", 0.0);
            return summary;
        }
        
        summary.put("总人数", students.size());
        summary.put("平均分", calculateAverageScore(students));
        summary.put("最高分", findHighestScore(students));
        summary.put("最低分", findLowestScore(students));
        
        return summary;
    }
    
    /**
     * 按性别统计成绩
     * @param students 学生列表
     * @return 按性别分组的统计信息
     */
    public Map<String, Map<String, Object>> getStatisticsByGender(List<Student> students) {
        Map<String, Map<String, Object>> genderStats = new HashMap<>();
        
        if (students == null || students.isEmpty()) {
            return genderStats;
        }
        
        // 按性别分组
        Map<String, List<Student>> genderGroups = new HashMap<>();
        for (Student student : students) {
            String gender = student.getGender();
            genderGroups.computeIfAbsent(gender, k -> new java.util.ArrayList<>()).add(student);
        }
        
        // 为每个性别组计算统计数据
        for (Map.Entry<String, List<Student>> entry : genderGroups.entrySet()) {
            String gender = entry.getKey();
            List<Student> genderStudents = entry.getValue();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("人数", genderStudents.size());
            stats.put("平均分", calculateAverageScore(genderStudents));
            stats.put("最高分", findHighestScore(genderStudents));
            stats.put("最低分", findLowestScore(genderStudents));
            
            genderStats.put(gender, stats);
        }
        
        return genderStats;
    }
}