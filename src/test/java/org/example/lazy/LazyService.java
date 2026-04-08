package org.example.lazy;

import org.springframework.stereotype.Service;

@Service
public class LazyService {
    
    private static boolean initialized = false;
    
    public LazyService() {
        initialized = true;
        System.out.println("LazyService initialized");
    }
    
    public String getMessage() {
        return "Hello from LazyService";
    }
    
    public static boolean isInitialized() {
        return initialized;
    }
    
    public static void reset() {
        initialized = false;
    }
}