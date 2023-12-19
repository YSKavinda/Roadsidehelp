package com.codevibe.roadsidehelp_client.viewmodels;

import androidx.lifecycle.ViewModel;

import com.codevibe.roadsidehelp_client.model.Driver;


public class MainViewModel extends ViewModel {

    Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void updateDriver(Driver driver) {
        this.driver = driver;
    }
}
