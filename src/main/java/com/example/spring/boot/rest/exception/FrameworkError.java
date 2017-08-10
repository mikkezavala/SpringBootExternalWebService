package com.example.spring.boot.rest.exception;

import java.util.List;
import java.util.Map;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class FrameworkError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Message errorMessage;
    private Map<String, Object> details;
    private List<Message> validationMessages;

    public List<Message> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(List<Message> validationMessages) {
        this.validationMessages = validationMessages;
    }

    /**
     * An exception with just a code. The message will be looked up based upon
     * the code provided.
     * 
     * @param code
     *            A unique code for the error
     */
    public FrameworkError(String code) {
        this.setErrorMessage(new Message(code));
    }

    /**
     * An exception with a code and the root cause.
     * 
     * @param code
     *            A unique code for the error
     * @param cause
     *            The root cause
     */
    public FrameworkError(String code, Throwable cause) {
        super(cause);
        this.setErrorMessage(new Message(code));
    }

    /**
     * An exception with both a code and a pre-created message.
     * 
     * @param code
     *            A unique code for the error
     * @param message
     *            A description of the error
     */
    public FrameworkError(String code, String message) {
        this.setErrorMessage(new Message(code, message, null));
    }

    /**
     * An exception with both a code and a list of additional details.
     * 
     * @param code
     *            A unique code for the error
     * @param details
     *            A {@link Map} containing a list of additional details
     *            pertinent to the error.
     */
    public FrameworkError(String code, Map<String, Object> details) {
        this.setErrorMessage(new Message(code));
        this.details = details;
    }

    /**
     * An exception with both a code, pre-created message, and a list of
     * additional details.
     * 
     * @param code
     *            A unique code for the error
     * @param message
     *            A description of the error
     * @param details
     *            A {@link Map} containing a list of additional details
     *            pertinent to the error.
     */
    public FrameworkError(String code, String message, Map<String, Object> details) {
        this.setErrorMessage(new Message(code, message));
        this.details = details;
    }

    /**
     * An exception with a code, list of additional details, and a root cause.
     * 
     * @param code
     *            A unique code for the error
     * @param details
     *            A {@link Map} containing a list of additional details
     *            pertinent to the error.
     * @param cause
     *            The root cause
     */
    public FrameworkError(String code, Map<String, Object> details, Throwable cause) {
        super(cause);
        this.setErrorMessage(new Message(code));
        this.details = details;
    }

    /**
     * An exception with a code along with custome arguments that should appear
     * in the message.
     * 
     * @param code
     *            A unique code for the error
     * @param messageArgs
     *            An array of message arguments to be inserted into the error
     *            message
     */
    public FrameworkError(String code, Object[] messageArgs) {
        this.setErrorMessage(new Message(code, messageArgs));
    }

    /**
     * An exception with a code along with custom arguments that should appear
     * in the message, and list of additional details.
     * 
     * @param code
     *            A unique code for the error
     * @param messageArgs
     *            An array of message arguments to be inserted into the error
     *            message
     * @param details
     *            A {@link Map} containing a list of additional details
     *            pertinent to the error.
     */
    public FrameworkError(String code, Object[] messageArgs, Map<String, Object> details) {
        this.setErrorMessage(new Message(code, messageArgs));
        this.details = details;
    }

    /**
     * An exception with a code along with custom arguments that should appear
     * in the message, and list of additional details.
     * 
     * @param code
     *            A unique code for the error
     * @param messageArgs
     *            An array of message arguments to be inserted into the error
     *            message
     * @param details
     *            A {@link Map} containing a list of additional details
     *            pertinent to the error.
     * @param cause
     *            The root cause
     */
    public FrameworkError(String code, Object[] messageArgs, Map<String, Object> details, Throwable cause) {
        super(cause);
        this.setErrorMessage(new Message(code, messageArgs));
        this.details = details;
    }

    public Message getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Message errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "FrameworkError [errorMessage=" + errorMessage + ", details=" + details + ", toString()="
                + super.toString() + "]";
    }

}
