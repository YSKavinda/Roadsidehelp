package com.codevibe.roadsidehelp_client.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevibe.roadsidehelp_client.R;

public class Emergency extends Fragment {

    private static Emergency emergency;

    public Emergency() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency, container, false);
    }

    public static Emergency getInstance(){
        if(emergency==null){
            emergency = new Emergency();
        }
        return emergency;
    }
}