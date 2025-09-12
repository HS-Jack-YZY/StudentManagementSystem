package com.hsjack.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final String CONFIG_FILE = "database.properties";
    private static Properties properties = new Properties();
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        try (InputStream is = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                properties.load(is);
            } else {
                // 使用默认配置
                setDefaultConfig();
            }
        } catch (IOException e) {
            System.out.println("加载数据库配置失败，使用默认配置: " + e.getMessage());
            setDefaultConfig();
        }
    }
    
    private static void setDefaultConfig() {
        properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/student_management");
        properties.setProperty("db.username", "postgres");
        properties.setProperty("db.password", "password");
        properties.setProperty("db.driver", "org.postgresql.Driver");
    }
    
    public static String getUrl() {
        return properties.getProperty("db.url");
    }
    
    public static String getUsername() {
        return properties.getProperty("db.username");
    }
    
    public static String getPassword() {
        return properties.getProperty("db.password");
    }
    
    public static String getDriver() {
        return properties.getProperty("db.driver");
    }
}