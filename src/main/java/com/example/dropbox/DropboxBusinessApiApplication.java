package com.example.dropbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DropboxBusinessApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DropboxBusinessApiApplication.class, args);
        System.out.println("\nApplication started successfully!");
        System.out.println("Available endpoints:");
        System.out.println("   - http://localhost:8081/auth         (Perform OAuth)");
        System.out.println("   - http://localhost:8081/team-info    (Get team info)");
        System.out.println();
    }
}