package com.example.spring.boot.soap.controller;

/**
 * This is the mapper class which converts soap request to rest request.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class SpringBootExternalSoapServiceRequestMapper {

    public static com.example.spring.boot.rest.types.EmpConfigIdentifier mapQueryEmpConfigsSoapRequest(
            com.sample.soap.xml.dm.QueryEmpConfigs soapQueryEmpConfigs) {

        com.example.spring.boot.rest.types.EmpConfigIdentifier restEmpConfigIdentifier = new com.example.spring.boot.rest.types.EmpConfigIdentifier();
        com.sample.soap.xml.dm.EmpConfigIdentifier soapEmpConfigIdentifier = soapQueryEmpConfigs.getArg0();

        if (null != soapEmpConfigIdentifier) {
            restEmpConfigIdentifier.setId(soapEmpConfigIdentifier.getId());
        }
        return restEmpConfigIdentifier;

    }

    public static com.example.spring.boot.rest.types.EmpAddress mapSelectAllEmpAllDataSoapRequest(
            com.sample.soap.xml.dm.SelectAllEmpAllData soapSelectAllEmpAllData) {

        com.example.spring.boot.rest.types.EmpAddress restEmpAddress = new com.example.spring.boot.rest.types.EmpAddress();
        com.sample.soap.xml.dm.EmpAddress soapEmpAddress = soapSelectAllEmpAllData.getArg0();

        if (null != soapEmpAddress) {
            restEmpAddress.setAddress(soapEmpAddress.getAddress());
        }

        return restEmpAddress;
    }

}
