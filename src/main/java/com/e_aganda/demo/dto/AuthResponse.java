package com.e_aganda.demo.dto;

public class AuthResponse {
    private String token;
    private String lastname;
    private String firstname;
    private String email;

    public AuthResponse(String token, String lastname, String firstname, String email) {
        this.token = token;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}