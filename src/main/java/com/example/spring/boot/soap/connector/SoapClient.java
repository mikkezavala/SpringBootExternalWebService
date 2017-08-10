package com.example.spring.boot.soap.connector;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

/**
 * A simple wrapper around Spring WS's WebServiceTemplate to allow for easy
 * creation with spring boot instrumentation. Use of this class does not
 * guarantee the availability of timeout functionality.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component
public class SoapClient extends WebServiceTemplate {

    public SoapClient() {
    };

    public SoapClient(Marshaller marshaller, String remoteServiceUrl, ClientInterceptor... clientInterceptor) {
        super(marshaller);
        setDefaultUri(remoteServiceUrl);
        setInterceptors(clientInterceptor);
    }

    /*
     * Use this constructor for clients that call external services outside our
     * application
     */
    public SoapClient(Marshaller marshaller, String remoteServiceUrl) {
        super(marshaller);
        setDefaultUri(remoteServiceUrl);
    }

    /*
     * Call this method to add an interceptor instead of calling
     * setInterceptors(ClientInterceptor[] interceptors)
     */
    public void addClientInterceptor(ClientInterceptor clientInterceptor) {
        ClientInterceptor[] interceptors = getInterceptors();
        if (interceptors != null) {
            setInterceptors(ArrayUtils.add(interceptors, clientInterceptor));
        } else {
            setInterceptors(new ClientInterceptor[] { clientInterceptor });
        }
    }

    @Override
    public <T> T sendAndReceive(String uriString, WebServiceMessageCallback requestCallback,
            WebServiceMessageExtractor<T> responseExtractor) {
        // ServiceContext.put("REMOTE_SERVICE_URL", uriString);
        return super.sendAndReceive(uriString, requestCallback, responseExtractor);
    }

    public List<ClientInterceptor> getInterceptorList() {
        return Arrays.asList(this.getInterceptors());
    }

}
