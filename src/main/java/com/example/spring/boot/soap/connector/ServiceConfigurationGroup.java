package com.example.spring.boot.soap.connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * This is the class that houses a set of properties that are related by a
 * common path prefix.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class ServiceConfigurationGroup extends AbstractServiceConfiguration implements Iterable<String> {

    public static final List<String> PASSWORD_SLUGS = Collections.unmodifiableList(new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add("pwd");
            add("password");
            add("passwd");
        }
    });

    private String path;
    private Map<String, String> propMap = new HashMap<>();

    public ServiceConfigurationGroup(String path, Map<String, String> propMap) {
        this.path = path;
        this.propMap = (propMap == null ? this.propMap : propMap);
    }

    public ServiceConfigurationGroup(String path) {
        this.path = path;
    }

    @Override
    public String getPropertyValue(String name) {
        return this.propMap.get(name);
    }

    @Override
    public boolean contains(String name) {
        return this.propMap.containsKey(name);
    }

    @Override
    public boolean containsValue(String name) {
        return this.propMap.get(name) != null;
    }

    public String getPath() {
        return this.path;
    }

    public void add(String name, String value) {
        this.propMap.put(name, value);
    }

    public void addAll(Map<String, String> hashMap) {
        this.propMap.putAll(hashMap);
    }

    public Map<String, String> setAll(Map<String, String> hashMap) {
        Map<String, String> oldMap = this.propMap;
        this.propMap = new HashMap<>(hashMap);
        return oldMap;
    }

    public Set<String> keySet() {
        return new HashSet<String>(this.propMap.keySet());
    }

    public List<String> values() {
        return new ArrayList<>(this.propMap.values());
    }

    /**
     * @return A map containing the property names and values.
     */
    public Map<String, String> toMap() {
        return new HashMap<>(this.propMap);
    }

    /**
     * Breaks out a subset of the properties matching the given path.
     * <p>
     * Given this grouping:
     * 
     * <pre>
     * Path: my.property.tree
     * Entries:
     *     branch1.leaf2=value2
     *     branch1.leaf3=value3
     *     branch2.leaf4=value4
     *     branch2.leaf5=value5
     *     branch3.leaf6=value6
     *     pool.db.leaf7=value7
     *     pool.cb.leaf8=value8
     * </pre>
     * 
     * Calling <code>group.breakout("pool.db")</code> will result in a returned
     * group with:
     * 
     * <pre>
     * Path: my.property.tree.pool.db
     * Entries:
     *     leaf7=value7
     * </pre>
     * 
     * </p>
     * 
     * @param path
     *            The path of the values to break out of the grouping.
     * @return A new configuration group containing entries which matched the
     *         path.
     */
    public ServiceConfigurationGroup breakOut(String path) {
        ServiceConfigurationGroup breakGroup = null;
        String key = null;
        String value = null;
        String breakId = null;
        String breakKey = null;
        path = (path.endsWith(".") ? path : path + ".");
        for (Map.Entry<String, String> entry : this.propMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (key.startsWith(path)) {
                breakId = key.substring(0, path.length() - 1);
                breakKey = key.substring(path.length());
                if (breakGroup == null) {
                    breakGroup = new ServiceConfigurationGroup(this.path + "." + breakId);
                }
                breakGroup.add(breakKey, value);
            }
        }
        return breakGroup;
    }

    /**
     * Breaks down the property group like {@link #breakDownToMap()}, but only
     * returns the resultant groups without the map and broken down property
     * prefix keys.
     * 
     * @return
     */
    public List<ServiceConfigurationGroup> breakDown() {
        Map<String, ServiceConfigurationGroup> bdMap = this.breakDownToMap();
        return new ArrayList<>(bdMap.values());
    }

    /**
     * Takes the current collected properties and breaks them down into smaller
     * groups based on the current property names. For example, give the
     * properties:
     * 
     * <pre>
     * {@code
     * oracle.connection.evildb.url=****
     * oracle.connection.evildb.name=****
     * oracle.connection.evildb.pass=****
     * oracle.connection.gooddb.url=****
     * oracle.connection.gooddb.name=****
     * oracle.connection.gooddb.pass=****
     * }
     * </pre>
     * 
     * First you get the group:
     * 
     * <pre>
     * {@code
     * ServiceConfigurationGroup oracleGroup = serviceConfiguration.getStringGroup("oracle.connection");
     * oracleGroup.getPath() // = "oracle.connection"
     * oracleGroup.keySet() // = {"evildb.url","evildb.name","evildb.pass","gooddb.url","gooddb.name","gooddb.pass"}
     * }
     * </pre>
     * 
     * The break down will automatically separate the properties using the next
     * suffix of the keys:
     * 
     * <pre>
     * {@code
     * Map<String,ServiceConfigurationGroup> breakDownMap = oracleGroup.breakDownToMap();
     * ServiceConfigurationGroup evilGroup = breakDownMap.get("evildb");
     * evilGroup.getPath() // = "oracle.connection.evildb"
     * evilGroup.keySet() // = {"url","name","pass"}
     * ServiceConfigurationGroup goodGroup = breakDownMap.get("gooddb");
     * evilGroup.getPath() // = "oracle.connection.gooddb"
     * evilGroup.keySet() // = {"url","name","pass"}
     * }
     * </pre>
     * 
     * @return
     */
    public Map<String, ServiceConfigurationGroup> breakDownToMap() {
        Map<String, ServiceConfigurationGroup> breakMap = new HashMap<>();
        ServiceConfigurationGroup breakGroup = null;
        String key = null;
        String value = null;
        int breakIdx = 0;
        String breakId = null;
        String breakKey = null;
        for (Map.Entry<String, String> entry : this.propMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            breakIdx = key.indexOf(".");
            if (breakIdx < 0) {
                continue;
            }
            breakId = key.substring(0, breakIdx);
            breakKey = key.substring(breakIdx + 1);
            breakGroup = breakMap.get(breakId);
            if (breakGroup == null) {
                breakGroup = new ServiceConfigurationGroup(this.path + "." + breakId);
                breakMap.put(breakId, breakGroup);
            }
            breakGroup.add(breakKey, value);
        }
        return breakMap;
    }

    /**
     * Breaks down the property group like {@link #breakDownToMap(String)}, but
     * only returns the resultant groups without the map and broken down
     * property prefix keys.
     * 
     * @return
     */
    public List<ServiceConfigurationGroup> breakDown(String prefix) {
        Map<String, ServiceConfigurationGroup> bdMap = this.breakDownToMap(prefix);
        return new ArrayList<>(bdMap.values());
    }

    /**
     * 
     * @param prefix
     * @return
     */
    public Map<String, ServiceConfigurationGroup> breakDownToMap(String prefix) {
        Map<String, ServiceConfigurationGroup> breakMap = new HashMap<>();
        ServiceConfigurationGroup breakGroup = null;
        prefix = (prefix.endsWith(".") ? prefix : prefix + ".");
        String key = null;
        String trunkKey = null;
        String value = null;
        int breakIdx = 0;
        String breakId = null;
        String breakKey = null;

        for (Map.Entry<String, String> entry : this.propMap.entrySet()) {
            key = entry.getKey();
            if (key.startsWith(prefix)) {
                trunkKey = key.substring(prefix.length());
                value = entry.getValue();
                breakIdx = trunkKey.indexOf(".");
                if (breakIdx < 0) {
                    continue;
                }
                breakId = trunkKey.substring(0, breakIdx);
                breakKey = trunkKey.substring(breakIdx + 1);
                breakGroup = breakMap.get(breakId);
                if (breakGroup == null) {
                    breakGroup = new ServiceConfigurationGroup(this.path + "." + prefix + breakId);
                    breakMap.put(breakId, breakGroup);
                }
                breakGroup.add(breakKey, value);
            }
        }
        return breakMap;
    }

    @Override
    public Iterator<String> iterator() {
        return this.propMap.values().iterator();
    }

    public int size() {
        return this.propMap.size();
    }

    /**
     * 
     * @return
     */
    public boolean isEmpty() {
        return this.propMap.isEmpty();
    }

    @Override
    public String toString() {
        return this.toString("  ");
    }

    public String toString(String separator) {
        List<String> entries = new ArrayList<>();
        String key = null;
        String value = null;
        for (Map.Entry<String, String> entry : this.propMap.entrySet()) {
            key = entry.getKey();
            value = (this.checkFromList(key, PASSWORD_SLUGS) ? "**********" : entry.getValue());
            entries.add(this.getPath() + "." + key + "=" + value);
        }
        return String.join(separator, entries);
    }

    boolean checkFromList(String text, List<String> targets) {
        for (String target : targets) {
            if (StringUtils.contains(text, target)) {
                return true;
            }
        }
        return false;
    }
}
