package com.example.spring.boot.soap.connector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The service configuration object to be used by all REST services. This class
 * takes its property values from the Spring environment, which is being
 * populated by the Spring Cloud Config server or may be local.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component
public class ServiceConfiguration extends AbstractServiceConfiguration {

    private static Logger LOG = LoggerFactory.getLogger(ServiceConfiguration.class);

    private Environment env;

    @Autowired
    public ServiceConfiguration(Environment env) {
        this.env = env;
        LOG.info("Service configuration has been created.");
    }

    public static String convertValueToString(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Integer) {
            return Integer.toString((Integer) value);
        } else if (value instanceof Float) {
            return Float.toString((Float) value);
        } else if (value instanceof Double) {
            return Double.toString((Double) value);
        } else if (value instanceof Boolean) {
            return Boolean.toString((Boolean) value);
        } else if (value instanceof Long) {
            return Long.toString((Long) value);
        } else {
            try {
                LOG.warn("Unknown property type. No specific conversion handler found: {}", value.getClass());
                return value.toString();
            } catch (Throwable thrown) {
                LOG.error("Failed to convert unknown property type to string.", value.getClass());
                return null;
            }
        }
    }

    @Override
    public String getPropertyValue(String name) {
        return this.env.getProperty(name);
    }

    public boolean contains(String name) {
        return this.env.containsProperty(name);
    }

    public boolean containsValue(String name) {
        return this.env.getProperty(name) != null;
    }

    /**
     * Searches through the existing properties and collects all those with the
     * given path prefix.
     * 
     * @param path
     *            The path prefix of the properties to collect.
     * @return A group collection of the properties found which shared the
     *         argumented path prefix.
     */
    public ServiceConfigurationGroup getStringGroup(String path) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> map = getAllProperties();
        String name = null;
        String pathPrefix = (path.endsWith(".") ? path : path + ".");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(pathPrefix)) {
                name = StringUtils.replace(entry.getKey(), pathPrefix, "");
                result.put(name, entry.getValue());
            }
        }
        return new ServiceConfigurationGroup(path, result);
    }

    /**
     * @return Returns all properties in one group object.
     */
    public ServiceConfigurationGroup getAll() {
        return new ServiceConfigurationGroup("", this.getAllProperties());
    }

    private Map<String, String> getAllProperties() {
        Map<String, String> result = new HashMap<>();
        ((ConfigurableEnvironment) this.env).getPropertySources().forEach(
                ps -> this.addAll(result, getAllProperties(ps)));
        return result;
    }

    private Map<String, String> getAllProperties(PropertySource<?> aPropSource) {
        Map<String, String> result = new HashMap<>();

        if (aPropSource instanceof CompositePropertySource) {
            CompositePropertySource cps = (CompositePropertySource) aPropSource;
            cps.getPropertySources().forEach(ps -> this.addAll(result, getAllProperties(ps)));
            return result;
        } else if (aPropSource instanceof EnumerablePropertySource<?>) {
            EnumerablePropertySource<?> ps = (EnumerablePropertySource<?>) aPropSource;
            Arrays.asList(ps.getPropertyNames()).forEach(
                    key -> result.put(key, ServiceConfiguration.convertValueToString(ps.getProperty(key))));
            return result;
        } else {
            LOG.warn("Given PropertySource is instanceof {} and cannot be iterated on", aPropSource.getClass()
                    .getName());
        }
        return result;
    }

    private void addAll(Map<String, String> aBase, Map<String, String> aToBeAdded) {
        for (Map.Entry<String, String> entry : aToBeAdded.entrySet()) {
            if (aBase.containsKey(entry.getKey())) {
                continue;
            }
            aBase.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Convenience method for getting an Integer property value from a
     * {@link AbstractServiceConfiguration} implementation, and which also
     * checks if the configuration object is <code>null</code>.
     * 
     * @param configuration
     *            The configuration object to extract the value from.
     * @param key
     *            The value key.
     * @param defaultVal
     *            The default value to be returned if the configuration object
     *            is <code>null</code> or no value is found in the configuration
     *            object.
     * @return The key value if one exists in the configuration, or the default
     *         value if either the configuration object is <code>null</code> or
     *         no value for the key exists in the configuration.
     */
    public static Integer extractInteger(AbstractServiceConfiguration configuration, String key, Integer defaultVal) {
        return (configuration != null ? configuration.getInteger(key, defaultVal) : defaultVal);
    }

    /**
     * Convenience method for getting an String property value from a
     * {@link AbstractServiceConfiguration} implementation, and which also
     * checks if the configuration object is <code>null</code>.
     * 
     * @param configuration
     *            The configuration object to extract the value from.
     * @param key
     *            The value key.
     * @param defaultVal
     *            The default value to be returned if the configuration object
     *            is <code>null</code> or no value is found in the configuration
     *            object.
     * @return The key value if one exists in the configuration, or the default
     *         value if either the configuration object is <code>null</code> or
     *         no value for the key exists in the configuration.
     */
    public static String extractString(AbstractServiceConfiguration configuration, String key, String defaultVal) {
        return (configuration != null ? configuration.getString(key, defaultVal) : defaultVal);
    }

    /**
     * Convenience method for getting an Boolean property value from a
     * {@link AbstractServiceConfiguration} implementation, and which also
     * checks if the configuration object is <code>null</code>.
     * 
     * @param configuration
     *            The configuration object to extract the value from.
     * @param key
     *            The value key.
     * @param defaultVal
     *            The default value to be returned if the configuration object
     *            is <code>null</code> or no value is found in the configuration
     *            object.
     * @return The key value if one exists in the configuration, or the default
     *         value if either the configuration object is <code>null</code> or
     *         no value for the key exists in the configuration.
     */
    public static Boolean extractBoolean(AbstractServiceConfiguration configuration, String key, Boolean defaultVal) {
        return (configuration != null ? configuration.getBoolean(key, defaultVal) : defaultVal);
    }

    /**
     * Convenience method for getting an Double property value from a
     * {@link AbstractServiceConfiguration} implementation, and which also
     * checks if the configuration object is <code>null</code>.
     * 
     * @param configuration
     *            The configuration object to extract the value from.
     * @param key
     *            The value key.
     * @param defaultVal
     *            The default value to be returned if the configuration object
     *            is <code>null</code> or no value is found in the configuration
     *            object.
     * @return The key value if one exists in the configuration, or the default
     *         value if either the configuration object is <code>null</code> or
     *         no value for the key exists in the configuration.
     */
    public static Double extractDouble(AbstractServiceConfiguration configuration, String key, Double defaultVal) {
        return (configuration != null ? configuration.getDouble(key, defaultVal) : defaultVal);
    }

    /**
     * Convenience method for getting an Float property value from a
     * {@link AbstractServiceConfiguration} implementation, and which also
     * checks if the configuration object is <code>null</code>.
     * 
     * @param configuration
     *            The configuration object to extract the value from.
     * @param key
     *            The value key.
     * @param defaultVal
     *            The default value to be returned if the configuration object
     *            is <code>null</code> or no value is found in the configuration
     *            object.
     * @return The key value if one exists in the configuration, or the default
     *         value if either the configuration object is <code>null</code> or
     *         no value for the key exists in the configuration.
     */
    public static Float extractFloat(AbstractServiceConfiguration configuration, String key, Float defaultVal) {
        return (configuration != null ? configuration.getFloat(key, defaultVal) : defaultVal);
    }

    /**
     * Convenience method for getting an Long property value from a
     * {@link AbstractServiceConfiguration} implementation, and which also
     * checks if the configuration object is <code>null</code>.
     * 
     * @param configuration
     *            The configuration object to extract the value from.
     * @param key
     *            The value key.
     * @param defaultVal
     *            The default value to be returned if the configuration object
     *            is <code>null</code> or no value is found in the configuration
     *            object.
     * @return The key value if one exists in the configuration, or the default
     *         value if either the configuration object is <code>null</code> or
     *         no value for the key exists in the configuration.
     */
    public static Long extractLong(AbstractServiceConfiguration configuration, String key, Long defaultVal) {
        return (configuration != null ? configuration.getLong(key, defaultVal) : defaultVal);
    }

}