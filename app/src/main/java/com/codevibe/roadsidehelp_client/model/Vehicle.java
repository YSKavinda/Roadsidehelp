package com.codevibe.roadsidehelp_client.model;

public class Vehicle {

    public Vehicle() {
    }

    private String category;
    private String model_no;
    private String license_no;
    private String image;
    private String owner;
    private String status;

    public Vehicle(String category, String model_no, String license_no, String image, String owner, String status) {
        this.category = category;
        this.model_no = model_no;
        this.license_no = license_no;
        this.image = image;
        this.owner = owner;
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModel_no() {
        return model_no;
    }

    public void setModel_no(String model_no) {
        this.model_no = model_no;
    }

    public String getLicense_no() {
        return license_no;
    }

    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
