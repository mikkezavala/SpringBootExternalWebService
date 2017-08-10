package com.example.spring.boot.rest.service;

import java.util.List;

import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public abstract interface DmServiceInterface {

    /**
     * <!-- begin-UML-doc -->
     * <p>
     * QueryEmpConfigs provides the ability to seek a new Customer to eCust. A
     * Customer can be a person or an organization.
     * </p>
     * <p>
     * The input is EmpConfigIdentifier which should comprise of , at least,
     * Customer(Person or Organization),Person or OrganizationName and
     * customerContacts.
     * </p>
     * <p>
     * The output is EmpDetails which consists of the Customer object,Customer
     * association ,CustomerProfileCategory(presently only DETAILED),Customer
     * Product Reference, Customer Offer reference,CustomerServiceRequest&nbsp;,
     * a flag(isSynchronized) which indiacates whether the Account Number is in
     * synch with the billers, CustomerRole(a new Customer would have a
     * CustomerRole of PROSPECT- addition of Offers and Products would change
     * the CustomerRole to CONSUMER) and tenureDays.<br />
     * </p>
     * <!-- end-UML-doc -->
     * 
     * @param EmpConfigIdentifier
     * @return EmpDetails
     * @generated 
     *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
     */
    public EmpDetails queryEmpConfigs(EmpConfigIdentifier id);

    /**
     * <!-- begin-UML-doc --> <!-- end-UML-doc -->
     * 
     * @param EmpAddress
     * @return List<EmpDetails>
     * @generated 
     *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
     */
    public List<EmpDetails> selectAllEmpAllData(EmpAddress empAddr);

}
