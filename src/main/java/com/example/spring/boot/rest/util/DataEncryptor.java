package com.example.spring.boot.rest.util;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class DataEncryptor {

    private static final Logger LOG = LoggerFactory.getLogger(DataEncryptor.class);

    public static String encode(String data) {

        byte[] dataBytes = data.getBytes();
        String output = Base64.encode(dataBytes);
        LOG.debug("ENCODING: " + data + " > " + output);
        return output;
    }

    public static String decode(String data) {

        byte[] dataBytes;
        String output = null;
        try {
            dataBytes = Base64.decode(data);
            output = new String(dataBytes);
        } catch (WSSecurityException e) {
            LOG.warn("Failed to decode string [" + data + "]", e);
        }
        LOG.debug("DECODING: " + data + " > " + output);
        return output;
    }

}
