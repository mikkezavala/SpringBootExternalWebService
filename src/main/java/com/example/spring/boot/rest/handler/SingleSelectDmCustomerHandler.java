package com.example.spring.boot.rest.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;
import com.example.spring.boot.soap.connector.MathsCalculatorServiceConnector;
import com.uhc.ubh.arithmatic.calculation.Sum;
import com.uhc.ubh.arithmatic.calculation.SumResponse;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component
public class SingleSelectDmCustomerHandler {

    private final static Logger LOG = LoggerFactory.getLogger(SingleSelectDmCustomerHandler.class);

    @Autowired
    private com.example.spring.boot.rest.dao.DmDbService mathsDbService;

    @Autowired
    private MathsCalculatorServiceConnector mathsCalculatorServiceConnector;

    public com.example.spring.boot.rest.dao.DmDbService getMathsDbService() {
        return mathsDbService;
    }

    public void setMathsDbService(com.example.spring.boot.rest.dao.DmDbService mathsDbService) {
        this.mathsDbService = mathsDbService;
    }

    public EmpDetails queryEmpConfigs(EmpConfigIdentifier id) {

        EmpDetails empDetails = null;

        if (id != null) {
            empDetails = getMathsDbService().queryEmpConfigs(id);
        }

        LOG.info("External SpringBootSoapServiceDemo calling ...... ");

        Sum request = new Sum();
        request.setIn0(1);
        request.setIn1(4);
        SumResponse sumResponse = mathsCalculatorServiceConnector.sum(request);
        System.out.println("sumResponse of SpringBootSoapServiceDemo " + sumResponse);

        LOG.info("printing response of external SpringBootSoapServiceDemo ...... " + sumResponse.getOut());

        return empDetails;
    }

}
