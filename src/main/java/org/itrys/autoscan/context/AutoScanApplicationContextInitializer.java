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
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

        // Add base packages with wildcard support
        for (String pkg : basePackages) {
            if (pkg != null && !pkg.trim().isEmpty()) {
                List<String> resolvedPackages = resolvePackagesWithWildcards(Collections.singletonList(pkg.trim()));
                packagesToScan.addAll(resolvedPackages);
            }
        }

        // Read business packages configuration (optional, only needed when this project is used as a foundation)
        List<String> businessPackages = properties.getBusinessPackages();
        
        if (!businessPackages.isEmpty() && devMode) {
            System.out.println(">>> [AutoScan] Configured business packages: " + businessPackages);
        }

        // Add business packages (if configured) with wildcard support
        for (String pkg : businessPackages) {
            if (pkg != null && !pkg.trim().isEmpty()) {
                List<String> resolvedPackages = resolvePackagesWithWildcards(Collections.singletonList(pkg.trim()));
                packagesToScan.addAll(resolvedPackages);
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

        // Add custom annotation filters
        List<String> includeAnnotations = properties.getIncludeAnnotations();
        if (!includeAnnotations.isEmpty()) {
            addAnnotationFilters(scanner, includeAnnotations, devMode);
        }

        // Add exclude filters
        List<String> excludePackages = properties.getExcludePackages();
        List<String> excludeClasses = properties.getExcludeClasses();
        if (!excludePackages.isEmpty() || !excludeClasses.isEmpty()) {
            addExcludeFilters(scanner, excludePackages, excludeClasses, devMode);
        }

        // Perform scanning
        int scannedCount = scanner.scan(scanPackages);
        
        if (devMode) {
            System.out.println(">>> [AutoScan] Successfully registered " + scannedCount + " bean(s) from base packages.");
        }
    }

    /**
     * Resolve packages with wildcards
     */
    private List<String> resolvePackagesWithWildcards(List<String> packages) {
        List<String> resolvedPackages = new ArrayList<>();
        for (String pkg : packages) {
            if (pkg.contains("*")) {
                // Handle wildcard
                resolvedPackages.addAll(resolveWildcardPackage(pkg));
            } else {
                resolvedPackages.add(pkg);
            }
        }
        return resolvedPackages;
    }

    /**
     * Resolve wildcard package pattern to actual packages
     */
    private List<String> resolveWildcardPackage(String pattern) {
        List<String> result = new ArrayList<>();
        try {
            // Get class loader
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            
            // Convert package pattern to resource path
            String resourcePath = pattern.replace('.', '/');
            
            // Handle single level wildcard
            if (resourcePath.contains("/*/")) {
                String basePath = resourcePath.substring(0, resourcePath.indexOf("/*/"));
                URL url = classLoader.getResource(basePath);
                if (url != null) {
                    File baseDir = new File(url.getFile());
                    if (baseDir.exists() && baseDir.isDirectory()) {
                        File[] subDirs = baseDir.listFiles(File::isDirectory);
                        if (subDirs != null) {
                            for (File subDir : subDirs) {
                                String subPath = basePath + "/" + subDir.getName() + resourcePath.substring(resourcePath.indexOf("/*/") + 3);
                                if (subPath.contains("*")) {
                                    // Recursive resolution
                                    List<String> subResult = resolveWildcardPackage(subPath.replace('/', '.'));
                                    result.addAll(subResult);
                                } else {
                                    result.add(subPath.replace('/', '.'));
                                }
                            }
                        }
                    }
                }
            }
            // Handle multi level wildcard
            else if (resourcePath.contains("/**")) {
                String basePath = resourcePath.substring(0, resourcePath.indexOf("/**"));
                URL url = classLoader.getResource(basePath);
                if (url != null) {
                    File baseDir = new File(url.getFile());
                    if (baseDir.exists() && baseDir.isDirectory()) {
                        collectAllSubPackages(baseDir, basePath, result);
                    }
                }
            }
            // Handle single wildcard at the end
            else if (resourcePath.endsWith("/*")) {
                String basePath = resourcePath.substring(0, resourcePath.length() - 2);
                URL url = classLoader.getResource(basePath);
                if (url != null) {
                    File baseDir = new File(url.getFile());
                    if (baseDir.exists() && baseDir.isDirectory()) {
                        File[] subDirs = baseDir.listFiles(File::isDirectory);
                        if (subDirs != null) {
                            for (File subDir : subDirs) {
                                result.add((basePath + "/" + subDir.getName()).replace('/', '.'));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(">>> [AutoScan] Error resolving wildcard package: " + pattern + ", error: " + e.getMessage());
        }
        return result;
    }

    /**
     * Collect all sub packages recursively
     */
    private void collectAllSubPackages(File dir, String basePath, List<String> result) {
        result.add(basePath.replace('/', '.'));
        File[] subDirs = dir.listFiles(File::isDirectory);
        if (subDirs != null) {
            for (File subDir : subDirs) {
                collectAllSubPackages(subDir, basePath + "/" + subDir.getName(), result);
            }
        }
    }

    /**
     * Add custom annotation filters
     */
    private void addAnnotationFilters(ClassPathBeanDefinitionScanner scanner, List<String> annotations, boolean devMode) {
        for (String annotationClassName : annotations) {
            try {
                Class<?> annotationClass = Class.forName(annotationClassName);
                if (annotationClass.isAnnotation()) {
                    scanner.addIncludeFilter(new AnnotationTypeFilter((Class<? extends java.lang.annotation.Annotation>) annotationClass));
                    if (devMode) {
                        System.out.println(">>> [AutoScan] Added include filter for annotation: " + annotationClassName);
                    }
                }
            } catch (ClassNotFoundException e) {
                System.err.println(">>> [AutoScan] Annotation class not found: " + annotationClassName);
            }
        }
    }

    /**
     * Add exclude filters
     */
    private void addExcludeFilters(ClassPathBeanDefinitionScanner scanner, List<String> excludePackages, List<String> excludeClasses, boolean devMode) {
        scanner.addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            
            // Check if class is in exclude list
            for (String excludedClass : excludeClasses) {
                if (className.equals(excludedClass)) {
                    return true;
                }
            }
            
            // Check if class is in exclude package
            for (String excludedPackage : excludePackages) {
                if (className.startsWith(excludedPackage)) {
                    return true;
                }
            }
            
            return false;
        });
        
        if (devMode) {
            if (!excludePackages.isEmpty()) {
                System.out.println(">>> [AutoScan] Added exclude filter for packages: " + excludePackages);
            }
            if (!excludeClasses.isEmpty()) {
                System.out.println(">>> [AutoScan] Added exclude filter for classes: " + excludeClasses);
            }
        }
    }
}
