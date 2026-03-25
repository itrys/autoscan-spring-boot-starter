# AutoScan v1.1.0 Configuration Guide

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
