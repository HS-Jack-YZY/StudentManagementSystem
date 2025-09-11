package com.hsjack.service;

import com.hsjack.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class GradeStatisticsTest {
    
    private GradeStatistics gradeStatistics;
    private List<Student> students;
    
    @BeforeEach
    void setUp() {
        gradeStatistics = new GradeStatistics();
        students = new ArrayList<>();
    }
    
    @Test
    void testCalculateAverageScoreWithEmptyList() {
        // Test with empty student list
        double average = gradeStatistics.calculateAverageScore(students);
        assertEquals(0.0, average, 0.001);
    }
    
    @Test
    void testCalculateAverageScoreWithSingleStudent() {
        students.add(new Student("001", "张三", "男", 20, 85.0));
        
        double average = gradeStatistics.calculateAverageScore(students);
        assertEquals(85.0, average, 0.001);
    }
    
    @Test
    void testCalculateAverageScoreWithMultipleStudents() {
        students.add(new Student("001", "张三", "男", 20, 80.0));
        students.add(new Student("002", "李四", "女", 21, 90.0));
        students.add(new Student("003", "王五", "男", 22, 70.0));
        
        double average = gradeStatistics.calculateAverageScore(students);
        assertEquals(80.0, average, 0.001);
    }
    
    @Test
    void testFindHighestScoreWithEmptyList() {
        double highest = gradeStatistics.findHighestScore(students);
        assertEquals(0.0, highest, 0.001);
    }
    
    @Test
    void testFindHighestScoreWithMultipleStudents() {
        students.add(new Student("001", "张三", "男", 20, 80.0));
        students.add(new Student("002", "李四", "女", 21, 95.0));
        students.add(new Student("003", "王五", "男", 22, 70.0));
        
        double highest = gradeStatistics.findHighestScore(students);
        assertEquals(95.0, highest, 0.001);
    }
    
    @Test
    void testFindLowestScoreWithEmptyList() {
        double lowest = gradeStatistics.findLowestScore(students);
        assertEquals(0.0, lowest, 0.001);
    }
    
    @Test
    void testFindLowestScoreWithMultipleStudents() {
        students.add(new Student("001", "张三", "男", 20, 80.0));
        students.add(new Student("002", "李四", "女", 21, 95.0));
        students.add(new Student("003", "王五", "男", 22, 70.0));
        
        double lowest = gradeStatistics.findLowestScore(students);
        assertEquals(70.0, lowest, 0.001);
    }
    
    @Test
    void testGetScoreDistributionWithEmptyList() {
        Map<String, Integer> distribution = gradeStatistics.getScoreDistribution(students);
        assertNotNull(distribution);
        assertEquals(0, distribution.get("优秀(90-100)"));
        assertEquals(0, distribution.get("良好(80-89)"));
        assertEquals(0, distribution.get("中等(70-79)"));
        assertEquals(0, distribution.get("及格(60-69)"));
        assertEquals(0, distribution.get("不及格(0-59)"));
    }
    
    @Test
    void testGetScoreDistributionWithVariousScores() {
        students.add(new Student("001", "张三", "男", 20, 95.0)); // 优秀
        students.add(new Student("002", "李四", "女", 21, 85.0)); // 良好
        students.add(new Student("003", "王五", "男", 22, 75.0)); // 中等
        students.add(new Student("004", "赵六", "女", 23, 65.0)); // 及格
        students.add(new Student("005", "钱七", "男", 24, 50.0)); // 不及格
        
        Map<String, Integer> distribution = gradeStatistics.getScoreDistribution(students);
        assertEquals(1, distribution.get("优秀(90-100)"));
        assertEquals(1, distribution.get("良好(80-89)"));
        assertEquals(1, distribution.get("中等(70-79)"));
        assertEquals(1, distribution.get("及格(60-69)"));
        assertEquals(1, distribution.get("不及格(0-59)"));
    }
    
    @Test
    void testGetStatisticsSummaryWithMultipleStudents() {
        students.add(new Student("001", "张三", "男", 20, 80.0));
        students.add(new Student("002", "李四", "女", 21, 90.0));
        students.add(new Student("003", "王五", "男", 22, 70.0));
        students.add(new Student("004", "赵六", "女", 23, 85.0));
        
        Map<String, Object> summary = gradeStatistics.getStatisticsSummary(students);
        assertNotNull(summary);
        assertEquals(4, summary.get("总人数"));
        assertEquals(81.25, (Double) summary.get("平均分"), 0.001);
        assertEquals(90.0, (Double) summary.get("最高分"), 0.001);
        assertEquals(70.0, (Double) summary.get("最低分"), 0.001);
    }
    
    @Test
    void testGetStatisticsByGender() {
        students.add(new Student("001", "张三", "男", 20, 80.0));
        students.add(new Student("002", "李四", "女", 21, 90.0));
        students.add(new Student("003", "王五", "男", 22, 85.0));
        students.add(new Student("004", "赵六", "女", 23, 95.0));
        
        Map<String, Map<String, Object>> genderStats = gradeStatistics.getStatisticsByGender(students);
        
        Map<String, Object> maleStats = genderStats.get("男");
        assertEquals(2, maleStats.get("人数"));
        assertEquals(82.5, (Double) maleStats.get("平均分"), 0.001);
        
        Map<String, Object> femaleStats = genderStats.get("女");
        assertEquals(2, femaleStats.get("人数"));
        assertEquals(92.5, (Double) femaleStats.get("平均分"), 0.001);
    }
}