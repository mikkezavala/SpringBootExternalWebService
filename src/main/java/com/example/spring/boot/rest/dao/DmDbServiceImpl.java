package com.example.spring.boot.rest.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring.boot.rest.db.connector.DmDatabaseConnector;
import com.example.spring.boot.rest.db.extractor.EmpConfigResultSetExtractor;
import com.example.spring.boot.rest.exception.ErrorCode;
import com.example.spring.boot.rest.exception.FrameworkError;
import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Repository("DmDbServiceImpl")
public class DmDbServiceImpl implements DmDbService {

    private static final Logger LOG = LoggerFactory.getLogger(DmDbServiceImpl.class);

    @Autowired
    private DmDatabaseConnector dmDatabaseConnector;

    public DmDatabaseConnector getDmDatabaseConnector() {
        return dmDatabaseConnector;
    }

    public void setDmDatabaseConnector(DmDatabaseConnector dmDatabaseConnector) {
        this.dmDatabaseConnector = dmDatabaseConnector;
    }

    public EmpDetails queryEmpConfigs(EmpConfigIdentifier id) {

        List<EmpDetails> configList = new ArrayList<EmpDetails>();
        List<Integer> args = new ArrayList<Integer>();
        args.add(id.getId());

        String query = "select * from employee where id = ?";

        try {
            configList = dmDatabaseConnector.getJdbcTemplate().query(query, args.toArray(),
                    new EmpConfigResultSetExtractor());
            LOG.debug(" Query for queryEmpConfigs : " + query);
        } catch (Exception e) {
            LOG.error("Exception while getting emp configurations  ", e);
        }
        if (org.apache.commons.collections.CollectionUtils.isEmpty(configList)) {
            throw new FrameworkError(ErrorCode.CS_1001.getValue());
        }

        if (!configList.isEmpty()) {

            return configList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<EmpDetails> selectAllEmpAllData(EmpAddress empAddr) {

        List<EmpDetails> configList = new ArrayList<EmpDetails>();
        List<String> args = new ArrayList<String>();
        args.add(empAddr.getAddress());

        String query = "select * from employee where address = ?";

        try {
            configList = dmDatabaseConnector.getJdbcTemplate().query(query, args.toArray(),
                    new EmpConfigResultSetExtractor());
            LOG.debug(" Query for selectAllEmpAllData : " + query);
        } catch (Exception e) {
            LOG.error("Exception while getting emp configurations  ", e);
        }

        if (org.apache.commons.collections.CollectionUtils.isEmpty(configList)) {
            throw new FrameworkError(ErrorCode.CS_1002.getValue());
        }

        if (!configList.isEmpty()) {

            return configList;
        } else {
            return null;
        }
    }

}
