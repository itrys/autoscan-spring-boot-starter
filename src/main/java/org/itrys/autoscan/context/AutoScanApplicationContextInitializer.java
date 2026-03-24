package org.itrys.autoscan.context;

import jakarta.annotation.Nonnull;
import org.itrys.autoscan.properties.AutoScanProperties;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Auto Scan Application Context Initializer
 * <p>
 * Automatically scans the base package paths for configuration, supporting component scanning
 * for both technical foundation and business foundation components.
 * When business projects are developed based on the foundation, there's no need to configure 'business-packages',
 * because @SpringBootApplication automatically scans the package where the main application class resides.
 *
 * @author denghuafeng
 */
public class AutoScanApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    // Use a unified prefix constant, consistent with AutoScanProperties
    private static final String PREFIX = "auto-scan";

    @Override
    public void initialize(@Nonnull ConfigurableApplicationContext applicationContext) {
        if (!(applicationContext instanceof BeanDefinitionRegistry registry)) {
            System.err.println(">>> [AutoScan] ApplicationContext is not a BeanDefinitionRegistry. Skip scanning.");
            return;
        }

        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        // Check if it's a development environment
        List<String> devProfiles = Arrays.asList("dev", "local", "test");
        boolean isDev = Arrays.stream(environment.getActiveProfiles())
            .anyMatch(devProfiles::contains);

        // Use ConfigurationProperties binding instead of direct Binder usage
        AutoScanProperties properties = new AutoScanProperties();
        Binder.get(environment).bind(PREFIX, Bindable.ofInstance(properties));
        // Read dev mode configuration
        boolean devMode = properties.isDevMode()||isDev;

        if (devMode) {
            System.out.println(">>> [AutoScan] Initializing base package scanner...");
        }

        // Build the list of packages to scan
        Set<String> packagesToScan = new LinkedHashSet<>();

        // Read base packages configuration (required)
        List<String> basePackages = properties.getBasePackages();
        
        if (basePackages.isEmpty()) {
            if (devMode) {
                System.out.println(">>> [AutoScan] No base packages configured. Skip scanning.");
            }
            return;
        }
        
        if (devMode) {
            System.out.println(">>> [AutoScan] Configured base packages: " + basePackages);
        }

        // Add base packages
        for (String pkg : basePackages) {
            if (pkg != null && !pkg.trim().isEmpty()) {
                packagesToScan.add(pkg.trim());
            }
        }

        // Read business packages configuration (optional, only needed when this project is used as a foundation)
        List<String> businessPackages = properties.getBusinessPackages();
        
        if (!businessPackages.isEmpty() && devMode) {
            System.out.println(">>> [AutoScan] Configured business packages: " + businessPackages);
        }

        // Add business packages (if configured)
        for (String pkg : businessPackages) {
            if (pkg != null && !pkg.trim().isEmpty()) {
                packagesToScan.add(pkg.trim());
            }
        }

        // Convert to array
        String[] scanPackages = packagesToScan.toArray(new String[0]);
        
        if (devMode) {
            System.out.println(">>> [AutoScan] Final packages to scan: " + Arrays.toString(scanPackages));
        }

        // Create scanner
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Configuration.class));

        // Perform scanning
        int scannedCount = scanner.scan(scanPackages);
        
        if (devMode) {
            System.out.println(">>> [AutoScan] Successfully registered " + scannedCount + " bean(s) from base packages.");
        }
    }
}
