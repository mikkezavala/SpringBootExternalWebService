package com.example.multiple.spring.rest.types;

import java.io.Serializable;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class CustomerConfigIdentifier implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
