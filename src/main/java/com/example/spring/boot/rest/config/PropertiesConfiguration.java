package com.example.spring.boot.rest.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.spring.boot.rest.db.connector.DmDatabaseConnector;
import com.example.spring.boot.soap.connector.ConnectorContants;
import com.example.spring.boot.soap.connector.SoapClient;
import com.example.spring.boot.soap.connector.SoapClientFactory;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Configuration
@EnableTransactionManagement
public class PropertiesConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesConfiguration.class);

    @Autowired
    private Environment env;

    @Autowired
    private ResourceLoader resourceLoader;

    @Resource(name = "factory.soapClient")
    protected SoapClientFactory soapClientFactory;

    @Bean(name = "MessageSource")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource obj = new ResourceBundleMessageSource();
        obj.setBasenames("bundles/Messages");
        return obj;
    }

    @Bean(name = { "dmDataSource" })
    @ConfigurationProperties(prefix = "datasource.dm")
    public DataSource dmDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dmJdbcTemplate")
    public JdbcOperations dmJdbcTemplate(@Qualifier("dmDataSource") DataSource ds) {
        LOG.info("\n\n\n\n jdbcTeamplatedm BEAN " + ds + " \n\n\n");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.setFetchSize(propInt("Default.fetchSize", 200));
        return jdbcTemplate;
    }

    @Bean(name = "dmDao")
    @Autowired
    public DmDatabaseConnector dmDao(@Qualifier("dmJdbcTemplate") JdbcOperations jop) {
        DmDatabaseConnector obj = new DmDatabaseConnector();
        obj.setJdbcTemplate(jop);
        try {
            obj.getJdbcTemplate().execute("select 1 from dual");
            LOG.info("DmDatabaseConnector Database sucessfully connected. ");
        } catch (Exception e) {
            LOG.error(" Error while connecting to DmDatabaseConnector.", e);
        }
        return obj;
    }

    @Bean(name = "mcDataClient")
    public SoapClient mcDataClient() {

        SoapClient client = this.soapClientFactory.getClient(ConnectorContants.MATHS_CALCULATOR_SERVICE_CLIENT);
        return client;
    }

    /**
     * org.springframework.oxm.jaxb.Jaxb2Marshaller
     * 
     * if we have multiple packages then use below
     * marshaller.setPackagesToScan("com.uhc.ubh.arithmatic.calculation"
     * ,"com.uhc.ubh.arithmatic.addition");
     * 
     */

    @Bean(name = "mathsCalculatorServiceClientMarshaller")
    public Jaxb2Marshaller imsDataServiceClientMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.uhc.ubh.arithmatic.calculation");
        return marshaller;
    }

    protected Integer propInt(String property, int defaultVal) {
        return toInteger(env.getProperty(property), defaultVal);
    }

    protected Integer toInteger(String val, int defaultVal) {
        if (this.isNull(val)) {
            return defaultVal;
        } else
            return toInteger(val);
    }

    protected Integer toInteger(String val) {
        return Integer.valueOf(val);
    }

    private boolean isNull(String str) {
        return !isNotNull(str);
    }

    private boolean isNotNull(String str) {
        return StringUtils.isNotBlank(str);
    }

}
