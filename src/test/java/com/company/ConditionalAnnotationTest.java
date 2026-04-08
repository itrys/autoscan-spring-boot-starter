package com.company;

import org.example.boot.ConditionalTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConditionalAnnotationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testConditionalOnClassPresent() {
        ConditionalTestConfiguration.ConditionalService service = 
            applicationContext.getBean(ConditionalTestConfiguration.ConditionalService.class);
        
        assertNotNull(service);
        assertEquals("Always present (RestTemplate exists)", service.getMessage());
    }

    @Test
    void testConditionalOnMissingBean() {
        ConditionalTestConfiguration.DefaultService service = 
            applicationContext.getBean(ConditionalTestConfiguration.DefaultService.class);
        
        assertNotNull(service);
        assertEquals("Default service (created when no other bean exists)", service.getMessage());
    }

    @Test
    void testConditionalOnClassNotPresent() {
        String[] beanNames = applicationContext.getBeanNamesForType(ConditionalTestConfiguration.ConditionalService.class);
        
        assertEquals(1, beanNames.length, "Should only have one ConditionalService bean");
    }
}
