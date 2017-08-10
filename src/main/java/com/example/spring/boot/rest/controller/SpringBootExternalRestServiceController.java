package com.example.spring.boot.rest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.boot.rest.service.DmServiceInterface;
import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@RestController
@RequestMapping(value = "/api/externalDmCustomer/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class SpringBootExternalRestServiceController {

    @Autowired
    @Qualifier("DmServiceImpl")
    private DmServiceInterface dmService;

    private static final Logger LOG = LoggerFactory.getLogger(SpringBootExternalRestServiceController.class);

    @Lazy(false)
    @RequestMapping(method = RequestMethod.POST, value = "/empConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmpDetails> getEmpConfiguration(@RequestBody EmpConfigIdentifier id) {

        if (LOG.isDebugEnabled())
            LOG.debug("Received Request for getEmpConfiguration {} ", id);

        EmpDetails response = dmService.queryEmpConfigs(id);

        if (LOG.isDebugEnabled())
            LOG.debug("Returning Response for getEmpConfiguration");

        return new ResponseEntity<EmpDetails>(response, HttpStatus.OK);
    }

    @Lazy(false)
    @RequestMapping(method = RequestMethod.POST, value = "/empConfigAddress", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<EmpDetails>> getAllEmpByAddress(@RequestBody EmpAddress empAddr) {

        if (LOG.isDebugEnabled())
            LOG.debug("Received Request for getAllEmpByAddress {} ", empAddr);

        List<EmpDetails> response = dmService.selectAllEmpAllData(empAddr);
        if (LOG.isDebugEnabled())
            LOG.debug("Returning Response for getAllEmpByAddress");

        return new ResponseEntity<List<EmpDetails>>(response, HttpStatus.OK);
    }

}
