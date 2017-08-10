package com.example.spring.boot.soap.exception;

import static com.example.spring.boot.soap.exception.SoapFaultHelper.addServiceFaultMessage;
import static com.google.common.base.Joiner.on;
import static com.google.common.collect.ImmutableBiMap.of;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.AbstractSoapFaultDefinitionExceptionResolver;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;

import com.example.spring.boot.rest.exception.FrameworkError;
import com.example.spring.boot.rest.exception.Message;
import com.example.spring.boot.rest.validator.FrameworkValidationError;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class FrameworkExceptionSoapFaultResolver extends AbstractSoapFaultDefinitionExceptionResolver {

    private ResourceBundleMessageSource messageSource;

    public FrameworkExceptionSoapFaultResolver(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * This method is used for fetching fault definition.
     * 
     * @param endpoint
     *            Object type
     * @param ex
     *            Exception type
     * @return soapFaultDefinition SoapFaultDefinition type
     */

    @Override
    public SoapFaultDefinition getFaultDefinition(Object endpoint, Exception ex) {

        Map<String, String> requestHeaders = new HashMap<>();
        SoapFaultDefinition soapFaultDefinition = new SoapFaultDefinition();
        soapFaultDefinition.setFaultCode(FaultCode.CLIENT.value());

        soapFaultDefinition.setFaultStringOrReason("An error occurred. Please check the detail section. [ASPECTS: ("
                + on(",").withKeyValueSeparator("=").join(requestHeaders) + ")]");
        return soapFaultDefinition;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof FrameworkValidationError) {
            FrameworkValidationError error = (FrameworkValidationError) ex;
            if (error.getValidationMessages() != null && !error.getValidationMessages().isEmpty()) {
                List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
                for (Message vMessage : error.getValidationMessages()) {
                    String message = null;
                    if (vMessage.getText() != null) {
                        message = vMessage.getText();
                    } else {
                        message = messageSource.getMessage(vMessage.getCode(), vMessage.getArgs(), Locale.getDefault());
                    }
                    list.add(Pair.of(vMessage.getCode(), message));
                }
                addServiceFaultMessage(fault, list);

            } else if (error.getErrorMessage() != null) {
                String message = null;
                Message m = error.getErrorMessage();
                if (m.getText() != null) {
                    message = m.getText();
                } else {
                    message = messageSource.getMessage(error.getErrorMessage().getCode(), m.getArgs(),
                            Locale.getDefault());
                }

                addServiceFaultMessage(fault, of(error.getErrorMessage().getCode(), message));
            }

        } else if (ex instanceof FrameworkError) {
            FrameworkError error = (FrameworkError) ex;
            String message = null;
            if (error.getErrorMessage() != null) {
                Message m = error.getErrorMessage();
                if (m.getText() != null) {
                    message = m.getText();
                } else {
                    message = messageSource.getMessage(error.getErrorMessage().getCode(), m.getArgs(),
                            Locale.getDefault());
                }
            }
            addServiceFaultMessage(fault, of(error.getErrorMessage().getCode(), message));
        } else if (ex instanceof RuntimeException) {
            if (ex.getCause() != null && ex.getCause().getCause() instanceof FrameworkError) {
                FrameworkError error = (FrameworkError) (ex.getCause().getCause());
                String message = null;
                if (error.getErrorMessage() != null) {
                    Message m = error.getErrorMessage();
                    if (m.getText() != null) {
                        message = m.getText();
                    } else {
                        message = messageSource.getMessage(error.getErrorMessage().getCode(), m.getArgs(),
                                Locale.getDefault());
                    }
                    addServiceFaultMessage(fault, of(error.getErrorMessage().getCode(), message));
                }
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
