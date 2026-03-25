package org.example.business;

import org.springframework.stereotype.Service;

/**
 * Test service with @Service annotation
 */
@Service
public class TestBusinessService {
    
    public String getServiceName() {
        return "TestService";
    }
    
    public String processData(String input) {
        return "Processed: " + input;
    }
}
