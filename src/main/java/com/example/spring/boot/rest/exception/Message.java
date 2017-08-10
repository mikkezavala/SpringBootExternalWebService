package com.example.spring.boot.rest.exception;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents an error message associated used by most service exceptions that
 * use message bundles.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String text;
    private Object[] args;
    private String defaultMessage;

    public Message() {
    }

    public Message(String code, Object[] args) {
        this(code, args, null);
    }

    public Message(String code) {
        this.code = code;
    }

    public Message(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public Message(String code, String text, Object[] args) {
        this.code = code;
        this.args = args;
        this.text = text;
    }

    public Message(String code, Object[] args, String defaultMessage) {
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    /**
     * Gets the message code.
     *
     * @return the message code.
     */

    public String getCode() {
        return code;
    }

    /**
     * Sets the message code.
     *
     * @param code
     *            message code
     */

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("code", code);

        if (text != null) {
            builder.append("text", text);
        } else if (args != null && args.length > 0) {
            builder.append("args", args);
        }
        if (defaultMessage != null) {
            builder.append("defaultMessage", defaultMessage);
        }
        return builder.toString();

    }

}
