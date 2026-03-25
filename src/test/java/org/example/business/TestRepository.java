package org.example.business;

import org.springframework.stereotype.Repository;

/**
 * Test repository with @Repository annotation
 */
@Repository
public class TestRepository {
    
    public String findById(Long id) {
        return "Entity " + id;
    }
    
    public void save(String data) {
        // Simulate save operation
        System.out.println("Saved: " + data);
    }
}
