package com.example.spring.boot.rest.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.spring.boot.rest.service.DmServiceInterface;
import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@RunWith(MockitoJUnitRunner.class)
public class SpringBootExternalRestServiceControllerTest {

    @InjectMocks
    private SpringBootExternalRestServiceController dmRestServiceController;

    @Mock
    private DmServiceInterface dmService;

    @SuppressWarnings("static-access")
    @Test
    public void testGetEmpConfiguration() {

        EmpConfigIdentifier empConfigIdentifier = new EmpConfigIdentifier();
        empConfigIdentifier.setId(1);

        EmpDetails empDetails = getEmpDetails();

        Mockito.when(dmService.queryEmpConfigs(empConfigIdentifier)).thenReturn(empDetails);

        ResponseEntity<EmpDetails> response = dmRestServiceController.getEmpConfiguration(empConfigIdentifier);
        assertNotNull("ResponseEntity<EmpDetails> is Not Null", response);
        assertNotNull(HttpStatus.OK.toString(), response.getStatusCode().OK);
        assertEquals("200", response.getStatusCode().toString());

    }

    @SuppressWarnings("static-access")
    @Test
    public void testGetAllEmpByAddress() {

        EmpAddress empAddr = new EmpAddress();
        empAddr.setAddress("Sec49");

        List<EmpDetails> valueList = new ArrayList<EmpDetails>();
        valueList.add(getEmpDetails());

        Mockito.when(dmService.selectAllEmpAllData(empAddr)).thenReturn(valueList);

        ResponseEntity<List<EmpDetails>> response = dmRestServiceController.getAllEmpByAddress(empAddr);
        assertNotNull("ResponseEntity<List<EmpDetails>> is Not Null", response);
        assertNotNull(HttpStatus.OK.toString(), response.getStatusCode().OK);
        assertEquals("200", response.getStatusCode().toString());

    }

    private EmpDetails getEmpDetails() {
        EmpDetails empDetails = new EmpDetails();
        empDetails.setId(1);
        empDetails.setFirstName("Manu");
        empDetails.setLastName("L");
        empDetails.setAddress("Sec49");
        empDetails.setCity("Noida");
        empDetails.setEmail("manul30@yahoo.in");
        empDetails.setMobile("9450304100");
        return empDetails;
    }

}
