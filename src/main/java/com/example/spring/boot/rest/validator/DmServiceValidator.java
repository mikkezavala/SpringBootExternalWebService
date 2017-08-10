package com.example.spring.boot.rest.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.example.spring.boot.rest.exception.ErrorCode;
import com.example.spring.boot.rest.exception.FrameworkError;
import com.example.spring.boot.rest.exception.Message;
import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@Component
@Aspect
public class DmServiceValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DmServiceValidator.class);

    /**
     * Validates the {@link DmService#queryEmpConfigs(EmpConfigIdentifier id)}
     * operation.
     * 
     * @param errors
     *            A {@link Errors} object containing the errors to return to the
     *            client.
     * @param id
     *            The <code>empConfigId</code> parameter passed in by the
     *            client.
     */

    @Pointcut("execution(* com.example.spring.boot.rest.service.DmServiceImpl.queryEmpConfigs(..)) && args(id,..))")
    public void validateQueryEmpConf(EmpConfigIdentifier id) {

    }

    @Before("validateQueryEmpConf(empConfigId)")
    public void validateQueryEmpConfigs(EmpConfigIdentifier empConfigId) {

        List<Message> messageList = new ArrayList<Message>();
        if (empConfigId.getId() == 0) {
            LOGGER.debug("please provide the valid id");
            messageList.add(new Message(ErrorCode.CS_1005.getValue()));
        }

        else {
            if (empConfigId.getId() < 0) {
                messageList.add(new Message(ErrorCode.CS_1006.getValue()));
            }
        }
        checkAndThrowMessage(messageList);
    }

    /**
     * Validates the {@link DmService#selectAllEmpAllData(EmpAddress empAddr)}
     * operation.
     * 
     * @param errors
     *            A {@link Errors} object containing the errors to return to the
     *            client.
     * @param id
     *            The <code>empAddr</code> parameter passed in by the client.
     */

    public void validateSelectAllEmpAllData(Errors errors, EmpAddress empAddr) {

        List<Message> messageList = new ArrayList<Message>();

        if (StringUtils.isBlank(empAddr.getAddress())) {
            messageList.add(new Message(ErrorCode.CS_1007.getValue()));
        }

        else {
            if (empAddr.getAddress() == null) {
                messageList.add(new Message(ErrorCode.CS_1008.getValue()));
            }

        }

        checkAndThrowMessage(messageList);
    }

    private void checkAndThrowMessage(List<Message> messageList) {
        if (!messageList.isEmpty()) {
            FrameworkError error = new FrameworkError(messageList.get(0).getCode(), messageList.get(0).getArgs(), null,
                    null);
            error.setValidationMessages(messageList);
            throw error;
        }

    }

}
