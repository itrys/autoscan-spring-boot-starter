package org.example.imports;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnotherImportedConfiguration {
    
    @Bean
    public AnotherImportedService anotherImportedService() {
        return new AnotherImportedService("Another imported service from AnotherImportedConfiguration");
    }
    
    public static class AnotherImportedService {
        private final String message;
        
        public AnotherImportedService(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
}