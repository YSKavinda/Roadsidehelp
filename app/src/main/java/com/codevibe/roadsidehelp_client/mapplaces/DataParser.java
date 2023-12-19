package com.codevibe.roadsidehelp_client.mapplaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String, String> getSingleNearbyPlace (JsonObject googlPlaceJSON) {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String NameOfPlace = "-NA-";
        String viscinity = "-NA-";
        String latitude = "-NA-";
        String longitude = "-NA-";
        String reference = "-NA-";


        try {
            if (!googlPlaceJSON.get("name").isJsonNull()) {
                NameOfPlace = googlPlaceJSON.get("name").toString();
            }
            if (!googlPlaceJSON.get("name").isJsonNull()) {
                NameOfPlace = googlPlaceJSON.get("name").toString();
            }
            latitude = googlPlaceJSON.getAsJsonObject("geometry").getAsJsonObject("location").get("lat").toString();
            longitude = googlPlaceJSON.getAsJsonObject("geometry").getAsJsonObject("location").get("lng").toString();
            reference = googlPlaceJSON.get("reference").toString();

            googlePlaceMap.put("place_name", NameOfPlace);
            googlePlaceMap.put("viscinity", viscinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);

            System.out.println("Data:  "+NameOfPlace+"  "+viscinity+"  "+latitude+"  "+longitude+"  "+reference);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return googlePlaceMap;
    }

    private List<HashMap<String, String>> geAllNearbyPlaces(JsonArray jsonArray) {
        int counter = jsonArray.size();
        List<HashMap<String, String>> NearbyPlacesList = new ArrayList<>();
        HashMap<String, String> NearbyplaceMap = null;
        for (int i = 0; i < counter; i++) {
            try {
                NearbyplaceMap = getSingleNearbyPlace((JsonObject) jsonArray.get(i));
                NearbyPlacesList.add(NearbyplaceMap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return NearbyPlacesList;
    }

    public List<HashMap<String, String>> parse(JsonObject jsondata) {
        JsonArray jsonArray = null;
        JsonObject jsonObject;
        try {
            jsonObject = jsondata;

           jsonArray  = jsonObject.getAsJsonArray("results");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return geAllNearbyPlaces(jsonArray);
    }
}
