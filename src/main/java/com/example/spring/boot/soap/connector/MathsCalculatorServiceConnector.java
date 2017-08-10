package com.example.spring.boot.soap.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.SoapFaultException;

import com.example.spring.boot.rest.exception.ErrorCode;
import com.example.spring.boot.rest.exception.FrameworkError;
import com.uhc.ubh.arithmatic.calculation.Sum;
import com.uhc.ubh.arithmatic.calculation.SumResponse;

/**
 * This is the class where we will call the external SpringBootSoapServiceDemo
 * methods.
 * 
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component
public class MathsCalculatorServiceConnector {

    private static final Logger LOG = LoggerFactory.getLogger(MathsCalculatorServiceConnector.class);

    @Autowired
    @Qualifier("mcDataClient")
    private SoapClient mcDataClient;

    /**
     * Based on the sum ID, this method will return the summation
     * 
     * @param request
     * @return
     */
    public SumResponse sum(Sum request) {
        SumResponse response = null;
        try {
            LOG.debug("Request received for sum method: " + request);
            response = (SumResponse) mcDataClient.marshalSendAndReceive(request);
            LOG.debug("Response send for sum method : " + response);
        } catch (org.springframework.ws.soap.client.SoapFaultClientException appEx) {
            System.out.println(appEx);
        }

        catch (SoapFaultException ex) {
            LOG.warn("Something bad happened!");
            LOG.warn("Error ", ex);
            System.out.println(ex);
        }

        catch (Exception e) {
            System.out.println(e);
            throw new FrameworkError(ErrorCode.CS_1012.getValue());
        }

        return response;
    }

}
