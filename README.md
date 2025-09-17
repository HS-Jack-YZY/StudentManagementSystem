# 学生信息管理系统 (Student Management System)

一个基于Java的学生信息管理系统，支持学生信息的增删改查、成绩统计分析、CSV导入导出等功能。

## 版本更新

**最新版本**: 已将文件存储替换为PostgreSQL数据库存储

## 功能特性

- 学生信息管理（增删改查）
- 成绩统计与分析
- CSV文件导入导出
- PostgreSQL数据库存储
- 完整的单元测试覆盖

## 技术栈

- Java 21
- PostgreSQL数据库
- Gradle构建工具
- JUnit 5测试框架

## 数据库配置

### PostgreSQL安装和配置

1. 安装PostgreSQL数据库
2. 创建数据库：
```sql
CREATE DATABASE student_management;
```

3. 修改数据库配置文件 `src/main/resources/database.properties`：
```properties
db.url=jdbc:postgresql://localhost:5432/student_management
db.username=your_username
db.password=your_password
db.driver=org.postgresql.Driver
```

### 测试环境

测试环境使用H2内存数据库，无需额外配置。

## 构建和运行

### 构建项目
```bash
./gradlew build
```

### 运行测试
```bash
./gradlew test
```

### 运行主程序
```bash
./gradlew run
```

## 系统架构

- `com.hsjack.model.Student`: 学生实体类
- `com.hsjack.service.StudentManager`: 学生管理服务类
- `com.hsjack.util.DatabaseUtils`: 数据库操作工具类
- `com.hsjack.util.CsvUtils`: CSV导入导出工具类
- `com.hsjack.config.DatabaseConfig`: 数据库配置类
- `com.hsjack.Main`: 主程序入口

## 数据库表结构

```sql
CREATE TABLE students (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    gender VARCHAR(10),
    age INTEGER,
    score DECIMAL(5,2)
);
```

## 从文件存储迁移说明

本系统已从原来的文件存储（students.dat）迁移到PostgreSQL数据库存储。主要变更：

1. **存储方式**: 从序列化文件存储改为PostgreSQL数据库存储
2. **ID唯一性**: 数据库版本中学生ID是唯一的（主键约束）
3. **实时持久化**: 数据修改立即保存到数据库，无需手动调用save()方法
4. **向后兼容**: 保持了原有的API接口，现有代码无需修改

## API接口

### StudentManager主要方法

- `addStudent(Student student)`: 添加学生
- `removeStudentById(String id)`: 删除学生
- `updateStudent(String id, Student newStudent)`: 更新学生信息
- `getStudentById(String id)`: 根据ID查询学生
- `getAllStudents()`: 获取所有学生
- `exportToCsv(String fileName)`: 导出到CSV
- `importFromCsv(String fileName, boolean append)`: 从CSV导入

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。