package com.example.spring.boot.rest.types;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

public class EmpAppVO {

    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String email;
    private String mobile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "EmpAppVO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", address=" + address
                + ", city=" + city + ", email=" + email + ", mobile=" + mobile + "]";
    }

}
