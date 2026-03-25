package org.example.boot;

import org.springframework.stereotype.Component;

/**
 * Test component for AutoScan
 */
@Component
public class TestComponent {
    
    public String getMessage() {
        return "Hello from TestComponent!";
    }
}
