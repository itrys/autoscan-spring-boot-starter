package org.example.business;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test REST controller with @RestController annotation
 */
@RestController
public class TestRestController {
    
    @GetMapping("/api/test")
    public String test(@RequestParam("name") String name) {
        return "Hello, " + name + "!";
    }
    
    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }
}
