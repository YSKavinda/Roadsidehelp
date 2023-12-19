package com.codevibe.roadsidehelp_client.model;

import com.google.android.gms.maps.model.LatLng;

public class Driver {

    private String username;
    private String email;
    private String mobile;
    private String password;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    private LatLng location;
    private boolean active;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


    public Driver(String username, String email, String mobile) {
        this.username = username;
        this.email = email;
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }



    public Driver() {

    }

}
