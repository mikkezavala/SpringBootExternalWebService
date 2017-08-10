package com.example.spring.boot.rest.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.example.multiple.spring.rest.types.CustomerConfigIdentifier;
import com.example.multiple.spring.rest.types.CustomerDetails;
import com.example.spring.boot.rest.exception.ErrorCode;
import com.example.spring.boot.rest.exception.FrameworkError;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component
public class MultiRestServiceConnector {

    private static final Logger LOG = LoggerFactory.getLogger(MultiRestServiceConnector.class);

    @Autowired
    private RestClientFactory restClientFactory;

    /*
     * Get method
     */
    public String greetingTime() {

        try {
            String response = restClientFactory.getClient().getForObject(
                    "http://localhost:6070/multipleCustomer/v1/welcome", String.class);

            System.out.println("response of get type request " + response);

            return response;
        } catch (Exception e) {
            throw new FrameworkError(ErrorCode.CS_1013.getValue());
        }

    }

    /*
     * Post method
     */
    public ResponseEntity<CustomerDetails> getCustConfiguration(CustomerConfigIdentifier id) {

        ResponseEntity<CustomerDetails> customerDetails = null;

        CustomerConfigIdentifier restRequest = new CustomerConfigIdentifier();
        restRequest.setId(id.getId());

        HttpHeaders headers = this.setHttpHeaders();

        HttpEntity<CustomerConfigIdentifier> entity = new HttpEntity<CustomerConfigIdentifier>(restRequest, headers);
        try {

            customerDetails = restClientFactory.getClient().postForEntity(
                    "http://localhost:6070/multipleCustomer/v1/custDetails", entity, CustomerDetails.class);

            System.out.println("External rest web service request ........................");

            System.out.println("returning customer details ..... " + customerDetails.toString());
            System.out.println("returning customer details ..... " + customerDetails.getBody().getId());
            System.out.println("returning customer details ..... " + customerDetails.getBody().getFirstName());
            System.out.println("returning customer details ..... " + customerDetails.getBody().getLastName());
            System.out.println("returning customer details ..... " + customerDetails.getBody().getAddress());
            System.out.println("returning customer details ..... " + customerDetails.getBody().getCity());

            System.out.println("External rest web service response ........................");

        } catch (RestClientException e) {
            e.printStackTrace();
            System.out.println(e);
            LOG.error("While processing of getCustConfiguration request HttpStatusCodeException is occure" + e);
            throw new FrameworkError(ErrorCode.CS_1014.getValue());
        }

        catch (Exception ex) {

            LOG.error("While processing of getCustConfiguration request GenericException is occured" + ex);
        }

        return customerDetails;
    }

    /**
     * This is utility method to set the headers
     * 
     * @return
     */
    private HttpHeaders setHttpHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        return headers;
    }

}
