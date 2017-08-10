package com.example.spring.boot.soap.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ws.context.MessageContext;

import com.example.spring.boot.rest.service.DmServiceInterface;
import com.example.spring.boot.rest.types.EmpDetails;
import com.sample.soap.xml.dm.EmpAddress;
import com.sample.soap.xml.dm.EmpConfigIdentifier;
import com.sample.soap.xml.dm.QueryEmpConfigs;
import com.sample.soap.xml.dm.QueryEmpConfigsResponse;
import com.sample.soap.xml.dm.SelectAllEmpAllData;
import com.sample.soap.xml.dm.SelectAllEmpAllDataResponse;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@RunWith(MockitoJUnitRunner.class)
public class SpringBootExternalSoapServiceControllerTest {

    @InjectMocks
    private SpringBootExternalSoapServiceController dmSoapServiceController;

    @Mock
    private DmServiceInterface dmService;

    @Test
    public void testQueryEmpConfigs() throws Exception {

        MessageContext messageContext = null;

        QueryEmpConfigs queryCustomer = new QueryEmpConfigs();
        EmpConfigIdentifier empConfigIdentifier = new EmpConfigIdentifier();
        empConfigIdentifier.setId(1);
        queryCustomer.setArg0(empConfigIdentifier);

        com.example.spring.boot.rest.types.EmpDetails restEmpDetails = getRestEmpDetails();

        Mockito.when(
                dmService.queryEmpConfigs(Mockito.any(com.example.spring.boot.rest.types.EmpConfigIdentifier.class)))
                .thenReturn(restEmpDetails);

        QueryEmpConfigsResponse response = dmSoapServiceController.queryEmpConfigs(queryCustomer, messageContext);
        assertNotNull(response);
        assertNotNull(response.getReturn());
        assertNotNull(response.getReturn().getFirstName());
        assertEquals("Manu", response.getReturn().getFirstName());
        assertEquals("L", response.getReturn().getLastName());

    }

    @Test
    public void testSelectAllEmpAllData() throws Exception {

        MessageContext messageContext = null;

        SelectAllEmpAllData selectAllEmpAllData = new SelectAllEmpAllData();
        EmpAddress empAddress = new EmpAddress();
        empAddress.setAddress("Sec49");
        selectAllEmpAllData.setArg0(empAddress);

        List<EmpDetails> valueList = new ArrayList<EmpDetails>();
        valueList.add(getRestEmpDetails());

        Mockito.when(dmService.selectAllEmpAllData(Mockito.any(com.example.spring.boot.rest.types.EmpAddress.class)))
                .thenReturn(valueList);

        SelectAllEmpAllDataResponse response = dmSoapServiceController.selectAllEmpAllData(selectAllEmpAllData,
                messageContext);
        assertNotNull(response);
        assertNotNull(response.getReturn());
        assertNotNull(response.getReturn().get(0).getFirstName());
        assertEquals("Manu", response.getReturn().get(0).getFirstName());
        assertEquals("L", response.getReturn().get(0).getLastName());
        assertEquals(response.getReturn().size(), 1);
    }

    private com.example.spring.boot.rest.types.EmpDetails getRestEmpDetails() {
        com.example.spring.boot.rest.types.EmpDetails restEmpDetails = new com.example.spring.boot.rest.types.EmpDetails();
        restEmpDetails.setId(1);
        restEmpDetails.setFirstName("Manu");
        restEmpDetails.setLastName("L");
        restEmpDetails.setAddress("Sec49");
        restEmpDetails.setCity("Noida");
        restEmpDetails.setEmail("manul30@yahoo.in");
        restEmpDetails.setMobile("9450304100");
        return restEmpDetails;
    }

}
