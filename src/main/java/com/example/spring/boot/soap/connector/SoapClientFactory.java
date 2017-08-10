package com.example.spring.boot.soap.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.AbstractWsSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import com.example.spring.boot.rest.exception.ServiceException;

/**
 * This is the Base SOAP client factory. This factory creates and stores SOAP
 * client instances for repeated use. Wire this factory into any class that uses
 * a {@link SoapClient} and retrieve your instance from it. This will ensure
 * that all {@link ClientInterceptor} beans will have been initialized before
 * the client creation process does an application context scan for interceptors
 * to use. This will also ensure that a properties refresh event will trigger
 * new clients to be created with any new values.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component("factory.soapClient")
public class SoapClientFactory {

    private static Logger LOG = LoggerFactory.getLogger(SoapClientFactory.class);

    @Resource
    private ServiceConfiguration serviceConfig;

    @Resource
    private ApplicationContext appContext;

    private Map<String, SoapClient> clientMap = new HashMap<>();

    /**
     * Creates and stores a SOAP client. If a client with a specific ID was
     * retrieved before, the same client will not be recreated and the original
     * client will be returned.
     * 
     * @param id
     *            The ID of the client to create, used to retrieve the
     *            properties that define the client's functionality.
     * @return
     */
    public SoapClient getClient(String id) {
        if (!this.clientMap.containsKey(id)) {
            this.clientMap.put(id, this.createClient(id));
        }
        return this.clientMap.get(id);
    }

    /**
     * Override to change how clients are initially created.
     * 
     * @param id
     *            The ID of the client to create, used to retrieve the
     *            properties that define the client's functionality.
     * @return A fully initialized client.
     */
    protected SoapClient createClient(String id) {
        Map<String, SoapClientConfiguration> configMap = this.getConfigurations();
        SoapClientConfiguration config = configMap.get(id);
        if (config == null) {
            config = configMap.get(id + ".configuration");
        }
        if (config == null) {
            config = new SoapClientConfiguration();
        }
        String prefix = config.getPropertyPrefix();
        ServiceConfigurationGroup propGrp = serviceConfig.getStringGroup(prefix + id);
        config.setProperties(propGrp);
        String defaultUri = config.getEndpoint();
        SoapClient newClient = new SoapClient();
        if (StringUtils.isNotBlank(defaultUri)) {
            newClient.setDefaultUri(defaultUri);
        }
        return this.initializeClient(newClient, config);
    }

    /**
     * 
     * @return
     */
    protected Map<String, SoapClientConfiguration> getConfigurations() {
        return this.appContext.getBeansOfType(SoapClientConfiguration.class);
    }

    /**
     * Override this to add or change initialization of new clients.
     * 
     * @param client
     *            The new client to be initialized.
     * @param config
     *            The client configuration object.
     * @return The client, fully initialized.
     */
    protected SoapClient initializeClient(SoapClient client, SoapClientConfiguration config) {
        this.addInterceptors(client, config, this.getInterceptorList());
        this.addMarshaller(client, config);

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectTimeout(config.getConnectionTimeout())
                .setConnectionRequestTimeout(config.getConnectRequestTimeout())
                .setSocketTimeout(config.getReadTimeout());
        if (config.getDisableCookies()) {
            requestConfigBuilder.setCookieSpec(CookieSpecs.IGNORE_COOKIES);
        }

        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultRequestConfig(requestConfigBuilder.build())
                .addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());
        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(1, true));
        this.addProxy(builder, config);
        this.addPooling(builder, config);

        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
        messageSender.setHttpClient(builder.build());
        client.setMessageSender(messageSender);

        return client;
    }

    /**
     * Override to change what marshaller is added to factory-made clients.
     * Default behaviour is to check the configuration for the fully qualified
     * name of the marshaller class to retrieve. If <code>null</code>, will
     * default to using the first instance of {@link Marshaller} found in the
     * application context.
     * 
     * @param client
     *            The client to add the marshaller to.
     * @param config
     *            The client configuration.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void addMarshaller(SoapClient client, SoapClientConfiguration config) {
        Marshaller marshaller = null;
        String marshallerBeanName = config.getMarshallerBeanName();
        if (StringUtils.isNotBlank(marshallerBeanName)) {
            marshaller = (Marshaller) this.appContext.getBean(marshallerBeanName);
            if (marshaller == null) {
                throw new ServiceException("Indicated marshaller bean '" + marshallerBeanName
                        + "' was not found in the bean context.");
            }
        }
        if (marshaller == null) {
            String classname = config.getMarshallerClassName();
            Class marshallerClass = null;
            try {
                marshallerClass = (StringUtils.isBlank(classname) ? Jaxb2Marshaller.class : Class.forName(classname));
            } catch (ClassNotFoundException cnfEx) {
                throw new ServiceException("Marshaller class '" + classname
                        + "' for new SoapClient instance was not found.", cnfEx);
            }
            Map<String, Jaxb2Marshaller> marshallerMap = (Map) this.appContext.getBeansOfType(marshallerClass);
            if (marshallerMap == null || marshallerMap.isEmpty()) {
                if (StringUtils.isBlank(classname)) {
                    LOG.warn("No marshaller found for new SoapClient instance.");
                } else {
                    throw new ServiceException("Indicated marshaller class '" + classname
                            + "' was not found in the bean context.");
                }
            } else {
                marshaller = marshallerMap.values().iterator().next();
            }
        }
        client.setMarshaller(marshaller);
        if (marshaller instanceof Unmarshaller) {
            client.setUnmarshaller((Unmarshaller) marshaller);
        } else {
            throw new ServiceException(
                    "Marshaller does not implement the Unmarshaller interface. You need to provide a combination marshaller/unmarshaller");
        }
    }

    /**
     * Override to change how or what interceptors are added to generated
     * clients. By default, this will filter out any interceptor extended from
     * {@link AbstractWsSecurityInterceptor}, then add the remaining
     * interceptors to the client.
     * 
     * @param client
     * @param config
     *            The client configuration.
     * @param interceptorList
     */
    protected void addInterceptors(SoapClient client, SoapClientConfiguration config,
            List<ClientInterceptor> interceptorList) {
        List<ClientInterceptor> newList;
        if (config.hasInterceptorOverrides()) {
            newList = config.getInterceptorOverrideList();
        } else {
            newList = interceptorList;
            if (!newList.isEmpty()) {
                newList = newList.stream()
                        .filter(interceptor -> (!(interceptor instanceof AbstractWsSecurityInterceptor)))
                        .collect(Collectors.toList());
            }
            if (config.hasInterceptorSubstitutes()) {
                for (ListIterator<ClientInterceptor> i = newList.listIterator(); i.hasNext();) {
                    ClientInterceptor element = i.next();
                    ClientInterceptor substitute = config.getInterceptorSubstitute(element.getClass());
                    if (substitute != null) {
                        i.set(substitute);
                    }
                }
            }
        }
        Wss4jSecurityInterceptor securityInterceptor = this.getWss4jSecurityInterceptor(config);
        if (securityInterceptor != null) {
            newList.add(0, securityInterceptor);
        }
        client.setInterceptors(this.enforceInterceptorOrder(newList).toArray(new ClientInterceptor[] {}));
    }

    private List<ClientInterceptor> enforceInterceptorOrder(List<ClientInterceptor> interceptorList) {
        AbstractWsSecurityInterceptor securityInterceptor = null;
        // BackendLoggingInterceptor backendLoggingInterceptor = null;
        List<ClientInterceptor> orderedList = new ArrayList<>();
        for (ClientInterceptor interceptor : interceptorList) {
            if (interceptor instanceof AbstractWsSecurityInterceptor) {
                securityInterceptor = (AbstractWsSecurityInterceptor) interceptor;
            }
            // else if(interceptor instanceof BackendLoggingInterceptor) {
            // backendLoggingInterceptor =
            // (BackendLoggingInterceptor)interceptor; }
            else {
                orderedList.add(interceptor);
            }
        }
        if (securityInterceptor != null) {
            orderedList.add(0, securityInterceptor);
        }

        // if(backendLoggingInterceptor!=null) {
        // orderedList.add(backendLoggingInterceptor); }
        return orderedList;
    }

    /**
     * @param builder
     * @param config
     */
    protected void addProxy(HttpClientBuilder builder, SoapClientConfiguration config) {
        if (config.isProxyEnabled()) {
            HttpHost proxy = new HttpHost(config.getProxyHost(), config.getProxyPort(), null);
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(config.getProxyHost(), config.getProxyPort()),
                    new UsernamePasswordCredentials(config.getProxyUsername(), config.getProxyPassword()));
            builder.setProxy(proxy);
            builder.setDefaultCredentialsProvider(credsProvider);
        }
    }

    /**
     * Extracts configuration values for a WSS4J security interceptor if any
     * exist. If they do, creates the interceptor and adds it to the interceptor
     * array of the client. If no configuration values exist, no changes are
     * made.
     * 
     * @param config
     *            The client properties configuration group.
     * @return
     */
    private Wss4jSecurityInterceptor getWss4jSecurityInterceptor(SoapClientConfiguration config) {
        if (config.isWss4jEnabled()) {
            Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
            interceptor.setSecurementActions(config.getWss4jActions());
            interceptor.setSecurementUsername(config.getWss4jUsername());
            interceptor.setSecurementPassword(config.getWss4jPassword());
            interceptor.setSecurementPasswordType(config.getWss4jPasswordType());
            interceptor.setSecurementMustUnderstand(config.getWss4jMustUnderstand());
            return interceptor;
        }
        return null;
    }

    /**
     * 
     * @param builder
     * @param config
     */
    protected void addPooling(HttpClientBuilder builder, SoapClientConfiguration config) {
        PoolingHttpClientConnectionManager clientManager = new PoolingHttpClientConnectionManager();
        clientManager.setDefaultMaxPerRoute(config.getPoolMaxRouteConnections());
        clientManager.setMaxTotal(config.getPoolMaxTotalConnections());
        clientManager.closeIdleConnections(config.getPoolIdleTimout(), TimeUnit.MILLISECONDS);
        builder.setConnectionManager(clientManager);
    }

    /**
     * By default, searches the application context for any beans which
     * implement the {@link ClientHttpRequestInterceptor} and returns them.
     * <p>
     * Override this method in your own factory to change which interceptors are
     * collected.
     * </p>
     * 
     * @return The list of available interceptors.
     */
    protected List<ClientInterceptor> getInterceptorList() {
        Map<String, ClientInterceptor> interceptorMap = this.appContext.getBeansOfType(ClientInterceptor.class);
        if (interceptorMap != null && !interceptorMap.isEmpty()) {
            return new ArrayList<>(interceptorMap.values());
        } else {
            return new ArrayList<>();
        }
    }

}
