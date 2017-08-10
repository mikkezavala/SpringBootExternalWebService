package com.example.spring.boot.rest.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.multiple.spring.rest.types.CustomerConfigIdentifier;
import com.example.multiple.spring.rest.types.CustomerDetails;
import com.example.spring.boot.rest.connector.MultiRestServiceConnector;
import com.example.spring.boot.rest.dao.DmDbService;
import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component
public class SelectAllDmCustomerHandler {

    private final static Logger LOG = LoggerFactory.getLogger(SelectAllDmCustomerHandler.class);

    @Autowired
    private DmDbService mathsDbService;

    @Autowired
    private MultiRestServiceConnector multiRestServiceConnector;

    public DmDbService getMathsDbService() {
        return mathsDbService;
    }

    public void setMathsDbService(DmDbService mathsDbService) {
        this.mathsDbService = mathsDbService;
    }

    public List<EmpDetails> selectAllEmpAllData(EmpAddress empAddr) {

        List<EmpDetails> empDetailsList = null;

        if (empAddr.getAddress() != null) {
            empDetailsList = getMathsDbService().selectAllEmpAllData(empAddr);
        }

        LOG.info("External SpringBootRestServiceDemo calling for Post method ...... ");

        CustomerConfigIdentifier id = new CustomerConfigIdentifier();
        id.setId(5);

        ResponseEntity<CustomerDetails> response = multiRestServiceConnector.getCustConfiguration(id);

        System.out.println("External SpringBootRestServiceDemo " + response);

        LOG.info("External SpringBootRestServiceDemo calling for Get method ...... ");

        String getResponse = multiRestServiceConnector.greetingTime();

        System.out.println("External SpringBootRestServiceDemo " + getResponse);

        return empDetailsList;
    }

}
