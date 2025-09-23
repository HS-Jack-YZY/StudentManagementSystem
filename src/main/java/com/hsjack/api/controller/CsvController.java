package com.hsjack.api.controller;

import com.hsjack.api.dto.ApiResponse;
import com.hsjack.service.StudentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * REST Controller for CSV Import/Export operations
 * Provides API endpoints for data import and export functionality
 */
@RestController
@RequestMapping("/api/csv")
@CrossOrigin(origins = "*")
public class CsvController {
    
    private final StudentManager studentManager;
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    
    @Autowired
    public CsvController(StudentManager studentManager) {
        this.studentManager = studentManager;
    }
    
    /**
     * Export students data to CSV
     * GET /api/csv/export
     */
    @GetMapping("/export")
    public ResponseEntity<?> exportToCsv(@RequestParam(defaultValue = "students.csv") String filename) {
        try {
            // Create temporary file for export
            String tempFileName = TEMP_DIR + File.separator + "export_" + System.currentTimeMillis() + "_" + filename;
            studentManager.exportToCsv(tempFileName);
            
            File file = new File(tempFileName);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Failed to generate CSV file"));
            }
            
            // Read file content
            byte[] fileContent = Files.readAllBytes(file.toPath());
            
            // Clean up temporary file
            file.delete();
            
            // Return file as download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(fileContent.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to export CSV: " + e.getMessage()));
        }
    }
    
    /**
     * Import students data from CSV
     * POST /api/csv/import
     */
    @PostMapping("/import")
    public ResponseEntity<ApiResponse<String>> importFromCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "false") boolean append) {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File is empty"));
        }
        
        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File must be a CSV file"));
        }
        
        try {
            // Save uploaded file temporarily
            String tempFileName = TEMP_DIR + File.separator + "import_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path tempPath = Paths.get(tempFileName);
            Files.write(tempPath, file.getBytes());
            
            // Import from temporary file
            studentManager.importFromCsv(tempFileName, append);
            
            // Clean up temporary file
            Files.delete(tempPath);
            
            String message = append ? "Students imported and appended successfully" : "Students imported and existing data replaced";
            return ResponseEntity.ok(ApiResponse.success("Import completed", message));
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to process uploaded file: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to import CSV: " + e.getMessage()));
        }
    }
    
    /**
     * Get import/export status
     * GET /api/csv/status
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> getCsvStatus() {
        return ResponseEntity.ok(ApiResponse.success("CSV service is operational", "Ready for import/export operations"));
    }
}