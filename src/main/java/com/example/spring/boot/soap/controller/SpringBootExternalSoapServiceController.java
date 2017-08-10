package com.example.spring.boot.soap.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.spring.boot.rest.service.DmServiceInterface;
import com.example.spring.boot.rest.types.EmpDetails;
import com.sample.soap.xml.dm.QueryEmpConfigs;
import com.sample.soap.xml.dm.QueryEmpConfigsResponse;
import com.sample.soap.xml.dm.SelectAllEmpAllData;
import com.sample.soap.xml.dm.SelectAllEmpAllDataResponse;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component
@Endpoint("DmSoapServiceController")
public class SpringBootExternalSoapServiceController {

    private static final String NAMESPACE_URI = "http://dm.xml.soap.sample.com/";

    private static final Logger logger = LoggerFactory.getLogger(SpringBootExternalSoapServiceController.class);

    @Autowired
    private DmServiceInterface dmService;

    /**
     * This method is use to consume soap request and internally calling
     * business processing for the queryEmpConfigs operation
     * 
     * @param QueryEmpConfigs
     * @param MessageContext
     * @return QueryEmpConfigsResponse
     */

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "queryEmpConfigs")
    @ResponsePayload
    public QueryEmpConfigsResponse queryEmpConfigs(@RequestPayload QueryEmpConfigs queryCustomer,
            MessageContext messageContext) throws Exception {

        logger.info("SOAP Request Received Of queryCustomer " + queryCustomer);

        com.example.spring.boot.rest.types.EmpDetails restImplResponse = dmService
                .queryEmpConfigs(SpringBootExternalSoapServiceRequestMapper
                        .mapQueryEmpConfigsSoapRequest(queryCustomer));

        if (logger.isDebugEnabled())
            logger.debug("Returning Response for queryEmpConfigs");

        return SpringBootExternalSoapServiceResponseMapper.mapQueryEmpConfigsSoapResponse(restImplResponse);
    }

    /**
     * This method is use to consume soap request and internally calling
     * business processing for the selectAllEmpAllData operation
     * 
     * @param SelectAllEmpAllData
     * @param MessageContext
     * @return SelectAllEmpAllDataResponse
     */

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "selectAllEmpAllData")
    @ResponsePayload
    public SelectAllEmpAllDataResponse selectAllEmpAllData(@RequestPayload SelectAllEmpAllData selectAllEmpAllData,
            MessageContext messageContext) throws Exception {

        logger.info("SOAP Request Received Of selectAllEmpAddrData " + selectAllEmpAllData);

        List<EmpDetails> restImplResponse = dmService.selectAllEmpAllData(SpringBootExternalSoapServiceRequestMapper
                .mapSelectAllEmpAllDataSoapRequest(selectAllEmpAllData));

        if (logger.isDebugEnabled())
            logger.debug("Returning Response for selectAllEmpAllData");

        return SpringBootExternalSoapServiceResponseMapper.mapSelectAllEmpAllDataSoapResponse(restImplResponse);

    }

}
