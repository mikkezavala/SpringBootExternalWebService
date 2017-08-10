package com.example.spring.boot.rest.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;

import com.example.spring.boot.rest.exception.FrameworkError;
import com.example.spring.boot.soap.exception.SoapExceptionResolverMapper;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@SuppressWarnings("deprecation")
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = { "com.example", "com.sample.soap.xml.dm", "com.uhc.ubh.arithmatic.calculation" })
@EnableWs
@EnableAspectJAutoProxy
public class SpringBootExternalWebServiceApplication extends WsConfigurerAdapter {

    @Autowired
    @Qualifier("MessageSource")
    private ResourceBundleMessageSource messageSource;

    public static void main(String[] args) {
        System.out.println("Start");
        SpringApplication.run(SpringBootExternalWebServiceApplication.class, args);
        System.out.println("End");
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ExternalDmService/*");
    }

    @Bean(name = "1.0")
    public SimpleWsdl11Definition simpletWsdl11Definition() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(dmSchema());
        return wsdl11Definition;
    }

    @Bean
    public Resource dmSchema() {
        return new ClassPathResource("wsdl/1.0/MathsDbService.wsdl");
    }

    @Bean
    public SoapExceptionResolverMapper frameworkExceptionResolver() {
        SoapExceptionResolverMapper soapExceptionResolverMapper = new SoapExceptionResolverMapper();
        soapExceptionResolverMapper.setOrder(Integer.MIN_VALUE);
        soapExceptionResolverMapper.registerResolver(FrameworkError.class,
                new com.example.spring.boot.soap.exception.FrameworkExceptionSoapFaultResolver(messageSource));
        soapExceptionResolverMapper.registerResolver(RuntimeException.class,
                new com.example.spring.boot.soap.exception.FrameworkExceptionSoapFaultResolver(messageSource));
        return soapExceptionResolverMapper;
    }

}
