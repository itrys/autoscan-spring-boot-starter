package org.example.lazy;

import org.springframework.stereotype.Controller;

@Controller
public class LazyController {
    
    private static boolean initialized = false;
    
    public LazyController() {
        initialized = true;
        System.out.println("LazyController initialized");
    }
    
    public String getMessage() {
        return "Hello from LazyController";
    }
    
    public static boolean isInitialized() {
        return initialized;
    }
    
    public static void reset() {
        initialized = false;
    }
}