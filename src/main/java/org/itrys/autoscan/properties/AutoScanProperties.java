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
}
