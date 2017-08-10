package com.sample.soap.xml.dm;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.sample.soap.xml.dm package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SelectAllEmpAllDataResponse_QNAME = new QName("http://dm.xml.soap.sample.com/",
            "selectAllEmpAllDataResponse");
    private final static QName _SelectAllEmpAllData_QNAME = new QName("http://dm.xml.soap.sample.com/",
            "selectAllEmpAllData");
    private final static QName _QueryEmpConfigsResponse_QNAME = new QName("http://dm.xml.soap.sample.com/",
            "queryEmpConfigsResponse");
    private final static QName _QueryEmpConfigs_QNAME = new QName("http://dm.xml.soap.sample.com/", "queryEmpConfigs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: com.sample.soap.xml.dm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QueryEmpConfigsResponse }
     * 
     */
    public QueryEmpConfigsResponse createQueryEmpConfigsResponse() {
        return new QueryEmpConfigsResponse();
    }

    /**
     * Create an instance of {@link SelectAllEmpAllDataResponse }
     * 
     */
    public SelectAllEmpAllDataResponse createSelectAllEmpAllDataResponse() {
        return new SelectAllEmpAllDataResponse();
    }

    /**
     * Create an instance of {@link SelectAllEmpAllData }
     * 
     */
    public SelectAllEmpAllData createSelectAllEmpAllData() {
        return new SelectAllEmpAllData();
    }

    /**
     * Create an instance of {@link QueryEmpConfigs }
     * 
     */
    public QueryEmpConfigs createQueryEmpConfigs() {
        return new QueryEmpConfigs();
    }

    /**
     * Create an instance of {@link EmpConfigIdentifier }
     * 
     */
    public EmpConfigIdentifier createEmpConfigIdentifier() {
        return new EmpConfigIdentifier();
    }

    /**
     * Create an instance of {@link EmpDetails }
     * 
     */
    public EmpDetails createEmpDetails() {
        return new EmpDetails();
    }

    /**
     * Create an instance of {@link EmpAddress }
     * 
     */
    public EmpAddress createEmpAddress() {
        return new EmpAddress();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link SelectAllEmpAllDataResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://dm.xml.soap.sample.com/", name = "selectAllEmpAllDataResponse")
    public JAXBElement<SelectAllEmpAllDataResponse> createSelectAllEmpAllDataResponse(SelectAllEmpAllDataResponse value) {
        return new JAXBElement<SelectAllEmpAllDataResponse>(_SelectAllEmpAllDataResponse_QNAME,
                SelectAllEmpAllDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link SelectAllEmpAllData }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://dm.xml.soap.sample.com/", name = "selectAllEmpAllData")
    public JAXBElement<SelectAllEmpAllData> createSelectAllEmpAllData(SelectAllEmpAllData value) {
        return new JAXBElement<SelectAllEmpAllData>(_SelectAllEmpAllData_QNAME, SelectAllEmpAllData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link QueryEmpConfigsResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://dm.xml.soap.sample.com/", name = "queryEmpConfigsResponse")
    public JAXBElement<QueryEmpConfigsResponse> createQueryEmpConfigsResponse(QueryEmpConfigsResponse value) {
        return new JAXBElement<QueryEmpConfigsResponse>(_QueryEmpConfigsResponse_QNAME, QueryEmpConfigsResponse.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryEmpConfigs }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://dm.xml.soap.sample.com/", name = "queryEmpConfigs")
    public JAXBElement<QueryEmpConfigs> createQueryEmpConfigs(QueryEmpConfigs value) {
        return new JAXBElement<QueryEmpConfigs>(_QueryEmpConfigs_QNAME, QueryEmpConfigs.class, null, value);
    }

}
