package com.hsjack.util;

import com.hsjack.model.Student;

import java.io.*;
import java.util.List;

public class FileUtils {
    private static final String FILE_NAME = "students.dat";

    // 保存学生列表到文件
    public static void saveToFile(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("保存数据失败：" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Student> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            // 第一次运行没有文件，返回空列表
            return new java.util.ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("加载数据失败：" + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}
