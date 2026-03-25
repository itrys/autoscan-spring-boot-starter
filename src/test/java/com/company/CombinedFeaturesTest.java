package com.company;

import org.example.wildcard.service.WildcardService;
import org.example.exclude.normal.NormalService;
import org.example.annotation.service.CustomAnnotatedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "auto-scan.base-packages[0]=org.example.wildcard.*",
    "auto-scan.base-packages[1]=org.example.exclude",
    "auto-scan.base-packages[2]=org.example.annotation",
    "auto-scan.exclude-packages[0]=org.example.exclude.test",
    "auto-scan.include-annotations[0]=org.example.annotation.CustomComponent",
    "auto-scan.dev-mode=true"
})
class CombinedFeaturesTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testAllFeaturesWorkTogether() {
        WildcardService wildcardService = applicationContext.getBean(WildcardService.class);
        NormalService normalService = applicationContext.getBean(NormalService.class);
        CustomAnnotatedService customService = applicationContext.getBean(CustomAnnotatedService.class);
        
        assertNotNull(wildcardService, "WildcardService should be scanned");
        assertNotNull(normalService, "NormalService should be scanned");
        assertNotNull(customService, "CustomAnnotatedService should be scanned");
    }

    @Test
    void testWildcardAndExcludeWorkTogether() {
        assertNotNull(applicationContext.getBean(WildcardService.class), 
            "Wildcard scanned bean should exist");
        assertNotNull(applicationContext.getBean(NormalService.class), 
            "Non-excluded bean should exist");
        
        assertThrows(Exception.class, () -> {
            applicationContext.getBean(org.example.exclude.test.ExcludedService.class);
        }, "Excluded bean should not exist");
    }

    @Test
    void testCustomAnnotationAndWildcardWorkTogether() {
        assertNotNull(applicationContext.getBean(WildcardService.class), 
            "Wildcard scanned bean should exist");
        assertNotNull(applicationContext.getBean(CustomAnnotatedService.class), 
            "Custom annotated bean should exist");
    }

    @Test
    void testComprehensiveScanning() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        
        int wildcardBeans = 0;
        int excludeBeans = 0;
        int customAnnotationBeans = 0;
        
        for (String beanName : beanNames) {
            if (beanName.contains("wildcard")) {
                wildcardBeans++;
            }
            if (beanName.contains("normalService")) {
                excludeBeans++;
            }
            if (beanName.contains("customAnnotated")) {
                customAnnotationBeans++;
            }
        }
        
        assertTrue(wildcardBeans > 0, "Should have wildcard scanned beans");
        assertTrue(excludeBeans > 0, "Should have non-excluded beans");
        assertTrue(customAnnotationBeans > 0, "Should have custom annotated beans");
    }
}
