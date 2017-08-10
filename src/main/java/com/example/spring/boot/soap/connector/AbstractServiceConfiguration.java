package com.example.spring.boot.soap.connector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the Base property conversion and retrieval methods used by both the
 * {@link ServiceConfiguration} and {@link ServiceConfigurationGroup}. If you
 * need another type of property conversion, add it here so both of the
 * aforementioned classes get the benefit.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public abstract class AbstractServiceConfiguration {

    private static Logger LOG = LoggerFactory.getLogger(AbstractServiceConfiguration.class);

    abstract public String getPropertyValue(String name);

    abstract public boolean contains(String name);

    abstract public boolean containsValue(String name);

    public String getString(String name) {
        return this.getPropertyValue(name);
    }

    public String getString(String name, String defaultValue) {
        return this.resolve(this.getPropertyValue(name), defaultValue);
    }

    public String[] getStringArray(String name) {
        String arrayStr = this.getString(name);
        return (arrayStr == null ? null : arrayStr.split(","));
    }

    /**
     * Retrieves the value of a property and then attempts to split it up on
     * commas, then returning a {@link List} of the separated values.
     * 
     * @param name
     *            The name of the property to retrieve.
     * @return Returns a list of properties based on the property name. Will
     *         return an empty list if no properties are found.
     */
    public List<String> getStringList(String name) {
        return this.getStringList(name, ",");
    }

    /**
     * Retrieves the value of a property and then attempts to split it up on the
     * argumented separator, then returning a {@link List} of the separated
     * values.
     * 
     * @param name
     *            The name of the property to retrieve.
     * @param separator
     *            The string or character to split the property value on.
     * @return Returns a list of properties based on the property name. Will
     *         return an empty list if no properties are found.
     */
    public List<String> getStringList(String name, String separator) {
        String arrayStr = this.getString(name);
        return (arrayStr == null ? new ArrayList<>() : Arrays.asList(arrayStr.split(separator)));
    }

    public Boolean getBoolean(String name) {
        return this.getBoolean(name, null);
    }

    public Boolean getBoolean(String name, Boolean defaultValue) {
        String value = this.getPropertyValue(name);
        if (value == null) {
            return defaultValue;
        } else {
            return Boolean.valueOf(value);
        }
    }

    public Integer getInteger(String name) {
        return this.getInteger(name, null);
    }

    public Integer getInteger(String name, Integer defaultValue) {
        if (this.contains(name)) {
            try {
                return this.resolve(Integer.valueOf(this.getPropertyValue(name)), defaultValue);
            } catch (NumberFormatException nfEx) {
                LOG.warn("Property value could not be converted to an integer. name={}", name);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public Double getDouble(String name) {
        return this.getDouble(name, null);
    }

    public Double getDouble(String name, Double defaultValue) {
        if (this.contains(name)) {
            try {
                return this.resolve(Double.valueOf(this.getPropertyValue(name)), defaultValue);
            } catch (NumberFormatException nfEx) {
                LOG.warn("Property value could not be converted to a double. name={}", name);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public Long getLong(String name) {
        return this.getLong(name, null);
    }

    public Long getLong(String name, Long defaultValue) {
        if (this.contains(name)) {
            try {
                return this.resolve(Long.valueOf(this.getPropertyValue(name)), defaultValue);
            } catch (NumberFormatException nfEx) {
                LOG.warn("Property value could not be converted to a long. name={}", name);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public Float getFloat(String name) {
        return this.getFloat(name, null);
    }

    public Float getFloat(String name, Float defaultValue) {
        if (this.contains(name)) {
            try {
                return this.resolve(Float.valueOf(this.getPropertyValue(name)), defaultValue);
            } catch (NumberFormatException nfEx) {
                LOG.warn("Property value could not be converted to a float. name={}", name);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    <T> T resolve(T value, T defaultValue) {
        return (value == null ? defaultValue : value);
    }

}
