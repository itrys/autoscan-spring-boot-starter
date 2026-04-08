package org.example.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConditionalTestConfiguration {
    
    @Bean
    @ConditionalOnClass(name = "org.springframework.web.client.RestTemplate")
    public ConditionalService conditionalServiceAlwaysPresent() {
        return new ConditionalService("Always present (RestTemplate exists)");
    }
    
    @Bean
    @ConditionalOnClass(name = "com.nonexistent.ClassThatNeverExists")
    public ConditionalService conditionalServiceNeverPresent() {
        return new ConditionalService("Never present (class doesn't exist)");
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DefaultService defaultService() {
        return new DefaultService("Default service (created when no other bean exists)");
    }
    
    public static class ConditionalService {
        private final String message;
        
        public ConditionalService(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    public static class DefaultService {
        private final String message;
        
        public DefaultService(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
