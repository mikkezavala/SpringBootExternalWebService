package com.example.spring.boot.soap.connector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.WSConstants;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

/**
 * A configuration object used by {@link SoapClientFactory} to create clients.
 * The {@link RefreshScope} annotation ensures that the configuration object
 * will be refreshed if a refresh event is triggered for the service.
 * 
 * The format is: [propertiesPrefix].[clientId].[property]=[value]
 * 
 * soapclient.def.myClient.readTimeout=500
 * soapclient.def.myClient.connectTimeout=500
 * soapclient.def.myClient.wss4j.actions=UsernameToken
 * soapclient.def.myClient.wss4j.username=myUser
 * soapclient.def.myClient.wss4j.password=aPassword
 * soapclient.def.myClient.wss4j.passwordType=1000
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class SoapClientConfiguration {

    public static final String CONFIG_PREFIX = "soapclient.def.";
    public static final String KEY_CONNECT_TIMEOUT = "connectTimeout";
    public static final String KEY_CONNECT_REQUEST_TIMEOUT = "connectRequestTimeout";
    public static final String KEY_READ_TIMEOUT = "readTimeout";
    public static final String KEY_MARSHALLER_CLASSNAME = "marshallerClassName";
    public static final String KEY_MARSHALLER_BEANNAME = "marshallerBeanName";
    public static final String KEY_ENDPOINT = "endpoint";
    public static final String KEY_DISABLE_COOKIES = "disableCookies";
    public static final String KEY_POOL = "pool";
    public static final String KEY_POOL_MAX_TOTAL_CONNECTIONS = "maxTotalConnections";
    public static final String KEY_POOL_MAX_ROUTE_CONNECTIONS = "maxRouteConnections";
    public static final String KEY_POOL_IDLE_TIMEOUT = "idleTimeout";
    public static final String KEY_WSS4J = "wss4j";
    public static final String KEY_WSS4J_ACTIONS = "actions";
    public static final String KEY_WSS4J_USERNAME = "username";
    public static final String KEY_WSS4J_PASSWORD = "password";
    public static final String KEY_WSS4J_PASSWORD_TYPE = "passwordType";
    public static final String KEY_WSS4J_MUST_UNDERSTAND = "mustUnderstand";
    public static final String KEY_PROXY = "proxy";
    public static final String KEY_PROXY_HOST = "host";
    public static final String KEY_PROXY_PORT = "port";
    public static final String KEY_PROXY_USERNAME = "username";
    public static final String KEY_PROXY_PASSWORD = "password";

    public static final int DEFAULT_CONNECT_TIMEOUT = 1000;
    public static final int DEFAULT_CONNECT_REQUEST_TIMEOUT = 1000;
    public static final int DEFAULT_READ_TIMEOUT = 10000;
    public static final boolean DEFAULT_DISABLE_COOKIES = false;
    public static final int DEFAULT_POOL_MAX_TOTAL_CONNECTIONS = 80;
    public static final int DEFAULT_POOL_MAX_ROUTE_CONNECTIONS = 20;
    public static final int DEFAULT_POOL_IDLE_TIMEOUT = 10000;
    public static final String DEFAULT_WSS4J_ACTIONS = "UsernameToken Timestamp";
    public static final boolean DEFAULT_WSS4J_MUST_UNDERSTAND = false;

    private String propertyPrefix = CONFIG_PREFIX;

    private List<ClientInterceptor> interceptorSubstitutions = new ArrayList<>();

    private List<ClientInterceptor> interceptorOverrideList = new ArrayList<>();
    private Integer connectionTimeout;
    private Integer connectRequestTimeout;
    private Integer readTimeout;
    private String endpoint;
    private boolean disableCookies;

    private String marshallerBeanName;
    private String marshallerClassName;

    private boolean wss4jEnabled = false;
    private String wss4jActions;
    private String wss4jUsername;
    private String wss4jPassword;
    private String wss4jPasswordType;
    private Boolean wss4jMustUnderstand;

    private boolean proxyEnabled = false;
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUsername;
    private String proxyPassword;

    private Integer poolMaxTotalConnections;
    private Integer poolMaxRouteConnections;
    private Integer poolIdleTimout;

    /**
     * Creates a default configuration.
     */
    public SoapClientConfiguration() {
        this.setProperties(null);
    }

    /**
     * Creates a configuration using the values in a
     * {@link ServiceConfigurationGroup}
     * 
     * @param propGrp
     *            The property group from which to take configuration values.
     */
    public SoapClientConfiguration(ServiceConfigurationGroup propGrp) {
        this.setProperties(propGrp);
    }

    /**
     * 
     * @param prefix
     * @return
     */
    public SoapClientConfiguration setPropertyPrefix(String prefix) {
        this.propertyPrefix = prefix;
        return this;
    }

    /**
     * 
     * @param propGroup
     */
    public void setProperties(ServiceConfigurationGroup propGroup) {
        this.connectionTimeout = this.determineIntegerValue(propGroup, KEY_CONNECT_TIMEOUT, this.connectionTimeout,
                DEFAULT_CONNECT_TIMEOUT);
        this.connectRequestTimeout = this.determineIntegerValue(propGroup, KEY_CONNECT_REQUEST_TIMEOUT,
                this.connectRequestTimeout, DEFAULT_CONNECT_REQUEST_TIMEOUT);
        this.readTimeout = this.determineIntegerValue(propGroup, KEY_READ_TIMEOUT, this.readTimeout,
                DEFAULT_READ_TIMEOUT);
        this.marshallerBeanName = this.determineStringValue(propGroup, KEY_MARSHALLER_BEANNAME,
                this.marshallerBeanName, null);
        this.marshallerClassName = this.determineStringValue(propGroup, KEY_MARSHALLER_CLASSNAME,
                this.marshallerClassName, null);
        this.endpoint = this.determineStringValue(propGroup, KEY_ENDPOINT, this.endpoint, null);
        this.disableCookies = this.determineBooleanValue(propGroup, KEY_DISABLE_COOKIES, this.disableCookies,
                DEFAULT_DISABLE_COOKIES);

        if (propGroup != null) {
            ServiceConfigurationGroup wss4jProps = propGroup.breakOut(KEY_WSS4J);
            if (wss4jProps != null) {
                this.wss4jEnabled = true;
                this.wss4jActions = this.determineStringValue(wss4jProps, KEY_WSS4J_ACTIONS, this.wss4jActions,
                        DEFAULT_WSS4J_ACTIONS);
                this.wss4jUsername = this
                        .determineStringValue(wss4jProps, KEY_WSS4J_USERNAME, this.wss4jUsername, null);
                this.wss4jPassword = this
                        .determineStringValue(wss4jProps, KEY_WSS4J_PASSWORD, this.wss4jPassword, null);
                this.wss4jPasswordType = this.determineStringValue(wss4jProps, KEY_WSS4J_PASSWORD_TYPE,
                        this.wss4jPasswordType, null);
                this.wss4jMustUnderstand = this.determineBooleanValue(wss4jProps, KEY_WSS4J_MUST_UNDERSTAND,
                        this.wss4jMustUnderstand, DEFAULT_WSS4J_MUST_UNDERSTAND);
            }
            ServiceConfigurationGroup proxyProps = propGroup.breakOut(KEY_PROXY);
            if (proxyProps != null) {
                this.proxyEnabled = true;
                this.proxyHost = this.determineStringValue(proxyProps, KEY_PROXY_HOST, this.proxyHost, null);
                this.proxyPort = this.determineIntegerValue(proxyProps, KEY_PROXY_PORT, this.proxyPort, null);
                this.proxyUsername = this
                        .determineStringValue(proxyProps, KEY_PROXY_USERNAME, this.proxyUsername, null);
                this.proxyPassword = this
                        .determineStringValue(proxyProps, KEY_PROXY_PASSWORD, this.proxyPassword, null);
            }
        }
        ServiceConfigurationGroup poolProps = (propGroup == null ? null : propGroup.breakOut(KEY_POOL));
        this.poolMaxTotalConnections = this.determineIntegerValue(poolProps, KEY_POOL_MAX_TOTAL_CONNECTIONS,
                this.poolMaxTotalConnections, DEFAULT_POOL_MAX_TOTAL_CONNECTIONS);
        this.poolMaxRouteConnections = this.determineIntegerValue(poolProps, KEY_POOL_MAX_ROUTE_CONNECTIONS,
                this.poolMaxRouteConnections, DEFAULT_POOL_MAX_ROUTE_CONNECTIONS);
        this.poolIdleTimout = this.determineIntegerValue(poolProps, KEY_POOL_IDLE_TIMEOUT, this.poolIdleTimout,
                DEFAULT_POOL_IDLE_TIMEOUT);

    }

    private Integer determineIntegerValue(ServiceConfigurationGroup propGroup, String propName, Integer currentValue,
            Integer defaultValue) {
        Integer propVal = (propGroup == null ? null : propGroup.getInteger(propName));
        return (propVal != null ? propVal : (currentValue != null ? currentValue : defaultValue));
    }

    private String determineStringValue(ServiceConfigurationGroup propGroup, String propName, String currentValue,
            String defaultValue) {
        String propVal = (propGroup == null ? null : propGroup.getString(propName));
        return (propVal != null ? propVal : (currentValue != null ? currentValue : defaultValue));
    }

    private Boolean determineBooleanValue(ServiceConfigurationGroup propGroup, String propName, Boolean currentValue,
            Boolean defaultValue) {
        Boolean propVal = (propGroup == null ? null : propGroup.getBoolean(propName));
        return (propVal != null ? propVal : (currentValue != null ? currentValue : defaultValue));
    }

    /**
     * 
     * @return
     */
    public String getPropertyPrefix() {
        return this.propertyPrefix;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout == null ? DEFAULT_CONNECT_TIMEOUT : this.connectionTimeout;
    }

    public void setConnectTimeout(int milliseconds) {
        this.connectionTimeout = milliseconds;
    }

    public int getConnectRequestTimeout() {
        return this.connectRequestTimeout == null ? DEFAULT_CONNECT_REQUEST_TIMEOUT : this.connectRequestTimeout;
    }

    /**
     * 
     * @param milliseconds
     */
    public void setConnectRequestTimeout(int milliseconds) {
        this.connectRequestTimeout = milliseconds;
    }

    public int getReadTimeout() {
        return this.readTimeout == null ? DEFAULT_READ_TIMEOUT : this.readTimeout;
    }

    public void setReadTimeout(int milliseconds) {
        this.readTimeout = milliseconds;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(String uri) {
        this.endpoint = uri;
    }

    /**
     * Default is <code>false</code>.
     * 
     * @param disableCookies
     *            Set to <code>true</code> if cookies should be disabled, or
     *            <code>false</code> if not.
     */
    public void setDisableCookies(boolean disableCookies) {
        this.disableCookies = disableCookies;
    }

    /**
     * @return Returns <code>true</code> if cookies should be disabled, or
     *         <code>false</code> if not.
     */
    public boolean getDisableCookies() {
        return this.disableCookies;
    }

    public String getMarshallerBeanName() {
        return this.marshallerBeanName;
    }

    public String getMarshallerClassName() {
        return this.marshallerClassName;
    }

    public boolean isWss4jEnabled() {
        return this.wss4jEnabled;
    }

    public String getWss4jActions() {
        return this.wss4jActions;
    }

    public String getWss4jUsername() {
        return this.wss4jUsername;
    }

    public String getWss4jPassword() {
        return this.wss4jPassword;
    }

    public boolean isProxyEnabled() {
        return this.proxyEnabled;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public String getProxyUsername() {
        return this.proxyUsername;
    }

    public String getProxyPassword() {
        return this.proxyPassword;
    }

    public int getPoolMaxTotalConnections() {
        return this.poolMaxTotalConnections == null ? DEFAULT_POOL_MAX_TOTAL_CONNECTIONS : this.poolMaxTotalConnections;
    }

    public int getPoolMaxRouteConnections() {
        return this.poolMaxRouteConnections == null ? DEFAULT_POOL_MAX_ROUTE_CONNECTIONS : this.poolMaxRouteConnections;
    }

    public int getPoolIdleTimout() {
        return this.poolIdleTimout == null ? DEFAULT_POOL_IDLE_TIMEOUT : this.poolIdleTimout;
    }

    public String getWss4jPasswordType() {
        return (StringUtils.isBlank(this.wss4jPassword) ? WSConstants.PW_NONE : (StringUtils
                .isBlank(this.wss4jPasswordType) ? WSConstants.PW_TEXT : this.wss4jPasswordType));
    }

    public boolean getWss4jMustUnderstand() {
        return this.wss4jMustUnderstand;
    }

    /**
     * <p>
     * <strong>Note:</strong>
     * </p>
     * 
     * @return
     */
    public boolean hasInterceptorOverrides() {
        return !this.interceptorOverrideList.isEmpty();
    }

    public List<ClientInterceptor> getInterceptorOverrideList() {
        return this.interceptorOverrideList;
    }

    public SoapClientConfiguration setInterceptorOverrideList(List<ClientInterceptor> overrideList) {
        this.interceptorOverrideList = new ArrayList<>(overrideList);
        return this;
    }

    public SoapClientConfiguration setInterceptorOverrideList(ClientInterceptor interceptor,
            ClientInterceptor... interceptors) {
        this.interceptorOverrideList = new ArrayList<>();
        if (interceptors != null) {
            this.interceptorOverrideList.addAll(Arrays.asList(interceptors));
        }
        this.interceptorOverrideList.add(0, interceptor);
        return this;
    }

    public boolean hasInterceptorSubstitutes() {
        return !this.interceptorSubstitutions.isEmpty();
    }

    /**
     * 
     * @param interceptorClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends ClientInterceptor> T getInterceptorSubstitute(Class<T> interceptorClass) {
        if (interceptorClass != null) {
            for (ClientInterceptor interceptor : this.interceptorSubstitutions) {
                if (interceptorClass.isInstance(interceptor)) {
                    return (T) interceptor;
                }
            }
        }
        return null;
    }

    public SoapClientConfiguration addInterceptorSubstitute(ClientInterceptor interceptor) {
        this.interceptorSubstitutions.add(interceptor);
        return this;
    }

}
