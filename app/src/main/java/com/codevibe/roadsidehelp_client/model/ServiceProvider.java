package com.codevibe.roadsidehelp_client.model;

public class ServiceProvider {

    public ServiceProvider(){

    };
    public ServiceProvider(String title, String lat,String lng, String status, String contact) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.contact = contact;
    }

    private String title;
   private String lat;
   private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    private String status;
   private String contact;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
