package com.example.spring.boot.soap.controller;

import java.util.List;

/**
 * This is the mapper class which converts rest response to soap response.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class SpringBootExternalSoapServiceResponseMapper {

    public static com.sample.soap.xml.dm.QueryEmpConfigsResponse mapQueryEmpConfigsSoapResponse(
            com.example.spring.boot.rest.types.EmpDetails restEmpDetails) {

        com.sample.soap.xml.dm.QueryEmpConfigsResponse queryEmpConfigsSoapResponse = new com.sample.soap.xml.dm.QueryEmpConfigsResponse();
        com.sample.soap.xml.dm.EmpDetails soapEmpDetails = new com.sample.soap.xml.dm.EmpDetails();

        if (null != restEmpDetails) {
            setDetails(soapEmpDetails, restEmpDetails);
        }
        queryEmpConfigsSoapResponse.setReturn(soapEmpDetails);

        return queryEmpConfigsSoapResponse;
    }

    public static com.sample.soap.xml.dm.SelectAllEmpAllDataResponse mapSelectAllEmpAllDataSoapResponse(
            List<com.example.spring.boot.rest.types.EmpDetails> restEmpDetailsList) {

        com.sample.soap.xml.dm.SelectAllEmpAllDataResponse soapSelectAllEmpAllDataResponse = new com.sample.soap.xml.dm.SelectAllEmpAllDataResponse();
        com.sample.soap.xml.dm.EmpDetails soapEmpDetails = null;

        if (restEmpDetailsList != null && !restEmpDetailsList.isEmpty()) {

            for (com.example.spring.boot.rest.types.EmpDetails restEmpDetails : restEmpDetailsList) {
                soapEmpDetails = new com.sample.soap.xml.dm.EmpDetails();
                setDetails(soapEmpDetails, restEmpDetails);
                soapSelectAllEmpAllDataResponse.getReturn().add(soapEmpDetails);
            }
        }

        return soapSelectAllEmpAllDataResponse;
    }

    private static void setDetails(com.sample.soap.xml.dm.EmpDetails soapEmpDetails,
            com.example.spring.boot.rest.types.EmpDetails restEmpDetails) {
        soapEmpDetails.setId(restEmpDetails.getId());
        soapEmpDetails.setFirstName(restEmpDetails.getFirstName());
        soapEmpDetails.setLastName(restEmpDetails.getLastName());
        soapEmpDetails.setAddress(restEmpDetails.getAddress());
        soapEmpDetails.setCity(restEmpDetails.getCity());
        soapEmpDetails.setEmail(restEmpDetails.getEmail());
        soapEmpDetails.setMobile(restEmpDetails.getMobile());
    }

}
