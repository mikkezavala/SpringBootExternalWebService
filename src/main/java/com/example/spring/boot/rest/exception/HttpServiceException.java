package com.example.spring.boot.rest.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class HttpServiceException extends ServiceException {

    private static final long serialVersionUID = 1L;

    private HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public HttpServiceException(String message) {
        super(message);
    }

    public HttpServiceException(String message, HttpStatus status) {
        super(message);
        this.responseStatus = status;
    }

    public HttpServiceException(String message, Map<String, Object> details) {
        super(message, details);
    }

    public HttpServiceException(String message, Map<String, Object> details, HttpStatus status) {
        super(message, details);
        this.responseStatus = status;
    }

    public HttpServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpServiceException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.responseStatus = status;
    }

    public HttpServiceException(String message, Throwable cause, Map<String, Object> details) {
        super(message, cause, details);
    }

    public HttpServiceException(String message, Throwable cause, Map<String, Object> details, HttpStatus status) {
        super(message, cause, details);
    }

    public HttpServiceException(Throwable cause) {
        super(cause);
    }

    public HttpServiceException(Throwable cause, HttpStatus status) {
        super(cause);
        this.responseStatus = status;
    }

    public HttpServiceException(Throwable cause, Map<String, Object> details) {
        super(cause, details);
    }

    public HttpServiceException(Throwable cause, Map<String, Object> details, HttpStatus status) {
        super(cause, details);
        this.responseStatus = status;
    }

    /**
     * @return The status code to use for any REST response resulting from the
     *         exception.
     */
    public HttpStatus getStatus() {
        return this.responseStatus;
    }

    /**
     * @param status
     *            The status code to use for any REST response resulting from
     *            the exception.
     */
    public void setStatus(HttpStatus status) {
        this.responseStatus = (status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status);
    }

}
