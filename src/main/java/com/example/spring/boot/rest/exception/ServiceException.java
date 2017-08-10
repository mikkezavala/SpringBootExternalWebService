package com.example.spring.boot.rest.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> detailMap = new HashMap<>();
    private long timestamp = (new Date()).getTime();
    private String code;

    /**
     *
     * @param message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param details
     */
    public ServiceException(String message, Map<String, Object> details) {
        super(message);
        this.detailMap = details;
    }

    /**
     *
     * @param message
     * @param cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param details
     */
    public ServiceException(String message, Throwable cause, Map<String, Object> details) {
        super(message, cause);
        this.detailMap = details;
    }

    /**
     *
     * @param cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param cause
     */
    public ServiceException(Throwable cause, Map<String, Object> details) {
        super(cause);
        this.detailMap = details;
    }

    public void setDetails(Map<String, Object> details) {
        if (details != null) {
            this.detailMap = new HashMap<>(details);
        }
    }

    /**
     * Used to add extra details of the exception.
     * 
     * @param key
     * @param value
     */
    public void addDetail(String key, Object value) {
        this.detailMap.put(key, value);
    }

    public boolean hasDetails() {
        return this.detailMap.isEmpty();
    }

    public Map<String, Object> getDetails() {
        return this.detailMap;
    }

    /**
     * @param timestamp
     *            A timestamp for when the actual exception occurred.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @param date
     *            A time stamp for when the actual exception occurred.
     */
    public void setTimestamp(Date date) {
        if (date != null) {
            this.timestamp = date.getTime();
        }
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
