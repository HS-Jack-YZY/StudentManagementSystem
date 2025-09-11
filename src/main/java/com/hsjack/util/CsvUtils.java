package com.hsjack.util;

import com.hsjack.model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {
    public static void exportToCsv(List<Student> students, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println("id,name,gender,age,score");
            for (Student s : students) {
                pw.printf("%s,%s,%s,%d,%.2f\n",
                        s.getId(), s.getName(), s.getGender(), s.getAge(), s.getScore());
            }
            System.out.println("数据已导出至" + fileName);
        } catch (Exception e) {
            System.out.println("导出失败：" + e.getMessage());
        }
    }

    public static List<Student> importFromCsv(String fileName) {
        List<Student> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] arr = line.split(",");
                if (arr.length == 5) {
                    String id = arr[0];
                    String name = arr[1];
                    String gender = arr[2];
                    int age = Integer.parseInt(arr[3]);
                    double score = Double.parseDouble(arr[4]);
                    result.add(new Student(id, name, gender, age, score));
                }
            }
            System.out.println("已从 " + fileName + " 导入 " + result.size() + " 条数据");
        }  catch (IOException e) {
            System.out.println("导入失败：" + e.getMessage());
        }
        return result;
    }
}
