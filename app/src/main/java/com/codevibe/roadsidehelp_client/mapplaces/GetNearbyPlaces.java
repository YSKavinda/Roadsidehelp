package com.codevibe.roadsidehelp_client.mapplaces;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object,String, JsonObject>{
    private static final String TAG = GetNearbyPlaces.class.getName();
  private String url;
  private JsonObject googlePlaceData;

  private double lat,lang;
  private GoogleMap map;
    @Override
    protected JsonObject doInBackground(Object... objects) {
        map = (GoogleMap) objects[0];
        lat = (Double) objects[1];
        lang = (Double) objects[2];

        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            System.out.println("data hoyanawaaaaaaaaa");
            googlePlaceData = downloadUrl.ReadTheURL(lat,lang);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(JsonObject result) {
        List<HashMap<String, String>> nearByPlacesList = null;
        DataParser dataParser = new DataParser();
        nearByPlacesList = dataParser.parse(result);
        DisplayNearbyPlaces(nearByPlacesList);
    }

    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearByPlacesList) {
        for (int i = 0; i < nearByPlacesList.size(); i++) {

            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googleNearbyPlace = nearByPlacesList.get(i);
            String nameOfPlace = googleNearbyPlace.get("place_name");
            String viscinity = googleNearbyPlace.get("viscinity");
            double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
            double lng = Double.parseDouble(googleNearbyPlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title("Tow Trucks :-" + nameOfPlace + " : " + viscinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            map.addMarker(markerOptions);
            Log.i(TAG,"Marker Add wenawa");
        }
    }

}
