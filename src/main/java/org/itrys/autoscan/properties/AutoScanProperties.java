package org.itrys.autoscan.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Auto Scan Configuration Properties
 *
 * @author denghuafeng
 */
public class AutoScanProperties {

    /**
     * List of base package paths (technical foundation, business foundation, etc.)
     * Must be configured, used to scan base components
     */
    private List<String> basePackages = new ArrayList<>();

    /**
     * List of business package paths (optional)
     * Only needed when this project is used as a foundation for other projects
     */
    private List<String> businessPackages = new ArrayList<>();

    /**
     * Development mode (used for outputting detailed logs)
     */
    private boolean devMode = false;

    /**
     * List of packages to exclude from scanning
     */
    private List<String> excludePackages = new ArrayList<>();

    /**
     * List of classes to exclude from scanning
     */
    private List<String> excludeClasses = new ArrayList<>();

    /**
     * List of annotations to include in scanning
     */
    private List<String> includeAnnotations = new ArrayList<>();

    /**
     * Get the list of base package paths
     *
     * @return List&lt;String&gt;
     */
    public List<String> getBasePackages() {
        return basePackages;
    }

    /**
     * Set the list of base package paths
     *
     * @param basePackages List&lt;String&gt;
     */
    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    /**
     * Get the list of business package paths
     *
     * @return List&lt;String&gt;
     */
    public List<String> getBusinessPackages() {
        return businessPackages;
    }

    /**
     * Set the list of business package paths
     *
     * @param businessPackages List&lt;String&gt;
     */
    public void setBusinessPackages(List<String> businessPackages) {
        this.businessPackages = businessPackages;
    }

    /**
     * Get development mode status
     *
     * @return boolean
     */
    public boolean isDevMode() {
        return devMode;
    }

    /**
     * Set development mode
     *
     * @param devMode boolean
     */
    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    /**
     * Get the list of packages to exclude from scanning
     *
     * @return List&lt;String&gt;
     */
    public List<String> getExcludePackages() {
        return excludePackages;
    }

    /**
     * Set the list of packages to exclude from scanning
     *
     * @param excludePackages List&lt;String&gt;
     */
    public void setExcludePackages(List<String> excludePackages) {
        this.excludePackages = excludePackages;
    }

    /**
     * Get the list of classes to exclude from scanning
     *
     * @return List&lt;String&gt;
     */
    public List<String> getExcludeClasses() {
        return excludeClasses;
    }

    /**
     * Set the list of classes to exclude from scanning
     *
     * @param excludeClasses List&lt;String&gt;
     */
    public void setExcludeClasses(List<String> excludeClasses) {
        this.excludeClasses = excludeClasses;
    }

    /**
     * Get the list of annotations to include in scanning
     *
     * @return List&lt;String&gt;
     */
    public List<String> getIncludeAnnotations() {
        return includeAnnotations;
    }

    /**
     * Set the list of annotations to include in scanning
     *
     * @param includeAnnotations List&lt;String&gt;
     */
    public void setIncludeAnnotations(List<String> includeAnnotations) {
        this.includeAnnotations = includeAnnotations;
    }
}
