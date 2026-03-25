package com.company;

import org.example.boot.TestComponent;
import org.example.boot.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AutoScanCompanyTest {

    @Autowired
    private TestComponent testComponent;

    @Autowired
    private TestConfiguration.TestService testService;

    @Test
    void contextLoads() {
        // Test if application context loads successfully
        assertNotNull(testComponent, "TestComponent should be injected");
        assertNotNull(testService, "TestService should be injected");
    }

    @Test
    void testComponentInjection() {
        // Test if TestComponent is working
        assertNotNull(testComponent);
        assertEquals("Hello from TestComponent!", testComponent.getMessage());
    }

    @Test
    void testConfigurationBeanInjection() {
        // Test if TestService from TestConfiguration is working
        assertNotNull(testService);
        assertEquals("Hello from TestService!", testService.getServiceMessage());
    }
}
