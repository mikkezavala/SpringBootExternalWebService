package com.example.spring.boot.rest.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.spring.boot.rest.exception.FrameworkError;
import com.example.spring.boot.rest.exception.Message;

/**
 * This is the base Runtime Exception class for all service validation
 * exceptions.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class FrameworkValidationError extends FrameworkError implements Iterable<Message> {

    private static final long serialVersionUID = 1L;

    private List<Message> validationMessages;

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
    public FrameworkValidationError(String code, Map<String, Object> details, Throwable cause) {
        super(code, details, cause);
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
    public FrameworkValidationError(String code, Map<String, Object> details) {
        super(code, details);
    }

    /**
     * An exception with a code along with a number of validation errors and a
     * list of details.
     * 
     * @param code
     *            A unique code for the error
     * @param validationCodes
     *            A list of all the validation error codes
     * @param details
     *            A {@link Map} containing a list of additional details
     *            pertinent to the error.
     */
    public FrameworkValidationError(String code, List<String> validationCodes, Map<String, Object> details) {
        super(code, details);
        validationMessages = new ArrayList<Message>();
        if (validationCodes != null) {
            for (String vc : validationCodes) {
                validationMessages.add(new Message(vc));
            }
        }
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
    public FrameworkValidationError(String code, Object[] messageArgs, Map<String, Object> details, Throwable cause) {
        super(code, messageArgs, details, cause);
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
    public FrameworkValidationError(String code, Object[] messageArgs, Map<String, Object> details) {
        super(code, messageArgs, details);
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
    public FrameworkValidationError(String code, Object[] messageArgs) {
        super(code, messageArgs);
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
    public FrameworkValidationError(String code, String message, Map<String, Object> details) {
        super(code, message, details);
    }

    /**
     * An exception with both a code and a pre-created message.
     * 
     * @param code
     *            A unique code for the error
     * @param message
     *            A description of the error
     */
    public FrameworkValidationError(String code, String message) {
        super(code, message);
    }

    /**
     * An exception with a code and the root cause.
     * 
     * @param code
     *            A unique code for the error
     * @param cause
     *            The root cause
     */
    public FrameworkValidationError(String code, Throwable cause) {
        super(code, cause);
    }

    /**
     * An exception with just a code. The message will be looked up based upon
     * the code provided.
     * 
     * @param code
     *            A unique code for the error
     */
    public FrameworkValidationError(String code) {
        super(code);
    }

    public List<Message> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(List<Message> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public void addValidationMessage(Message message) {
        this.validationMessages.add(message);
    }

    public void addValidationMessages(Message... msgs) {
        if (msgs != null) {
            for (Message message : msgs) {
                this.addValidationMessage(message);
            }
        }
    }

    @Override
    public Iterator<Message> iterator() {
        return this.validationMessages.iterator();
    }

    @Override
    public String toString() {
        return "FrameworkValidationError [toString()=" + super.toString() + ", validationMessages="
                + validationMessages + "]";
    }
}
