package com.hsjack.util;

import com.hsjack.config.DatabaseConfig;
import com.hsjack.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {

    static {
        try {
            Class.forName(DatabaseConfig.getDriver());
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found: " + e.getMessage(), e);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword()
        );
    }

    private static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            createStudentsTable(conn);
        } catch (SQLException e) {
            System.out.println("数据库初始化失败: " + e.getMessage());
            // 如果数据库连接失败，不抛出异常，让程序继续运行
        }
    }

    private static void createStudentsTable(Connection conn) throws SQLException {
        String sql = """
                    CREATE TABLE IF NOT EXISTS students (
                        id VARCHAR(50) PRIMARY KEY,
                        name VARCHAR(100),
                        gender VARCHAR(10),
                        age INTEGER,
                        score DECIMAL(5,2)
                    )
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static void saveToFile(List<Student> students) {
        try (Connection conn = getConnection()) {
            // 清空现有数据
            clearAllStudents(conn);

            // 插入新数据
            String sql = "INSERT INTO students (id, name, gender, age, score) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Student student : students) {
                    pstmt.setString(1, student.getId());
                    pstmt.setString(2, student.getName());
                    pstmt.setString(3, student.getGender());
                    pstmt.setInt(4, student.getAge());
                    pstmt.setDouble(5, student.getScore());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        } catch (SQLException e) {
            System.out.println("保存数据到数据库失败：" + e.getMessage());
        }
    }

    public static List<Student> loadFromFile() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = getConnection()) {
            String sql = "SELECT id, name, gender, age, score FROM students ORDER BY id";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Student student = new Student(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getDouble("score")
                    );
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            System.out.println("从数据库加载数据失败：" + e.getMessage());
            // 返回空列表，与原FileUtils行为一致
        }

        return students;
    }

    private static void clearAllStudents(Connection conn) throws SQLException {
        String sql = "DELETE FROM students";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    // 新增的数据库特有方法，用于更高效的单个操作
    public static boolean addStudent(Student student) {
        try (Connection conn = getConnection()) {
            // 首先尝试插入，如果ID已存在，则更新
            String insertSql = "INSERT INTO students (id, name, gender, age, score) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, student.getId());
                pstmt.setString(2, student.getName());
                pstmt.setString(3, student.getGender());
                pstmt.setInt(4, student.getAge());
                pstmt.setDouble(5, student.getScore());
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                // 如果是主键冲突，尝试更新现有记录
                if (e.getSQLState() != null && e.getSQLState().startsWith("23")) { // 主键冲突
                    return updateStudent(student.getId(), student);
                }
                System.out.println("添加学生到数据库失败：" + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("添加学生到数据库失败：" + e.getMessage());
            return false;
        }
    }

    public static boolean removeStudentById(String id) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM students WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("从数据库删除学生失败：" + e.getMessage());
            return false;
        }
    }

    public static boolean updateStudent(String id, Student newStudent) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE students SET name = ?, gender = ?, age = ?, score = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newStudent.getName());
                pstmt.setString(2, newStudent.getGender());
                pstmt.setInt(3, newStudent.getAge());
                pstmt.setDouble(4, newStudent.getScore());
                pstmt.setString(5, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("更新数据库中的学生信息失败：" + e.getMessage());
            return false;
        }
    }

    public static Student getStudentById(String id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT id, name, gender, age, score FROM students WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Student(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("gender"),
                                rs.getInt("age"),
                                rs.getDouble("score")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("从数据库查询学生失败：" + e.getMessage());
        }
        return null;
    }
}