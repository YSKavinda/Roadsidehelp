package com.codevibe.roadsidehelp_client.mapplaces;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadUrl {

    Retrofit retrofit;

    JsonObject jsonObject= null;
    private static final String key = "AIzaSyCToPw-mRNukZdPAZw3P7weVfriGx6Orog";
    public JsonObject ReadTheURL(double lat,double lng) throws IOException {




        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/textsearch/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MapDataService mapDataService = retrofit.create(MapDataService.class);
        Call<JsonObject> call = mapDataService.getLocationInfo("tow_truck", lat + "," + lng, "10000", "AIzaSyDFMho1DhWJ3f1s1IpIN-UkRrvP1aOi1LQ");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                 if(response.isSuccessful()){
                     jsonObject = response.body();
                 }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
//        InputStream inputStream = null;
//        HttpURLConnection httpURLConnection=null;
//        try {
//            URL url = new URL(placeURL);
//            httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.connect();
//
//            inputStream = httpURLConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            StringBuffer stringBuffer = new StringBuffer();
//
//            String line = "";
//            while ((line=bufferedReader.readLine())!=null){
//                stringBuffer.append(line);
//            }
//            Data = stringBuffer.toString();
//            bufferedReader.close();
//
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            inputStream.close();
//            httpURLConnection.disconnect();
//        }
      return jsonObject;
    }
}
