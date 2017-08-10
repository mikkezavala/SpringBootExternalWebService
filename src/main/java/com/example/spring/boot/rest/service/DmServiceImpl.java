package com.example.spring.boot.rest.service;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.spring.boot.rest.handler.SelectAllDmCustomerHandler;
import com.example.spring.boot.rest.handler.SingleSelectDmCustomerHandler;
import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component("DmServiceImpl")
public class DmServiceImpl implements DmServiceInterface {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(DmServiceImpl.class);

    @Autowired
    private SingleSelectDmCustomerHandler singleSelectDmCustomerHandler;

    @Autowired
    private SelectAllDmCustomerHandler selectAllDmCustomerHandler;

    @Override
    public EmpDetails queryEmpConfigs(EmpConfigIdentifier id) {

        if (id != null) {
            LOG.debug("MyServiceImpl::queryEmpConfigs  id : " + id.getId());
        }
        return singleSelectDmCustomerHandler.queryEmpConfigs(id);
    }

    @Override
    public List<EmpDetails> selectAllEmpAllData(EmpAddress empAddr) {

        if (empAddr.getAddress() != null) {
            LOG.debug("MyServiceImpl::selectAllEmpAllData  address of your sector : " + empAddr.getAddress());
        }
        return selectAllDmCustomerHandler.selectAllEmpAllData(empAddr);
    }

}
