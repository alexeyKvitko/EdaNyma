package com.edanyma.model;

import com.edanyma.AppConstants;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.io.Serializable;

@AutoProperty
public class LoginUser implements Serializable {

    private String username;
    private String password;

    public LoginUser( ){
        this.username = AppConstants.DEFAULT_USER;
        this.password = AppConstants.DEFAULT_PASSWORD;
    }

    public LoginUser( String username, String password ) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override public boolean equals(Object o) {
        return Pojomatic.equals(this, o);
    }

    @Override public int hashCode() {
        return Pojomatic.hashCode(this);
    }

    @Override public String toString() {
        return Pojomatic.toString(this);
    }


}
