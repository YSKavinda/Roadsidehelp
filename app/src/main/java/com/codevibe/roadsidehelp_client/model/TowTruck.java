package com.codevibe.roadsidehelp_client.model;

public class TowTruck {
    public TowTruck() {
    }

    public TowTruck(String name, String contact, String towTruckAddress, String status, String latitude, String longitude) {
        this.name = name;
        this.contact = contact;
        this.address = towTruckAddress;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    private String name;
    private String contact;
    private String address;
    private String status;
    private String latitude;
    private String longitude;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTowTruckAddress() {
        return address;
    }

    public void setTowTruckAddress(String towTruckAddress) {
        this.address = towTruckAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
