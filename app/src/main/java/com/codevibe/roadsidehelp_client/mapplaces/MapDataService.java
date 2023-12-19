package com.codevibe.roadsidehelp_client.mapplaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MapDataService {
    @GET("json")
    Call<JsonObject> getLocationInfo(@Query("query")String query,@Query("location")String location,@Query("radius")String radius,@Query("key")String key);

}
