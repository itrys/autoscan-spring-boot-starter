package com.company;

import org.example.boot.TestComponent;
import org.example.boot.TestConfiguration;
import org.example.boot.TestController;
import org.example.business.TestRepository;
import org.example.business.TestRestController;
import org.example.business.TestBusinessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DerivedAnnotationsTest {

    @Autowired
    private TestComponent testComponent;
    
    @Autowired
    private TestBusinessService testService;
    
    @Autowired
    private TestRepository testRepository;
    
    @Autowired
    private TestController testController;
    
    @Autowired
    private TestRestController testRestController;
    
    @Autowired
    private TestConfiguration.TestService configurationTestService;

    @Test
    void testAllComponentsInjected() {
        // Test @Component
        assertNotNull(testComponent, "TestComponent should be injected");
        
        // Test @Service
        assertNotNull(testService, "TestService should be injected");
        
        // Test @Repository
        assertNotNull(testRepository, "TestRepository should be injected");
        
        // Test @Controller
        assertNotNull(testController, "TestController should be injected");
        
        // Test @RestController
        assertNotNull(testRestController, "TestRestController should be injected");
        
        // Test @Configuration
        assertNotNull(configurationTestService, "TestService from TestConfiguration should be injected");
    }
    
    @Test
    void testServiceFunctionality() {
        assertNotNull(testService);
        assertEquals("TestService", testService.getServiceName());
        assertEquals("Processed: test", testService.processData("test"));
    }
    
    @Test
    void testRepositoryFunctionality() {
        assertNotNull(testRepository);
        assertEquals("Entity 1", testRepository.findById(1L));
    }
    
    @Test
    void testComponentFunctionality() {
        assertNotNull(testComponent);
        assertEquals("Hello from TestComponent!", testComponent.getMessage());
    }
    
    @Test
    void testConfigurationBeanFunctionality() {
        assertNotNull(configurationTestService);
        assertEquals("Hello from TestService!", configurationTestService.getServiceMessage());
    }
}
