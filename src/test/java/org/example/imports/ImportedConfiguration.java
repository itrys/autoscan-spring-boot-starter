package org.example.imports;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportedConfiguration {
    
    @Bean
    public ImportedService importedService() {
        return new ImportedService("Imported service from ImportedConfiguration");
    }
    
    public static class ImportedService {
        private final String message;
        
        public ImportedService(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
