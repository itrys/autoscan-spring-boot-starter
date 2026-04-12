# AutoScan v1.3.0 Configuration Guide

## Configuration File Structure

AutoScan supports configuration through `application.yml` or `application.properties` files.

## Configuration Items

### 1. base-packages (Required)

**Type**: `List<String>`

**Description**: Base package paths for scanning. Supports wildcards starting from v1.1.0.

**Wildcard Support**:
- `*` - Single-level wildcard (matches one level of packages)
- `**` - Multi-level wildcard (matches all sub-packages)

**Examples**:

```yaml
# Basic configuration
auto-scan:
  base-packages:
    - org.example.boot
    - org.example.business

# Single-level wildcard
auto-scan:
  base-packages:
    - org.example.*  # Matches: org.example.boot, org.example.business, etc.

# Multi-level wildcard
auto-scan:
  base-packages:
    - org.example.**  # Matches: org.example.boot, org.example.boot.service, etc.

# Mixed configuration
auto-scan:
  base-packages:
    - org.example.*
    - com.company.**
```

### 2. business-packages (Optional)

**Type**: `List<String>`

**Description**: Business package paths. Only needed when this project serves as infrastructure for other projects.

**Examples**:

```yaml
auto-scan:
  base-packages:
    - org.example.boot
  business-packages:
    - org.example.business  # This project's business packages
```

### 3. exclude-packages (Optional) - v1.1.0+

**Type**: `List<String>`

**Description**: Package paths to exclude from scanning.

**Examples**:

```yaml
auto-scan:
  base-packages:
    - org.example
  exclude-packages:
    - org.example.test      # Exclude test packages
    - org.example.example   # Exclude example packages
    - org.example.demo      # Exclude demo packages
```

### 4. exclude-classes (Optional) - v1.1.0+

**Type**: `List<String>`

**Description**: Fully qualified class names to exclude from scanning.

**Examples**:

```yaml
auto-scan:
  base-packages:
    - org.example
  exclude-classes:
    - org.example.demo.DemoClass
    - org.example.temp.TempComponent
    - org.example.test.TestService
```

### 5. include-annotations (Optional) - v1.1.0+

**Type**: `List<String>`

**Description**: Annotation fully qualified names to include in scanning. By default, AutoScan scans `@Component` and `@Configuration` annotations.

**Examples**:

```yaml
auto-scan:
  base-packages:
    - org.example
  include-annotations:
    # Built-in Spring annotations
    - org.springframework.stereotype.Service
    - org.springframework.stereotype.Controller
    - org.springframework.stereotype.Repository
    
    # Custom annotations
    - org.example.annotation.CustomComponent
    - org.example.annotation.BusinessService
```

### 6. dev-mode (Optional)

**Type**: `boolean`

**Default**: Auto-detected based on `spring.profiles.active` (true for dev/local/test profiles)

**Description**: Enable development mode to output detailed scanning logs.

**Examples**:

```yaml
# Enable dev mode
auto-scan:
  dev-mode: true

# Disable dev mode (production)
auto-scan:
  dev-mode: false

# Auto-detection (default)
auto-scan:
  # dev-mode will be auto-detected
```

### 7. imports (Optional) - v1.2.0+

**Type**: `List<String>`

**Description**: Fully qualified class names to directly import (like `@Import` annotation).

**Examples**:

```yaml
# Import single class
auto-scan:
  imports:
    - org.example.config.AppConfig

# Import multiple classes
auto-scan:
  imports:
    - org.example.config.AppConfig
    - org.example.config.WebConfig
    - org.example.config.SecurityConfig
```

### 8. lazy-initialization (Optional) - v1.2.0+

**Type**: `boolean`

**Default**: `false`

**Description**: Enable global lazy initialization for all scanned beans.

**Examples**:

```yaml
# Enable global lazy initialization
auto-scan:
  lazy-initialization: true

# Disable global lazy initialization (default)
auto-scan:
  lazy-initialization: false
```

### 9. lazy-packages (Optional) - v1.2.0+

**Type**: `List<String>`

**Description**: Package paths for which beans should be lazily initialized.

**Examples**:

```yaml
# Specify packages for lazy initialization
auto-scan:
  lazy-packages:
    - org.example.service
    - org.example.repository
    - org.example.controller
```

### 10. lazy-classes (Optional) - v1.2.0+

**Type**: `List<String>`

**Description**: Fully qualified class names for which beans should be lazily initialized.

**Examples**:

```yaml
# Specify classes for lazy initialization
auto-scan:
  lazy-classes:
    - org.example.controller.UserController
    - org.example.service.UserService
    - org.example.repository.UserRepository
```

### 11. enabled (Optional) - v1.2.0+

**Type**: `boolean`

**Default**: `true`

**Description**: Enable or disable the AutoScan component entirely.

**Examples**:

```yaml
# Enable AutoScan (default)
auto-scan:
  enabled: true

# Disable AutoScan
auto-scan:
  enabled: false
```

### 12. exclude-packages-regex (Optional) - v1.3.0+

**Type**: `List<String>`

**Description**: Regex patterns for packages to exclude from scanning.

**Examples**:

```yaml
# Exclude packages using regex patterns
auto-scan:
  exclude-packages-regex:
    - org\.example\.test\..*  # Exclude all test packages
    - org\.example\.example\..*  # Exclude all example packages
    - .*\.temp\..*  # Exclude packages containing "temp"
    - .*\.demo\..*  # Exclude packages containing "demo"
```

### 13. include-packages-regex (Optional) - v1.3.0+

**Type**: `List<String>`

**Description**: Regex patterns for packages to include in scanning. If specified, only packages matching these patterns will be included.

**Examples**:

```yaml
# Include packages using regex patterns
auto-scan:
  include-packages-regex:
    - org\.example\.boot\..*  # Include boot packages
    - org\.example\.business\..*  # Include business packages
    - org\.example\.controller\..*  # Include controller packages
    - .*Service  # Include classes ending with "Service"
```

## Configuration Scenarios

### Scenario 1: Basic Infrastructure Project

```yaml
auto-scan:
  base-packages:
    - org.example.boot
    - org.example.business
  dev-mode: true
```

### Scenario 2: Multi-module Project with Wildcards

```yaml
auto-scan:
  base-packages:
    - org.example.*      # All first-level packages
    - com.company.**     # All packages under com.company
  dev-mode: true
```

### Scenario 3: Exclude Test and Demo Code

```yaml
auto-scan:
  base-packages:
    - org.example
  exclude-packages:
    - org.example.test
    - org.example.demo
  exclude-classes:
    - org.example.example.ExampleClass
  dev-mode: true
```

### Scenario 4: Custom Annotation Scanning

```yaml
auto-scan:
  base-packages:
    - org.example
  include-annotations:
    - org.springframework.stereotype.Service
    - org.example.annotation.CustomComponent
  dev-mode: true
```

### Scenario 5: Production Configuration

```yaml
spring:
  profiles:
    active: prod

auto-scan:
  base-packages:
    - org.example.boot
    - org.example.business
  exclude-packages:
    - org.example.test
  dev-mode: false  # Disable logs in production
```

### Scenario 6: Development Configuration

```yaml
spring:
  profiles:
    active: dev

auto-scan:
  base-packages:
    - org.example.*  # Use wildcards for flexibility
  include-annotations:
    - org.springframework.stereotype.Service
    - org.example.annotation.CustomComponent
  dev-mode: true  # Enable detailed logs
```

### Scenario 7: @Import Compatibility (v1.2.0+)

```yaml
spring:
  profiles:
    active: import

auto-scan:
  base-packages:
    - org.example
  imports:
    - org.example.config.AppConfig
    - org.example.config.WebConfig
    - org.example.config.SecurityConfig
  dev-mode: true
```

### Scenario 8: Lazy Initialization (v1.2.0+)

```yaml
spring:
  profiles:
    active: lazy

auto-scan:
  base-packages:
    - org.example
  lazy-initialization: true  # Enable global lazy initialization
  lazy-packages:
    - org.example.service  # Additional package-specific lazy initialization
  lazy-classes:
    - org.example.controller.UserController  # Additional class-specific lazy initialization
  dev-mode: true
```

### Scenario 9: Combined Features (v1.2.0+)

```yaml
spring:
  profiles:
    active: combined

auto-scan:
  base-packages:
    - org.example.*
  imports:
    - org.example.config.AppConfig
    - org.example.config.WebConfig
  lazy-initialization: true
  lazy-packages:
    - org.example.service
  exclude-packages:
    - org.example.test
  dev-mode: true
```

### Scenario 10: Disable AutoScan (v1.2.0+)

```yaml
spring:
  profiles:
    active: disabled

auto-scan:
  enabled: false  # Disable AutoScan entirely
  # Other configurations will be ignored when enabled is false
  base-packages:
    - org.example
  dev-mode: true
```

### Scenario 11: Regex-based Package Filtering (v1.3.0+)

```yaml
spring:
  profiles:
    active: regex

auto-scan:
  base-packages:
    - org.example
  
  # Regex patterns for excluding packages
  exclude-packages-regex:
    - org\.example\.test\..*  # Exclude test packages
    - org\.example\.example\..*  # Exclude example packages
    - .*\.temp\..*  # Exclude temporary packages
  
  # Regex patterns for including packages
  include-packages-regex:
    - org\.example\.boot\..*  # Include boot packages
    - org\.example\.business\..*  # Include business packages
    - org\.example\.controller\..*  # Include controller packages
  
  dev-mode: true
```

### Scenario 12: Environment-based Conditional Scanning (v1.3.0+)

**Development Environment**:

```yaml
spring:
  profiles:
    active: dev

auto-scan:
  base-packages:
    - org.example.*  # Use wildcard for flexibility
  dev-mode: true
  include-annotations:
    - org.springframework.stereotype.Component
    - org.springframework.stereotype.Service
    - org.springframework.stereotype.Controller
    - org.springframework.stereotype.Repository
  exclude-packages:
    - org.example.test
```

**Test Environment**:

```yaml
spring:
  profiles:
    active: test

auto-scan:
  base-packages:
    - org.example
  dev-mode: true
  include-annotations:
    - org.springframework.stereotype.Service
    - org.springframework.stereotype.Controller
    - org.springframework.stereotype.Repository
  exclude-packages:
    - org.example.test
    - org.example.example
```

**Production Environment**:

```yaml
spring:
  profiles:
    active: prod

auto-scan:
  base-packages:
    - org.example.boot
    - org.example.business
    - org.example.controller
  dev-mode: false
  exclude-packages-regex:
    - org\.example\.test\..*  # Exclude test packages
    - org\.example\.example\..*  # Exclude example packages
    - .*\.temp\..*  # Exclude temporary packages
    - .*\.demo\..*  # Exclude demo packages
```

## Configuration Priority

When using multiple configuration files, the priority is:

1. `application-{profile}.yml` (profile-specific)
2. `application.yml` (default)

## Best Practices

1. **Use wildcards wisely**: Wildcards can simplify configuration but may scan unnecessary packages.

2. **Exclude test code**: Always exclude test and example packages in production.

3. **Custom annotations**: Use custom annotations for better organization and control.

4. **Environment-specific configuration**: Use different configurations for dev, test, and prod environments.

5. **Monitor scanning logs**: Enable dev-mode during development to verify scanning behavior.

## Troubleshooting

### Issue 1: Components not scanned

**Solution**:
1. Check if `base-packages` configuration is correct
2. Enable `dev-mode: true` to view scanning logs
3. Verify components have proper annotations
4. Check if packages are excluded by `exclude-packages` or `exclude-classes`

### Issue 2: Wildcard not working

**Solution**:
1. Ensure wildcard syntax is correct (`*` or `**`)
2. Verify package structure matches wildcard pattern
3. Check if wildcards are supported in your version (v1.1.0+)

### Issue 3: Custom annotation not recognized

**Solution**:
1. Add custom annotation to `include-annotations`
2. Verify annotation is properly defined with `@Target` and `@Retention`
3. Check annotation fully qualified name is correct

### Issue 4: Excluded components still scanned

**Solution**:
1. Verify exclude configuration syntax
2. Check if package path or class name is correct
3. Ensure exclude configuration is in the correct profile

## Migration from v1.2.0 to v1.3.0

v1.3.0 is fully backward compatible with v1.2.0. Existing configurations will continue to work without any changes.

**New features in v1.3.0**:
- Advanced filtering - Regex-based package filtering for more flexible scanning control
- Conditional configuration - Environment-based scanning configuration

**Migration steps**:
1. Update dependency version to 1.3.0
2. (Optional) Add `exclude-packages-regex` configuration to exclude packages using regex patterns
3. (Optional) Add `include-packages-regex` configuration to include packages using regex patterns
4. (Optional) Use Spring Boot's profile-specific configuration for environment-based scanning

## Migration from v1.1.0 to v1.2.0

v1.2.0 is fully backward compatible with v1.1.0. Existing configurations will continue to work without any changes.

**New features in v1.2.0**:
- @Import compatibility - Directly import specific classes
- Lazy initialization - Support lazy loading for beans
- Enabled switch - Enable or disable AutoScan entirely

**Migration steps**:
1. Update dependency version to 1.2.0
2. (Optional) Add `imports` configuration to directly import classes
3. (Optional) Add `lazy-initialization`, `lazy-packages`, or `lazy-classes` configuration for lazy loading
4. (Optional) Add `enabled` configuration to control AutoScan activation

## Migration from v1.0.0 to v1.1.0

v1.1.0 is fully backward compatible with v1.0.0. Existing configurations will continue to work without any changes.

**New features in v1.1.0**:
- Wildcard support in package paths
- Exclude packages and classes
- Custom annotation scanning

**Migration steps**:
1. Update dependency version to 1.1.0
2. (Optional) Add wildcard support to simplify configuration
3. (Optional) Add exclude configuration if needed
4. (Optional) Add custom annotation configuration if needed
