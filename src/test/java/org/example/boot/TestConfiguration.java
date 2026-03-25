package org.example.boot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Test configuration for AutoScan
 */
@Configuration
public class TestConfiguration {
    
    @Bean
    public TestService testService() {
        return new TestService();
    }
    
    public static class TestService {
        public String getServiceMessage() {
            return "Hello from TestService!";
        }
    }
}
