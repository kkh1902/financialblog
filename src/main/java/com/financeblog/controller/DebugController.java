package com.financeblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private Environment env;
    
    @Value("${spring.profiles.active:default}")
    private String activeProfile;
    
    @GetMapping("/api/debug/database")
    public Map<String, String> getDatabaseInfo() {
        Map<String, String> info = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            info.put("profile", activeProfile);
            info.put("database_product", metaData.getDatabaseProductName());
            info.put("database_version", metaData.getDatabaseProductVersion());
            info.put("driver", metaData.getDriverName());
            info.put("url", metaData.getURL());
            info.put("username", metaData.getUserName());
            
            // 현재 사용 중인 DB 확인
            if (metaData.getURL().contains("h2")) {
                info.put("database_type", "H2 Database (In-Memory)");
            } else if (metaData.getURL().contains("postgresql")) {
                info.put("database_type", "PostgreSQL Database");
            } else {
                info.put("database_type", "Unknown");
            }
            
        } catch (Exception e) {
            info.put("error", e.getMessage());
        }
        
        return info;
    }
}