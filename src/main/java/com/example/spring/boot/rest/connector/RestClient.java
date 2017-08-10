package com.example.spring.boot.rest.connector;

import java.util.List;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A simple wrapper around Spring's RestTemplate to allow for easy creation to
 * get your client. Use of this class does not guarantee the availability of
 * timeout functionality.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component
public class RestClient extends RestTemplate {

    public RestClient() {
        super();
    }

    public RestClient(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    public RestClient(List<ClientHttpRequestInterceptor> interceptors) {
        super();
        setInterceptors(interceptors);
    }

    public RestClient(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors) {
        super(requestFactory);
        setInterceptors(interceptors);
    }

    public RestClient(List<HttpMessageConverter<?>> messageConverters, List<ClientHttpRequestInterceptor> interceptors) {
        super(messageConverters);
        setInterceptors(interceptors);
    }
}
