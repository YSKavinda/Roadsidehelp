package com.codevibe.roadsidehelp_client.uiItems;

public class ServiceProviderItem {
    public String getTitle() {
        return title;
    }

    public String getContact() {
        return contact;
    }

    public String getStatus() {
        return status;
    }



    public ServiceProviderItem(int spImageThumbnail, String title, String contact, String status) {
        this.spImageThumbnail = spImageThumbnail;
        this.title = title;
        this.contact = contact;
        this.status = status;

    }

    public int getSpImageThumbnail() {
        return spImageThumbnail;
    }

    private int spImageThumbnail;

    private String title;
    private String contact;
    private String status;


}
