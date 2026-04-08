package org.example.lazy.another;

import org.springframework.stereotype.Service;

@Service
public class AnotherLazyService {
    
    private static boolean initialized = false;
    
    public AnotherLazyService() {
        initialized = true;
        System.out.println("AnotherLazyService initialized");
    }
    
    public String getMessage() {
        return "Hello from AnotherLazyService";
    }
    
    public static boolean isInitialized() {
        return initialized;
    }
    
    public static void reset() {
        initialized = false;
    }
}