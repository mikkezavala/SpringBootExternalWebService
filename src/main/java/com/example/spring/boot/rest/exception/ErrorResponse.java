package com.example.spring.boot.rest.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "timestamp", "message", "code", "type", "validationMessages", "detailMap" })
@XmlRootElement(name = "ErrorResponse")
public class ErrorResponse {

    @JsonProperty
    @XmlElement(name = "message", required = true)
    private String message;

    @JsonProperty("validationMessages")
    @XmlElement(name = "validationMessages", required = false)
    private List<String> validationMessages;

    @JsonProperty
    @XmlElement(name = "code")
    private String code;

    @JsonProperty
    @XmlElement(name = "timestamp", required = true)
    private long timestamp = (new Date()).getTime();

    @JsonProperty
    @XmlElement(name = "type")
    private String type;

    @JsonProperty
    @XmlElement(name = "id")
    private String id;

    @JsonProperty
    @XmlElement(name = "details")
    private Map<String, Object> detailMap;

    @XmlTransient
    private HttpStatus status;

    /**
     * 
     */
    public ErrorResponse() {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 
     * @param status
     */
    public ErrorResponse(HttpStatus status) {
        this.status = status;
    }

    /**
     * 
     * @param message
     */
    public ErrorResponse(String message) {
        this();
        this.setMessage(message);
    }

    /**
     * 
     * @param message
     * @param status
     */
    public ErrorResponse(String message, HttpStatus status) {
        this.setMessage(message);
        this.setStatus(status);
    }

    /**
     * 
     * @param thrown
     */
    public ErrorResponse(Throwable thrown) {
        this();
        this.setMessage(thrown.getMessage());
        this.setType(thrown.getClass().getName());
        this.parseException(thrown);
    }

    /**
     * 
     * @param thrown
     * @param status
     */
    public ErrorResponse(Throwable thrown, HttpStatus status) {
        this(thrown);
        this.setStatus(status);
    }

    private void parseException(Throwable thrown) {
        if (thrown instanceof HttpServiceException) {
            this.parseHttpServiceException((HttpServiceException) thrown);
        } else if (thrown instanceof ServiceException) {
            this.parseServiceException((ServiceException) thrown);
        }
    }

    private void parseHttpServiceException(HttpServiceException ex) {
        this.parseServiceException(ex);
        this.setStatus(ex.getStatus());
    }

    private void parseServiceException(ServiceException ex) {
        this.setTimestamp(ex.getTimestamp());
        this.setDetails(ex.getDetails());
        this.setMessage(ex.getMessage());
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDetails(Map<String, Object> details) {
        this.detailMap = details;
    }

    public Map<String, Object> getDetails() {
        return this.detailMap;
    }

    /**
     * 
     * @param status
     *            The status code to use in the response to the client.
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    /**
     * 
     * @return
     */
    public HttpStatus getStatus() {
        return this.status;
    }

    public void addValidationMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            this.validationMessages.add(message);
        }
    }

    public void addValidationMessages(String... messages) {
        if (messages != null) {
            for (String msg : messages) {
                this.addValidationMessage(msg);
            }
        }
    }

    public void addValidationMessages(List<String> msgList) {
        if (msgList != null) {
            for (String msg : msgList) {
                this.addValidationMessage(msg);
            }
        }
    }

    /**
     * 
     * @param messages
     */
    public void setValidationMessages(String... messages) {
        if (messages != null) {
            this.setValidationMessages(Arrays.asList(messages));
        }
    }

    /**
     * 
     * @param messageList
     */
    public void setValidationMessages(List<String> messageList) {
        if (messageList != null) {
            this.validationMessages = new ArrayList<>(messageList);
        }
    }

}
