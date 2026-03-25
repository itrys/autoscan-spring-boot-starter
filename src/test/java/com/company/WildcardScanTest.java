package com.company;

import org.example.wildcard.service.WildcardService;
import org.example.wildcard.controller.WildcardController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "auto-scan.base-packages[0]=org.example.wildcard.*",
    "auto-scan.dev-mode=true"
})
class WildcardScanTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testWildcardServiceScanned() {
        WildcardService service = applicationContext.getBean(WildcardService.class);
        assertNotNull(service, "WildcardService should be scanned and registered");
        assertEquals("Hello from WildcardService!", service.getMessage());
    }

    @Test
    void testWildcardControllerScanned() {
        WildcardController controller = applicationContext.getBean(WildcardController.class);
        assertNotNull(controller, "WildcardController should be scanned and registered");
        assertEquals("Hello from WildcardController!", controller.getMessage());
    }

    @Test
    void testSingleLevelWildcard() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        assertTrue(beanNames.length > 0, "Should have scanned beans with wildcard pattern");
        
        boolean hasWildcardService = false;
        boolean hasWildcardController = false;
        
        for (String beanName : beanNames) {
            if (beanName.contains("wildcardService")) {
                hasWildcardService = true;
            }
            if (beanName.contains("wildcardController")) {
                hasWildcardController = true;
            }
        }
        
        assertTrue(hasWildcardService, "Should find WildcardService bean");
        assertTrue(hasWildcardController, "Should find WildcardController bean");
    }
}
