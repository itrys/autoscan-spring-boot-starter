package com.company;

import org.example.exclude.test.ExcludedService;
import org.example.exclude.example.ExcludedComponent;
import org.example.exclude.normal.NormalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "auto-scan.base-packages[0]=org.example.exclude",
    "auto-scan.exclude-packages[0]=org.example.exclude.test",
    "auto-scan.exclude-classes[0]=org.example.exclude.example.ExcludedComponent",
    "auto-scan.dev-mode=true"
})
class ExcludeScanTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testNormalServiceScanned() {
        NormalService service = applicationContext.getBean(NormalService.class);
        assertNotNull(service, "NormalService should be scanned and registered");
        assertEquals("Hello from NormalService!", service.getMessage());
    }

    @Test
    void testExcludedPackageNotScanned() {
        assertThrows(Exception.class, () -> {
            applicationContext.getBean(ExcludedService.class);
        }, "ExcludedService should not be scanned because its package is excluded");
    }

    @Test
    void testExcludedClassNotScanned() {
        assertThrows(Exception.class, () -> {
            applicationContext.getBean(ExcludedComponent.class);
        }, "ExcludedComponent should not be scanned because it is explicitly excluded");
    }

    @Test
    void testExcludeFilteringWorks() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        
        boolean hasNormalService = false;
        boolean hasExcludedService = false;
        boolean hasExcludedComponent = false;
        
        for (String beanName : beanNames) {
            if (beanName.contains("normalService")) {
                hasNormalService = true;
            }
            if (beanName.contains("excludedService")) {
                hasExcludedService = true;
            }
            if (beanName.contains("excludedComponent")) {
                hasExcludedComponent = true;
            }
        }
        
        assertTrue(hasNormalService, "Should find NormalService bean");
        assertFalse(hasExcludedService, "Should not find ExcludedService bean");
        assertFalse(hasExcludedComponent, "Should not find ExcludedComponent bean");
    }
}
